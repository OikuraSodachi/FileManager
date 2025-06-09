package com.todokanai.filemanager.components.fragments

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todokanai.filemanager.abstracts.BaseFragment
import com.todokanai.filemanager.adapters.ServerRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FragmentLoginBinding
import com.todokanai.filemanager.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment(val viewPagerAdapter: ViewPagerAdapter) : BaseFragment() {

    override val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    lateinit var serverAdapter: ServerRecyclerAdapter

    private val viewModel: LoginViewModel by viewModels()

    override fun prepareLateInit() {
        serverAdapter = ServerRecyclerAdapter(
            onDeleteServer = { viewModel.deleteServer(it) },
            onItemClick = {
                viewModel.onServerClick(
                    requireActivity(),
                    it,
                    { viewPagerAdapter.toNetFragment(it) })
            }
        )
    }

    override fun prepareView() {
        binding.run {
            serverRecyclerView.run {
                val linearManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = serverAdapter
                layoutManager = linearManager
                DividerItemDecoration(context, linearManager.orientation)
            }
            serverAddButton.setOnClickListener {
                viewModel.saveServerInfo(
                    name = nameEditText.text.toString(),
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