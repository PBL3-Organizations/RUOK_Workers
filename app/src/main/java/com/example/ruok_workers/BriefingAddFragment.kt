package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BriefingAddFragment : Fragment() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var loginNum: Int = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_briefing_add, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        val submitButton = view.findViewById<Button>(R.id.button_submit_briefing)
        val setAsNoticeCheckbox = view.findViewById<CheckBox>(R.id.checkbox_set_as_notice)
        val titleEditText = view.findViewById<EditText>(R.id.editText_briefing_title)
        val contentEditText = view.findViewById<EditText>(R.id.editText_briefing_content)

        val tabPosition = requireArguments().getInt("tabPosition", 0)

        submitButton.setOnClickListener {
            val isSetAsNotice = setAsNoticeCheckbox.isChecked
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
            sqlitedb = dbManager.writableDatabase

            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val isNotice = if (isSetAsNotice) 1 else 0

            sqlitedb.execSQL("INSERT INTO briefing (b_title, b_content, m_num, b_time, b_type, b_notice) " +
                    "VALUES ('$title', '$content', '$loginNum', '$currentDate', $tabPosition, $isNotice);")

            sqlitedb.close()
            dbManager.close()

            Toast.makeText(requireContext(), "브리핑이 저장되었습니다.", Toast.LENGTH_SHORT).show()

            // 이전 화면으로 돌아가기 (선택 사항)
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
