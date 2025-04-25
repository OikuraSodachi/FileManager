package com.todokanai.filemanager.abstracts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class ViewPagerFragment() : Fragment() {

    abstract val binding: ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prepareLateInit()
        prepareView()
        // collectUIState()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectUIState()
            }
        }
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
    abstract suspend fun collectUIState()

    /** set this to null if you don't want customize back button callback **/
    abstract val overrideBackButton: OnBackPressedCallback?
}