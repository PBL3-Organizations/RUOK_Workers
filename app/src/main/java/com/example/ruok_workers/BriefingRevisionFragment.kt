package com.example.ruok_workers

import BriefingBoardFragment
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class BriefingRevisionFragment : Fragment() {

    private var initialTitle: String = "기본 제목"
    private var initialContent: String = "기본 내용"

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    var b_num = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_briefing_revision, container, false)
        val CheckBox = view.findViewById<TextView>(R.id.checkbox_set_as_notice)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        b_num =arguments?.getInt("b_num",0)!!
        var b_title = arguments?.getString("b_title").toString()
        var b_content = arguments?.getString("b_content").toString()
        // Accessing TextView for title and content
        val titleTextView = view.findViewById<TextView>(R.id.title_text)
        val contentTextView = view.findViewById<TextView>(R.id.content_text)

        // Setting initial text for title and content
        titleTextView.text = b_title
        contentTextView.text = b_content

        // Accessing Button and setting click listener
        val editButton = view.findViewById<Button>(R.id.edit_button)
        editButton.setOnClickListener {
            //수정 완료 시 '완료' 알림
            Toast.makeText(activity, "수정 완료", Toast.LENGTH_SHORT).show()

            // 수정 후에 다시 브리핑 보드로 돌아가기
            val fragment = BriefingBoardFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.rootLayout, fragment)
            transaction.addToBackStack(null) // Add to back stack so user can navigate back
            transaction.commit()
        }

        return view
    }
}
