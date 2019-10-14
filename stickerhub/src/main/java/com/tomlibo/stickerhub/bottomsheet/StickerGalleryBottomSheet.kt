package com.tomlibo.stickerhub.bottomsheet

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tomlibo.stickerhub.R
import com.tomlibo.stickerhub.StickerGalleryFragment
import com.tomlibo.stickerhub.listener.StickerClickListener
import com.tomlibo.stickerhub.model.StickerInfo
import com.tomlibo.stickerhub.util.Constants
import kotlinx.android.synthetic.main.bottom_sheet_stickers.*
import java.io.*
import java.net.URL

class StickerGalleryBottomSheet : BottomSheetDialogFragment() {

    private var stickerClickListener: StickerClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_stickers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle.text = resources.getString(R.string.sticker_header)

        val fragment = StickerGalleryFragment.newInstance()
        fragment.setStickerClickListener(stickerClickListener)

        // add sticker gallery fragment
        childFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment, StickerGalleryFragment().tag)
                .commit()

        ReadStrickerJsonFile().execute(Constants.STICKER_BASE_URL + "/sticker-info.json")
    }

    fun setStickerClickListener(stickerClickListener: StickerClickListener) {
        this.stickerClickListener = stickerClickListener
    }

    private inner class ReadStrickerJsonFile : AsyncTask<String, String, String>() {

        private var fileDirectory: File? = null
        private var fileName: String? = null
        private var file: File? = null

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
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

                Log.e("JSON", Gson().toJson(parse(inputStream)))

                inputStream.close()

                return "Downloaded at: $file"
            } catch (e: Exception) {
                Log.e("Error: ", e.message)
            }

            return "Something went wrong"
        }

        override fun onPostExecute(message: String) {
            progressBar.visibility = View.GONE
        }
    }

    /*
    * Method to parse InputStream to String JSON
    * */
    private fun parse(inputStream: InputStream): List<StickerInfo>? {
        val result: StringBuilder
        return try {
            val reader = BufferedReader(InputStreamReader(inputStream))
            result = StringBuilder()
            var line: String? = null

            while ({ line = reader.readLine(); line }() != null) {
                result.append(line)
            }

            //Log.d("JSON Parser", "result: $result")
            Gson().fromJson<List<StickerInfo>>(result.toString(), object : TypeToken<List<StickerInfo>>() {
            }.type)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
