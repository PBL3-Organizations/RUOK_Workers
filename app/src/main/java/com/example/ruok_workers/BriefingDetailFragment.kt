package com.example.ruok_workers

import android.app.AlertDialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.ruok_workers.databinding.FragmentBriefingDetailBinding

class BriefingDetailFragment : Fragment() {
    lateinit var binding:FragmentBriefingDetailBinding
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    var b_num = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_briefing_detail, container, false)
        val buttonEdit = view.findViewById<Button>(R.id.button_edit)
        val buttonBack = view.findViewById<Button>(R.id.button_back)
        val buttonDelete = view.findViewById<Button>(R.id.button_delete)
        val tvDetailAuthor = view.findViewById<TextView>(R.id.tvDetailAuthor)
        val tvDetailTitle = view.findViewById<TextView>(R.id.tvDetailTitle)
        val tvDetailTimestamp = view.findViewById<TextView>(R.id.tvDetailTimestamp)
        val tvBriefingDetails = view.findViewById<TextView>(R.id.etBriefingDetails)


        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        b_num =arguments?.getInt("b_num",0)!!
        var cursor:Cursor
        val sql = "SELECT b.b_num, b.b_title, b.b_content, m.m_name, b.b_time FROM briefing b JOIN member m ON m.m_num = b.m_num WHERE b.b_num = ?;"
        cursor = sqlitedb.rawQuery(sql, arrayOf(b_num.toString()))
        cursor.moveToNext()
            tvDetailAuthor.text = cursor.getString(cursor.getColumnIndexOrThrow("m.m_name"))
            tvDetailTitle.text = cursor.getString(cursor.getColumnIndexOrThrow("b.b_title"))
            tvDetailTimestamp.text = cursor.getString(cursor.getColumnIndexOrThrow("b.b_time"))
            tvBriefingDetails.text = cursor.getString(cursor.getColumnIndexOrThrow("b.b_content"))
        cursor.close()
        sqlitedb.close()
        dbManager.close()
        var b_title:String = tvDetailTitle.text.toString()
        var b_content:String = tvBriefingDetails.text.toString()

        buttonEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("b_num", b_num)
            bundle.putString("b_title",b_title)
            bundle.putString("b_content",b_content)

            val BriefingRevisionFragment = BriefingRevisionFragment()
            BriefingRevisionFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, BriefingRevisionFragment).commit()
//            val DashboardActivity = activity as DashboardActivity
//            DashboardActivity.setFragment(BriefingRevisionFragment())
        }


        buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        buttonDelete.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("삭제하시겠습니까?")
            alertDialogBuilder.setPositiveButton("예") { dialog, which ->
                // 삭제 동작을 여기에 추가
                //데이터베이스 연동
                dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                dbManager.close()

                requireActivity().onBackPressed()
            }
            alertDialogBuilder.setNegativeButton("아니오") { dialog, which ->
                // 아무 동작 없음
            }
            alertDialogBuilder.show()
        }
        return view
    }
}
