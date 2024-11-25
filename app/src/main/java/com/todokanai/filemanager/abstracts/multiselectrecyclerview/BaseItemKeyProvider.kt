package com.todokanai.filemanager.abstracts.multiselectrecyclerview

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder

/** Base class of [ItemKeyProvider] for [MultiSelectRecyclerAdapter] **/
class BaseItemKeyProvider(private val recyclerView: RecyclerView): ItemKeyProvider<Long>(SCOPE_MAPPED) {
    override fun getKey(position: Int): Long? {
        val holder = recyclerView.findViewHolderForAdapterPosition(position)
        return holder?.itemId ?: throw IllegalStateException("No Holder")    }

    override fun getPosition(key: Long): Int {
        val holder = recyclerView.findViewHolderForItemId(key)
        return if (holder is BaseRecyclerViewHolder<*>) {
            holder.adapterPosition
        } else {
            RecyclerView.NO_POSITION
        }
    }
}