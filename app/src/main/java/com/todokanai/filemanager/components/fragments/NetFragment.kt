package com.todokanai.filemanager.components.fragments

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstracts.BaseFragment
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.NetRecyclerAdapter
import com.todokanai.filemanager.adapters.ServerRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FragmentNetBinding
import com.todokanai.filemanager.viewmodel.NetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetFragment(viewPagerAdapter: ViewPagerAdapter) : BaseFragment() {

    lateinit var netAdapter: NetRecyclerAdapter
    lateinit var directoryAdapter: DirectoryRecyclerAdapter
    lateinit var serverAdapter: ServerRecyclerAdapter

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

        serverAdapter = ServerRecyclerAdapter(
            onDeleteServer = { viewModel.deleteServer(it) },
            onItemClick = { viewModel.onServerClick(it) }
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
            netAdapter.bottomMenuEnabled.observe(viewLifecycleOwner) {
                netBottomMenuLayout.visibility =
                    if (it) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
            serverRecyclerView.run {
                val linearManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = serverAdapter
                layoutManager = linearManager
                DividerItemDecoration(context, linearManager.orientation)
            }
            serverAddButton.setOnClickListener {
                viewModel.saveServerInfo(
                    name = nameEditText.text.toString(),
                    ip = ipEditText.text.toString(),
                    id = idEditText.text.toString(),
                    password = passwordEditText.text.toString()
                )
            }
        }
    }

    override suspend fun collectUIState() {
        viewModel.uiState.collect { uiState ->
            netAdapter.submitList(uiState.itemList)
            directoryAdapter.submitList(uiState.dirTree)
            serverAdapter.submitList(uiState.serverList)
            if (uiState.loggedIn) {
                binding.run {
                    netLayout.visibility = View.VISIBLE
                    loginLayout.visibility = View.GONE
                }
            } else {
                binding.run {
                    netLayout.visibility = View.GONE
                    loginLayout.visibility = View.VISIBLE
                }
            }
        }
//        netAdapter.bottomMenuEnabled.observe(viewLifecycleOwner) {
//            binding.netBottomMenuLayout.visibility =
//                if (it) {
//                    View.VISIBLE
//                } else {
//                    View.GONE
//                }
//        }
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