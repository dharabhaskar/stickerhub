package com.tomlibo.stickerhub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.tomlibo.stickerhub.adapter.StickerAdapter
import kotlinx.android.synthetic.main.activity_sticker_home.*
import java.util.ArrayList

class StickerHomeActivity : AppCompatActivity() {

    private val stickerList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticker_home)

        //load all stickers
        addStickers()

        rcvSticker.layoutManager = GridLayoutManager(this, 3)
        rcvSticker.adapter = StickerAdapter(this, stickerList)
    }

    private fun addStickers() {
        stickerList.add("https://3.imimg.com/data3/CH/JD/MY-19098000/cartoon-sticker-500x500.jpg")
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
        stickerList.add("https://i.ebayimg.com/images/g/xZIAAOSweW5Vcrn3/s-l300.jpg")
    }
}
