package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentCountingRevisionBinding
import java.util.Vector


class CountingRevisionFragment : Fragment() {
    lateinit var binding: FragmentCountingRevisionBinding
    lateinit var detailAdapter: CountingDetailAdapter
    lateinit var revisionAdapter: CountingRevisionAdapter

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
        binding = FragmentCountingRevisionBinding.inflate(inflater, container, false)

        val bundle = Bundle()

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        var title : String = arguments?.getString("CL_TITLE").toString()
        var course: String = arguments?.getString("CC_NAME").toString()
        var workers : String = arguments?.getString("M_NAME").toString()
        var ccNum : Int = arguments?.getInt("CC_NUM")!!
        var clDate : String = arguments?.getString("CL_DATE").toString()
        var clOrder : String = arguments?.getInt("CL_ORDER").toString()

        binding.tvTitleCountingRevision.text = title
        binding.tvCourseCountingRevision.text = course
        binding.tvWorkerCountingRevision.text = "작성자: " + workers

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        var list = Vector<CountingRevisionItem>()

        var query: String = ""
        query += "SELECT ca.ca_name, cr.cr_male, cr.cr_female, m.m_name "
        query += "FROM counting_record cr "
        query += "JOIN member m ON cr.m_num = m.m_num "
        query += "JOIN counting_area ca ON cr.ca_num = ca.ca_num "
        query += "JOIN counting_course cc ON cc.cc_num = ca.cc_num "
        query += "WHERE cr.cl_date ='$clDate' AND cr.cl_order='$clOrder' AND cc.cc_num='$ccNum';"

        var cursor: Cursor = sqlitedb.rawQuery(query, arrayOf())

        while (cursor.moveToNext()){
            var place = cursor.getString(cursor.getColumnIndexOrThrow("ca_name")).toString()
            var worker = cursor.getString(cursor.getColumnIndexOrThrow("m_name")).toString()
            var women = cursor.getInt(cursor.getColumnIndexOrThrow("cr_female"))
            var men = cursor.getInt(cursor.getColumnIndexOrThrow("cr_male"))

            var item = CountingRevisionItem(place, worker, women, men)
            list.add(item)
        }

        cursor.close()

        val layoutManager = LinearLayoutManager(context)
        binding!!.rvCountingRevision.layoutManager = layoutManager

        revisionAdapter = CountingRevisionAdapter(requireContext(), list)
        binding!!.rvCountingRevision.adapter = revisionAdapter

        sqlitedb.close()
        dbManager.close()

        var total : Int = 0

        binding.btnCountingRevision.setOnClickListener {

            try {
                //데이터베이스 연동
                dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                sqlitedb = dbManager.writableDatabase

                val itemList = revisionAdapter.items
                for (item in itemList) {
                    var place = item.place
                    var women = item.women
                    var men = item.men
                    var worker = item.worker
                    var sum = women + men
                    total += sum

                    //카운팅 코스 번호 가져오기
                    val ccNumQuery = "SELECT cc.cc_num FROM counting_course cc WHERE cc_num = '$ccNum'"
                    val ccNumCursor: Cursor = sqlitedb.rawQuery(ccNumQuery, arrayOf())
                    var ccNum = if (ccNumCursor.moveToFirst()) ccNumCursor.getInt(
                        ccNumCursor.getColumnIndexOrThrow("cc_num")
                    ) else -1
                    ccNumCursor.close()

                    //카운팅 리스트 수정
                    sqlitedb.execSQL("UPDATE counting_list SET cl_sum='$total' WHERE cl_date ='$clDate' AND cl_order='$clOrder' AND cc_num='$ccNum';")

                    //카운팅 구역 번호 가져오기
                    val caNumQuery = "SELECT ca_num, ca_name FROM counting_area WHERE ca_name = '$place'"
                    val caNumCursor: Cursor = sqlitedb.rawQuery(caNumQuery, arrayOf())
                    var caNum : Int ?= null

                    //카운팅 레코드 수정
                    while (caNumCursor.moveToNext()) {
                        caNum = caNumCursor.getInt(caNumCursor.getColumnIndexOrThrow("ca_num"))
                        sqlitedb.execSQL("UPDATE counting_record SET cr_male='$men', cr_female='$women', cr_sum='$sum' WHERE cl_date = '$clDate' AND cl_order='$clOrder' AND ca_num='$caNum';")
                    }
                    caNumCursor.close()

                }
            } catch (e: Exception) {
                Log.e("CountingRevisionFragment", "Error saving counting data", e)
            } finally {
                sqlitedb.close()
                dbManager.close()
            }

            val countingDetailFragment = CountingDetailFragment()
            bundle.putString("CL_TITLE", title)
            bundle.putString("CC_NAME", course)
            bundle.putInt("CL_SUM", total)
            countingDetailFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, countingDetailFragment).commit()
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingRevisionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}