package com.todokanai.filemanager.components.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.adapters.StorageRecyclerAdapter
import com.todokanai.filemanager.components.activity.MainActivity.Companion.fragmentCode
import com.todokanai.filemanager.databinding.FragmentStorageBinding
import com.todokanai.filemanager.viewmodel.StorageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageFragment : Fragment() {

    private val viewModel: StorageViewModel by viewModels()
    private val binding by lazy { FragmentStorageBinding.inflate(layoutInflater) }
    private val linearManager by lazy {
        LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }
    private lateinit var storageAdapter: StorageRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        storageAdapter = StorageRecyclerAdapter(
            onItemClick = {
                viewModel.onItemClick(it)
                fragmentCode.value = 2
            },
            //itemFlow = viewModel.storageHolderList
        )
        binding.storageRecyclerView.run {
            adapter = storageAdapter
            layoutManager = linearManager
        }

        viewModel.storageHolderList.asLiveData().observe(viewLifecycleOwner) {
            storageAdapter.submitList(it)
        }

        return binding.root
    }
}