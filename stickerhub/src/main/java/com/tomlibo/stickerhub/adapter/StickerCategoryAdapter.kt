package com.tomlibo.stickerhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tomlibo.stickerhub.R
import com.tomlibo.stickerhub.model.StickerInfo
import com.tomlibo.stickerhub.uicomponent.SquareImageView
import kotlinx.android.synthetic.main.list_item_sticker.view.ivSticker
import kotlinx.android.synthetic.main.list_item_sticker_category.view.*
import java.util.*

class StickerCategoryAdapter(private val context: Context, private val items: ArrayList<StickerInfo>) : RecyclerView.Adapter<StickerCategoryAdapter.StickerViewHolder>() {
    private var lastSelection = 0

    init {
        if (items.size > 0) {
            items[lastSelection].isSelected = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        return StickerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_sticker_category, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        Glide.with(context)
                .load(items[position].thumbUrl)
                .into(holder.ivSticker)

        holder.tvCategoryName.text = items[position].categoryTitle

        holder.bottomLine.visibility = if (items[position].isSelected) View.VISIBLE else View.GONE
    }

    fun updateSelection(position: Int) {
        items[lastSelection].isSelected = false
        items[position].isSelected = true

        lastSelection = position
        notifyDataSetChanged()
    }

    fun getItem(position: Int): StickerInfo {
        return items[position]
    }

    class StickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivSticker: SquareImageView = view.ivSticker
        val tvCategoryName: AppCompatTextView = view.tvCategoryName
        val bottomLine: View = view.bottomLine
    }
}
