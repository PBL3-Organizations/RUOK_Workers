package com.example.ruok_workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentListBinding
import com.google.android.material.search.SearchView
import java.util.Vector

class ListFragment : Fragment() {
    lateinit var binding: FragmentListBinding
    lateinit var adapter: ListAdapter


    private lateinit var listRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container,false)

        val list = Vector<ListCard>()

        // 임시 데이터셋 추가
        val sampleData = listOf(
            ListCard("김알유","없음","20241011", "서울역 5번 출구"),
            ListCard("박아무개","무릎 아픔","20240911", "서울역 5번 출구"),
            ListCard("김모씨","치과치료 필요","20240312", "서울역 5번 출구"),
            ListCard("정모씨","없음","20240701", "서울역 5번 출구"),
            ListCard("한모씨","없음","20240401", "서울역 5번 출구")
        )

        // 리스트에 데이터 추가
        list.addAll(sampleData)
        var homelessName:String = ""
        var homelessUnusual:String = ""
        var homelessPlace:String=""
        var homelessLog:String=""


        //리사이클러뷰 아이템 추가
        val item = ListCard(homelessName, homelessUnusual, homelessPlace, homelessLog)
        list.add(item)
        homelessName = ""
        homelessUnusual = ""
        homelessPlace = ""
        homelessLog = ""


        val layoutManager = LinearLayoutManager(context)
        binding!!.listRecyclerView.layoutManager = layoutManager

        adapter = ListAdapter(requireContext(),list)
        binding!!.listRecyclerView.adapter = adapter

        binding.searchviewList.onActionViewExpanded()

        return binding.root
    }
}