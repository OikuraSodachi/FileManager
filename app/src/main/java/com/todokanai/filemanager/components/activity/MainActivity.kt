package com.todokanai.filemanager.components.activity

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.asLiveData
import com.todokanai.filemanager.adapters.ViewpagerAdapter
import com.todokanai.filemanager.components.fragments.FileListFragment
import com.todokanai.filemanager.components.fragments.StorageFragment
import com.todokanai.filemanager.compose.MenuBtn
import com.todokanai.filemanager.compose.dialog.SortDialog
import com.todokanai.filemanager.databinding.ActivityMainBinding
import com.todokanai.filemanager.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val fragmentList = listOf(StorageFragment(), FileListFragment())
    private val viewpagerAdapter by lazy { ViewpagerAdapter(this) }

    val shouldShowDialog = mutableStateOf(false)
    val menuBtnExpanded = mutableStateOf(false)

    companion object {
        /** Todo: 임시조치로 companion object에 배치함. 나중에 옮길 것 **/
        val isStorageFragment = MutableStateFlow<Boolean>(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareView(viewModel)
        viewModel.run {
            prepareObjects(applicationContext, this@MainActivity)
            requestStorageManageAccess(this@MainActivity)
            allowNotification(this@MainActivity)
        }
        onBackPressedDispatcher.addCallback { /* disable back button by overriding with a empty callback */ }

        setContentView(binding.root)
    }



    /** view 내용 setter 구분 편리성을 위해 묶어서 분리 **/
    private fun prepareView(viewModel: MainViewModel){
        viewpagerAdapter.fragmentList = fragmentList

        binding.run {
            composeDialogView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        if (shouldShowDialog.value) {
                            SortDialog(
                                onDismissRequest = { shouldShowDialog.value = false }
                            )
                        }
                    }
                }
            }
            composePopupmenuView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        if (menuBtnExpanded.value) {
                            MenuBtn(
                                expanded = menuBtnExpanded,
                                exit = { viewModel.exit(this@MainActivity) }
                            )
                        }
                    }
                }
            }
            btn2.setOnClickListener { menuBtnExpanded.value = true }
            exitBtn.setOnClickListener {
                viewModel.exit(this@MainActivity)
            }
            storageBtn.setOnClickListener {
                isStorageFragment.value = true
            }

            mainViewPager.run {
                adapter = viewpagerAdapter
                isUserInputEnabled = false
            }
        }

        isStorageFragment.asLiveData().observe(this) {
            if (it == true) {
                binding.mainViewPager.setCurrentItem(0, false)  // toStorageFrag
            } else {
                binding.mainViewPager.setCurrentItem(1, false)  // toFileListFrag
            }
        }
    }
}