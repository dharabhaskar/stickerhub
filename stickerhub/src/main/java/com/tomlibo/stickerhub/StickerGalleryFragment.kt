package com.tomlibo.stickerhub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tomlibo.stickerhub.adapter.StickerAdapter
import com.tomlibo.stickerhub.listener.StickerClickListener
import com.tomlibo.stickerhub.model.StickerInfo
import com.tomlibo.stickerhub.uicomponent.GridSpacingItemDecoration
import com.tomlibo.stickerhub.util.RecyclerItemClickListener
import com.tomlibo.stickerhub.util.StickerDataReader
import kotlinx.android.synthetic.main.fragment_sticker_gallery.*

class StickerGalleryFragment : Fragment() {
    private val stickerList: ArrayList<String> = ArrayList()
    private var stickerClickListener: StickerClickListener? = null
    private var stickerAdapter: StickerAdapter?=null
    private var categoryAdapter: StickerAdapter?=null

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

        stickerAdapter=StickerAdapter(requireContext(), ArrayList())
        categoryAdapter=StickerAdapter(requireContext(), ArrayList(StickerDataReader.getAllCategoryThumbs(context)))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcvCategory.visibility=View.VISIBLE
        rcvCategory.layoutManager=GridLayoutManager(activity,6)
        rcvCategory.adapter=categoryAdapter
        rcvCategory.addOnItemTouchListener(RecyclerItemClickListener(context, RecyclerItemClickListener.OnItemClickListener { v, position ->
            val stickerInfo=StickerDataReader.getStickerInfoByIndex(context,position);
            stickerAdapter?.replaceItems(stickerInfo.stickerUrlList)

        }))



        //load all stickers
        //addStickers()
        rcvSticker.layoutManager = GridLayoutManager(context, 3)
        rcvSticker.addItemDecoration(GridSpacingItemDecoration(3, 4, false))
        rcvSticker.adapter = stickerAdapter
        stickerAdapter?.replaceItems(StickerDataReader.getStickersByIndex(activity,0))

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

    private fun addStickers() {
       /* stickerList.add("https://3.imimg.com/data3/CH/JD/MY-19098000/cartoon-sticker-500x500.jpg")
        stickerList.add("https://4.imimg.com/data4/CY/KW/MY-6014750/1-250x250.jpg")
        stickerList.add("https://i.ebayimg.com/images/g/ZqsAAOSwcgNZNWxr/s-l300.jpg")
        stickerList.add("https://cartoonbeing.com/cartoonbeing/wp-content/uploads/2017/02/jazz-cat-sticker.png")
        stickerList.add("https://png.pngtree.com/element_origin_min_pic/16/08/07/2257a73ffa0a999.jpg")
        stickerList.add("https://ih0.redbubble.net/image.143500727.5869/st%2Csmall%2C215x235-pad%2C210x230%2Cf8f8f8.lite-1u4.jpg")
        stickerList.add("https://dejpknyizje2n.cloudfront.net/marketplace/products/cute-smiling-sea-turtle-cartoon-sticker-1539124797.3059962.png")
        stickerList.add("https://i.pinimg.com/originals/2a/90/d6/2a90d6404b88884aeb3a3119eb99ff6f.jpg")
        stickerList.add("https://lezebre.lu/images/detailed/16/22048-sexy-betty-boop.png")
        stickerList.add("http://clipart-library.com/images/qTB6n46T5.jpg")
        stickerList.add("https://i.pinimg.com/originals/da/04/12/da0412838d2930d4b3b0e75d2e15b9c1.jpg")
        stickerList.add("https://i.ebayimg.com/images/g/xZIAAOSweW5Vcrn3/s-l300.jpg")*/

        stickerList.addAll(StickerDataReader.getAllStikers(activity))
        //stickerList.addAll(StickerDataReader.getAllStikers(activity))
    }
}
