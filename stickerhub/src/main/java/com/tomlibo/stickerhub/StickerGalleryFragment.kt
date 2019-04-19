package com.tomlibo.stickerhub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tomlibo.stickerhub.adapter.StickerAdapter
import com.tomlibo.stickerhub.adapter.StickerCategoryAdapter
import com.tomlibo.stickerhub.listener.StickerClickListener
import com.tomlibo.stickerhub.model.StickerInfo
import com.tomlibo.stickerhub.uicomponent.GridSpacingItemDecoration
import com.tomlibo.stickerhub.util.RecyclerItemClickListener
import com.tomlibo.stickerhub.util.StickerDataReader
import kotlinx.android.synthetic.main.fragment_sticker_gallery.*

class StickerGalleryFragment : Fragment() {

    private val stickerList: ArrayList<String> = ArrayList()
    private var stickerClickListener: StickerClickListener? = null
    private var categoryAdapter: StickerCategoryAdapter?=null
    private var stickerAdapter: StickerAdapter?=null

    companion object {
        fun newInstance(): StickerGalleryFragment {
            return StickerGalleryFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sticker_gallery, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = StickerCategoryAdapter(requireContext(), ArrayList(StickerDataReader.getOnlyStickers(context)))
        stickerAdapter = StickerAdapter(requireContext(), ArrayList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load all categories
        rcvCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcvCategory.adapter = categoryAdapter

        rcvCategory.addOnItemTouchListener(RecyclerItemClickListener(context, RecyclerItemClickListener.OnItemClickListener { v, position ->
            categoryAdapter?.updateSelection(position)
            val stickerInfo = categoryAdapter?.getItem(position)
            stickerAdapter?.replaceItems(stickerInfo?.stickerUrlList as List<String>)
        }))

        //load all stickers
        rcvSticker.layoutManager = GridLayoutManager(context, 3)
        rcvSticker.addItemDecoration(GridSpacingItemDecoration(3, 4, false))
        rcvSticker.adapter = stickerAdapter
        stickerAdapter?.replaceItems(StickerDataReader.getStickersByIndex(activity, 0))

        rcvSticker.addOnItemTouchListener(RecyclerItemClickListener(context, RecyclerItemClickListener.OnItemClickListener { view, position ->
            if (context is StickerClickListener) {
                stickerClickListener = context as StickerClickListener
            }
            stickerClickListener?.onSelectedSticker(stickerAdapter?.getItem(position))
        }))
    }

    fun setStickerClickListener(stickerClickListener: StickerClickListener?) {
        this.stickerClickListener = stickerClickListener
    }

    private fun addAllZStickers() {
        stickerList.addAll(StickerDataReader.getAllStikers(activity))
    }
}
