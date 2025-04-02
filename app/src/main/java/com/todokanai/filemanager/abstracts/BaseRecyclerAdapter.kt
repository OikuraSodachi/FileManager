package com.todokanai.filemanager.abstracts

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/** Base class of [RecyclerView.Adapter] **/
abstract class BaseRecyclerAdapter<E:Any,VH:RecyclerView.ViewHolder>(): RecyclerView.Adapter<VH>() {

    private var itemList = emptyList<E>()

    fun itemList() = itemList

    @CallSuper
    /** [newList] 값을 갱신하고 , [notifyDataSetChanged] 의 최적화 버전(?)을 호출 **/
    open fun updateDataSet(newList:List<E>){
        val oldList = itemList
        itemList = newList
        val diffResult = DiffUtil.calculateDiff(BaseRecyclerDiffUtil(oldList, newList))
        diffResult.dispatchUpdatesTo(this)
    }    // oldList 와 newList 를 비교해서 dataSetChanged 적용

    override fun getItemCount(): Int {
        return itemList.size
    }

    abstract fun areItemsSame(oldItem:E, newItem:E):Boolean

    inner class BaseRecyclerDiffUtil(
        private val oldList:List<E>,
        private val newList:List<E>
    ): DiffUtil.Callback(){
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsSame(oldList[oldItemPosition],newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}