
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.ruok_workers.BriefingAddFragment
import com.example.ruok_workers.DashboardActivity
import com.example.ruok_workers.R
import com.example.ruok_workers.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BriefingBoardFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var newPostButton: Button
    private var lastSelectedTabPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_briefing_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)
        newPostButton = view.findViewById(R.id.btn_new_post)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // 탭과 뷰페이저를 연결하는 부분
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                1 -> {
                    tab.text = "아웃리치 전"
                    tab.contentDescription = "아웃리치 전 탭"
                }
                0 -> {
                    tab.text = "아웃리치 후"
                    tab.contentDescription = "아웃리치 후 탭"
                }
                2 -> {
                    tab.text = "아웃리치 중"
                    tab.contentDescription = "아웃리치 중 탭"
                }
            }
        }.attach()

        newPostButton.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            val fragment = BriefingAddFragment()

            // BriefingAddFragment로 마지막으로 선택된 탭 위치를 전달하는 부분
            val args = Bundle()
            args.putInt("tabPosition", lastSelectedTabPosition)
            fragment.arguments = args

            parentActivity.setFragment(fragment)
        }

        // 탭 선택이 변경될 때마다 마지막으로 선택된 탭 위치를 업데이트하는 리스너
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    lastSelectedTabPosition = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}