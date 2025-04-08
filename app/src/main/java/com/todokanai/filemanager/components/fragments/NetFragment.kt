package com.todokanai.filemanager.components.fragments

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstractlogics.NetFragmentLogic
import com.todokanai.filemanager.adapters.NetRecyclerAdapter
import kotlinx.coroutines.launch

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

    override fun collectUiState(){
        lifecycleScope.launch {
            viewModel.uiState.collect {
                netAdapter.submitList(it)
            }
        }
    }

    override fun backButtonCallback() {
        viewModel.toParent()
    }
}