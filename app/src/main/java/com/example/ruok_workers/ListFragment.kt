package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
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
        binding = FragmentListBinding.inflate(inflater, container,false)

        val list = Vector<ListCard>()

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        val sql = "SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, h.h_name FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN homeless h ON c.h_num = h.h_num ORDER BY c.c_time DESC;"
        cursor = sqlitedb.rawQuery(sql, arrayOf())
        while(cursor.moveToNext()) {
            // 리스트에 데이터 추가
            var consultationNum: Int = cursor.getInt(cursor.getColumnIndexOrThrow("c.c_num"))
            var homelessName:String = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name"))
            var homelessUnusual:String = cursor.getString(cursor.getColumnIndexOrThrow("c.c_unusual"))
            var homelessPlace:String = cursor.getString(cursor.getColumnIndexOrThrow("l.l_addr"))
            var homelessLog:String = cursor.getString(cursor.getColumnIndexOrThrow("c.c_time"))

            //리사이클러뷰 아이템 추가
            val item = ListCard(consultationNum, homelessName, homelessUnusual, homelessLog, homelessPlace)
            list.add(item)
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        val layoutManager = LinearLayoutManager(context)
        binding!!.listRecyclerView.layoutManager = layoutManager

        adapter = ListAdapter(requireContext(),list)
        binding!!.listRecyclerView.adapter = adapter

        binding.searchviewList.onActionViewExpanded()

        return binding.root
    }
}