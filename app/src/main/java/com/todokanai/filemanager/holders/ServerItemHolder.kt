package com.todokanai.filemanager.holders

import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.databinding.ServerRecyclerBinding

class ServerItemHolder(val binding:ServerRecyclerBinding,val onClick:(ServerHolderItem)->Unit): RecyclerView.ViewHolder(binding.root) {

    fun onInit(item:ServerHolderItem){
        binding.run{
            serverName.text = item.name
        }
        itemView.setOnClickListener{
            onClick(item)
        }
    }
}