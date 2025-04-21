package com.todokanai.filemanager.abstracts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/** [androidx.viewpager2.widget.ViewPager2] 안에 배치되는 Fragment 의 onBackPressed 케어 **/
abstract class ViewPagerFragment() : Fragment() {

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

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            overrideBackButton
        )
    }

    override fun onPause() {
        super.onPause()
        overrideBackButton.remove()
    }

    /** lateInit property initialization **/
    abstract fun prepareLateInit()

    /** view 준비 **/
    abstract fun prepareView()

    /** update view**/
    abstract fun collectUIState()

    /** back button 설정 **/
    abstract val overrideBackButton: OnBackPressedCallback
}