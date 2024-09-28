package com.todokanai.filemanager.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.base.BaseRecyclerAdapter
import com.todokanai.filemanager.holders.FileItemHolder
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.test.MyItemKeyProvider
import com.todokanai.filemanager.test.MyItemLookup
import kotlinx.coroutines.flow.Flow
import java.io.File

class FileListRecyclerAdapter(
    private val onItemLongClick:(File)->Unit,
    private val onFileClick:(Context, File)->Unit,
    itemListNew: Flow<List<File>>,
    val lifecycleOwner: LifecycleOwner,
): BaseRecyclerAdapter<File, FileItemHolder>(itemListNew,lifecycleOwner) {

    private val modeManager = Objects.modeManager
    var selectedItems = emptyArray<File>()
    //var selectedItemsGeneric = emptyArray<Int>()
    lateinit var selectionTracker: SelectionTracker<Long>

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

        selectionTracker = SelectionTracker.Builder(
            "my_selection_tracker_id",
            recyclerView,
            MyItemKeyProvider(recyclerView),
            MyItemLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()


        modeManager.run{
            selectedFiles.asLiveData().observe(lifecycleOwner){
                selectedItems = it
                notifyDataSetChanged()
            }
            isMultiSelectMode.asLiveData().observe(lifecycleOwner){
                notifyDataSetChanged()
            }
        }
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler,parent,false)
        return FileItemHolder(view)
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
                  //  fetchSelectedItems()
                    if(modeManager.isMultiSelectMode()){
                        modeManager.toggleToSelectedFiles(file)
                        /*
                        val pos = position.toLong()
                        val test = selectionTracker.hasSelection()
                        println("hasSelection: $test")
                        if(selection_td.contains(pos)) {
                            println("select: ${selectionTracker.deselect(pos)}")
                        }else{
                            println("select: ${selectionTracker.select(pos)}")
                        }
                        println("hasSelection: $test")

                         */

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
                }
            }
        }
    }
    fun fetchSelectedItems():Set<File>{
        val out = mutableSetOf<File>()
        selectionTracker.selection.forEach(){
            out.add(itemList[it.toInt()])
        }
        println("fetch: ${out.map { it.name }}")
        return out
    }
}