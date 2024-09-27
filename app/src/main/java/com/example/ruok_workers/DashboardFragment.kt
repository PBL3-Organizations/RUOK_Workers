package com.example.ruok_workers

import BriefingBoardFragment
import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import java.util.Vector

class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    lateinit var adapter: DashboardAdapter

    private lateinit var recyclerViewProfile: RecyclerView

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var loginNum: Int = -1

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(DashboardFragment())

            // 현재 Fragment가 DashboardFragment인지 확인하고 백 스택 비우기
            if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                requireActivity().supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            } else {
                // 뒤로 가기 기본 동작 수행
                isEnabled = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        val sql = "SELECT h.h_num, h.h_name, h.h_birth, h.h_photo FROM homeless h JOIN bookmark b ON h.h_num = b.h_num WHERE b.m_num = ?;"
        cursor = sqlitedb.rawQuery(sql, arrayOf(loginNum.toString()))

        val list = Vector<Dashboard>()
        while(cursor.moveToNext()) {
            var hNum: Int = cursor.getInt(cursor.getColumnIndex("h_num"))
            var photoFilename: String = cursor.getString(cursor.getColumnIndex("h_photo"))
            var hName:String = cursor.getString(cursor.getColumnIndex("h_name"))
            var hBirth:String = cursor.getString(cursor.getColumnIndex("h_birth"))

            var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
            val item = Dashboard(hNum, resId, hName, hBirth)
            list.add(item)
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        val layoutManager = LinearLayoutManager(context)
        binding!!.recyclerViewProfile.layoutManager = layoutManager

        adapter = DashboardAdapter(requireContext(), list)
        binding!!.recyclerViewProfile.adapter = adapter

        //btnBriefing 클릭시 DashboardFragment에서 BriefingBoardFragment로 이동
        binding.btnBriefing.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(BriefingBoardFragment())
        }

        //btnCounsel 클릭시 DashboardFragment에서 HomelessListFragment로 이동
        binding.btnCounsel.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(HomelessListFragment())
        }

        //btnOutReach 클릭시 DashboardFragment에서 LocationShowFragment로 이동
        binding.btnOutreach.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(LocationShowFragment())
//            parentActivity.setFragment(LocationTrackingFragment.newInstance(LocationTrackingFragment.State.START))
        }

        //btnCounting 클릭시 DashboardFragment에서 CountingAddFragment로 이동
        binding.btnCounting.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(CountingAddFragment())
        }

        //tvProfile 클릭시 DashboardFragment에서 SearchFragment로 이동
        binding.tvProfile.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(SearchFragment())
        }

        return binding.root
    }



}