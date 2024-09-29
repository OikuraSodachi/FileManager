package com.todokanai.filemanager.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.base.BaseRecyclerAdapter
import com.todokanai.filemanager.holders.FileItemHolder
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.test.MyItemKeyProvider
import com.todokanai.filemanager.test.MyItemLookup
import com.todokanai.filemanager.test.MySelectionPredicate
import kotlinx.coroutines.flow.Flow
import java.io.File

class FileListRecyclerAdapter(
    private val onItemLongClick:(File)->Unit,
    private val onFileClick:(Context, File)->Unit,
    itemListNew: Flow<List<File>>,
    val lifecycleOwner: LifecycleOwner,
): BaseRecyclerAdapter<File, FileItemHolder>(itemListNew,lifecycleOwner) {

    private val modeManager = Objects.modeManager

    fun fetchSelectedItems():Array<File>{
        val out = selectionTracker.selection.map{
            itemList[it.toInt()]
        }.toTypedArray()
        return out
    }

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
                callback = { callback(it) }
            )
        )
        modeManager.run{
            /*
            selectedFiles.asLiveData().observe(lifecycleOwner){
                selectedItems = it
                notifyDataSetChanged()
            }

             */
            isMultiSelectMode.asLiveData().observe(lifecycleOwner){
                notifyDataSetChanged()
            }
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
                    if(modeManager.isMultiSelectMode()){
                        modeManager.toggleToSelectedFiles(file)
                        toggleSelection(itemId)
                    }else{
                        onFileClick(context,file)
                    }
                    //  onItemClick(file,isMultiSelectMode())
                }
                setOnLongClickListener {
                    if(modeManager.isDefaultMode()) {
                        onItemLongClick(file)
                    }
                    true
                }
                setBackgroundColor(backgroundColor)
                if(modeManager.isMultiSelectMode()) {
                    multiSelectMode(isFileSelected)
                } else{
                    onDefaultMode()
                    selectionTracker.clearSelection()
                }
            }
        }
    }
    /*
    fun toggleSelection(itemId:Long){
       /// val test = selectionTracker.hasSelection()    //  값이  false로 뜨고있음. 여기부터 해결할 것.
        if(selectionTracker.selection.contains(itemId)) {
            selectionTracker.deselect(itemId)
        }else{
            selectionTracker.select(itemId)
        }
    }

     */
    inner class MySelectionObserver(val selectionTracker: SelectionTracker<Long>,val callback:(List<File>)->Unit): SelectionObserver<Long>() {

        override fun onSelectionChanged() {
            callback(
                selectionTracker.selection.map{
                    itemList[it.toInt()]
                }
            )
            super.onSelectionChanged()
        }
    }
}