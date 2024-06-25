package com.example.ruok_workers

import android.app.AlertDialog
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
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding:FragmentBriefingDetailBinding
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_briefing_detail, container, false)
        val buttonEdit = view.findViewById<Button>(R.id.button_edit)
        val buttonBack = view.findViewById<Button>(R.id.button_back)
        val buttonDelete = view.findViewById<Button>(R.id.button_delete)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()
        Log.i("DB", "dbManager")

        var b_title:String =arguments?.getString("b_title").toString()
        var b_time:String =arguments?.getString("b_time").toString()
        lateinit var name:String
        lateinit var detail:String

        Log.i("DB", "$b_time,$b_title")

//        val cursor = sqlitedb.rawQuery("SELECT b.b_num, b.b_content, m.m_name FROM briefing b JOIN member m ON m.m_num = b.m_num WHERE b_title = ? AND b_time =? ", arrayOf(b_title,b_time))
//        Log.i("DB", "cursor")
//        while (cursor.moveToFirst()) {
//            Log.i("DB", "while")
//            name = cursor.getString(cursor.getColumnIndexOrThrow("m.m_name")).toString()
//            detail = cursor.getString(cursor.getColumnIndexOrThrow("b.b_content")).toString()
//
//        }
//        cursor.close()
//        //TextView에 데이터 표시
//        binding.textViewAuthor.text = name
//        binding.textViewTitle.text = b_title
//        binding.textViewTimestamp.text = b_time
//        binding.textViewBriefingDetails.text = detail


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

        return view
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
