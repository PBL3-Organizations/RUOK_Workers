package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import java.util.Vector


class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    lateinit var adapter: DashboardAdapter

    private lateinit var recyclerViewProfile: RecyclerView


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

        //btnList 클릭시 DashboardFragment에서 ListFragment로 이동
        binding.btnList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ListFragment())
        }

        //btnLocation 클릭시 DashboardFragment에서 LocationTrackingFragment로 이동
        binding.btnLocation.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(LocationTrackingFragment())
        }

        //btnSearch 클릭시 DashboardFragment에서 SearchFragment로 이동
        binding.btnSearch.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(SearchFragment())
        }

        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


}