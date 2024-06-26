package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var c_num = -1
    var h_num = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        //상담내역 번호, 노숙인 이름 가져오기
        c_num = arguments?.getInt("c_num", 0)!!
        h_num = arguments?.getInt("h_num", -1)!!

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        var sql = ""
        if (h_num == 0) {
            sql = "SELECT c.c_num, m.m_name, '미상' AS h_name, '미상' AS h_birth, c.c_time, c.c_health, c.c_unusual, c.c_measure, c.c_content, l.l_addr FROM consultation c JOIN member m ON c.m_num = m.m_num JOIN location l ON c.c_num = l.c_num WHERE c.c_num = ?;"
        } else {
            sql = "SELECT c.c_num, m.m_name, h.h_name, h.h_birth, c.c_time, c.c_health, c.c_unusual, c.c_measure, c.c_content, l.l_addr FROM consultation c JOIN member m ON c.m_num = m.m_num JOIN homeless h ON c.h_num = h.h_num JOIN location l ON c.c_num = l.c_num WHERE c.c_num = ?;"
        }
        cursor = sqlitedb.rawQuery(sql, arrayOf(c_num.toString()))
        cursor.moveToNext()
        binding.tvAddr.text = "만난 장소: "+cursor.getString(cursor.getColumnIndexOrThrow("l.l_addr"))
        binding.tvUnusual.text = "특이사항: "+cursor.getString(cursor.getColumnIndexOrThrow("c.c_unusual"))
        binding.tvContent.text = "상담 내용: "+cursor.getString(cursor.getColumnIndexOrThrow("c.c_content"))
        binding.tvMeasure.text = "조치사항: "+cursor.getString(cursor.getColumnIndexOrThrow("c.c_measure"))
        //건강상태코드에서 값 가져오기
        val health = cursor.getInt(cursor.getColumnIndexOrThrow("c.c_health"))
        when (health) {
            1 -> binding.tvHealth.text = "건강상태: 양호"
            2 -> binding.tvHealth.text = "건강상태: 활동에 다소 지장"
            3 -> binding.tvHealth.text = "건강상태: 활동에 많이 지장"
            4 -> binding.tvHealth.text = "건강상태: 재활치료 필요"
        }
        //작성시간 데이터 가공
        val c_time = cursor.getString(cursor.getColumnIndexOrThrow("c.c_time"))
        binding.tvTime.text = "입력시간: "+c_time.substring(0,16)

        if (h_num == 0) {
            binding.tvName.text = "이름: 미상"
            binding.tvBirth.text = "이름: 미상"
        } else {
            binding.tvName.text = "이름: "+cursor.getString(cursor.getColumnIndexOrThrow("h.h_name"))
            //생년월일 데이터 가공
            val h_birth = cursor.getString(cursor.getColumnIndexOrThrow("h.h_birth"))
            binding.tvBirth.text = "생년월일: "+h_birth.substring(0,4)+"."+h_birth.substring(4,6)+"."+h_birth.substring(6)
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        //btnGoRevision클릭시 DatailsFragment에서 RevisionFragment로 이동
        binding.btnGoRevision.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            val bundle = Bundle()
            bundle.putInt("c_num", c_num)
            bundle.putInt("h_num", h_num)
            val revisionFragment = RevisionFragment()
            revisionFragment.arguments = bundle
            DashboardActivity.setFragment(revisionFragment)
        }
        //btnGoList클릭시 DatailsFragment에서 ListFragment로 이동
        binding.btnGoList.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(ListFragment())
        }
        //btnDelete클릭시 AlertDialog 생성
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage("본 게시물을 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, which ->
                    //데이터베이스에서 해당 상담내역 삭제
                    dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                    sqlitedb = dbManager.writableDatabase
                    var sql = "DELETE FROM consultation WHERE c_num = ?;"
                    sqlitedb.execSQL(sql, arrayOf(c_num.toString()))
                    sql = "DELETE FROM location WHERE c_num = ?;"
                    sqlitedb.execSQL(sql, arrayOf(c_num.toString()))
                    sql = "DELETE FROM photo WHERE c_num = ?;"
                    sqlitedb.execSQL(sql, arrayOf(c_num.toString()))
                    sqlitedb.close()
                    dbManager.close()

                    val DashboardActivity = activity as DashboardActivity
                    DashboardActivity.setFragment(ListFragment())
                    Toast.makeText(requireContext(), "상담내역 삭제!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("취소", null)
                .show()

        }

        return this.binding.root
    }

}