package com.tomlibo.stickerapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tomlibo.stickerhub.StickerHomeActivity
import com.tomlibo.stickerhub.bottomsheet.StickerGalleryBottomSheet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
}
