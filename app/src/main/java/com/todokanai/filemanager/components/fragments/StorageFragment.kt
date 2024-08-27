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
import com.todokanai.filemanager.databinding.FragmentStorageBinding
import com.todokanai.filemanager.viewmodel.StorageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageFragment : Fragment() {

    private val viewModel : StorageViewModel by viewModels()
    private val binding by lazy{FragmentStorageBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val storageAdapter = StorageRecyclerAdapter({viewModel.onItemClick(it)})
        val manager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.storageRecyclerView.run{
            adapter = storageAdapter
            layoutManager = manager
        }

        viewModel.storageHolderList.asLiveData().observe(viewLifecycleOwner){
            storageAdapter.storageList = it
            storageAdapter.notifyDataSetChanged()
        }
        return binding.root
    }
}