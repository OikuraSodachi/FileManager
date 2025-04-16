package com.todokanai.filemanager.components.fragments

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstractlogics.NetFragmentLogics
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.NetRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentNetBinding
import com.todokanai.filemanager.viewmodel.NetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NetFragment : NetFragmentLogics() {

    lateinit var netAdapter: NetRecyclerAdapter
    lateinit var directoryAdapter: DirectoryRecyclerAdapter
    override val binding by lazy { FragmentNetBinding.inflate(layoutInflater) }
    private val viewModel: NetViewModel by viewModels()

    override fun prepareLateInit() {
        netAdapter = NetRecyclerAdapter(
            onItemClick = { viewModel.onItemClick(it) }
        )
        directoryAdapter = DirectoryRecyclerAdapter(
            onClick = { viewModel.onDirectoryClick(it) }
        )
    }

    override fun prepareView() {
        binding.run {
            netRecyclerView.run {
                val verticalManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = netAdapter
                layoutManager = verticalManager
                addItemDecoration(DividerItemDecoration(context, verticalManager.orientation))
            }

            netDirectoryRecyclerView.run{
                val horizontalManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = directoryAdapter
                layoutManager = horizontalManager
            }

//            loginButton.setOnClickListener {
//            }
//            loggedInButton.setOnClickListener {
//            }
        }
    }

    override fun collectUIState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                netAdapter.submitList(uiState.itemList)
                directoryAdapter.submitList(uiState.dirTree)
//                binding.loggedInButton.visibility =
//                    if(uiState.loggedIn){
//                        View.VISIBLE
//                    }else{
//                        View.GONE
//                    }
            }
        }
    }

    override fun overrideBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.toParent()
                }
            })
    }
}