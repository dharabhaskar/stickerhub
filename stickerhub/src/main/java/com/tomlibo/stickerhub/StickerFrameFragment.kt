package com.tomlibo.stickerhub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tomlibo.stickerhub.adapter.StickerAdapter
import com.tomlibo.stickerhub.listener.FrameClickListener
import com.tomlibo.stickerhub.uicomponent.GridSpacingItemDecoration
import com.tomlibo.stickerhub.util.RecyclerItemClickListener
import com.tomlibo.stickerhub.util.StickerDataReader
import kotlinx.android.synthetic.main.fragment_sticker_gallery.*
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StickerFrameFragment : Fragment() {

    private val stickerList: ArrayList<String> = ArrayList()
    private var frameClickListener: FrameClickListener? = null

    companion object {
        fun newInstance(): StickerFrameFragment {
            return StickerFrameFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sticker_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load all frames
        addFrames()
        rcvSticker.layoutManager = GridLayoutManager(context, 3)
        rcvSticker.addItemDecoration(GridSpacingItemDecoration(3, 4, false))
        rcvSticker.adapter = StickerAdapter(requireContext(), stickerList)
        rcvSticker.addOnItemTouchListener(RecyclerItemClickListener(context, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (context is FrameClickListener) {
                    frameClickListener = context as FrameClickListener
                }
                frameClickListener?.onSelectedFrame(stickerList[position])
            }
        }))
    }

    fun setFrameClickListener(frameClickListener: FrameClickListener?) {
        this.frameClickListener = frameClickListener
    }

    private fun addFrames() {
        /*stickerList.add("https://github.com/iQueSoft/iQuePhoto/blob/master/iQuePhoto/app/src/main/res/drawable-nodpi/frame_grunge_01.png?raw=true")
        stickerList.add("https://github.com/iQueSoft/iQuePhoto/blob/master/iQuePhoto/app/src/main/res/drawable-nodpi/frame_grunge_02.png?raw=true")
        stickerList.add("https://github.com/iQueSoft/iQuePhoto/blob/master/iQuePhoto/app/src/main/res/drawable-nodpi/frame_grunge_03.png?raw=true")
        stickerList.add("https://github.com/iQueSoft/iQuePhoto/blob/master/iQuePhoto/app/src/main/res/drawable-nodpi/frame_grunge_04.png?raw=true")
        stickerList.add("https://github.com/iQueSoft/iQuePhoto/blob/master/iQuePhoto/app/src/main/res/drawable-nodpi/frame_h_01.png?raw=true")
        stickerList.add("http://img.tomlibo.com/app/stiker/frame/frame.9.png")*/
        stickerList.addAll(StickerDataReader.getFrames(activity))
    }
}
