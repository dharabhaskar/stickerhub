package com.tomlibo.stickerapp

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.tomlibo.stickerapp.stickerview.StickerImageView
import com.tomlibo.stickerhub.bottomsheet.StickerGalleryBottomSheet
import com.tomlibo.stickerhub.listener.StickerClickListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_sticker.view.*

class MainActivity : AppCompatActivity(), StickerClickListener {

    private val stickerGalleryBottomSheet = StickerGalleryBottomSheet()
    private lateinit var stickerImageView: StickerImageView
    private lateinit var view: View
    private lateinit var ivSticker: AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stickerImageView = StickerImageView(this)
        view = LayoutInflater.from(this).inflate(R.layout.item_sticker, null, false)
        ivSticker = view.ivSticker

        btSticker.setOnClickListener { v ->
            /*val intent = Intent(this, StickerHomeActivity::class.java)
            startActivity(intent)*/

            stickerGalleryBottomSheet.show(supportFragmentManager, StickerGalleryBottomSheet().tag)
        }
    }

    override fun onSelectedSticker(url: String) {
        stickerGalleryBottomSheet.dismiss()

        flCanvas.removeView(stickerImageView)

        Picasso.get()
                .load(url)
                .into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        ivSticker.setImageBitmap(bitmap)
                        stickerImageView.setImageDrawable(ivSticker.drawable)
                        flCanvas.addView(stickerImageView)
                    }

                })
    }
}
