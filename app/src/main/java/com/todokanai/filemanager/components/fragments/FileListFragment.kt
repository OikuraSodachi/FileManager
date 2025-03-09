package com.todokanai.filemanager.components.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.adapters.DirectoryRecyclerAdapter
import com.todokanai.filemanager.adapters.FileListRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentFileListBinding
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint

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
            disableSelection = {fileListAdapter.isSelectionEnabled = false}
        )

        fileListAdapter = FileListRecyclerAdapter(
            onFileClick = {
                viewModel.onFileClick(requireActivity(),it)
            },
            itemList = viewModel.fileHolderList
        ).apply {
            setHasStableIds(true)
        }

        directoryAdapter = DirectoryRecyclerAdapter(
            {
                if (!fileListAdapter.isSelectionEnabled) {
                    viewModel.onDirectoryClick(it)
                }
            },
            viewModel.directoryList
        )

        initDirectoryView(directoryAdapter)
        initFileListView(fileListAdapter)
       // initSwipe()
        prepareView(binding)
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
                if(fileListAdapter.isSelectionEnabled){
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

    private fun prepareView(binding:FragmentFileListBinding){
        binding.run {

            emptyDirectoryText.visibility.apply {
                viewModel.fileHolderList.asLiveData().observe(viewLifecycleOwner) {
                    if (it.isEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            accessFailText.visibility.apply {
                viewModel.notAccessible.asLiveData().observe(viewLifecycleOwner) {
                    if (it) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }
        }
    }
}