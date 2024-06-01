package com.example.ruok_workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentListBinding
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
            ListCard("김알유","없음","20241011"),
            ListCard("박아무개","무릎 아픔","20240911"),
            ListCard("김모씨","치과치료 필요","20240312"),
            ListCard("정모씨","없음","20240701"),
            ListCard("한모씨","없음","20240401")
        )

        // 리스트에 데이터 추가
        list.addAll(sampleData)
        var homelessName:String = ""
        var homelessUnusual:String = ""
        var homelessLog:String=""

        //리사이클러뷰 아이템 추가
        val item = ListCard(homelessName, homelessUnusual, homelessLog)
        list.add(item)
        homelessName = ""
        homelessUnusual = ""
        homelessLog = ""

        val layoutManager = LinearLayoutManager(context)
        binding!!.listRecyclerView.layoutManager = layoutManager

        adapter = ListAdapter(requireContext(),list)
        binding!!.listRecyclerView.adapter = adapter

        return binding.root
    }

}