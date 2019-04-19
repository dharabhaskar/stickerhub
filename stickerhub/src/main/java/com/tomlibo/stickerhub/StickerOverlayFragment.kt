package com.tomlibo.stickerhub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tomlibo.stickerhub.adapter.OverlayAdapter
import com.tomlibo.stickerhub.listener.OverlayClickListener
import com.tomlibo.stickerhub.uicomponent.GridSpacingItemDecoration
import com.tomlibo.stickerhub.util.RecyclerItemClickListener
import com.tomlibo.stickerhub.util.StickerDataReader
import kotlinx.android.synthetic.main.fragment_sticker_gallery.*
import java.util.*

class StickerOverlayFragment : Fragment() {

    private var mainImageUrl: String = String()
    private val stickerList: ArrayList<String> = ArrayList()
    private var overlayClickListener: OverlayClickListener? = null

    companion object {
        private const val MAIN_IMAGE_URL = "main_image_url"

        fun newInstance(mainImageUrl: String): StickerOverlayFragment {
            val args = Bundle()
            args.putSerializable(MAIN_IMAGE_URL, mainImageUrl)
            val fragment = StickerOverlayFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainImageUrl = arguments!!.getSerializable(MAIN_IMAGE_URL) as String
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sticker_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load all overlays
        addOverLays()

        rcvSticker.layoutManager = GridLayoutManager(context, 3)
        rcvSticker.addItemDecoration(GridSpacingItemDecoration(3, 4, false))
        rcvSticker.adapter = OverlayAdapter(requireContext(), stickerList, mainImageUrl)

        rcvSticker.addOnItemTouchListener(RecyclerItemClickListener(context, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (context is OverlayClickListener) {
                    overlayClickListener = context as OverlayClickListener
                }
                overlayClickListener?.onSelectedOverlay(stickerList[position])
            }
        }))
    }

    fun setOverlayClickListener(overlayClickListener: OverlayClickListener?) {
        this.overlayClickListener = overlayClickListener
    }

    private fun addOverLays() {
        /*stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall1box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall2box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall3box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall4box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall5box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall6box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall7box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall8box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall9box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall10box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall11box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall12box.png?raw=true")
        stickerList.add("https://github.com/mnafian/ImageEffectFilter/blob/master/app/src/main/res/drawable/wall12box.png?raw=true")*/

        stickerList.addAll(StickerDataReader.getOverlays(activity))
    }
}
