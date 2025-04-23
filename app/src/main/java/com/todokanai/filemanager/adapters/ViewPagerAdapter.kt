package com.todokanai.filemanager.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class ViewPagerAdapter(activity: AppCompatActivity, val viewPager: ViewPager2) : FragmentStateAdapter(activity) {
    var fragmentList = listOf<Fragment>()

    /** 1: StorageFragment
     *
     * 2: FileListFragment
     *
     * 3: NetFragment
     *
     * 4: LoginFragment **/

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun toStorageFragment(){
        viewPager.setCurrentItem(0,false)
    }

    fun toFileListFragment(){
        viewPager.setCurrentItem(1,false)
    }

    fun toNetFragment(){
        viewPager.setCurrentItem(2,false)
    }

    fun toLoginFragment(){
        viewPager.setCurrentItem(3,false)
    }
}