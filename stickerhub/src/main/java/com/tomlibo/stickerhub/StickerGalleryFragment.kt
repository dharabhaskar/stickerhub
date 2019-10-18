package com.tomlibo.stickerhub

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tomlibo.stickerhub.adapter.StickerAdapter
import com.tomlibo.stickerhub.adapter.StickerCategoryAdapter
import com.tomlibo.stickerhub.listener.StickerClickListener
import com.tomlibo.stickerhub.model.StickerInfo
import com.tomlibo.stickerhub.uicomponent.GridSpacingItemDecoration
import com.tomlibo.stickerhub.util.Constants
import com.tomlibo.stickerhub.util.RecyclerItemClickListener
import com.tomlibo.stickerhub.util.StickerDataReader
import kotlinx.android.synthetic.main.fragment_sticker_gallery.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class StickerGalleryFragment : Fragment() {

    private val TAG = StickerGalleryFragment::class.java.simpleName

    private var stickerInfoList: ArrayList<StickerInfo>? = ArrayList()
    private var stickerClickListener: StickerClickListener? = null
    private var categoryAdapter: StickerCategoryAdapter? = null
    private var stickerAdapter: StickerAdapter? = null
    private var fileDirectory: File? = null
    private var isLoading: Boolean = false

    companion object {
        private const val PARAM_STICKER_INFO = "PARAM_STICKER_INFO"

        fun newInstance(stickerInfoList: List<StickerInfo>): StickerGalleryFragment {
            val args = Bundle()
            args.putParcelableArrayList(PARAM_STICKER_INFO, stickerInfoList as ArrayList)
            val fragment = StickerGalleryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sticker_gallery, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stickerInfoList = arguments!!.getParcelableArrayList<StickerInfo>(PARAM_STICKER_INFO)

        StickerDataReader.init(stickerInfoList)

        categoryAdapter = StickerCategoryAdapter(requireContext(), ArrayList(StickerDataReader.getOnlyStickers()))
        stickerAdapter = StickerAdapter(requireContext(), ArrayList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load all categories
        rcvCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcvCategory.adapter = categoryAdapter

        rcvCategory.addOnItemTouchListener(RecyclerItemClickListener(context, RecyclerItemClickListener.OnItemClickListener { v, position ->
            if (!isLoading) {
                categoryAdapter?.updateSelection(position)
                val stickerInfo = categoryAdapter?.getItem(position)
                if (!stickerInfo!!.isOnline) {
                    val directoryPath = "sticker/" + stickerInfo.formattedTitle
                    val fileUrl = Constants.STICKER_BASE_URL + "/" + stickerInfo.formattedTitle + "/" + stickerInfo.formattedTitle + ".zip"
                    showStickersByCategory(directoryPath, fileUrl)
                } else {
                    stickerAdapter!!.replaceItems(StickerDataReader.getStickersByIndex(position))
                }
            }
        }))

        //load all stickers
        rcvSticker.layoutManager = GridLayoutManager(context, 3)
        rcvSticker.addItemDecoration(GridSpacingItemDecoration(3, 4, false))
        rcvSticker.adapter = stickerAdapter

        // show stickers from 1st index category
        val stickerInfo = StickerDataReader.getStickerInfoByIndex(0)
        if (!stickerInfo!!.isOnline) {
            val directoryPath = "sticker/" + stickerInfo.formattedTitle
            val fileUrl = Constants.STICKER_BASE_URL + "/" + stickerInfo.formattedTitle + "/" + stickerInfo.formattedTitle + ".zip"
            showStickersByCategory(directoryPath, fileUrl)
        } else {
            stickerAdapter!!.replaceItems(StickerDataReader.getStickersByIndex(0))
        }

        rcvSticker.addOnItemTouchListener(RecyclerItemClickListener(context, RecyclerItemClickListener.OnItemClickListener { view, position ->
            if (context is StickerClickListener) {
                stickerClickListener = context as StickerClickListener
            }
            stickerClickListener?.onSelectedSticker(stickerAdapter?.getItem(position))
        }))
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun setStickerClickListener(stickerClickListener: StickerClickListener?) {
        this.stickerClickListener = stickerClickListener
    }

    private fun showStickersByCategory(directoryPath: String, fileUrl: String) {
        val directory = File(activity!!.filesDir, directoryPath)
        val contents = directory.listFiles()
        if (contents == null || contents.isEmpty()) {
            GlobalScope.launch(Dispatchers.Main) {
                val f = fetchStickerPack(fileUrl.replace(" ", ""))
                showImages(f)
            }
        } else {
            val stickerFiles = imageReaderNew(directory)
            if (stickerFiles.isNotEmpty()) {
                val stickerList: ArrayList<String> = ArrayList()
                for (file in stickerFiles) {
                    stickerList.add(Uri.fromFile(file).toString())
                }

                stickerAdapter!!.replaceItems(stickerList)

                layoutDownload.visibility = View.GONE
                rcvSticker.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun fetchStickerPack(url: String): File {
        isLoading = true

        return withContext(Dispatchers.IO) {
            // download zip file
            val file = downloadZipFile(url)

            // start unzip the downloaded file
            unzip(file, fileDirectory!!)

            isLoading = false

            file
        }
    }

    private fun showImages(file: File) {
        val stickerFiles = imageReaderNew(File(fileDirectory!!, file.nameWithoutExtension))
        if (stickerFiles.isNotEmpty()) {
            val stickerList: ArrayList<String> = ArrayList()
            for (f in stickerFiles) {
                stickerList.add(Uri.fromFile(f).toString())
            }

            stickerAdapter!!.replaceItems(stickerList)
        }

        layoutDownload.visibility = View.GONE
        rcvSticker.visibility = View.VISIBLE
    }

    private suspend fun downloadZipFile(fileUrl: String): File {
        /**
         * UI visibility control
         */
        withContext(Dispatchers.Main) {
            rcvSticker.visibility = View.GONE
            layoutDownload.visibility = View.VISIBLE
            progressBar.progress = 0
            tvDownloadPercentage.text = "0%"
            tvDownloadStatus.text = "0/100"
        }

        val fileName: String
        var file: File? = null

        try {
            val url = URL(fileUrl)
            val connection = url.openConnection()
            connection.connect()

            // getting file length
            val lengthOfFile = connection.contentLength

            // input stream to read file - with 8k buffer
            val inputStream = BufferedInputStream(url.openStream(), 8192)

            //Extract file name from URL
            fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1, fileUrl.length)

            /*
            * Store file in internal storage
            * */
            fileDirectory = File(activity!!.filesDir, "sticker")
            if (!fileDirectory!!.exists() && !fileDirectory!!.isDirectory) {
                fileDirectory!!.mkdirs()
            }

            file = File(fileDirectory, fileName)

            val outputStream = FileOutputStream(file)

            val buffer = ByteArray(1024)
            var dataSize = 0
            var loadedSize: Long = 0

            while ({ dataSize = inputStream.read(buffer); dataSize }() != -1) {
                loadedSize += dataSize

                // setting progress percentage
                withContext(Dispatchers.Main) {
                    val progress = ((loadedSize * 100) / lengthOfFile).toInt().toString()
                    progressBar.progress = Integer.parseInt(progress)
                    tvDownloadPercentage.text = String.format("%s%s", progress, "%")
                    tvDownloadStatus.text = String.format("%s%s", progress, "/100")
                }

                outputStream.write(buffer, 0, dataSize)
            }

            // flushing output
            outputStream.flush()

            // closing streams
            outputStream.close()
            inputStream.close()
        } catch (e: Exception) {
            Log.e("Error: ", e.message)
        }

        return file!!
    }

    @Throws(IOException::class)
    private fun unzip(zipFile: File, targetDirectory: File) {
        val zis = ZipInputStream(
                BufferedInputStream(FileInputStream(zipFile)))
        zis.use { zis ->
            var ze: ZipEntry? = null
            var count = 0
            val buffer = ByteArray(8192)
            while ({ ze = zis.nextEntry; ze }() != null) {
                val file = File(targetDirectory, ze!!.name)
                val dir = if (ze!!.isDirectory) file else file.parentFile
                if (!dir.isDirectory && !dir.mkdirs())
                    throw FileNotFoundException("Failed to ensure directory: " + dir.absolutePath)
                if (ze!!.isDirectory)
                    continue
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.use { fileOutputStream ->
                    while ({ count = zis.read(buffer); count }() != -1) {
                        fileOutputStream.write(buffer, 0, count)
                    }
                }
            }

            // delete downloaded zip file
            zipFile.delete()
        }
    }

    private fun imageReaderNew(root: File): List<File> {
        val fileList: ArrayList<File> = ArrayList()
        val listAllFiles = root.listFiles()

        if (listAllFiles != null && listAllFiles.isNotEmpty()) {
            for (currentFile in listAllFiles) {
                fileList.add(currentFile.absoluteFile)
            }
        }

        return fileList
    }
}
