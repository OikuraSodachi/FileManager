package com.todokanai.filemanager.components.fragments

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstracts.BaseFragment
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.FileListRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.filemanager.tools.independent.popupMenu_td
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FileListFragment(viewPagerAdapter: ViewPagerAdapter) : BaseFragment() {

    override val binding by lazy { FragmentFileListBinding.inflate(layoutInflater) }
    private val viewModel: FileListViewModel by viewModels()
    lateinit var fileListAdapter: FileListRecyclerAdapter
    lateinit var directoryAdapter: DirectoryRecyclerAdapter

    fun selectMode() = viewModel.uiState.value.selectMode

    override fun prepareLateInit() {
        fileListAdapter = FileListRecyclerAdapter(
            onFileClick = {
                viewModel.onFileClick(requireActivity(), it, selectMode())
            },
            onFileLongClick = {
                viewModel.onFileLongClick(it, selectMode())
            }
        )

        directoryAdapter = DirectoryRecyclerAdapter(
            {
                if ( selectMode() != MULTI_SELECT_MODE ) {
                    viewModel.onDirectoryClick(it, selectMode())
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
                itemList = popupMenuList(viewModel.uiState.value.selectedItems)
            )
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
            binding.bottomMenuLayout.visibility =
                if (it.selectMode != DEFAULT_MODE) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    override val overrideBackButton: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed( selectMode() )
            }
        }

    fun popupMenuList(selected: Array<String>): List<Pair<String, () -> Unit>> {
        val result = mutableListOf<Pair<String, () -> Unit>>()
        val files = selected.map{ File(it) }.toTypedArray()
        result.run {
            add(Pair("Upload", { println("${selected}") }))
            add(Pair("Zip", {}))
            add(Pair("Copy", { }))
            add(Pair("Move", {  }))
            add(Pair("Info", {}))
            if (selected.size == 1) {
                add(Pair("Rename", {}))
            }
        }
        return result
    }
}