package com.example.ruok_workers

import ProfileDetailFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import java.util.Vector

class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    lateinit var adapter: DashboardAdapter

    private lateinit var recyclerViewProfile: RecyclerView

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(DashboardFragment())

            // 현재 Fragment가 DashboardFragment인지 확인하고 백 스택 비우기
            if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                requireActivity().supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            } else {
                // 뒤로 가기 기본 동작 수행
                isEnabled = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)


        val list = Vector<Dashboard>()

        // 임시 데이터셋 추가
        val sampleData = listOf(
            Dashboard("hyorim", "20021002"),
            Dashboard("Alice", "19900101"),
            Dashboard("Bob", "19850523"),
            Dashboard("Charlie", "19921112"),
            Dashboard("Diana", "19880704"),
            Dashboard("Edward", "19950930")
        )

        // 리스트에 데이터 추가
        list.addAll(sampleData)

        var hName:String = ""
        var hBirth:String = ""

        //리사이클러뷰 아이템 추가
        val item = Dashboard(hName, hBirth)
        list.add(item)
        hName = ""
        hBirth = ""

        val layoutManager = LinearLayoutManager(context)
        binding!!.recyclerViewProfile.layoutManager = layoutManager

        adapter = DashboardAdapter(requireContext(), list)
        binding!!.recyclerViewProfile.adapter = adapter

        //btnBriefing 클릭시 DashboardFragment에서 BriefingBoardFragment로 이동
        binding.btnBriefing.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(BriefingBoardFragment())
        }

        //btnCounsel 클릭시 DashboardFragment에서 QuestionnaireFragment로 이동
        binding.btnCounsel.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(QuestionnaireFragment())
        }

        //btnOutReach 클릭시 DashboardFragment에서 LocationTrackingFragment로 이동
        binding.btnOutreach.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(LocationTrackingFragment.newInstance(LocationTrackingFragment.State.START))
        }

        //btnCounting 클릭시 DashboardFragment에서 CountingAddFragment로 이동
        binding.btnCounting.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(CountingAddFragment())
        }

        //tvProfile 클릭시 DashboardFragment에서 SearchFragment로 이동
        binding.tvProfile.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(SearchFragment())
        }

        return binding.root
    }



}