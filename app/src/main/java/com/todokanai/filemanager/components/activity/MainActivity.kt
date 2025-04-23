package com.todokanai.filemanager.components.activity

import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.todokanai.filemanager.abstactlogics.MainActivityLogics
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.components.fragments.FileListFragment
import com.todokanai.filemanager.components.fragments.LoginFragment
import com.todokanai.filemanager.components.fragments.NetFragment
import com.todokanai.filemanager.components.fragments.StorageFragment
import com.todokanai.filemanager.compose.MenuBtn
import com.todokanai.filemanager.compose.dialog.SortDialog
import com.todokanai.filemanager.databinding.ActivityMainBinding
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : MainActivityLogics() {

    private val permissions = Objects.permissions
    private val viewModel: MainViewModel by viewModels()
    override val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val pagerAdapter by lazy { ViewPagerAdapter(this, binding.mainViewPager) }
    private val fragments by lazy {
        listOf(
            StorageFragment(pagerAdapter),
            FileListFragment(pagerAdapter),
            NetFragment(pagerAdapter),
            LoginFragment(pagerAdapter)
        )
    }

    val shouldShowDialog = mutableStateOf(false)
    val menuBtnExpanded = mutableStateOf(false)

    override fun handlePermission() {
        if (permissions.isNotEmpty()) {
            requestPermissions(permissions, Constants.PERMISSION_REQUEST_CODE)
        }
    }

    override fun prepareLateInit() {
        viewModel.run {
            prepareObjects(applicationContext, this@MainActivity)
            requestStorageManageAccess(this@MainActivity)
        }
        pagerAdapter.fragmentList = fragments
    }

    override fun prepareView() {
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
            localListButton.setOnClickListener {
                pagerAdapter.toFileListFragment()
            }
            storageBtn.setOnClickListener {
                pagerAdapter.toStorageFragment()
            }
            netButton.setOnClickListener {
                pagerAdapter.toNetFragment()
            }
            mainBottomButton.setOnClickListener {
                pagerAdapter.toLoginFragment()
            }

            mainViewPager.run {
                adapter = pagerAdapter
                isUserInputEnabled = false
            }
        }
        onBackPressedDispatcher.addCallback {

        }
    }

    override fun collectUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {

                }
            }
        }
    }
}