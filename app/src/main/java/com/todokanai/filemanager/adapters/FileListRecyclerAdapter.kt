package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder
import com.todokanai.filemanager.abstracts.multiselectrecyclerview.MultiSelectRecyclerAdapter
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.holders.FileItemHolder
import kotlinx.coroutines.flow.Flow

class FileListRecyclerAdapter(
    private val onFileClick:(FileHolderItem)->Unit,
    itemList: Flow<List<FileHolderItem>>
): MultiSelectRecyclerAdapter<FileHolderItem>(itemList) {

    private val _bottomMenuEnabled = MutableLiveData<Boolean>(false)
    val bottomMenuEnabled : LiveData<Boolean>
        get() = _bottomMenuEnabled

    fun fetchSelectedItems() = selectedItems().toTypedArray()
    override val selectionId = "selectionId"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler,parent,false)
        return FileItemHolder(view)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<FileHolderItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val file = itemList[position]

        holder.run {
            itemView.run{
                setOnClickListener {
                    if(isSelectionEnabled){
                        updateToSelection(position)
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

    override fun observerCallback() {
        /** position of item (starts from 0 ) **/
        val position = selectionTracker.selection.map{it.toInt()}
        println("observerCallback: $position")
    }

    override fun onSelectionChanged(holder: BaseRecyclerViewHolder<FileHolderItem>, isSelected: Boolean) {
        holder.onSelectionChanged(isSelected)
    }

    override fun onSelectionStateChanged(enabled: Boolean) {
        super.onSelectionStateChanged(enabled)
        _bottomMenuEnabled.value = enabled
    }
}