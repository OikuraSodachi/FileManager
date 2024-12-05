package com.todokanai.filemanager.abstracts

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<E:Any>(view: View):RecyclerView.ViewHolder(view) {

    abstract fun onInit(item:E)

    /** for [com.todokanai.baseproject.abstracts.multiselectrecyclerview.MultiSelectRecyclerAdapter]
     * @param isSelected is the item selected
     * **/
    open fun onSelectionChanged(isSelected:Boolean){

    }
}