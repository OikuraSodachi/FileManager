package com.todokanai.filemanager.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder
import com.todokanai.filemanager.abstracts.multiselectrecyclerview.MultiSelectRecyclerAdapter
import com.todokanai.filemanager.holders.FileItemHolder
import kotlinx.coroutines.flow.Flow
import java.io.File

class FileListRecyclerAdapter(
    private val onItemLongClick:(File)->Unit,
    private val onFileClick:(Context, File)->Unit,
    itemList: Flow<List<File>>,
    val isDefaultMode:()->Boolean
): MultiSelectRecyclerAdapter<File>(itemList) {

    override var isSelectionEnabled: Boolean = false
    fun fetchSelectedItems() = selectedItems.toTypedArray()
    override val selectionId: String
        get() = "selectionId"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler,parent,false)
        return FileItemHolder(view)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<File>, position: Int) {
        super.onBindViewHolder(holder, position)
        val file = itemList[position]

        holder.run {
            itemView.run{
                setOnClickListener {
                    if(isSelectionEnabled){
                        toggleSelection(itemId)
                    }else{
                        onFileClick(context,file)
                    }
                }
                setOnLongClickListener {
                    if(isDefaultMode()) {
                        onItemLongClick(file)
                    }
                    true
                }
            }
        }
    }

    override fun selectedHolderUI(holder: BaseRecyclerViewHolder<File>, isSelected: Boolean) {
        if(isSelected){
            holder.view.setBackgroundColor(Color.GRAY)
        }else{
            holder.view.setBackgroundColor(0)
        }
    }
}