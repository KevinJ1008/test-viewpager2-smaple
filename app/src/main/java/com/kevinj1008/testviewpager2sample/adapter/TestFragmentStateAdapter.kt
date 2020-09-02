package com.kevinj1008.testviewpager2sample.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kevinj1008.testviewpager2sample.fragment.TestFragment

class TestFragmentStateAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return TestFragment()
    }

    fun fragmentTitle(position: Int): String {
        return "TestFragment: $position"
    }
}