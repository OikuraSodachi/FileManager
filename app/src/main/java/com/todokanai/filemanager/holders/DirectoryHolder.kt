package com.todokanai.filemanager.holders

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.databinding.DirectoryRecyclerBinding

class DirectoryHolder(val binding:DirectoryRecyclerBinding, private val onClick: (DirectoryHolderItem) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun onInit(item: DirectoryHolderItem) {
        binding.directoryName.text = item.name
        binding.divider.setImageDrawable(
            getDrawable(
                itemView.context,
                R.drawable.baseline_label_important_24
            )
        )
        itemView.setOnClickListener {
            onClick(item)
        }
    }
}