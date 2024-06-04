package com.example.ruok_workers

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BriefingBeforeFragment()
            1 -> BriefingAfterFragment()
            2 -> BriefingDuringFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
