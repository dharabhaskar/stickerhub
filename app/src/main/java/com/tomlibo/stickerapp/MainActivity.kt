package com.tomlibo.stickerapp

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tomlibo.stickerhub.bottomsheet.StickerFrameBottomSheet
import com.tomlibo.stickerhub.bottomsheet.StickerGalleryBottomSheet
import com.tomlibo.stickerhub.bottomsheet.StickerOverlayBottomSheet
import com.tomlibo.stickerhub.listener.FrameClickListener
import com.tomlibo.stickerhub.listener.OverlayClickListener
import com.tomlibo.stickerhub.listener.StickerClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), StickerClickListener, FrameClickListener, OverlayClickListener {

    private val mainImageUrl: String = "https://cdn.pixabay.com/photo/2017/05/07/19/32/strawberry-2293337_960_720.jpg"
    private val stickerGalleryBottomSheet = StickerGalleryBottomSheet()
    private val stickerFrameBottomSheet = StickerFrameBottomSheet()
    private var stickerOverlayBottomSheet: StickerOverlayBottomSheet = StickerOverlayBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stickerOverlayBottomSheet = StickerOverlayBottomSheet.newInstance(mainImageUrl)

        // load background image
        Glide.with(this)
                .load(mainImageUrl)
                .into(stickerHolder.backgroundImageView)

        btSticker.setOnClickListener { v ->
            stickerGalleryBottomSheet.show(supportFragmentManager, StickerGalleryBottomSheet().tag)
        }

        btFrame.setOnClickListener { v ->
            stickerFrameBottomSheet.show(supportFragmentManager, StickerFrameBottomSheet().tag)
        }

        btOverLay.setOnClickListener { v ->
            stickerOverlayBottomSheet.show(supportFragmentManager, StickerOverlayBottomSheet().tag)
        }

        btText.setOnClickListener { v ->
            stickerHolder.addTextSticker()
        }

        stickerGalleryBottomSheet.setStickerClickListener(this)
        stickerFrameBottomSheet.setFrameClickListener(this)
        stickerOverlayBottomSheet.setOverlayClickListener(this)
    }

    override fun onSelectedSticker(url: String?) {
        stickerGalleryBottomSheet.dismiss()

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        stickerHolder.addImageSticker(resource)
                    }
                })
    }

    override fun onSelectedFrame(url: String) {
        stickerFrameBottomSheet.dismiss()

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        stickerHolder.addFrameSticker(resource)
                    }
                })
    }

    override fun onSelectedOverlay(url: String) {
        stickerOverlayBottomSheet.dismiss()

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        stickerHolder.addOverlay(resource)
                    }
                })
    }
}
