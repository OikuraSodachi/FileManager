package com.todokanai.filemanager.components.fragments

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstracts.BaseFragment
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.FileListRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.tools.independent.popupMenu_td
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileListFragment(viewPagerAdapter: ViewPagerAdapter) : BaseFragment() {

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

        binding.moreBtn.setOnClickListener {
            popupMenu_td(
                context = requireActivity(),
                anchor = it,
                itemList = viewModel.popupMenuList(fileListAdapter.selectedItems().toTypedArray())
            )
        }

        fileListAdapter.bottomMenuEnabled.asLiveData().observe(viewLifecycleOwner) { enabled ->
            binding.bottomMenuLayout.visibility = if (enabled) View.VISIBLE else View.GONE
        }
    }

    override suspend fun collectUIState() {
        viewModel.uiState.collect {
            fileListAdapter.submitList(
                it.listFiles,
                {
                    /** Todo: position:Int 값을 viewModel 에서 완성시킨 채로 가져오기 **/
                    val position = viewModel.scrollPosition(it.listFiles, it.lastKnownDirectory)
                    /** Todo: item 이 화면 중앙에 오도록 scroll 하기 **/
                    binding.fileListRecyclerView.scrollToPosition(position)
                    println("scrollTo: ${position}")
                }
            )

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

    override val overrideBackButton: OnBackPressedCallback =
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