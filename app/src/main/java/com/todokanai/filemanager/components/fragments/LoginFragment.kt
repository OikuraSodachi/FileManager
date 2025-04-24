package com.todokanai.filemanager.components.fragments

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstracts.ViewPagerFragment
import com.todokanai.filemanager.adapters.ServerRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FragmentLoginBinding
import com.todokanai.filemanager.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                viewModel.saveServerInfo(
                    ip = ipEditText.text.toString(),
                    id = idEditText.text.toString(),
                    password = passwordEditText.text.toString()
                )

            }
        }
    }

    override suspend fun collectUIState() {
        viewModel.uiState.collect { uiState ->
            serverAdapter.submitList(uiState.serverList)
        }
    }

    override val overrideBackButton: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
}