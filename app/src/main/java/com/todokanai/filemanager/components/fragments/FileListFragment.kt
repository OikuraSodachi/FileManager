package com.todokanai.filemanager.components.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.FileListRecyclerAdapter
import com.todokanai.filemanager.compose.bottommenucontent.BottomConfirmMenu
import com.todokanai.filemanager.compose.bottommenucontent.BottomMultiSelectMenu
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FileListFragment : Fragment() {

    private val viewModel : FileListViewModel by viewModels()
    private val binding by lazy{FragmentFileListBinding.inflate(layoutInflater)}
    private val modeManager = Objects.modeManager

    private lateinit var verticalManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(modeManager.isMultiSelectMode()){
                    modeManager.onDefaultMode_new()
                }else {
                    viewModel.onBackPressed()
                }
            }
        })
        verticalManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        val horizontalManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        val fileListAdapter = FileListRecyclerAdapter(
            onItemLongClick = { onLongClick(it) },
            isDefaultMode = {modeManager.isDefaultMode()},
            isMultiSelectMode = {modeManager.isMultiSelectMode()},
            toggleToSelectedFiles = {modeManager.toggleToSelectedFiles(it)},
            onFileClick = { context,file ->
                viewModel.onFileClick(context,file)
            }
        )

        val directoryAdapter = DirectoryRecyclerAdapter(
            {viewModel.onDirectoryClick(it)},
            {modeManager.isNotMultiSelectMode()}
        )

        binding.run{
            fileListRecyclerView.run{
                adapter = fileListAdapter
                layoutManager = verticalManager
                val dividerItemDecoration = DividerItemDecoration(
                    fileListRecyclerView.context,
                    verticalManager.orientation
                )
                addItemDecoration(dividerItemDecoration)
            }

            directoryRecyclerViewNew.run{
                adapter = directoryAdapter
                layoutManager = horizontalManager
                swipe.setOnRefreshListener {
                    viewModel.refreshFileList()
                    swipe.isRefreshing = false
                }
            }
            composeBottomMenuList.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        val modifier = Modifier
                            .fillMaxSize()
                        val isDefaultMode = modeManager.isDefaultMode.collectAsStateWithLifecycle(true,viewLifecycleOwner)
                        val isMultiSelectMode = modeManager.isMultiSelectMode.collectAsStateWithLifecycle(false,viewLifecycleOwner)
                        val enablePopupMenu = modeManager.selectedFiles.collectAsStateWithLifecycle()
                        if(isDefaultMode.value){
                            this.visibility = View.GONE
                        } else{
                            this.visibility = View.VISIBLE
                        }

                        if(isMultiSelectMode.value){
                            BottomMultiSelectMenu(
                                modifier = modifier,
                                move = {modeManager.onConfirmMoveMode_new()},
                                copy = {modeManager.onConfirmCopyMode_new()},
                                delete = {onConfirmDelete(fileListAdapter.selectedItemList)},
                                zip = {onConfirmZip(selected = fileListAdapter.selectedItemList, targetDirectory = it)},
                                enablePopupMenu = {enablePopupMenu.value.isNotEmpty()}
                            )
                        } else{
                            BottomConfirmMenu(
                                modifier = modifier,
                                onCancel = {modeManager.onDefaultMode_new()},
                                copyWork = { selected,target -> viewModel.copyWork(selected,target) },
                                moveWork = {selected,target -> viewModel.moveWork(selected,target)},
                                selected = fileListAdapter.selectedItemList,
                                getDirectory = {viewModel.currentDirectory()}
                            )
                        }
                    }
                }
            }
        }

        viewModel.run{
            fileHolderList.asLiveData().observe(viewLifecycleOwner){ list ->
                if(list.isEmpty()){
                    binding.emptyDirectoryText.visibility = View.VISIBLE
                }else{
                    binding.emptyDirectoryText.visibility = View.INVISIBLE
                }
                fileListAdapter.itemList = list
                fileListAdapter.notifyDataSetChanged()
            }
            directoryList.asLiveData().observe(viewLifecycleOwner){
                directoryAdapter.directoryList = it ?: emptyList()
                directoryAdapter.notifyDataSetChanged()
            }

            notAccessible.asLiveData().observe(viewLifecycleOwner){
                if(it==true) {
                    binding.accessFailText.visibility = View.VISIBLE
                } else{
                    binding.accessFailText.visibility = View.GONE
                }
            }
        }

        modeManager.run{
            selectedFiles.asLiveData().observe(viewLifecycleOwner) {
                fileListAdapter.run{
                    selectedItemList = it
                    notifyDataSetChanged()
                }
            }
            isMultiSelectMode.asLiveData().observe(viewLifecycleOwner){
                fileListAdapter.run {
                    notifyDataSetChanged()
                }
            }
        }
        return binding.root
    }

    private fun onConfirmDelete(selected:Array<File>){
        viewModel.deleteWork(selected)
        modeManager.onDefaultMode_new()
    }

    private fun onConfirmZip(selected: Array<File>, targetDirectory:String){
        val current = viewModel.currentDirectory().absolutePath
        val targetFile = File("$current/$targetDirectory.zip")

        viewModel.zipWork(selected, targetFile)
        modeManager.onDefaultMode_new()
    }

    private fun onLongClick(file: File){
        modeManager.onMultiSelectMode_new()
        modeManager.toggleToSelectedFiles(file)
    }
}