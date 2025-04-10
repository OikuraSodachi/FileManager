package com.todokanai.filemanager.abstracts.multiselectrecyclerview

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

/** Base class of [ItemDetailsLookup] for [MultiSelectRecyclerAdapter] **/
class BaseItemLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        val holder = recyclerView.getChildViewHolder(view)
        return if (holder is RecyclerView.ViewHolder) {
            object : ItemDetails<Long>() {
                override fun getPosition(): Int {
                    return holder.adapterPosition
                }

                override fun getSelectionKey(): Long {
                    return holder.itemId
                }
            }
        } else {
            null
        }
    }
}