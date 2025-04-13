package com.todokanai.filemanager.components.fragments

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstractlogics.FileListFragmentLogics
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.FileListRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class FileListFragment : FileListFragmentLogics() {

    override val binding by lazy { FragmentFileListBinding.inflate(layoutInflater) }
    private val viewModel: FileListViewModel by viewModels()
    lateinit var fileListAdapter: FileListRecyclerAdapter
    lateinit var directoryAdapter: DirectoryRecyclerAdapter

    override fun prepareLateInit() {
        fileListAdapter = FileListRecyclerAdapter(
            onFileClick = {
                viewModel.onFileClick(requireActivity(), it.file())
            },
        ).apply {
            setHasStableIds(true)
        }

        directoryAdapter = DirectoryRecyclerAdapter(
            {
                if (!fileListAdapter.selectionTracker.hasSelection()) {
                    viewModel.onDirectoryClick(it)
                }
            },
        )
    }

    override fun prepareView() {
        val horizontalManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.directoryRecyclerView.run {
            adapter = directoryAdapter
            layoutManager = horizontalManager
        }

        val verticalManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.fileListRecyclerView.run {
            adapter = fileListAdapter
            layoutManager = verticalManager
            addItemDecoration(DividerItemDecoration(context, verticalManager.orientation))
        }
    }

    override fun collectUIState() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                fileListAdapter.submitList(
                    it.listFiles
                )
                directoryAdapter_bug(it.dirTree)
                binding.emptyDirectoryText.visibility =
                    if (it.emptyDirectoryText == true) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                binding.accessFailText.visibility =
                    if (it.accessFailText == true) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
        }
        fileListAdapter.bottomMenuEnabled.observe(viewLifecycleOwner) { enabled ->
            if (enabled) {
                binding.bottomMenuLayout.visibility = View.VISIBLE
            } else {
                binding.bottomMenuLayout.visibility = View.GONE
            }
        }
        directoryAdapterBugFix()
    }

    override fun overrideBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (fileListAdapter.selectionTracker.hasSelection()) {
                        fileListAdapter.selectionTracker.clearSelection()
                    } else {
                        viewModel.onBackPressed()
                    }
                }
            })
    }

    private fun directoryAdapter_bug(dirTree: List<File>) {
        directoryAdapter.submitList(dirTree)   // Todo: submitList 가 호출되지 않고 있음
    }

    /** temporary fix for directoryAdapter uiState collect issue  [directoryAdapter_bug] **/
    private fun directoryAdapterBugFix() {
        viewModel.dirTree.asLiveData().observe(viewLifecycleOwner) {
            directoryAdapter.submitList(it)
        }
    }

}