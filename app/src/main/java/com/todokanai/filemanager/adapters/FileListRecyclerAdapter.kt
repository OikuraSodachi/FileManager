package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder
import com.todokanai.filemanager.abstracts.multiselectrecyclerview.MultiSelectRecyclerAdapter
import com.todokanai.filemanager.holders.FileItemHolder
import kotlinx.coroutines.flow.Flow
import java.io.File

class FileListRecyclerAdapter(
    private val onFileClick:(File)->Unit,
    itemList: Flow<List<File>>
): MultiSelectRecyclerAdapter<File>(itemList) {

    fun fetchSelectedItems() = selectedItems().toTypedArray()
    override val selectionId = "selectionId"

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
                        toggleSelection(position)
                    }else{
                        onFileClick(file)       // default
                    }
                }
                setOnLongClickListener {
                    if(!isSelectionEnabled) {
                        isSelectionEnabled = true
                    }
                    true
                }
            }
        }
    }
    /*
    override fun selectedHolderUI(holder: BaseRecyclerViewHolder<File>, isSelected: Boolean) {
        if(isSelected){
            holder.view.setBackgroundColor(Color.GRAY)
        }else{
            holder.view.setBackgroundColor(0)
        }
    }
     */

    override fun observerCallback() {
        /** position of item (starts from 0 ) **/
        val position = selectionTracker.selection.map{it.toInt()}
        println("observerCallback: $position")    }


    fun toDefaultMode(){
        selectionTracker.clearSelection()
        isSelectionEnabled = false
    }
}