package com.todokanai.filemanager.components.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
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

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel:MainViewModel by viewModels()
    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    private val fragmentList = listOf(StorageFragment(), FileListFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewpagerAdapter = ViewpagerAdapter(this)
        viewpagerAdapter.fragmentList = fragmentList

        val shouldShowDialog =  mutableStateOf(false)
        val menuBtnExpanded = mutableStateOf(false)

        viewModel.run {
            prepareObjects(applicationContext,this@MainActivity)
            onBackPressedOverride(this@MainActivity)
            requestStorageManageAccess(this@MainActivity)
            allowNotification(this@MainActivity)
        }
        binding.run {
            composeDialogView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        if (shouldShowDialog.value) {
                            SortDialog(
                                modifier = Modifier,
                                onDismissRequest = { shouldShowDialog.value = false })
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
                                exit = {viewModel.exit(this@MainActivity)}
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
                viewModel.invalidateCurrentDirectory()
            }

            mainViewPager.run{
                adapter = viewpagerAdapter
                isUserInputEnabled = false
            }
        }
        viewModel.isStorageFragment.asLiveData().observe(this){
            if(it==true){
                toStorageFrag()
            } else{
                toFileListFrag()
            }
        }
        setContentView(binding.root)
    }

    private fun toStorageFrag(){
        binding.mainViewPager.setCurrentItem(0,false)
    }

    private fun toFileListFrag(){
        binding.mainViewPager.setCurrentItem(1,false)
    }
}