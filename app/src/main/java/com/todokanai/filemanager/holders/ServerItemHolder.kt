package com.todokanai.filemanager.holders

import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.databinding.ServerRecyclerBinding

class ServerItemHolder(
    val binding: ServerRecyclerBinding,
    val onDeleteServer: (ServerHolderItem) -> Unit,
    val onItemClick: (ServerHolderItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onInit(item: ServerHolderItem) {
        binding.run {
            serverName.run {
                text = item.name
                setOnClickListener {
                    onItemClick(item)
                }
            }
            serverDeleteButton.setOnClickListener {
                onDeleteServer(item)
            }

        }
    }
}