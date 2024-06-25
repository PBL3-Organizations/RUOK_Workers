package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentDetailsBinding
import com.example.ruok_workers.databinding.FragmentRevisionBinding

class RevisionFragment : Fragment() {
    lateinit var binding: FragmentRevisionBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var author = -1
    var homeless = -1
    var c_num = -1
    var health = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRevisionBinding.inflate(inflater, container, false)

        //상담내역 번호 가져오기
        c_num = arguments?.getInt("c_num", 0)!!

        //데이터베이스 연동 및 기존 데이터 적용
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        val sql = "SELECT m_num, h_num, c_health, c_unusual, c_measure, c_content FROM consultation WHERE c_num = ?;"
        cursor = sqlitedb.rawQuery(sql, arrayOf(c_num.toString()))
        cursor.moveToNext()
        binding.edtUnusualRev.setText(cursor.getString(cursor.getColumnIndexOrThrow("c_unusual")))
        binding.edtMeasureRev.setText(cursor.getString(cursor.getColumnIndexOrThrow("c_measure")))
        binding.edtContentRev.setText(cursor.getString(cursor.getColumnIndexOrThrow("c_content")))
        author = cursor.getInt(cursor.getColumnIndexOrThrow("m_num"))
        homeless = cursor.getInt(cursor.getColumnIndexOrThrow("h_num"))
        //건강상태 코드 적용
        health = cursor.getInt(cursor.getColumnIndexOrThrow("c_health"))
        when(health) {
            1 -> {
                setFirstBtnColor()
                binding.btnRevisionGood.setBackgroundColor(Color.GREEN)
            }
            2 -> {
                setFirstBtnColor()
                binding.btnRevisionNotbad.setBackgroundColor(Color.BLUE)
            }
            3 -> {
                setFirstBtnColor()
                binding.btnRevisionBad.setBackgroundColor(Color.parseColor("#FAA900"))
            }
            4 -> {
                setFirstBtnColor()
                binding.btnRevisionNeed.setBackgroundColor(Color.RED)
            }
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        //btnRevision클릭시 RevisionFragment에서 PhotoRevisionFragment로 이동
        binding.btnRevisionNext.setOnClickListener {
            val m_num = author
            val h_num = homeless
            val unusual = binding.edtUnusualRev.text.toString()
            val measure = binding.edtMeasureRev.text.toString()
            val content = binding.edtContentRev.text.toString()

            val bundle = Bundle()
            val item = ConsultationItem(m_num, h_num, "", health, unusual, measure, content, "", 0.0, 0.0, arrayOf(""))
            bundle.putInt("hasConsultation", 1)
            bundle.putInt("c_num", c_num)
            bundle.putParcelable("consultation_item", item)
            val photoRevisionFragment = PhotoRevisionFragment()
            photoRevisionFragment.arguments = bundle
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(photoRevisionFragment)
        }
        //건강상태 버튼 클릭시 색변경
        binding.btnRevisionGood.setOnClickListener {
            health = 1
            setFirstBtnColor()
            binding.btnRevisionGood.setBackgroundColor(Color.GREEN)
        }
        binding.btnRevisionNotbad.setOnClickListener {
            health = 2
            setFirstBtnColor()
            binding.btnRevisionNotbad.setBackgroundColor(Color.BLUE)
        }
        binding.btnRevisionBad.setOnClickListener {
            health = 3
            setFirstBtnColor()
            binding.btnRevisionBad.setBackgroundColor(Color.parseColor("#FAA900"))
        }
        binding.btnRevisionNeed.setOnClickListener {
            health = 4
            setFirstBtnColor()
            binding.btnRevisionNeed.setBackgroundColor(Color.RED)
        }
        return this.binding.root
    }
    private fun setFirstBtnColor(){
        binding.btnRevisionGood.setBackgroundColor(Color.parseColor("#8F9090"))
        binding.btnRevisionNotbad.setBackgroundColor(Color.parseColor("#8F9090"))
        binding.btnRevisionBad.setBackgroundColor(Color.parseColor("#8F9090"))
        binding.btnRevisionNeed.setBackgroundColor(Color.parseColor("#8F9090"))
    }
}