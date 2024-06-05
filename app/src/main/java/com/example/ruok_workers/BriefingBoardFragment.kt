package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import android.widget.Button

class BriefingBoardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_briefing_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TabLayout과 ViewPager2를 ID로 찾기
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        val newPostButton = view.findViewById<Button>(R.id.btn_new_post)

        // ViewPager를 어댑터로 설정
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "아웃리치 전"
                    tab.contentDescription = "아웃리치 전 탭"
                }
                1 -> {
                    tab.text = "아웃리치 후"
                    tab.contentDescription = "아웃리치 후 탭"
                }
                2 -> {
                    tab.text = "아웃리치 중"
                    tab.contentDescription = "아웃리치 중 탭"
                }
            }
        }.attach()

        // 새 글 작성 버튼 클릭 시 이벤트 처리
        newPostButton.setOnClickListener {
            // BriefingAddFragment로 이동
            val fragment = BriefingAddFragment()
            parentFragmentManager.beginTransaction().replace(R.id.rootLayout, fragment).addToBackStack(null).commit()
        }
    }
}
