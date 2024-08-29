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
import com.todokanai.filemanager.compose.BottomMenu
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileListFragment : Fragment() {

    private val viewModel : FileListViewModel by viewModels()
    private val binding by lazy{FragmentFileListBinding.inflate(layoutInflater)}
    private val modeManager = Objects.modeManager
    private val selectMode = modeManager.selectMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        println("FileListFrag: onCreateView")
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed(selectMode.value)
            }
        })
        val verticalManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        val horizontalManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        val fileListAdapter = FileListRecyclerAdapter(
            {viewModel.onClick(requireContext(),it,selectMode.value)}
        ) {
            viewModel.onLongClick(it,selectMode.value)
            modeManager.changeSelectMode(MULTI_SELECT_MODE)
        }
        val directoryAdapter = DirectoryRecyclerAdapter { viewModel.onDirectoryClick(it,selectMode.value) }

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
                        val selectMode = selectMode.collectAsStateWithLifecycle()

                        if(selectMode.value == DEFAULT_MODE){
                            this.visibility = View.GONE
                            fileListAdapter.isMultiSelectMode = false

                        }else {
                            this.visibility = View.VISIBLE
                            fileListAdapter.isMultiSelectMode = true
                            BottomMenu(
                                modifier = Modifier
                                    .fillMaxSize(),
                                onConfirmTest = { viewModel.onConfirmTest(selectMode.value) },
                                onCancel = {
                                    modeManager.changeSelectMode(DEFAULT_MODE)
                                    modeManager.clearSelectedFiles()
                                },
                                move = {modeManager.changeSelectMode(CONFIRM_MODE_MOVE)},
                                copy = {modeManager.changeSelectMode(CONFIRM_MODE_COPY)},
                                isMultiSelectMode = selectMode.value == MULTI_SELECT_MODE
                            )
                        }
                    }
                }
            }
        }

        viewModel.run{
            fileHolderListNew.asLiveData().observe(viewLifecycleOwner){
                fileListAdapter.itemList = it
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
            isEmpty.asLiveData().observe(viewLifecycleOwner){
                if(it==true) {
                    binding.emptyDirectoryText.visibility = View.VISIBLE
                } else{
                    binding.emptyDirectoryText.visibility = View.INVISIBLE
                }
            }
            /** viewLifecycleOwner 말고, multiSelectMode가 조건이 되야함 **/
            selectedFiles.asLiveData().observe(viewLifecycleOwner) {
                fileListAdapter.run{
                    selectedItemList = it
                    notifyDataSetChanged()
                }
            }
        }

        return binding.root
    }
}