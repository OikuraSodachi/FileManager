package com.todokanai.filemanager.components.fragments

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.todokanai.filemanager.abstracts.BaseFragment
import com.todokanai.filemanager.adapters.ServerRecyclerAdapter
import com.todokanai.filemanager.adapters.ViewPagerAdapter
import com.todokanai.filemanager.databinding.FragmentLoginBinding
import com.todokanai.filemanager.viewmodel.LoginViewModel

class LoginFragment(viewPagerAdapter: ViewPagerAdapter) : BaseFragment() {

    override val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    lateinit var serverAdapter: ServerRecyclerAdapter

    private val viewModel: LoginViewModel by viewModels()

    override fun prepareLateInit() {
        serverAdapter = ServerRecyclerAdapter(
            onDeleteServer = { viewModel.deleteServer(it) },
            onItemClick = { viewModel.onServerClick(requireActivity(),it) }
        )
    }

    override fun prepareView() {
        TODO("Not yet implemented")
    }

    override suspend fun collectUIState() {
        TODO("Not yet implemented")
    }


    override val overrideBackButton: OnBackPressedCallback
        get() = TODO("Not yet implemented")
}