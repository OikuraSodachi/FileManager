package com.todokanai.filemanager.abstracts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2

/** @param parentViewPager parent ViewPager2 instance **/
abstract class ViewPagerFragment(val parentViewPager: ViewPager2) : Fragment() {

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
        overrideBackButton?.let {
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                it
            )    // add callback while fragment is visible
        }
    }

    override fun onPause() {
        super.onPause()
        overrideBackButton?.remove()                                               // remove callback when fragment is no longer visible
    }

    /** lateInit property initialization **/
    abstract fun prepareLateInit()

    /** view 준비 **/
    abstract fun prepareView()

    /** update view**/
    abstract fun collectUIState()

    /** set this to null if you don't want customize back button callback **/
    abstract val overrideBackButton: OnBackPressedCallback?
}