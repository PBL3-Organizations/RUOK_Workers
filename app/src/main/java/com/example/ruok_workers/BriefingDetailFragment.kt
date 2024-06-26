package com.example.ruok_workers

import BriefingBoardFragment
import android.app.AlertDialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentBriefingDetailBinding

class BriefingDetailFragment : Fragment() {
    lateinit var binding: FragmentBriefingDetailBinding
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    var b_num = -1
    var loginNum : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            b_num = it.getInt("b_num", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_briefing_detail, container, false)
        val buttonEdit = view.findViewById<Button>(R.id.button_edit)
        val buttonBack = view.findViewById<Button>(R.id.button_back)
        val buttonDelete = view.findViewById<Button>(R.id.button_delete)
        val tvDetailAuthor = view.findViewById<TextView>(R.id.tvDetailAuthor)
        val tvDetailTitle = view.findViewById<TextView>(R.id.tvDetailTitle)
        val tvDetailTimestamp = view.findViewById<TextView>(R.id.tvDetailTimestamp)
        val tvBriefingDetails = view.findViewById<TextView>(R.id.etBriefingDetails)

        // 데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        // b_num을 확인하고, 유효하지 않은 경우 예외 처리
        if (b_num == -1) {
            Log.e("BriefingDetailFragment", "Invalid b_num")
            return view
        }
        Log.d("BriefingDetailFragment", "Received b_num: $b_num")

        val sql = """
            SELECT b.b_num, b.b_title AS title, b.b_content AS content, m.m_name AS author, b.b_time AS timestamp 
            FROM briefing b 
            JOIN member m ON m.m_num = b.m_num 
            WHERE b.b_num = ?
        """
        val cursor: Cursor = sqlitedb.rawQuery(sql, arrayOf(b_num.toString()))

        if (cursor.moveToFirst()) {
            tvDetailAuthor.text = cursor.getString(cursor.getColumnIndexOrThrow("author"))
            tvDetailTitle.text = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            tvDetailTimestamp.text = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"))
            tvBriefingDetails.text = cursor.getString(cursor.getColumnIndexOrThrow("content"))
        } else {
            Log.e("BriefingDetailFragment", "No data returned from query")

        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()

        buttonEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("b_num", b_num)
            bundle.putString("b_title", tvDetailTitle.text.toString())
            bundle.putString("b_content", tvBriefingDetails.text.toString())

            val briefingRevisionFragment = BriefingRevisionFragment()
            briefingRevisionFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.rootLayout, briefingRevisionFragment)
                .commit()
        }

        buttonBack.setOnClickListener {

            val bundle = Bundle()
            bundle.putInt("m_num", loginNum)

            val briefingBoardFragment = BriefingBoardFragment()
            briefingBoardFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.rootLayout, briefingBoardFragment)
                .commit()
        }

        buttonDelete.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("삭제하시겠습니까?")
            alertDialogBuilder.setPositiveButton("예") { _, _ ->
                // 삭제 동작을 여기에 추가
                dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                sqlitedb = dbManager.writableDatabase
                sqlitedb.delete("briefing", "b_num = ?", arrayOf(b_num.toString()))
                sqlitedb.close()
                dbManager.close()
                requireActivity().onBackPressed()
            }
            alertDialogBuilder.setNegativeButton("아니오") { dialog, _ ->
                dialog.dismiss()
            }
            alertDialogBuilder.show()
        }
        return view
    }
}