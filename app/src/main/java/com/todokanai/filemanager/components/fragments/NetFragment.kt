package com.todokanai.filemanager.components.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.adapters.NetRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentNetBinding
import com.todokanai.filemanager.viewmodel.NetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetFragment : Fragment() {

    lateinit var listAdapter : NetRecyclerAdapter
    private val viewModel:NetViewModel by viewModels()
    private val binding by lazy{FragmentNetBinding.inflate(layoutInflater)}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listAdapter = NetRecyclerAdapter(
            onItemClick = {viewModel.onItemClick(it)}
        )

        val verticalManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        binding.netRecyclerView.run{
            adapter = listAdapter
            layoutManager = verticalManager
            addItemDecoration(DividerItemDecoration(context, verticalManager.orientation))
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.toParent()
            }
        })
        viewModel.itemFlow.asLiveData().observe(viewLifecycleOwner){
            listAdapter.updateDataSet(it)
        }
        return binding.root
    }

}