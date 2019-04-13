package com.tomlibo.stickerhub.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tomlibo.stickerhub.R
import com.tomlibo.stickerhub.StickerFrameFragment
import com.tomlibo.stickerhub.StickerGalleryFragment
import com.tomlibo.stickerhub.listener.FrameClickListener
import kotlinx.android.synthetic.main.bottom_sheet_stickers.*

class StickerFrameBottomSheet : BottomSheetDialogFragment() {

    private var frameClickListener: FrameClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_stickers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle.text = resources.getString(R.string.frame_header)

        val fragment = StickerFrameFragment.newInstance()
        fragment.setFrameClickListener(frameClickListener)

        // add sticker gallery fragment
        childFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment, StickerGalleryFragment().tag)
                .commit()
    }

    fun setFrameClickListener(frameClickListener: FrameClickListener?) {
        this.frameClickListener = frameClickListener
    }
}
