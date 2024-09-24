package com.todokanai.filemanager.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.holders.FileItemHolder
import com.todokanai.filemanager.myobjects.Objects
import kotlinx.coroutines.flow.Flow
import java.io.File

class FileListRecyclerAdapter(
    private val onItemLongClick:(File)->Unit,
    private val onFileClick:(Context, File)->Unit,
    private val itemListNew: Flow<List<File>>,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<FileItemHolder>() {

    private val modeManager = Objects.modeManager
    var itemList = emptyList<File>()
    var selectedItems = emptyArray<File>()

    //var selectedItemsGeneric = emptyArray<Int>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        itemListNew.asLiveData().observe(lifecycleOwner){
            itemList = it
            notifyDataSetChanged()
        }
        super.onAttachedToRecyclerView(recyclerView)
    }

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
                    if(modeManager.isMultiSelectMode()){
                        modeManager.toggleToSelectedFiles(file)
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
}