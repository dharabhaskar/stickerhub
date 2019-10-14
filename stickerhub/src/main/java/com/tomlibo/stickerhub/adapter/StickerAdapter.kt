package com.tomlibo.stickerhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tomlibo.stickerhub.R
import com.tomlibo.stickerhub.uicomponent.SquareImageView
import kotlinx.android.synthetic.main.list_item_sticker.view.*
import java.util.*

class StickerAdapter(private val context: Context, private val items: ArrayList<String>) : RecyclerView.Adapter<StickerAdapter.StickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        return StickerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_sticker, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        Glide.with(context)
                .load(items[position])
                .into(holder.ivSticker)
    }

    fun addItems(stickers: List<String>) {
        items.addAll(stickers)
        notifyDataSetChanged()
    }

    fun replaceItems(newStickers: List<String>) {
        items.clear()
        items.addAll(newStickers)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): String {
        return items[position]
    }

    class StickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivSticker: SquareImageView = view.ivSticker
    }
}
