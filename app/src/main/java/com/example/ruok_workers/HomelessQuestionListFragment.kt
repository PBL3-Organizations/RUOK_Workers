package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentHomelesQuestionListBinding
import java.util.Vector

class HomelessQuestionListFragment : Fragment() {
    lateinit var binding: FragmentHomelesQuestionListBinding
    lateinit var adapter: HomelessQuestionListAdapter

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    var homelessId: Int = -1 // 노숙인 번호
    var list = Vector<HomelessQuestionListCard>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomelesQuestionListBinding.inflate(inflater, container,false)
        //homelessId 가져오기
        homelessId = arguments?.getInt("HomelessQuestionListHomelessId")!!
        Log.i("DB","$homelessId")

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        var sql = "SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, h.h_name, h.h_num FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN homeless h ON c.h_num = h.h_num WHERE c.h_num = '$homelessId' ORDER BY c.c_time DESC;"
        cursor = sqlitedb.rawQuery(sql, arrayOf())
        while(cursor.moveToNext()) {
            // 리스트에 데이터 추가
            var consultationNum: Int = cursor.getInt(cursor.getColumnIndexOrThrow("c.c_num"))
            var homelessNum: Int = homelessId
            var homelessName:String = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name"))
            var homelessUnusual:String = cursor.getString(cursor.getColumnIndexOrThrow("c.c_unusual"))
            var homelessPlace:String = cursor.getString(cursor.getColumnIndexOrThrow("l.l_addr"))
            var homelessLog:String = cursor.getString(cursor.getColumnIndexOrThrow("c.c_time"))

            //리사이클러뷰 아이템 추가
            val item = HomelessQuestionListCard(consultationNum, homelessNum, homelessName, homelessUnusual, homelessLog, homelessPlace)
            list.add(item)
        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()


        var layoutManager = LinearLayoutManager(context)
        binding!!.HomelesQuestionlistRecyclerView.layoutManager = layoutManager

        adapter = HomelessQuestionListAdapter(requireContext(),list)
        binding!!.HomelesQuestionlistRecyclerView.adapter = adapter

        return binding.root
    }
}