package com.todokanai.filemanager.abstracts

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow

/** Handles recyclerView Item update **/
abstract class BaseRecyclerAdapter<E:Any,VH:RecyclerView.ViewHolder>(
    private val itemFlow: Flow<List<E>>,
    private val lifecycleOwner: LifecycleOwner
):RecyclerView.Adapter<VH>() {

    open lateinit var selectionTracker: SelectionTracker<Long>
    open var itemList = emptyList<E>()

  //  open var enableSelection:Boolean = false

    fun toggleSelection(itemId:Long){
        /// val test = selectionTracker.hasSelection()    //  값이  false로 뜨고있음. 여기부터 해결할 것.
        if(selectionTracker.selection.contains(itemId)) {
            selectionTracker.deselect(itemId)
        }else{
            selectionTracker.select(itemId)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        // Todo: Memory Leak이 발생하는지 여부 체크할 것
        itemFlow.asLiveData().observe(lifecycleOwner){
            itemList = it
            notifyDataSetChanged()
        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
}