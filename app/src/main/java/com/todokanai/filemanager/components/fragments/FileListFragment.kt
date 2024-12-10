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
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.tools.independent.popupMenu_td
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
            {viewModel.onDirectoryClick(it)},
            viewModel.directoryList,
            isNotMultiSelectMode = {modeManager.isNotMultiSelectMode()}
        )

        initDirectoryView(directoryAdapter)
        initFileListView(fileListAdapter)
       // initSwipe()
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
       // modeManager.onDefaultMode_new()
    }

    private fun onConfirmUnzip(selectedZipFile:File,targetDirectory: String){
        val target = viewModel.currentDirectory().resolve(targetDirectory)
        viewModel.unzipWork(selectedZipFile,target)
     //   modeManager.onDefaultMode_new()
    }


    private fun onBackPressedOverride(onBackPressed:()->Unit, disableSelection:()->Unit){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(fileListAdapter.isSelectionEnabled){
                   // modeManager.onDefaultMode_new()
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
            /*
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

             */

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
            bottomMenuLayout.visibility.apply {
                fileListAdapter.bottomMenuEnabled.observe(viewLifecycleOwner){
                    if (it) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            cancelBtn.setOnClickListener {
                fileListAdapter.isSelectionEnabled = false
            }
            deleteBtn.setOnClickListener {
                onConfirmDelete(fileListAdapter.fetchSelectedItems())
            }
            confirmBtn.setOnClickListener {

            }
            moreBtn.setOnClickListener {
                popupMenu_td(
                    requireContext(),
                    it,
                    menuList()
                )
            }
        }
    }

    fun menuList():List<Pair<String,()->Unit>>{
        val out = mutableListOf<Pair<String,()->Unit>>()
        return out
    }
}