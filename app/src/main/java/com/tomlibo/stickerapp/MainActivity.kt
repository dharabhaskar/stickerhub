package com.tomlibo.stickerapp

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.tomlibo.stickerhub.bottomsheet.StickerGalleryBottomSheet
import com.tomlibo.stickerhub.listener.StickerClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), StickerClickListener {

    private val stickerGalleryBottomSheet = StickerGalleryBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // load background image
        Picasso.with(this)
                .load("https://cdn.pixabay.com/photo/2017/05/07/19/32/strawberry-2293337_960_720.jpg")
                .into(stickerHolder.backgroundImageView)

        btSticker.setOnClickListener { v ->
            stickerGalleryBottomSheet.show(supportFragmentManager, StickerGalleryBottomSheet().tag)
        }

        btText.setOnClickListener { v ->
            stickerHolder.addTextSticker()
        }

        stickerGalleryBottomSheet.setStickerClickListener(this)
    }

    override fun onSelectedSticker(url: String) {
        stickerGalleryBottomSheet.dismiss()

        Picasso.with(this)
                .load(url)
                .into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onBitmapFailed(errorDrawable: Drawable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        stickerHolder.addImageSticker(bitmap)
                    }
                })
    }
}
