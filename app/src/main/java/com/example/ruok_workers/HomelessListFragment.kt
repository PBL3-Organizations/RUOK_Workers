package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import com.example.ruok_workers.databinding.FragmentHomelessListBinding
import java.util.Vector


class HomelessListFragment : Fragment() {
    lateinit var binding: FragmentHomelessListBinding
    lateinit var adapter: HomelessListAdapter

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

        binding = FragmentHomelessListBinding.inflate(inflater, container, false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)

        var list = ArrayList<FaviconItem>()
        var name: String
        var birth: String

        binding.searchButton2.setOnClickListener {

            list.clear()
            val filter = binding.searchEditText2.text.toString().trim()

            if (filter.isNotEmpty()) {
                sqlitedb = dbManager.readableDatabase
                var query = "SELECT * FROM homeless h WHERE h_name LIKE '%${filter}%';"

                var cursor: Cursor
                cursor = sqlitedb.rawQuery(query, arrayOf())
                while (cursor.moveToNext()){

                    name = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name")).toString()
                    birth = cursor.getString(cursor.getColumnIndexOrThrow("h.h_birth")).toString()

                    var item = FaviconItem(name, birth)

                    list.add(item)
                }
                cursor.close()

                binding!!.centerTextView2.visibility = View.VISIBLE
                binding!!.recyclerView.visibility = View.VISIBLE

                val layoutManager = LinearLayoutManager(context)
                binding!!.recyclerView.layoutManager = layoutManager

                adapter = HomelessListAdapter(requireContext(), list)
                binding!!.recyclerView.adapter = adapter

                sqlitedb.close()
            }
        }

        dbManager.close()

        //btnBeforeHomelessList 클릭시 HomelessListFragment에서 PhotoAddFragment로 이동
        binding.btnBeforeHomelessList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(PhotoAddFragment())
        }

        //btnNextHomelessList 클릭시 HomelessListFragment에서 LocationAddFragment로 이동
        binding.btnNextHomelessList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(LocationAddFragment())
        }

        //btnNoName 클릭시 HomelessListFragment에서 ProfileAddFragment로 이동
        binding.btnNoName.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ProfileAddFragment())
        }

        //btnNewHomeless 클릭시 HomelessListFragment에서 ProfileAddFragment로 이동
        binding.btnNewHomeless.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ProfileAddFragment())
        }

        return binding!!.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomelessListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}