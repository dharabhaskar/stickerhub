package com.tomlibo.stickerhub.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tomlibo.stickerhub.R
import com.tomlibo.stickerhub.StickerGalleryFragment
import com.tomlibo.stickerhub.StickerOverlayFragment
import com.tomlibo.stickerhub.listener.OverlayClickListener
import kotlinx.android.synthetic.main.bottom_sheet_stickers.*

class StickerOverlayBottomSheet : BottomSheetDialogFragment() {

    private var mainImageUrl: String = String()
    private var overlayClickListener: OverlayClickListener? = null

    companion object {
        private const val MAIN_IMAGE_URL = "main_image_url"

        fun newInstance(mainImageUrl: String): StickerOverlayBottomSheet {
            val args = Bundle()
            args.putSerializable(MAIN_IMAGE_URL, mainImageUrl)
            val fragment = StickerOverlayBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainImageUrl = arguments!!.getSerializable(MAIN_IMAGE_URL) as String
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_stickers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle.text = resources.getString(R.string.overlay_header)

        val fragment = StickerOverlayFragment.newInstance(mainImageUrl)
        fragment.setOverlayClickListener(overlayClickListener)

        // add sticker gallery fragment
        childFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment, StickerGalleryFragment().tag)
                .commit()
    }

    fun setOverlayClickListener(overlayClickListener: OverlayClickListener?) {
        this.overlayClickListener = overlayClickListener
    }
}
