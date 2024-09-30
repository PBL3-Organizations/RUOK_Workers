package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentCountingListBinding
import java.util.Vector


class CountingListFragment : Fragment() {
    lateinit var binding: FragmentCountingListBinding
    lateinit var adapter: CountingListAdapter

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var loginNum: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountingListBinding.inflate(inflater, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        var list = Vector<CountingListItem>()
        var title: String
        var course: String
        var sum: Int

        list.clear()

        var query: String = ""
        query += "SELECT cl.cl_title, cl.cl_sum, cc.cc_name "
        query += "FROM counting_list cl JOIN counting_course cc "
        query += "ON cl.cc_num = cc.cc_num ORDER BY cl.cl_title DESC"
        var cursor: Cursor
        cursor = sqlitedb.rawQuery(query, arrayOf())
        while (cursor.moveToNext()){

            title = cursor.getString(cursor.getColumnIndexOrThrow("cl_title")).toString()
            course = cursor.getString(cursor.getColumnIndexOrThrow("cc_name")).toString()
            sum = cursor.getInt(cursor.getColumnIndexOrThrow("cl_sum"))

            var item = CountingListItem(title, course, sum)
            list.add(item)
        }

        cursor.close()

        val layoutManager = LinearLayoutManager(context)
        binding!!.rvCountingList.layoutManager = layoutManager

        adapter = CountingListAdapter(requireContext(), list)
        binding!!.rvCountingList.adapter = adapter

        sqlitedb.close()
        dbManager.close()

        return binding!!.root
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

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}