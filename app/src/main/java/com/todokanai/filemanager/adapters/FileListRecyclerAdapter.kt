package com.todokanai.filemanager.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.holders.FileItemHolder
import java.io.File

class FileListRecyclerAdapter(
    private val onItemClick:(File)->Unit,
    private val onItemLongClick:(File)->Unit,
):RecyclerView.Adapter<FileItemHolder>() {

    var isMultiSelectMode = false
    var itemList = emptyList<File>()
    var selectedItemList = emptyArray<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler,parent,false)
        return FileItemHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val file = itemList[position]
        val isSelected by lazy{ selectedItemList.contains(file)}

        val backgroundColor =
            if (isSelected) {
                Color.GRAY
            } else {
                0
            }

        holder.run {
            setDataNew(file)
            itemView.run{
                setOnClickListener { onItemClick(file) }
                setOnLongClickListener {
                    onItemLongClick(file)
                    true
                }
                setBackgroundColor(backgroundColor)
                if(isMultiSelectMode) {
                    multiSelectMode(isSelected)
                } else{
                    defaultMode()
                }
            }
        }
    }
}