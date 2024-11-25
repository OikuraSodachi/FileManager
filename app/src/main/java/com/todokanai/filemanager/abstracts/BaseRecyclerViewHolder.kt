package com.todokanai.filemanager.abstracts

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<E:Any>(val view: View):RecyclerView.ViewHolder(view) {

    abstract fun onInit(item:E)
}