package com.todokanai.filemanager.components.fragments

import android.content.DialogInterface
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstracts.BaseFragment
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.FileListRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FileinfoDialogBinding
import com.todokanai.filemanager.databinding.FilesinfoDialogBinding
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.databinding.RenameDialogBinding
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.tools.independent.getTotalSize_td
import com.todokanai.filemanager.tools.independent.popupMenu_td
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.DateFormat

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
            onFileLongClick = {
                viewModel.onFileLongClick(it)
            }
        )

        directoryAdapter = DirectoryRecyclerAdapter{ viewModel.onDirectoryClick(it) }

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

    // Todo: currentDirectory 값 가져오는 방식 고민할 것
    override val overrideBackButton: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed(viewModel.uiState.value.dirTree.last().absolutePath)
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
            add(Pair("Info", {info(files)}))
            if (selected.size == 1) {
                add(Pair("Rename", {renameDialog(files.first())}))
            }
        }
        return result
    }

    private fun info(files: Array<File>){
        if(files.size == 1){
            fileInfoDialog(files.first())
        }else if (files.size > 1){
            filesInfoDialog(files)
        }
    }

    private fun fileInfoDialog(file:File){
        val binding = FileinfoDialogBinding.inflate(layoutInflater).apply {
            fileInfoDialogFileNameText.text = file.name
            fileInfoDialogSizeText.text = readableFileSize_td(getTotalSize_td(arrayOf(file)))
            fileInfoDialogLastModified.text = DateFormat.getDateTimeInstance().format(file.lastModified())
        }
        AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .show()
    }

    private fun filesInfoDialog(files: Array<File>){
        val binding = FilesinfoDialogBinding.inflate(layoutInflater).apply {
            filesInfoDialogFilesNumberText.text = "${files.size} files"
            filesInfoDialogFileSizeText.text = readableFileSize_td(getTotalSize_td(files))
        }
        AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .show()
    }

    private fun renameDialog(file:File){
        val binding = RenameDialogBinding.inflate(layoutInflater).apply{
            renameDialogInputText.setText(file.name)
        }
        val listener = DialogInterface.OnClickListener { dialogInterface, i ->
            viewModel.renameFile(file,binding.renameDialogInputText.text.toString())
            file.parent?.let { viewModel.refresh(it) }
        }
        AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setPositiveButton("Confirm",listener)
            .show()
    }
}