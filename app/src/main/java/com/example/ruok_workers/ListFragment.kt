package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentListBinding
import java.util.Vector

class ListFragment : Fragment() {
    lateinit var binding: FragmentListBinding
    lateinit var adapter: ListAdapter

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var list = Vector<ListCard>()

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

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        var sql = "SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, h.h_name, h.h_num FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN homeless h ON c.h_num = h.h_num UNION SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, '미상' AS h_name, 0 AS h_num FROM consultation c JOIN location l ON c.c_num = l.c_num WHERE c.h_num = 0 ORDER BY c.c_time DESC;"
        cursor = sqlitedb.rawQuery(sql, arrayOf())
        while(cursor.moveToNext()) {
            // 리스트에 데이터 추가
            var consultationNum: Int = cursor.getInt(cursor.getColumnIndexOrThrow("c.c_num"))
            var homelessNum: Int = cursor.getInt(cursor.getColumnIndexOrThrow("h_num"))
            var homelessName:String = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name"))
            var homelessUnusual:String = cursor.getString(cursor.getColumnIndexOrThrow("c.c_unusual"))
            var homelessPlace:String = cursor.getString(cursor.getColumnIndexOrThrow("l.l_addr"))
            var homelessLog:String = cursor.getString(cursor.getColumnIndexOrThrow("c.c_time"))

            //리사이클러뷰 아이템 추가
            val item = ListCard(consultationNum, homelessNum, homelessName, homelessUnusual, homelessLog, homelessPlace)
            list.add(item)
        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()

        var filter = 0
        //스피너 처리
        binding.spinnerList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filter = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //서치뷰로 필터링 구현
        binding.searchviewList.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                when (filter) {
                    0 -> { //필터링 없음
                        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                        sqlitedb = dbManager.readableDatabase
                        var cursor: Cursor
                        sql = "SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, h.h_name FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN homeless h ON c.h_num = h.h_num ORDER BY c.c_time DESC;"
                        cursor = sqlitedb.rawQuery(sql, arrayOf())
                        list = addToList(cursor)
                        cursor.close()
                        sqlitedb.close()
                        dbManager.close()
                        setListAdapter(list)
                    }
                    1 -> { //날짜 필터링
                        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                        sqlitedb = dbManager.readableDatabase
                        var cursor: Cursor
                        sql = "SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, h.h_name FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN homeless h ON c.h_num = h.h_num WHERE strftime('%Y-%m-%d', c.c_time) LIKE ? ORDER BY c.c_time DESC;"
                        cursor = sqlitedb.rawQuery(sql, arrayOf("%$query%"))
                        list = addToList(cursor)
                        cursor.close()
                        sqlitedb.close()
                        dbManager.close()
                        setListAdapter(list)
                    }
                    2 -> {//만난 장소 필터링
                        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                        sqlitedb = dbManager.readableDatabase
                        var cursor: Cursor
                        sql = "SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, h.h_name FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN homeless h ON c.h_num = h.h_num WHERE l.l_addr LIKE ? ORDER BY c.c_time DESC;"
                        cursor = sqlitedb.rawQuery(sql, arrayOf("%$query%"))
                        list = addToList(cursor)
                        cursor.close()
                        sqlitedb.close()
                        dbManager.close()
                        setListAdapter(list)
                    }
                    3 -> {//작성자 필터링
                        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                        sqlitedb = dbManager.readableDatabase
                        var cursor: Cursor
                        sql = "SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, h.h_name FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN homeless h ON c.h_num = h.h_num JOIN member m ON c.m_num = m.m_num WHERE m.m_name LIKE ? ORDER BY c.c_time DESC;"
                        cursor = sqlitedb.rawQuery(sql, arrayOf("%$query%"))
                        list = addToList(cursor)
                        cursor.close()
                        sqlitedb.close()
                        dbManager.close()
                        setListAdapter(list)
                    }
                    4 -> {//작성기관 필터링
                        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                        sqlitedb = dbManager.readableDatabase
                        var cursor: Cursor
                        sql = "SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, h.h_name FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN homeless h ON c.h_num = h.h_num JOIN member m ON c.m_num = m.m_num JOIN welfare_facilities w ON m.wf_num = w.wf_num WHERE w.wf_name LIKE ? ORDER BY c.c_time DESC;"
                        cursor = sqlitedb.rawQuery(sql, arrayOf("%$query%"))
                        list = addToList(cursor)
                        cursor.close()
                        sqlitedb.close()
                        dbManager.close()
                        setListAdapter(list)
                    }
                    5 -> {//내담자 필터링
                        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                        sqlitedb = dbManager.readableDatabase
                        var cursor: Cursor
                        sql = "SELECT c.c_num, c.c_time, c.c_unusual, l.l_addr, h.h_name FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN homeless h ON c.h_num = h.h_num WHERE h.h_name LIKE ? ORDER BY c.c_time DESC;"
                        cursor = sqlitedb.rawQuery(sql, arrayOf("%$query%"))
                        list = addToList(cursor)
                        cursor.close()
                        sqlitedb.close()
                        dbManager.close()
                        setListAdapter(list)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {return true}
        })

        var layoutManager = LinearLayoutManager(context)
        binding!!.listRecyclerView.layoutManager = layoutManager

        adapter = ListAdapter(requireContext(),list)
        binding!!.listRecyclerView.adapter = adapter

        binding.searchviewList.onActionViewExpanded()

        return binding.root
    }

    private fun addToList(cursor: Cursor): Vector<ListCard> {
        val items = Vector<ListCard>()
        while(cursor.moveToNext()) {
            // 리스트에 데이터 추가
            var consultationNum: Int = cursor.getInt(cursor.getColumnIndexOrThrow("c.c_num"))
            var homelessName:String = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name"))
            var homelessUnusual:String = cursor.getString(cursor.getColumnIndexOrThrow("c.c_unusual"))
            var homelessPlace:String = cursor.getString(cursor.getColumnIndexOrThrow("l.l_addr"))
            var homelessLog:String = cursor.getString(cursor.getColumnIndexOrThrow("c.c_time"))

            //리사이클러뷰 아이템 추가
            val item = ListCard(consultationNum, -1, homelessName, homelessUnusual, homelessLog, homelessPlace)
            items.add(item)
        }
        return items
    }

    private fun setListAdapter(items: Vector<ListCard>) {
        adapter = ListAdapter(requireContext(),items)
        binding!!.listRecyclerView.adapter = adapter
        binding.ListResult.text = if (items.isEmpty()) "검색 결과가 없습니다." else "검색 결과: ${items.size}개"
        binding.ListResult.visibility = View.VISIBLE
        binding.searchviewList.onActionViewExpanded()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뒤로가기 버튼을 눌렀을 때 백스택을 비우고 DashboardFragment로 이동
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 백스택을 전부 비움
                    parentFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    // DashboardFragment로 이동
                    val parentActivity = activity as DashboardActivity
                    parentActivity.setFragment(DashboardFragment())
                }
            }
        )
    }
}