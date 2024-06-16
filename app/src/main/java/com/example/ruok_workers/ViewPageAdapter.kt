package com.example.ruok_workers


import BriefingBeforeFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ruok_workers.BriefingAfterFragment as BriefingAfterFragment1

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> BriefingBeforeFragment()
            0 -> BriefingAfterFragment1()
            2 -> BriefingDuringFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
