package com.todokanai.filemanager.abstractlogics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class FileListFragmentLogics : Fragment() {

    abstract val binding: ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prepareLateInit()
        prepareView()
        collectUIState()
        overrideBackButton()
        return binding.root
    }

    /** lateInit property initialization **/
    abstract fun prepareLateInit()

    /** view 준비 **/
    abstract fun prepareView()

    /** update view**/
    abstract fun collectUIState()

    /** back button 설정 **/
    abstract fun overrideBackButton()
}