package com.example.ruok_workers

import BriefingBoardFragment
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class BriefingRevisionFragment : Fragment() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var tabPosition = -1
    var b_num = -1
    var new_notice = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_briefing_revision, container, false)
        val CheckBox = view.findViewById<CheckBox>(R.id.checkbox_set_as_notice)

        b_num =arguments?.getInt("b_num",0)!!
        //tabPosition 가져오기
        tabPosition = arguments?.getInt("tabPosition", -1)!!
        var b_title = arguments?.getString("b_title").toString()
        var b_content = arguments?.getString("b_content").toString()
        // Accessing TextView for title and content
        val titleTextView = view.findViewById<TextView>(R.id.title_text)
        val contentTextView = view.findViewById<TextView>(R.id.content_text)

        // Setting initial text for title and content
        titleTextView.text = b_title
        contentTextView.text = b_content
       //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        val cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM briefing WHERE b_num = ?", arrayOf(b_num.toString()))
        while (cursor.moveToNext()){
            new_notice = cursor.getInt(cursor.getColumnIndexOrThrow("b_notice")).toInt()
            if (new_notice == 1){CheckBox.isChecked = true}
        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()
        // Accessing Button and setting click listener
        val editButton = view.findViewById<Button>(R.id.edit_button)
        editButton.setOnClickListener {
            val new_title = view.findViewById<EditText>(R.id.title_text).text
            val new_content = view.findViewById<EditText>(R.id.content_text).text
            if (CheckBox.isChecked) {
                new_notice = 1
            }

            //데이터베이스 연동
            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
            sqlitedb = dbManager.writableDatabase
            val sql = "UPDATE briefing SET b_title=?, b_content=?, b_notice=? WHERE b_num=?;"
            sqlitedb.execSQL(sql, arrayOf(new_title.toString(), new_content.toString(), new_notice.toString(), b_num.toString()))
            sqlitedb.close()
            dbManager.close()

            //수정 완료 시 '완료' 알림
            Toast.makeText(activity, "수정 완료", Toast.LENGTH_SHORT).show()

            // 수정 후에 다시 브리핑 보드로 돌아가기
            val briefingBoardFragment = BriefingBoardFragment()
            val bundle = Bundle()
            bundle.putInt("tabPosition", tabPosition)
            briefingBoardFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.rootLayout, briefingBoardFragment)
                .commit()

        }

        return view
    }
}
