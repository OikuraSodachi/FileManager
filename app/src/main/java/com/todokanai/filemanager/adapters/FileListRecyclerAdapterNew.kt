package com.todokanai.filemanager.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.selection.Selection
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
import com.todokanai.filemanager.test.MySelectionObserver
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
    val selection_td by lazy{selectionTracker.selection}

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

        selectionTracker = SelectionTracker.Builder(
            "my_selection_tracker_id",
            recyclerView,
            MyItemKeyProvider(recyclerView),
            MyItemLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
            .apply { addObserver(MySelectionObserver()) }


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

    override fun getItemId(position: Int):Long{
        return position.toLong()
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
                    if(modeManager.isMultiSelectMode()){
                        modeManager.toggleToSelectedFiles(file)
                        onClickAddOn(itemId)
                        notifyDataSetChanged()
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
    fun fetchSelectedItems(selection: Selection<Long>):Set<File>{
        val out = mutableSetOf<File>()
        selection.forEach(){
            out.add(itemList[it.toInt()])
        }
        return out
    }

    fun onClickAddOn(itemId:Long){
        //   /*
        val test = selectionTracker.hasSelection()    //  값이  false로 뜨고있음. 여기부터 해결할 것.
        if(selectionTracker.selection.contains(itemId)) {
            selectionTracker.deselect(itemId)
        }else{
            selectionTracker.select(itemId)
        }
        println("hasSelection: $test")
        println("fetch: ${fetchSelectedItems(selection_td)}")
    }
}