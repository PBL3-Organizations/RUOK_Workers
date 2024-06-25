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
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.LocationTrackingFragment.Companion.TAG
import com.example.ruok_workers.databinding.FragmentCountingAddBinding
import com.example.ruok_workers.databinding.FragmentCountingDetailBinding
import java.util.Vector


class CountingDetailFragment : Fragment() {
    lateinit var binding: FragmentCountingDetailBinding
    lateinit var adapter: CountingDetailAdapter

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
        binding = FragmentCountingDetailBinding.inflate(inflater, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        val TAG = "CountingDetailFragment"

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        var title : String = arguments?.getString("CL_TITLE").toString()
        var course: String = arguments?.getString("CC_NAME").toString()
        var total: Int? = arguments?.getInt("CL_SUM")

        Log.d(TAG, "Title: $title, Course: $course, Total: $total")

        binding.tvTitleCountingDetail.text = title
        binding.tvCourseCountingDetail.text = course
        binding.tvResultCountingDetail.text = "총 인원: " + total.toString() + "명"

        var list = Vector<CountingDetailItem>()
        var place: String = ""
        var worker: String = ""
        var women: Int = 0
        var men: Int = 0
        var sum: Int = women + men
        val workersSet = mutableSetOf<String>()  // 중복 방지를 위한 Set 사용

        var caNum: Int? = null

        // cc_num 가져오기
        var ccQuery = "SELECT cc.cc_num FROM counting_course cc WHERE cc_name = ?"
        var ccCursor: Cursor = sqlitedb.rawQuery(ccQuery, arrayOf(course))
        var ccNum: Int? = null

        if (ccCursor.moveToFirst()) {
            ccNum = ccCursor.getInt(ccCursor.getColumnIndexOrThrow("cc_num"))
            Log.d(TAG, "ccNum: $ccNum")
        }
        ccCursor.close()

        // cl_date 가져오기
        var dateQuery = "SELECT cl.cl_date FROM counting_list cl WHERE cl_title = ?"
        var dateCursor: Cursor = sqlitedb.rawQuery(dateQuery, arrayOf(title))
        var clDate: String = ""

        if (dateCursor.moveToFirst()) {
            clDate = dateCursor.getString(dateCursor.getColumnIndexOrThrow("cl_date"))
            Log.d(TAG, "clDate: $clDate")
        }
        dateCursor.close()

        // cl_order 가져오기
        var orderQuery = "SELECT cl.cl_order FROM counting_list cl WHERE cl_title = ?"
        var orderCursor: Cursor = sqlitedb.rawQuery(orderQuery, arrayOf(title))
        var clOrder: Int? = null

        if (orderCursor.moveToFirst()) {
            clOrder = orderCursor.getInt(orderCursor.getColumnIndexOrThrow("cl_order"))
            Log.d(TAG, "clOrder: $clOrder")
        }
        orderCursor.close()

        var query: String = ""
        query += "SELECT ca.ca_name, cr.cr_sum, cr.cr_male, cr.cr_female, m.m_name "
        query += "FROM counting_record cr "
        query += "JOIN member m ON cr.m_num = m.m_num "
        query += "JOIN counting_area ca ON cr.ca_num = ca.ca_num "
        query += "JOIN counting_course cc ON cc.cc_num = ca.cc_num "
        query += "WHERE cr.cl_date ='$clDate' AND cr.cl_order='$clOrder' AND cc.cc_num='$ccNum';"

        Log.d(TAG, "Query: $query")

        var cursor: Cursor = sqlitedb.rawQuery(query, arrayOf())

        while (cursor.moveToNext()){
            place = cursor.getString(cursor.getColumnIndexOrThrow("ca_name")).toString()
            worker = cursor.getString(cursor.getColumnIndexOrThrow("m_name")).toString()
            women = cursor.getInt(cursor.getColumnIndexOrThrow("cr_female"))
            men = cursor.getInt(cursor.getColumnIndexOrThrow("cr_male"))
            sum = cursor.getInt(cursor.getColumnIndexOrThrow("cr_sum"))
            workersSet.add(worker)  // Set에 worker 추가

            var item = CountingDetailItem(place, worker, women, men, sum)
            list.add(item)
            Log.d(TAG, "Item added: $item")
        }

        cursor.close()

        // Set을 쉼표로 구분된 문자열로 변환
        val workers = workersSet.joinToString(", ")

        binding.tvWorkerCountingDetail.text = "작성자: " + workers

        val layoutManager = LinearLayoutManager(context)
        binding!!.rvCountingDetail.layoutManager = layoutManager

        Log.d(TAG, "Setting adapter with list size: ${list.size}")

        adapter = CountingDetailAdapter(requireContext(), list)
        binding!!.rvCountingDetail.adapter = adapter

        sqlitedb.close()
        dbManager.close()


        binding.btnRevisionCountingDetail.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(CountingRevisionFragment())
        }

        binding.btnListCountingDetail.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(CountingListFragment())
        }

        binding.btnDeleteCountingDetail.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage("본 카운팅 테이블을 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, which ->
                    //데이터베이스 연동
                    dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                    sqlitedb = dbManager.writableDatabase

                    var query2: String = ""
                    query2 += "SELECT ca.ca_num "
                    query2 += "FROM counting_record cr "
                    query2 += "JOIN member m ON cr.m_num = m.m_num "
                    query2 += "JOIN counting_area ca ON cr.ca_num = ca.ca_num "
                    query2 += "JOIN counting_course cc ON cc.cc_num = ca.cc_num "
                    query2 += "WHERE cr.cl_date ='$clDate' AND cr.cl_order='$clOrder' AND cc.cc_num='$ccNum'"

                    var cursor2: Cursor
                    cursor2 = sqlitedb.rawQuery(query2, arrayOf())

                    // 레코드 삭제 쿼리 실행
                    while (cursor2.moveToNext()){
                        caNum = cursor2.getInt(cursor2.getColumnIndexOrThrow("ca_num"))
                        var deleteRecordQuery = "DELETE FROM counting_record WHERE cl_date = ? AND cl_order= ? AND ca_num= ?"
                        sqlitedb.execSQL(deleteRecordQuery, arrayOf(clDate, clOrder, caNum))
                    }

                    // 리스트 삭제 쿼리 실행
                    var deleteListQuery = "DELETE FROM counting_list WHERE cl_date = ? AND cl_order= ? AND cc_num= ?"
                    sqlitedb.execSQL(deleteListQuery, arrayOf(clDate, clOrder, ccNum))

                    sqlitedb.close()
                    dbManager.close()

                    val DashboardActivity = activity as DashboardActivity
                    DashboardActivity.setFragment(CountingListFragment())
                    Toast.makeText(requireContext(), "카운팅 삭제!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("취소", null)
                .show()
        }

        return binding!!.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}