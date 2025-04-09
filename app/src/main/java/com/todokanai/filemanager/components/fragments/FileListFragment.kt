package com.todokanai.filemanager.components.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.FileListRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FileListFragment : Fragment() {

    private val viewModel : FileListViewModel by viewModels()
    private val binding by lazy{FragmentFileListBinding.inflate(layoutInflater)}

    private lateinit var fileListAdapter : FileListRecyclerAdapter

    private lateinit var directoryAdapter : DirectoryRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        onBackPressedOverride(
            onBackPressed = { viewModel.onBackPressed() },
            disableSelection = {fileListAdapter.selectionTracker.clearSelection()}
        )

        fileListAdapter = FileListRecyclerAdapter(
            onFileClick = {
                viewModel.onFileClick(requireActivity(),it)
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

        initDirectoryView(directoryAdapter)
        initFileListView(fileListAdapter)
       // initSwipe()
        //prepareView(binding)
        collectUIState()

        fileListAdapter.bottomMenuEnabled.observe(viewLifecycleOwner){ enabled->
            if(enabled) {
                binding.bottomMenuLayout.visibility = View.VISIBLE
            }else{
                binding.bottomMenuLayout.visibility = View.GONE
            }
        }

        return binding.root
    }

    private fun onBackPressedOverride(onBackPressed:()->Unit, disableSelection:()->Unit){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(fileListAdapter.selectionTracker.hasSelection()){
                    disableSelection()
                }else {
                    onBackPressed()
                }
            }
        })
    }

    private fun initDirectoryView(directoryAdapter:DirectoryRecyclerAdapter){
        val horizontalManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.directoryRecyclerView.run{
            adapter = directoryAdapter
            layoutManager = horizontalManager
        }
    }

    private fun initFileListView(fileListAdapter:FileListRecyclerAdapter){
        val verticalManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.fileListRecyclerView.run {
            adapter = fileListAdapter
            layoutManager = verticalManager
            addItemDecoration(DividerItemDecoration(context, verticalManager.orientation))
        }
    }
    /*
    private fun initSwipe(){
        val swipe =binding.swipe
        swipe.setOnRefreshListener {
            viewModel.refreshFileList()
            swipe.isRefreshing = false
        }

        /*
        modeManager.isMultiSelectMode.asLiveData().observe(viewLifecycleOwner){ mode ->
            swipe.apply{
                viewTreeObserver.addOnScrollChangedListener() {
                    if(fileListAdapter.isSelectionEnabled) {
                        isEnabled = false
                    }else{
                        isEnabled = true
                    }
                }
            }
        }

         */

        fileListAdapter.isSelectionEnabled.asLiveData().observe(viewLifecycleOwner){
            swipe.apply {
                viewTreeObserver.addOnScrollChangedListener {
                    isEnabled = !it
                }
            }
        }
    }

     */

    private fun collectUIState(){
        lifecycleScope.launch {
            viewModel.uiState.collect {
                fileListAdapter.submitList(it.listFiles)
                directoryAdapter.submitList(it.dirTree)
                binding.emptyDirectoryText.visibility =
                    if(it.emptyDirectoryText==true){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }
                binding.accessFailText.visibility =
                    if(it.accessFailText == true){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }

            }
        }
    }
}