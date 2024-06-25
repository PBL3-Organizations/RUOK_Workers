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
        lateinit var name:String
        lateinit var title:String
        lateinit var time:String
        lateinit var content:String

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        Log.i("DB", "dbManager")

        b_num =arguments?.getInt("b_num",0)!!
        Log.i("DB", "$b_num")
//        var cursor:Cursor
//        val sql = "SELECT b.b_num, b.b_title, b.b_content, m.m_name, b.b_time FROM briefing b JOIN member m ON m.m_num = b.m_num WHERE b.b_num = ?;"
//        cursor = sqlitedb.rawQuery(sql, arrayOf(b_num.toString()))
//        Log.i("DB", "cursor")
//        cursor.moveToNext()
//        Log.i("DB", "while")
//            binding.tvDetailAuthor.text = cursor.getString(cursor.getColumnIndexOrThrow("m.m_name"))
//            Log.i("DB", "name")
//            binding.tvDetailTitle.text = cursor.getString(cursor.getColumnIndexOrThrow("b.b_title"))
//            Log.i("DB", "title")
//            binding.tvDetailTimestamp.text = cursor.getString(cursor.getColumnIndexOrThrow("b.b_time"))
//            Log.i("DB", "time")
//            binding.tvBriefingDetails.text = cursor.getString(cursor.getColumnIndexOrThrow("b.b_content"))
//            Log.i("DB", "content")
//        cursor.close()
//        sqlitedb.close()
//        dbManager.close()


        buttonEdit.setOnClickListener {
            val fragment = BriefingRevisionFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootLayout, fragment)
                .addToBackStack(null)
                .commit()
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
        return binding!!.root
    }
}
