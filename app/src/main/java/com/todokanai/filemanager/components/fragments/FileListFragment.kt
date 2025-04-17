package com.todokanai.filemanager.components.fragments

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstractlogics.FileListFragmentLogics
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.FileListRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FileListFragment : FileListFragmentLogics() {

    override val binding by lazy { FragmentFileListBinding.inflate(layoutInflater) }
    private val viewModel: FileListViewModel by viewModels()
    lateinit var fileListAdapter: FileListRecyclerAdapter
    lateinit var directoryAdapter: DirectoryRecyclerAdapter

    override fun prepareLateInit() {
        fileListAdapter = FileListRecyclerAdapter(
            onFileClick = {
                viewModel.onFileClick(requireActivity(), it)
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    fileListAdapter.submitList(it.listFiles)
                    directoryAdapter.submitList(it.dirTree)
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
        }
        fileListAdapter.bottomMenuEnabled.observe(viewLifecycleOwner) { enabled ->
            if (enabled) {
                binding.bottomMenuLayout.visibility = View.VISIBLE
            } else {
                binding.bottomMenuLayout.visibility = View.GONE
            }
        }
    }

    override val overrideBackButton :OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (fileListAdapter.selectionTracker.hasSelection()) {
                    fileListAdapter.selectionTracker.clearSelection()
                } else {
                    viewModel.onBackPressed()
                }
            }
        }
}