package com.todokanai.filemanager.abstractlogics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.todokanai.filemanager.abstracts.ViewPagerFragment

abstract class FileListFragmentLogics : ViewPagerFragment() {

    abstract val binding: ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prepareLateInit()
        prepareView()
        collectUIState()
        return binding.root
    }

    /** lateInit property initialization **/
    abstract fun prepareLateInit()

    /** view 준비 **/
    abstract fun prepareView()

    /** update view**/
    abstract fun collectUIState()
}