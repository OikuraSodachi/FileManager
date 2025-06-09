package com.todokanai.filemanager.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class ViewPagerAdapter(activity: AppCompatActivity, val viewPager: ViewPager2) :
    FragmentStateAdapter(activity) {
    var fragmentList = listOf<Fragment>()

    /** 0: StorageFragment
     *
     * 1: FileListFragment
     *
     * 2: NetFragment
     *
     * 3: LoginFragment **/

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun toStorageFragment() {
        viewPager.setCurrentItem(0, false)
    }

    fun toFileListFragment() {
        viewPager.setCurrentItem(1, false)
    }

    /** @param isLoggedIn whether if logged in to FTP Server **/
    fun toNetFragment(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            viewPager.setCurrentItem(2, false)      // netFragment
        } else {
            viewPager.setCurrentItem(3, false)      // loginFragment
        }
    }
}