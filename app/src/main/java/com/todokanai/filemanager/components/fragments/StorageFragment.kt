package com.todokanai.filemanager.components.fragments

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstracts.ViewPagerFragment
import com.todokanai.filemanager.adapters.StorageRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FragmentStorageBinding
import com.todokanai.filemanager.viewmodel.StorageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageFragment(val viewPagerAdapter: ViewPagerAdapter) : ViewPagerFragment() {

    private val viewModel: StorageViewModel by viewModels()
    override val binding by lazy { FragmentStorageBinding.inflate(layoutInflater) }
    private val linearManager by lazy {
        LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }
    private lateinit var storageAdapter: StorageRecyclerAdapter

    override fun prepareLateInit() {
        storageAdapter = StorageRecyclerAdapter(
            onItemClick = {
                viewModel.onItemClick(it)
                viewPagerAdapter.toFileListFragment()
            }
        )
    }

    override fun prepareView() {
        binding.storageRecyclerView.run {
            adapter = storageAdapter
            layoutManager = linearManager
        }
    }

    override suspend fun collectUIState() {
        viewModel.storageHolderList.collect {
            storageAdapter.submitList(it)
        }
    }

    override val overrideBackButton: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
}