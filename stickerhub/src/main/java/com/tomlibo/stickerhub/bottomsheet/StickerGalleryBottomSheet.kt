package com.tomlibo.stickerhub.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tomlibo.stickerhub.R
import com.tomlibo.stickerhub.StickerGalleryFragment

class StickerGalleryBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_stickers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // add sticker gallery fragment
        childFragmentManager
                .beginTransaction()
                .add(R.id.container, StickerGalleryFragment.newInstance(), StickerGalleryFragment().tag)
                .commit()
    }
}