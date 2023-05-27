package com.todokanai.filemanager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.adapter.DirectoryRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentOptionsBinding
import com.todokanai.filemanager.viewmodel.OptionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class OptionsFragment : Fragment() {

    private val binding by lazy{FragmentOptionsBinding.inflate(layoutInflater)}
    private val viewModel : OptionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val adapter = DirectoryRecyclerAdapter({viewModel.onItemClick(requireContext(),it)})
        binding.directoryRecyclerView.adapter = adapter
        binding.directoryRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        binding.run {
            storageBtn.setOnClickListener { viewModel.leftBtn(requireContext(), it) }
            middleBtn.setOnClickListener { viewModel.middleBtn(requireActivity()) }
            rightBtn.setOnClickListener { viewModel.rightBtn(requireContext(), it) }
        }
        viewModel.run {
            path.asLiveData().observe(viewLifecycleOwner) {
                adapter.directoryTree = viewModel.dirTree(File(it))
                adapter.notifyDataSetChanged()
                viewModel.updateContents()
            }
            storageList.asLiveData().observe(viewLifecycleOwner){
                viewModel.updateStorageList(it)
            }
        }

        return binding.root
    }
}