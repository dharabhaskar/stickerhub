package com.tomlibo.stickerhub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class StickerHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticker_home)

        // add sticker gallery fragment
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, StickerGalleryFragment.newInstance(), StickerGalleryFragment().tag)
                .commit()
    }
}
