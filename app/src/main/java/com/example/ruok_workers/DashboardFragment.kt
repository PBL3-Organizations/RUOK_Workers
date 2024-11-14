package com.example.ruok_workers

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import java.io.File
import java.util.Vector

class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    lateinit var adapter: DashboardAdapter

    private lateinit var recyclerViewProfile: RecyclerView

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var loginNum: Int = -1

    private var backPressedOnce = false // 뒤로 가기 버튼이 한 번 눌렸는지 여부

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (backPressedOnce) {
                requireActivity().finish() // 두 번 클릭 시 앱 종료
            } else {
                backPressedOnce = true
                Toast.makeText(context, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()

                // 2초 후에 backPressedOnce를 다시 false로 변경하여 초기화
                Handler(Looper.getMainLooper()).postDelayed({
                    backPressedOnce = false
                }, 2000)
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

            if (photoFilename.startsWith("/")) {
                // 내부 저장소 경로에서 이미지 불러오기
                val file = File(photoFilename)
                if (file.exists()) {
                    // Bitmap으로 변환하여 ImageView에 설정
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    list.add(Dashboard(hNum, null, bitmap, hName, hBirth))
                } else {
                    // 파일이 없을 경우 기본 이미지 설정
                    list.add(Dashboard(hNum, R.drawable.dflt, null, hName, hBirth))
                }
            } else {
                // drawable 이미지 불러오기
                val resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
                if (resId != 0) {
                    list.add(Dashboard(hNum, resId, null, hName, hBirth))
                } else {
                    // 이미지가 없는 경우 기본 이미지 표시
                    list.add(Dashboard(hNum, R.drawable.dflt, null, hName, hBirth))
                }
            }

//            var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
//            val item = Dashboard(hNum, resId, hName, hBirth)
//            list.add(item)
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        val layoutManager = LinearLayoutManager(context)
        binding!!.recyclerViewProfile.layoutManager = layoutManager

        adapter = DashboardAdapter(requireContext(), list)
        binding!!.recyclerViewProfile.adapter = adapter

        //btnBriefing 클릭시 DashboardFragment에서 SearchFragment로 이동
        binding.btnBriefing.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(SearchFragment())
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