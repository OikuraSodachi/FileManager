package com.todokanai.filemanager.abstracts.multiselectrecyclerview

import android.view.MotionEvent
import androidx.annotation.CallSuper
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/** RecyclerAdapter with multi-selection feature
 *
 * key:Long == position:Int .toLong()
 *
 * position:Int == key:Long .toInt()
 *
 * selectionTracker.hasSelection() -> return false if selection is a empty list **/
abstract class MultiSelectRecyclerAdapter<E:Any,VH:RecyclerView.ViewHolder>(
    diffUtil:DiffUtil.ItemCallback<E>
): ListAdapter<E, VH>(diffUtil) {
    lateinit var selectionTracker: SelectionTracker<Long>
    abstract val selectionId:String

    /** called when a change occurs in the selection
     *
     * view 의 갱신 처리 등 작업은 여기서 하는 게 맞는 듯? **/
    abstract fun onSelectionChanged(index:Int,item:E)

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        selectionBugFix(recyclerView)

        selectionTracker = SelectionTracker.Builder(
            selectionId,
            recyclerView,
            BaseItemKeyProvider(recyclerView),
            BaseItemLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            BaseSelectionPredicate(recyclerView)
        )
            .build()

        selectionTracker.addObserver(
            BaseSelectionObserver(
                callback = {
                    currentList.run{
                        forEachIndexed { index, item ->
                            onSelectionChanged(index,item)
                        }
                    }
                }
            )
        )
    }

    override fun getItemId(position: Int):Long{
        return position.toLong()
    }

    /** workaround fix for selection being cleared on touching outside
     *
     * [issueTracker link](https://issuetracker.google.com/issues/177046288#comment7) **/
    private fun selectionBugFix(recyclerView: RecyclerView){
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                val down = event.actionMasked == MotionEvent.ACTION_DOWN
                if(!down || !selectionTracker.hasSelection()) {
                    return false //Don't intercept, otherwise you break scrolling
                }

                val view = recyclerView.findChildViewUnder(event.x, event.y)
                return view == null
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) { }
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }
        })
    }
}