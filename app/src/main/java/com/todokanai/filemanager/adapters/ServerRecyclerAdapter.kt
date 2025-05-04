package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.databinding.ServerRecyclerBinding
import com.todokanai.filemanager.holders.ServerItemHolder

class ServerRecyclerAdapter(
    private val onDeleteServer: (ServerHolderItem) -> Unit,
    private val onItemClick: (ServerHolderItem) -> Unit
) : ListAdapter<ServerHolderItem, ServerItemHolder>(
    object : DiffUtil.ItemCallback<ServerHolderItem>() {
        override fun areItemsTheSame(
            oldItem: ServerHolderItem,
            newItem: ServerHolderItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ServerHolderItem,
            newItem: ServerHolderItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerItemHolder {
        val binding =
            ServerRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServerItemHolder(binding, onDeleteServer,onItemClick)
    }

    override fun onBindViewHolder(holder: ServerItemHolder, position: Int) {
        val item = getItem(position)
        holder.onInit(item)
    }

}