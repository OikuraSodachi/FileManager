package com.todokanai.filemanager.abstracts

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

/** [androidx.viewpager2.widget.ViewPager2] 안에 배치되는 Fragment 의 onBackPressed 케어 **/
abstract class ViewPagerFragment : Fragment() {

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

    /** back button 설정 **/
    abstract val overrideBackButton: OnBackPressedCallback
}