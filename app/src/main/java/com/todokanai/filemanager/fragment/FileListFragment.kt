package com.todokanai.filemanager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.adapter.FileRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileListFragment : Fragment() {

    private val binding by lazy { FragmentFileListBinding.inflate(layoutInflater) }
    private val viewModel: FileListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackPressed()
        }

        val adapter = FileRecyclerAdapter({ viewModel.onItemClick(requireContext(), it) },
            { viewModel.onItemLongClick(it) })
        binding.fileRecyclerView.adapter = adapter
        binding.fileRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.swipe.setOnRefreshListener {
            viewModel.updateContents()
            adapter.notifyDataSetChanged()
            binding.swipe.isRefreshing = false
        }



        viewModel.run {
            selectMode.asLiveData().observe(viewLifecycleOwner) {
                println("multiSelect = $it")
                when (it) {
                    DEFAULT_MODE -> {
                        binding.bottomMenuConstraint.visibility = View.GONE
                        viewModel.resetSelectedList()
                    }
                    MULTI_SELECT_MODE -> {
                        binding.bottomMenuConstraint.visibility = View.VISIBLE
                        binding.menuLayout.visibility = View.VISIBLE
                        binding.confirmLayout.visibility = View.GONE
                        adapter.notifyDataSetChanged()
                        println("list: ${selectedList.value}")
                    }
                    CONFIRM_MODE ->{
                        binding.bottomMenuConstraint.visibility = View.VISIBLE
                        binding.menuLayout.visibility = View.GONE
                        binding.confirmLayout.visibility = View.VISIBLE
                    }
                }

                path.asLiveData().observe(viewLifecycleOwner) {
                    viewModel.updateContents()
                }       // 현재 directory 값 observe
                fileList.asLiveData().observe(viewLifecycleOwner) {
                    if (it == null) {
                       println("fileList = $it")
                        viewModel.exit(requireActivity())
                    } else if (it.isEmpty()) {
                        binding.nofilesTextview.visibility = View.VISIBLE
                        adapter.filesAndFolders = it
                        adapter.selectedList = selectedList.value

                        adapter.notifyDataSetChanged()
                    } else {
                        binding.nofilesTextview.visibility = View.INVISIBLE
                        adapter.filesAndFolders = it
                        adapter.selectedList = selectedList.value
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            binding.run {
                moveBtn.setOnClickListener { viewModel.moveBtn(viewModel.selectedList.value) }
                copyBtn.setOnClickListener { viewModel.copyBtn(viewModel.selectedList.value) }
                renameBtn.setOnClickListener { viewModel.renameBtn(requireContext()) }
                deleteBtn.setOnClickListener { viewModel.deleteBtn(requireContext()) }
                moreBtn.setOnClickListener { viewModel.moreBtn(requireContext(), it) }
                cancelBtn.setOnClickListener { viewModel.cancelBtn() }
                confirmMoreBtn.setOnClickListener { viewModel.confirmMoreBtn() }
                confirmBtn.setOnClickListener { viewModel.confirmBtn(viewModel.confirmActionType) }
            }
            return binding.root
        }
    }
}