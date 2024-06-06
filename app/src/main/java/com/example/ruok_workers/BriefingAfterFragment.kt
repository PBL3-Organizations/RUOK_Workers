package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.AdapterView

class BriefingAfterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃을 인플레이트
        val view = inflater.inflate(R.layout.fragment_briefing_after, container, false)

        // 더미 데이터
        val dummyData = arrayOf("After 1", "아이템 2", "아이템 3", "아이템 4", "아이템 5")

        // ListView 찾기
        val listView: ListView = view.findViewById(R.id.listView_briefing_after)

        // ArrayAdapter를 생성하여 ListView를 더미 데이터로 채우기
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dummyData)

        // ListView에 어댑터를 설정
        listView.adapter = adapter

        // ListView의 항목을 클릭할 때 실행할 동작을 정의
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // 클릭된 항목의 위치(position)에 따라 Fragment 전환 코드를 작성
            when (position) {
                0 -> {
                    // BriefingDetailFragment와 연결
                    val fragment = BriefingDetailFragment()
                    val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.rootLayout, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                // 다른 항목을 클릭했을 때 필요한 작업을 추가
            }
        }

        return view
    }
}
