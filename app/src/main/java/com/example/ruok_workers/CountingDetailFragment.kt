package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentCountingAddBinding
import com.example.ruok_workers.databinding.FragmentCountingDetailBinding
import java.util.Vector


class CountingDetailFragment : Fragment() {
    lateinit var binding: FragmentCountingDetailBinding
    lateinit var adapter: CountingDetailAdapter

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
        binding = FragmentCountingDetailBinding.inflate(inflater, container, false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        var title : String = arguments?.getString("CL_TITLE").toString()
        var course: String = arguments?.getString("CC_NAME").toString()
        var total: Int? = arguments?.getInt("CL_SUM")

        binding.tvTitleCountingDetail.text = title
        binding.tvCourseCountingDetail.text = course
        binding.tvResultCountingDetail.text = "총 인원: " + total.toString() + "명"

        var list = Vector<CountingDetailItem>()
        var place: String
        var worker: String
        var women: Int
        var men: Int
        var sum: Int
        val workersSet = mutableSetOf<String>()  // 중복 방지를 위한 Set 사용

        // cc_num 가져오기
        var ccQuery = "SELECT ca.cc_num FROM counting_area ca WHERE ca_name = ?"
        var ccCursor: Cursor = sqlitedb.rawQuery(ccQuery, arrayOf(course))
        var ccNum: Int? = null

        if (ccCursor.moveToFirst()) {
            ccNum = ccCursor.getInt(ccCursor.getColumnIndexOrThrow("cc_num"))
        }
        ccCursor.close()

        // ca_num 가져오기
        var caQuery = "SELECT ca.ca_num FROM counting_area ca WHERE ca_name = ?"
        var caCursor: Cursor = sqlitedb.rawQuery(caQuery, arrayOf(course))
        var caNum: Int? = null

        if (caCursor.moveToFirst()) {
            caNum = caCursor.getInt(caCursor.getColumnIndexOrThrow("ca_num"))
        }
        caCursor.close()

        // cl_date 가져오기
        var dateQuery = "SELECT cl.cl_date FROM counting_list cl WHERE cl_title = ?"
        var dateCursor: Cursor = sqlitedb.rawQuery(dateQuery, arrayOf(title))
        var clDate: String = ""

        if (dateCursor.moveToFirst()) {
            clDate = dateCursor.getString(dateCursor.getColumnIndexOrThrow("cl_date"))
        }
        dateCursor.close()

        // cl_order 가져오기
        var orderQuery = "SELECT cl.cl_order FROM counting_list cl WHERE cl_title = ?"
        var orderCursor: Cursor = sqlitedb.rawQuery(orderQuery, arrayOf(title))
        var clOrder: Int? = null

        if (orderCursor.moveToFirst()) {
            clOrder = orderCursor.getInt(orderCursor.getColumnIndexOrThrow("cl_order"))
        }
        orderCursor.close()

        var query: String = ""
        query += "SELECT ca.ca_name, cr.cr_sum, cr.cr_male, cr.cr_female, m.m_name "
        query += "FROM counting_record cr "
        query += "JOIN counting_area ca ON cr.ca_num = ca.ca_num "
        query += "JOIN member m ON cr.m_num = m.m_num "
//        query+= "SELECT ca.ca_num, ca.ca_name, m.m_name, cr.cr_male, cr.cr_female, cr.cr_sum"
//        query+= "FROM counting_record cr"
//        query+= "JOIN member m ON cr.m_num = m.m_num"
//        query+= "JOIN counting_area ca ON cr.ca_num = ca.ca_num"
//        query+= "JOIN counting_course cc ON cc.cc_num = ca.cc_num"
//        query+= "WHERE cr.cl_date =? AND cr.cl_order=? AND cc.cc_num=?;"

        var cursor: Cursor
        cursor = sqlitedb.rawQuery(query, arrayOf(arrayOf(clDate, clOrder, ccNum).toString()))

        while (cursor.moveToNext()){
            place = cursor.getString(cursor.getColumnIndexOrThrow("ca.ca_name"))
            worker = cursor.getString(cursor.getColumnIndexOrThrow("m.m_name"))
            women = cursor.getInt(cursor.getColumnIndexOrThrow("cr.cr_female"))
            men = cursor.getInt(cursor.getColumnIndexOrThrow("cr.cr_male"))
            sum = cursor.getInt(cursor.getColumnIndexOrThrow("cr.cr_sum"))
            workersSet.add(worker)  // Set에 worker 추가

            var item = CountingDetailItem(place, worker, women, men, sum)
            list.add(item)
        }

        // Set을 쉼표로 구분된 문자열로 변환
        val workers = workersSet.joinToString(", ")

        binding.tvWorkerCountingDetail.text = "작성자: " + workers

        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding!!.rvCountingDetail.layoutManager = layoutManager

        adapter = CountingDetailAdapter(requireContext(), list)
        binding!!.rvCountingDetail.adapter = adapter

        cursor.close()
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

                    // 삭제 쿼리 실행
                    var deleteRecordQuery = "DELETE FROM counting_record WHERE cl_date = ? AND cl_order= ? AND ca_num= ?;"
                    var deleteListQuery = "DELETE FROM counting_list WHERE cl_date = ? AND cl_order= ? AND cc_num= ?;"

                    sqlitedb.execSQL(deleteRecordQuery, arrayOf(clDate, clOrder, caNum))
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

        return binding.root
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