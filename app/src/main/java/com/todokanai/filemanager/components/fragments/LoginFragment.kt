package com.todokanai.filemanager.components.fragments

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.todokanai.filemanager.abstractlogics.LoginFragmentLogics
import com.todokanai.filemanager.databinding.FragmentLoginBinding
import com.todokanai.filemanager.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : LoginFragmentLogics() {

    override val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()
    override fun prepareLateInit() {

    }

    override fun prepareView() {
        TODO("Not yet implemented")
    }

    override fun collectUIState() {
        TODO("Not yet implemented")
    }

    override val overrideBackButton: OnBackPressedCallback
        get() = TODO("Not yet implemented")
}