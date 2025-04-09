package com.todokanai.filemanager.components.fragments

import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstractlogics.NetFragmentLogic
import com.todokanai.filemanager.adapters.NetRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NetFragment : NetFragmentLogic() {

    override fun prepareView() {
        netAdapter = NetRecyclerAdapter(
            onItemClick = {viewModel.onItemClick(it)}
        )

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