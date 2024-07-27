package com.example.ruok_workers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentLogoutBinding

class LogoutFragment : Fragment() {

    private lateinit var binding: FragmentLogoutBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle fragment arguments if any
        }
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)

        //데이터베이스 연동: 로그인한 회원 이름 불러오기
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        val sql = "SELECT m_name FROM member WHERE m_num = " + arguments?.getInt("m_num").toString()
        cursor = sqlitedb.rawQuery(sql, null)
        cursor.moveToNext()
        val userName = cursor.getString(cursor.getColumnIndex("m_name"))

        // Set the text with the user ID
        binding.tvName.text = "$userName\n님 로그아웃\n하시겠습니까?"

        // btnYes 클릭시 LogoutFragment에서 MainActivity로 이동
        binding.btnYes.setOnClickListener {
            //자동 로그인 이력 지우기
            val auto = this.activity?.getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
            val autoLoginEdit = auto?.edit()
            autoLoginEdit?.putInt("saved_loginNum", -1)
            autoLoginEdit?.commit()

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish() // Finish current activity to prevent going back
        }

        // btnNo 클릭시 LogoutFragment에서 DashboardFragment로 이동
        binding.btnNo.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(DashboardFragment())
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogoutFragment().apply {
                arguments = Bundle().apply {
                    // Add any required arguments here
                }
            }
    }
}
