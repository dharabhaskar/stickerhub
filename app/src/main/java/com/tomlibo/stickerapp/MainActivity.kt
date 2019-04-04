package com.tomlibo.stickerapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tomlibo.stickerhub.bottomsheet.StickerGalleryBottomSheet
import com.tomlibo.stickerhub.listener.StickerClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), StickerClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btSticker.setOnClickListener { v ->
            /*val intent = Intent(this, StickerHomeActivity::class.java)
            startActivity(intent)*/

            val stickerGalleryBottomSheet = StickerGalleryBottomSheet()
            stickerGalleryBottomSheet.show(supportFragmentManager, StickerGalleryBottomSheet().tag)
        }
    }

    override fun onSelectedSticker(url: String) {
        Log.e("URL", url)
    }
}
