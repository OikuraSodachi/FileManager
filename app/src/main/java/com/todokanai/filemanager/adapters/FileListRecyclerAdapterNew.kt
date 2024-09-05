package com.todokanai.filemanager.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.holders.FileItemHolder
import com.todokanai.filemanager.tools.SelectModeManager
import java.io.File

class FileListRecyclerAdapterNew(
    private val onItemClick:(File,Boolean)->Unit,
    private val onItemLongClick:(File,Boolean)->Unit,
    private val modeManager:SelectModeManager
): RecyclerView.Adapter<FileItemHolder>() {

    var itemList = emptyList<File>()
    var selectedItemList = emptyArray<File>()

    private fun isMultiSelectMode() = modeManager.isMultiSelectMode()
    private fun isDefaultMode() = modeManager.isDefaultMode()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler,parent,false)
        return FileItemHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val file = itemList[position]
        val isFileSelected = selectedItemList.contains(file)

        val backgroundColor =
            if (isFileSelected) {
                Color.GRAY
            } else {
                0
            }

        holder.run {
            setData(file)
            itemView.run{
                setOnClickListener { onItemClick(file,isMultiSelectMode()) }
                setOnLongClickListener {
                    onItemLongClick(file,isDefaultMode())
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