package com.tomlibo.stickerhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tomlibo.stickerhub.R
import kotlinx.android.synthetic.main.list_item_overlay.view.*
import java.util.*

class OverlayAdapter(private val context: Context, private val items: ArrayList<String>, private val mainImageUrl: String) : RecyclerView.Adapter<OverlayAdapter.StickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        return StickerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_overlay, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        Glide.with(context)
                .load(mainImageUrl)
                .into(holder.ivMain)

        Glide.with(context)
                .load(items.get(position))
                .into(holder.ivOverlay)
    }

    class StickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivMain = view.ivMain
        val ivOverlay = view.ivOverlay
    }
}
