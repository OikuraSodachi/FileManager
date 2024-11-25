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
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP_HERE
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.viewmodel.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FileListFragment : Fragment() {

    private val viewModel : FileListViewModel by viewModels()
    private val binding by lazy{FragmentFileListBinding.inflate(layoutInflater)}
    private val modeManager = Objects.modeManager

    private lateinit var fileListAdapter : FileListRecyclerAdapter

    private lateinit var directoryAdapter : DirectoryRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        onBackPressedOverride({ viewModel.onBackPressed() })

        fileListAdapter = FileListRecyclerAdapter(
            onItemLongClick = { onLongClick(it) },
            onFileClick = { context, file ->
                viewModel.onFileClick(context, file)
            },
            itemList = viewModel.fileHolderList,
            isDefaultMode = { modeManager.isDefaultMode() }
        ).apply {
            setHasStableIds(true)
        }

        directoryAdapter = DirectoryRecyclerAdapter(
            {viewModel.onDirectoryClick(it)},
            viewModel.directoryList,
            isNotMultiSelectMode = {modeManager.isNotMultiSelectMode()}
        )

        initDirectoryView(directoryAdapter)
        initFileListView(fileListAdapter)
        initSwipe()
        prepareView(binding)

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

    private fun onConfirmUnzip(selectedZipFile:File,targetDirectory: String){
        val target = viewModel.currentDirectory().resolve(targetDirectory)
        viewModel.unzipWork(selectedZipFile,target)
        modeManager.onDefaultMode_new()
    }

    private fun onLongClick(file: File){
        modeManager.onMultiSelectMode_new()
    }


    private fun onBackPressedOverride(onBackPressed:()->Unit){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(modeManager.isMultiSelectMode()){
                    modeManager.onDefaultMode_new()
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

    private fun initSwipe(){
        val swipe =binding.swipe
        swipe.setOnRefreshListener {
            viewModel.refreshFileList()
            swipe.isRefreshing = false
        }

        modeManager.isMultiSelectMode.asLiveData().observe(viewLifecycleOwner){ mode ->
            swipe.apply{
                viewTreeObserver.addOnScrollChangedListener() {
                    if(mode) {
                        isEnabled = false
                    }else{
                        isEnabled = true
                    }
                }
            }
        }
    }

    private fun prepareView(binding:FragmentFileListBinding){
        binding.run {
            composeBottomMenuList.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        val modifier = Modifier
                            .fillMaxSize()
                        val isDefaultMode = modeManager.isDefaultMode.collectAsStateWithLifecycle(
                            true,
                            viewLifecycleOwner
                        )
                        val isMultiSelectMode =
                            modeManager.isMultiSelectMode.collectAsStateWithLifecycle(
                                false,
                                viewLifecycleOwner
                            )
                        if (isDefaultMode.value) {
                            this.visibility = View.GONE
                        } else {
                            this.visibility = View.VISIBLE
                        }

                        if (isMultiSelectMode.value) {
                            BottomMultiSelectMenu(
                                modifier = modifier,
                                move = { modeManager.onConfirmMoveMode_new() },
                                copy = { modeManager.onConfirmCopyMode_new() },
                                delete = { onConfirmDelete(fileListAdapter.fetchSelectedItems()) },
                                zip = {
                                    onConfirmZip(
                                        selected = fileListAdapter.fetchSelectedItems(),
                                        targetDirectory = it
                                    )
                                },
                                unzip = {
                                    onConfirmUnzip(
                                        selectedZipFile = fileListAdapter.fetchSelectedItems()
                                            .first(), targetDirectory = it
                                    )
                                },
                                selected = fileListAdapter.fetchSelectedItems()

                            )
                        } else {
                            BottomConfirmMenu(
                                modifier = modifier,
                                onCancel = { modeManager.onDefaultMode_new() },
                                onConfirm = {
                                    val selected = fileListAdapter.fetchSelectedItems()
                                    fun getDirectory() =
                                        viewModel.currentDirectory()       // targetDirectory를 invoke 시점에 get
                                    when (modeManager.selectMode()) {
                                        CONFIRM_MODE_COPY -> {
                                            viewModel.copyWork(selected, getDirectory())
                                        }

                                        CONFIRM_MODE_MOVE -> {
                                            viewModel.moveWork(selected, getDirectory())
                                        }

                                        CONFIRM_MODE_UNZIP -> {

                                        }

                                        CONFIRM_MODE_UNZIP_HERE -> {

                                        }
                                    }
                                    modeManager.onDefaultMode_new()
                                }
                            )
                        }
                    }
                }
            }

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