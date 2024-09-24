package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.holders.DirectoryHolder
import com.todokanai.filemanager.myobjects.Objects
import kotlinx.coroutines.flow.Flow
import java.io.File

/** Todo: dirTree를 깊게 들어갔다가 다시 상위 경로로 되돌아가면 viewHolder의 크기가 비정상적으로 처리되고 있음 **/
class DirectoryRecyclerAdapter(
    private val onClick:(File)->Unit,
    private val directoryListNew: Flow<List<File>>,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<DirectoryHolder>() {
    var directoryList = emptyList<File>()

    private val modeManager = Objects.modeManager

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // Todo: Memory Leak이 발생하는지 여부 체크할 것
        directoryListNew.asLiveData().observe(lifecycleOwner){
            directoryList = it
            notifyDataSetChanged()
        }
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_recycler,parent,false)
        return DirectoryHolder(view)
    }

    override fun getItemCount(): Int {
        return directoryList.size
    }

    override fun onBindViewHolder(holder: DirectoryHolder, position: Int) {
        val directory = directoryList[position]
        holder.setHolder(directory)
        holder.itemView.setOnClickListener {
            if(modeManager.isNotMultiSelectMode()) {
                onClick(directory)
            }
        }
    }

    fun updateItems(){
    //    directoryListNew.asLiveData().observe()
    }
}