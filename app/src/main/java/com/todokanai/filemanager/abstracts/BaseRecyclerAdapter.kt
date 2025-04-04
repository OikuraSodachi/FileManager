package com.todokanai.filemanager.abstracts

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/** dummy data **/
abstract class BaseRecyclerAdapter<E:Any,VH:RecyclerView.ViewHolder>(
    areItemsSame:(oldItem:E,newItem:E)->Boolean,
    areContentsSame:(oldItem:E,newItem:E)->Boolean
): ListAdapter<E,VH>(
    object : DiffUtil.ItemCallback<E>(){
        override fun areItemsTheSame(oldItem: E, newItem: E): Boolean {
            return areItemsSame(oldItem,newItem)
        }

        override fun areContentsTheSame(oldItem: E, newItem: E): Boolean {
            return areContentsSame(oldItem,newItem)
        }
    }
) {

}