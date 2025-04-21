package com.todokanai.filemanager.components.fragments

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.todokanai.filemanager.abstracts.ViewPagerFragment
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.NetRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentNetBinding
import com.todokanai.filemanager.viewmodel.NetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NetFragment(val parentViewPager: ViewPager2) : ViewPagerFragment() {

    lateinit var netAdapter: NetRecyclerAdapter
    lateinit var directoryAdapter: DirectoryRecyclerAdapter
    override val binding by lazy { FragmentNetBinding.inflate(layoutInflater) }
    private val viewModel: NetViewModel by viewModels()

    override fun prepareLateInit() {
        netAdapter = NetRecyclerAdapter(
            onItemClick = { viewModel.onItemClick(it) }
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
        }
    }

    override fun collectUIState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                netAdapter.submitList(uiState.itemList)
                directoryAdapter.submitList(uiState.dirTree)
            }
        }
        netAdapter.bottomMenuEnabled.observe(viewLifecycleOwner) {
            binding.netBottomMenuLayout.visibility =
                if (it) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    override val overrideBackButton: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (netAdapter.selectionTracker.hasSelection()) {
                    netAdapter.selectionTracker.clearSelection()
                } else {
                    viewModel.toParent()
                }
            }
        }
}