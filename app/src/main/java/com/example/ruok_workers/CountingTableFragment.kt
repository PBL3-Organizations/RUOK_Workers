package com.example.ruok_workers

import android.app.DownloadManager.Query
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentCountingAddBinding
import com.example.ruok_workers.databinding.FragmentCountingTableBinding
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Vector


class CountingTableFragment : Fragment() {
    lateinit var binding: FragmentCountingTableBinding
    lateinit var adapter: CountingTableAdapter

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
        binding = FragmentCountingTableBinding.inflate(inflater, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.writableDatabase

        var list = Vector<CountingTableItem>()
        var place: String
        var women: Int = 0
        var men: Int = 0
        var sum: Int = women + men

        var selectedCourse: String = arguments?.getString("course").toString()
        var selectedLocations = arguments?.getStringArrayList("locations") ?: arrayListOf()

        binding.tvShowCourse.text = "코스: $selectedCourse"

        for (place in selectedLocations) {

            val initialItem = CountingTableItem(place, 0, 0, 0)
            list.add(initialItem)
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvCountingTable.layoutManager = layoutManager

        adapter = CountingTableAdapter(requireContext(), list)
        binding.rvCountingTable.adapter = adapter

        binding.btnSaveCountingTable.setOnClickListener {
            val parentActivity = activity as DashboardActivity

            // AlertDialog 빌더 생성
            val builder = AlertDialog.Builder(parentActivity)
            builder.setTitle("몇 차 카운팅 업무인가요?")

            // 숫자를 입력받기 위한 EditText 생성
            var input = EditText(parentActivity)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            input.hint = "0"
            builder.setView(input)

            // 확인 버튼 설정
            builder.setPositiveButton("확인") { _, _ ->
                // 입력된 숫자 가져오기
                val countingOrder = input.text.toString().toInt()
                val currentTime : Long = System.currentTimeMillis() // ms로 반환
                val formatDate = SimpleDateFormat("yyyyMMdd")
                val date = formatDate.format(currentTime)
                val formatTitle = SimpleDateFormat("yyyy년M월dd일")
                val formatter = formatTitle.format(currentTime)
                val titleList = listOf(formatter, " ", countingOrder, "차 카운팅")
                val title = titleList.joinToString("")
                var total: Int = 0

                try {
                    // 데이터베이스 저장
                    for (item in list) {
                        val place = item.place
                        val women = item.women
                        val men = item.men
                        var sum = women + men
                        total += sum

                        //카운팅 구역 번호 가져오기
                        //val caNumQuery = "SELECT ca.ca_num FROM counting_area ca WHERE cc_num = " + ccNum
                        val caNumQuery = "SELECT ca.ca_num FROM counting_area ca WHERE ca_name = " + place
                        val caNumCursor: Cursor = sqlitedb.rawQuery(caNumQuery, arrayOf())
                        var caNum = if (caNumCursor.moveToFirst()) caNumCursor.getInt(
                            caNumCursor.getColumnIndexOrThrow("ca.ca_num")
                        ) else -1
                        caNumCursor.close()

                        //카운팅 코스 번호 가져오기
                        //val ccNumQuery = "SELECT cc.cc_num, cc.cc_name FROM counting_course cc JOIN welfare_facilities w ON cc.wf_num = w.wf_num JOIN member m ON m.wf_num = w.wf_num WHERE m.m_num = " + loginNum
                        val ccNumQuery = "SELECT ca.cc_num FROM counting_area ca WHERE ca_name = " + place
                        val ccNumCursor: Cursor = sqlitedb.rawQuery(ccNumQuery, arrayOf())
                        var ccNum = if (ccNumCursor.moveToFirst()) ccNumCursor.getInt(
                            ccNumCursor.getColumnIndexOrThrow("ca.cc_num")
                        ) else -1
                        ccNumCursor.close()

                        // 카운팅 테이블 저장하기 전 기존 내역 확인
                        val query =
                            "SELECT cl_date FROM counting_list WHERE cl_date=? AND cl_order=? AND cc_num=?"
                        val cursor: Cursor = sqlitedb.rawQuery(query,
                            arrayOf(arrayOf(date, countingOrder, ccNum).toString())
                        )

                        if (cursor.count > 0) {
                            // 기존에 저장내역이 있으면 수정
                            sqlitedb.execSQL(
                                "UPDATE counting_list SET cl_sum=? WHERE cl_date=? AND cl_order=? AND cc_num=?",
                                arrayOf(total, date, countingOrder, ccNum)
                            )
                        } else {
                            // 기존에 저장 내역이 없으면 새로운 카운팅 리스트 추가
                            sqlitedb.execSQL(
                                "INSERT INTO counting_list (cl_date, cl_order, cc_num, cl_title, cl_sum) VALUES (?, ?, ?, ?, ?)",
                                arrayOf(date, countingOrder, ccNum, title, total)
                            )
                        }

                        // 카운팅 레코드 추가
                        sqlitedb.execSQL(
                            "INSERT INTO counting_record (cl_date, cl_order, ca_num, m_num, cr_male, cr_female, cr_sum) VALUES (?, ?, ?, ?, ?, ?, ?)",
                            arrayOf(date, countingOrder, caNum, loginNum, men, women, sum)
                        )

                        cursor.close()

                    }
                } catch (e: Exception) {
                    Log.e("CountingTableFragment", "Error saving counting data", e)
                    Toast.makeText(requireContext(), "에러 발생: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    sqlitedb.close()
                    dbManager.close()
                }

                parentActivity.setFragment(CountingListFragment())
                Toast.makeText(requireContext(), "카운팅 저장!", Toast.LENGTH_SHORT).show()
            }

            // 취소 버튼 설정
            builder.setNegativeButton("취소") { dialog, which ->
                dialog.cancel()
            }

            // AlertDialog 보여주기
            builder.show()
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingTableFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}