package com.example.ruok_workers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentUnknownHomelessBinding
import java.util.Vector

class UnknownHomelessFragment : Fragment() {
    lateinit var binding: FragmentUnknownHomelessBinding
    lateinit var adapter: UnknownAdapter

    private lateinit var listRecyclerView: RecyclerView

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnknownHomelessBinding.inflate(inflater, container,false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        val list = Vector<UnknownCard>()

        // 임시 데이터셋 추가
        val sampleData = listOf(
            UnknownCard("20241011", "서울역 5번 출구"),
            UnknownCard("20240911", "서울역 4번 출구"),
            UnknownCard("20240312", "서울역 5번 출구"),
            UnknownCard("20240701", "서울역 13번 출구"),
            UnknownCard("20240401", "서울역 1번 출구")
        )

        // 리스트에 데이터 추가
        list.addAll(sampleData)
        var meetPlace:String = ""
        var meetLog:String=""


        //리사이클러뷰 아이템 추가
        val item = UnknownCard(meetPlace, meetLog)
        list.add(item)
        meetLog = ""
        meetPlace = ""


        val layoutManager = LinearLayoutManager(context)
        binding!!.UnknownRecyclerView.layoutManager = layoutManager

        adapter = UnknownAdapter(requireContext(),list)
        binding!!.UnknownRecyclerView.adapter = adapter


        return binding.root
    }
}