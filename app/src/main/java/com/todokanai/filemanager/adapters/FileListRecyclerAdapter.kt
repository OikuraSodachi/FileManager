package com.todokanai.filemanager.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.holders.FileItemHolder
import java.io.File

class FileListRecyclerAdapter(
    private val onItemLongClick:(File)->Unit,
    private val isDefaultMode:()->Boolean,
    private val isMultiSelectMode:()->Boolean,
    private val toggleToSelectedFiles:(File)->Unit,
    private val onFileClick:(Context, File)->Unit
): RecyclerView.Adapter<FileItemHolder>() {

    var itemList = emptyList<File>()
    var selectedItems = emptyArray<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler,parent,false)
        return FileItemHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val file = itemList[position]
        val isFileSelected = selectedItems.contains(file)

        val backgroundColor =
            if (isFileSelected) {
                Color.GRAY
            } else {
                0
            }

        holder.run {
            setData(file)
            itemView.run{
                setOnClickListener {
                    if(isMultiSelectMode()){
                        toggleToSelectedFiles(file)
                    }else{
                        onFileClick(context,file)
                    }

                  //  onItemClick(file,isMultiSelectMode())
                }
                setOnLongClickListener {
                    if(isDefaultMode()) {
                        onItemLongClick(file)
                    }
                    true
                }
                setBackgroundColor(backgroundColor)
                if(isMultiSelectMode()) {
                    multiSelectMode(isFileSelected)
                } else{
                    onDefaultMode()
                }
            }
        }
    }
}