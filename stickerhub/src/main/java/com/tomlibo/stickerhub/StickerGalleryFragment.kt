package com.tomlibo.stickerhub

import android.app.ProgressDialog
import android.os.AsyncTask
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
import com.tomlibo.stickerhub.uicomponent.GridSpacingItemDecoration
import com.tomlibo.stickerhub.util.RecyclerItemClickListener
import com.tomlibo.stickerhub.util.StickerDataReader
import kotlinx.android.synthetic.main.fragment_sticker_gallery.*
import java.io.*
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.nio.file.Files.isDirectory

class StickerGalleryFragment : Fragment() {

    private val TAG = StickerGalleryFragment::class.java.simpleName

    private val stickerList: ArrayList<String> = ArrayList()
    private var stickerClickListener: StickerClickListener? = null
    private var categoryAdapter: StickerCategoryAdapter? = null
    private var stickerAdapter: StickerAdapter? = null

    companion object {
        fun newInstance(): StickerGalleryFragment {
            return StickerGalleryFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sticker_gallery, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = StickerCategoryAdapter(requireContext(), ArrayList(StickerDataReader.getOnlyStickers(context)))
        stickerAdapter = StickerAdapter(requireContext(), ArrayList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load all categories
        rcvCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcvCategory.adapter = categoryAdapter

        rcvCategory.addOnItemTouchListener(RecyclerItemClickListener(context, RecyclerItemClickListener.OnItemClickListener { v, position ->
            categoryAdapter?.updateSelection(position)
            val stickerInfo = categoryAdapter?.getItem(position)
            stickerAdapter?.replaceItems(stickerInfo?.stickerUrlList as List<String>)
        }))

        //load all stickers
        rcvSticker.layoutManager = GridLayoutManager(context, 3)
        rcvSticker.addItemDecoration(GridSpacingItemDecoration(3, 4, false))
        rcvSticker.adapter = stickerAdapter
        stickerAdapter?.replaceItems(StickerDataReader.getStickersByIndex(activity, 0))

        rcvSticker.addOnItemTouchListener(RecyclerItemClickListener(context, RecyclerItemClickListener.OnItemClickListener { view, position ->
            if (context is StickerClickListener) {
                stickerClickListener = context as StickerClickListener
            }
            stickerClickListener?.onSelectedSticker(stickerAdapter?.getItem(position))
        }))

        DownloadFile().execute("https://storage.googleapis.com/tom-data-bucket/tom-stock-image/app/images/stickers/happy/thankyou.zip")
    }

    fun setStickerClickListener(stickerClickListener: StickerClickListener?) {
        this.stickerClickListener = stickerClickListener
    }

    private fun addAllStickers() {
        stickerList.addAll(StickerDataReader.getAllStickers(activity))
    }

    private inner class DownloadFile : AsyncTask<String, String, String>() {

        private var progressDialog: ProgressDialog? = null
        private var fileDirectory: File? = null
        private var fileName: String? = null
        private var file: File? = null

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        override fun onPreExecute() {
            super.onPreExecute()
            this.progressDialog = ProgressDialog(context)
            this.progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            this.progressDialog!!.setCancelable(false)
            this.progressDialog!!.show()
        }

        /**
         * Downloading file in background thread
         */
        override fun doInBackground(vararg f_url: String): String {
            try {
                val url = URL(f_url[0])
                val connection = url.openConnection()
                connection.connect()

                // getting file length
                val lengthOfFile = connection.contentLength

                // input stream to read file - with 8k buffer
                val inputStream = BufferedInputStream(url.openStream(), 8192)

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length)

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

                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + ((loadedSize * 100) / lengthOfFile).toInt())
                    Log.d(TAG, "Progress: " + ((loadedSize * 100) / lengthOfFile).toInt())

                    outputStream.write(buffer, 0, dataSize)
                }

                // flushing output
                outputStream.flush()

                // closing streams
                outputStream.close()
                inputStream.close()

                return "Downloaded at: $file"
            } catch (e: Exception) {
                Log.e("Error: ", e.message)
            }

            return "Something went wrong"
        }

        /**
         * Updating progress bar
         */
        override fun onProgressUpdate(vararg progress: String) {
            // setting progress percentage
            progressDialog!!.progress = Integer.parseInt(progress[0])
        }


        override fun onPostExecute(message: String) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog!!.dismiss()

            unzip(file!!, fileDirectory!!)
        }
    }

    @Throws(IOException::class)
    fun unzip(zipFile: File, targetDirectory: File) {
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
                    while ({ count = zis.read(buffer); count }() != -1)
                        fileOutputStream.write(buffer, 0, count)
                }
            }
        }
    }
}
