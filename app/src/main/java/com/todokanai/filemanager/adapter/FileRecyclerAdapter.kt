package com.todokanai.filemanager.adapter

import android.icu.text.DateFormat.getDateTimeInstance
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.tools.FileAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FileRecyclerAdapter( private val onItemClick:(file:File)->Unit, private val onItemLongClick:(file:File)->Unit ) : RecyclerView.Adapter<FileRecyclerViewHolder>(){

    private val fAction = FileAction()
    var filesAndFolders = emptyArray<File>()
    var selectedList = emptySet<File>()
    var sort : Int = SORT_BY_NAME

    private fun sortedList(sort:Int):List<File> {
        if(sort == SORT_BY_SIZE){
            return filesAndFolders.sortedWith(compareBy({it.extension},{ it.length() }))
        } else if (sort == SORT_BY_DATE) {
            return filesAndFolders.sortedWith(compareBy({it.extension},{ it.lastModified()}))
        } else {
            return filesAndFolders.sortedWith(compareBy({it.extension},{it.name}))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.file_list_recycler,parent,false)
        return FileRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sortedList(sort).size
    }

    override fun onBindViewHolder(holder: FileRecyclerViewHolder, position: Int) {
        val file = sortedList(sort)[position]
        fun selected(): Boolean {
            return selectedList.contains(file)
        }

        fun setContents(file: File) {
            CoroutineScope(Dispatchers.IO).launch {
                val fileName = file.name
                val fileDate = getDateTimeInstance().format(file.lastModified())
                val folderIcon = R.drawable.ic_baseline_folder_24
                val pdfIcon = R.drawable.ic_pdf
                val defaultIcon = R.drawable.ic_baseline_insert_drive_file_24
                val selectedColor = R.color.purple_200
                val defaultColor = 0
                val isSelected = selected()

                withContext(Dispatchers.Main) {
                    holder.typeImage.setImageResource(defaultIcon)  // #1으로 이동해야할지도?
                    if (file.isDirectory) {
                        holder.typeImage.setImageResource(folderIcon)
                    } else {
                        when (file.extension) {
                            "pdf" ->
                                holder.typeImage.setImageResource(pdfIcon)
                            "jpg", "jpeg", "png", "bmp", "gif", "webp" ->
                                holder.typeImage.setImageBitmap(fAction.getThumbnail(file))
                          // #1  else -> holder.typeImage.setImageResource(defaultIcon)

                        }
                    }

                    if (isSelected) {
                        holder.itemView.setBackgroundResource(selectedColor)
                    } else {
                        holder.itemView.setBackgroundColor(defaultColor)
                    }

                    holder.fileName.text = fileName
                    holder.fileDate.text = fileDate
                    if (file.isDirectory) {
                        holder.fileSize.text = "${file.listFiles()?.size} 개"
                    }else{
                        holder.fileSize.text = fAction.fileSizeFormat(file.length())
                    }
                }
            }
        }

        setContents(file)

        holder.itemView.setOnClickListener {
            onItemClick(file)
            notifyItemChanged(position)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(file)
            true
        }
    }


}
private const val SORT_BY_NAME : Int = 1
private const val SORT_BY_DATE : Int = 2
private const val SORT_BY_SIZE : Int = 3