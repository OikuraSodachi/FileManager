package com.todokanai.filemanager.components.fragments

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstractlogics.NetFragmentLogics
import com.todokanai.filemanager.adapters.NetRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentNetBinding
import com.todokanai.filemanager.viewmodel.NetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NetFragment : NetFragmentLogics() {

    lateinit var netAdapter : NetRecyclerAdapter
    override val binding by lazy{ FragmentNetBinding.inflate(layoutInflater) }
    private val viewModel: NetViewModel by viewModels()

    override fun prepareLateInit() {
        netAdapter = NetRecyclerAdapter(
            onItemClick = {viewModel.onItemClick(it)}
        )
    }

    override fun prepareView() {
        val verticalManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        binding.netRecyclerView.run{
            adapter = netAdapter
            layoutManager = verticalManager
            addItemDecoration(DividerItemDecoration(context, verticalManager.orientation))
        }
    }

    override fun collectUIState(){
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                netAdapter.submitList(uiState.itemList)
            }
        }
    }

    override fun overrideBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.toParent()
            }
        })
    }
}