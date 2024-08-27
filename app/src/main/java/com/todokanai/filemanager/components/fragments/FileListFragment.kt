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
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileListFragment : Fragment() {

    private val viewModel : FileListViewModel by viewModels()
    private val binding by lazy{FragmentFileListBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("FileListFrag: onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        println("FileListFrag: onCreateView")
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed()
            }
        })
        val verticalManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        val horizontalManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        val fileListAdapter = FileListRecyclerAdapter(
            {viewModel.onClick(requireContext(),it)}
        ) { viewModel.onLongClick(it) }
        val directoryAdapter = DirectoryRecyclerAdapter { viewModel.onDirectoryClick(it) }

        val modeManager = Objects.modeManager
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
                        val selectMode = viewModel.selectMode.collectAsStateWithLifecycle()
                        if (selectMode.value != DEFAULT_MODE) {
                            BottomMenu(
                                modifier = Modifier
                                    .fillMaxSize(),
                                selectMode = selectMode.value,
                                modeManager = modeManager
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
            sortMode.asLiveData().observe(viewLifecycleOwner){
                viewModel.refreshFileList()
            }

            selectMode.asLiveData().observe(viewLifecycleOwner){
                if(it!= DEFAULT_MODE){
                    binding.composeBottomMenuList.visibility = View.VISIBLE
                    fileListAdapter.isMultiSelectMode = true
                } else{
                    binding.composeBottomMenuList.visibility = View.GONE
                    fileListAdapter.isMultiSelectMode = false
                }
                fileListAdapter.notifyDataSetChanged()
            }

            notAccessible.asLiveData().observe(viewLifecycleOwner){
                if(it==true) {
                    binding.accessFailText.visibility = View.VISIBLE
                } else{
                    binding.accessFailText.visibility = View.INVISIBLE
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

    override fun onStop() {
        super.onStop()
        println("FileListFrag: onStop")
    }

    override fun onPause() {
        super.onPause()
        println("FileListFrag: onPause")
    }

    override fun onResume() {
        super.onResume()
        println("FileListFrag: onResume")
    }

    override fun onDetach() {
        super.onDetach()
        println("FileListFrag: onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("FileListFrag: onDestroyView")
    }
    override fun onDestroy() {
        super.onDestroy()
        println("FileListFrag: onDestroy")
    }
}