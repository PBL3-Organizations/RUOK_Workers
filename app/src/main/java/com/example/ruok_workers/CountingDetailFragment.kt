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

        var query: String = ""
        query += "SELECT ca.ca_name, cr.cr_sum, cr.cr_male, cr.cr_female, m.m_name "
        query += "FROM counting_record cr "
        query += "JOIN counting_area ca ON cr.ca_num = ca.ca_num "
        query += "JOIN member m ON cr.m_num = m.m_num "

        var cursor: Cursor
        cursor = sqlitedb.rawQuery(query, arrayOf())

        while (cursor.moveToNext()){
            place = cursor.getString(cursor.getColumnIndexOrThrow("ca_name"))
            worker = cursor.getString(cursor.getColumnIndexOrThrow("m_name"))
            women = cursor.getInt(cursor.getColumnIndexOrThrow("cr_female"))
            men = cursor.getInt(cursor.getColumnIndexOrThrow("cr_male"))
            sum = cursor.getInt(cursor.getColumnIndexOrThrow("cr_sum"))
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

                    // cc_num 가져오기
                    var selectQuery: String = "SELECT cc.cc_num FROM counting_course cc WHERE cc_name = " + course
                    var ccCursor: Cursor = sqlitedb.rawQuery(selectQuery, arrayOf(course))
                    var ccNum: Int? = null

                    if (ccCursor.moveToFirst()) {
                        ccNum = ccCursor.getInt(ccCursor.getColumnIndexOrThrow("cc_num"))
                    }
                    ccCursor.close()

                    // 삭제 쿼리 실행
                    var deleteQuery: String = "DELETE counting_list WHERE cl_title = " + title + " AND cc_num = " + ccNum

                    sqlitedb.execSQL(deleteQuery, arrayOf(title, ccNum))

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