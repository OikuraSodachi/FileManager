package com.todokanai.filemanager.abstractlogics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.todokanai.filemanager.adapters.NetRecyclerAdapter
import com.todokanai.filemanager.databinding.FragmentNetBinding
import com.todokanai.filemanager.viewmodel.NetViewModel

abstract class NetFragmentLogic: Fragment() {

    protected val binding by lazy { FragmentNetBinding.inflate(layoutInflater) }
    protected val viewModel: NetViewModel by viewModels()
    lateinit var netAdapter : NetRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prepareView()
        collectUiState()
        overrideBackButton()
        return binding.root
    }

    abstract fun prepareView()
    abstract fun collectUiState()
    abstract fun overrideBackButton()

}