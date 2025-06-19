package com.todokanai.filemanager.components.fragments

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstracts.BaseFragment
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.NetRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FragmentNetBinding
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.viewmodel.NetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class NetFragment(viewPagerAdapter: ViewPagerAdapter) : BaseFragment() {

    private val selected = Variables.selectedItems
    private val bottomMenuEnabled = selected.map {
        it.isNotEmpty()
    }

    lateinit var netAdapter: NetRecyclerAdapter
    lateinit var directoryAdapter: DirectoryRecyclerAdapter

    override val binding by lazy { FragmentNetBinding.inflate(layoutInflater) }
    private val viewModel: NetViewModel by viewModels()

    override fun prepareLateInit() {
        netAdapter = NetRecyclerAdapter(
            onItemClick = { viewModel.onItemClick(it) },
            onItemLongClick = { viewModel.onItemLongClick(it) }
        ).apply {
            setHasStableIds(true)
        }
        directoryAdapter = DirectoryRecyclerAdapter(
            onClick = { viewModel.onDirectoryClick(it) }
        )
    }

    override fun prepareView() {
        binding.run {
            netRecyclerView.run {
                val verticalManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = netAdapter
                layoutManager = verticalManager
                addItemDecoration(DividerItemDecoration(context, verticalManager.orientation))
            }

            netDirectoryRecyclerView.run {
                val horizontalManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = directoryAdapter
                layoutManager = horizontalManager
            }
            bottomMenuEnabled.asLiveData().observe(viewLifecycleOwner) {
                netBottomMenuLayout.visibility =
                    if (it) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
        }
    }

    override suspend fun collectUIState() {
        viewModel.uiState.collect { uiState ->
            netAdapter.submitList(uiState.itemList)
            directoryAdapter.submitList(uiState.dirTree)
            binding.netEmptyDirectoryText.visibility =
                if (uiState.emptyDirectoryText) View.VISIBLE else View.INVISIBLE
        }
    }

    override val overrideBackButton: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.toParent({ viewPagerAdapter.toNetFragment(it) })
            }
        }
}