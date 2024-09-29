package com.todokanai.filemanager.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.base.BaseRecyclerAdapter
import com.todokanai.filemanager.holders.FileItemHolder
import com.todokanai.filemanager.test.MyItemKeyProvider
import com.todokanai.filemanager.test.MyItemLookup
import com.todokanai.filemanager.test.MySelectionObserver
import com.todokanai.filemanager.test.MySelectionPredicate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class FileListRecyclerAdapter(
    private val onItemLongClick:(File)->Unit,
    private val onFileClick:(Context, File)->Unit,
    itemListNew: Flow<List<File>>,
    val lifecycleOwner: LifecycleOwner,
    val isMultiSelectMode_Unit:()->Boolean,
    val isMultiSelectMode:Flow<Boolean>,
    val isDefaultMode_Unit:()->Boolean
): BaseRecyclerAdapter<File, FileItemHolder>(itemListNew,lifecycleOwner) {

    fun fetchSelectedItems():Array<File>{
        val out = selectionTracker.selection.map{
            itemList[it.toInt()]
        }.toTypedArray()
        return out
    }

    private val _selectedFilesNew = MutableStateFlow<Array<File>>(emptyArray())
    val selectedFilesNew : StateFlow<Array<File>>
        get() = _selectedFilesNew

    private fun callback(selectedList:List<File>){
        println("list: ${selectedList.map{it.name}}")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

        selectionTracker = SelectionTracker.Builder(
            "my_selection_tracker_id",
            recyclerView,
            MyItemKeyProvider(recyclerView),
            MyItemLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(MySelectionPredicate(recyclerView))
            .build()

        selectionTracker.addObserver(
            MySelectionObserver(
                selectionTracker = selectionTracker,
                callback = { callback(it) },
                itemList = { itemList }
            )
        )
        isMultiSelectMode.asLiveData().observe(lifecycleOwner){
            notifyDataSetChanged()
        }
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemId(position: Int):Long{
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler,parent,false)
        return FileItemHolder(view)
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val file = itemList[position]
        val isFileSelected = selectionTracker.selection.contains(position.toLong())

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
                    if(isMultiSelectMode_Unit()){
                        toggleSelection(itemId)
                    }else{
                        onFileClick(context,file)
                    }
                    //  onItemClick(file,isMultiSelectMode())
                }
                setOnLongClickListener {
                    if(isDefaultMode_Unit()) {
                        onItemLongClick(file)
                    }
                    true
                }
                setBackgroundColor(backgroundColor)
                if(isMultiSelectMode_Unit()) {
                    multiSelectMode(isFileSelected)
                } else{
                    onDefaultMode()
                    selectionTracker.clearSelection()
                }
            }
        }
    }
}