package com.todokanai.filemanager.components.fragments

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstracts.ViewPagerFragment
import com.todokanai.filemanager.adapters.ServerRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FragmentLoginBinding
import com.todokanai.filemanager.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment(val viewPagerAdapter: ViewPagerAdapter) : ViewPagerFragment() {
    override val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()
    lateinit var serverAdapter: ServerRecyclerAdapter

    override fun prepareLateInit() {
        serverAdapter = ServerRecyclerAdapter(
            onItemClick = {
                viewModel.onServerClick(it)
                viewPagerAdapter.toNetFragment()
            }
        )
    }

    override fun prepareView() {
        val linearManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.run{
            serverRecyclerView.run {
                adapter = serverAdapter
                layoutManager = linearManager
                DividerItemDecoration(context, linearManager.orientation)
            }
            serverAddButton.setOnClickListener {
                // Todo: dialog for add server

            }
        }
    }

    override fun collectUIState() {
        lifecycleScope.launch {
            viewModel.uiState.collect {

            }
        }
    }

    override val overrideBackButton: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
}