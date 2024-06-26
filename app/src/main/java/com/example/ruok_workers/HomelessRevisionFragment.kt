package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentHomelessRevisionBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomelessRevisionFragment : Fragment() {
    lateinit var binding: FragmentHomelessRevisionBinding
    lateinit var adapter: HomelessListAdapter

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var item: ConsultationItem
    var c_num = -1
    var loginNum = -1

    var list = ArrayList<FaviconItem>()
    var name: String = ""
    var birth: String = ""
    var bookmark: Int = -1
    var num: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomelessRevisionBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num", 0)!!

        //전달받은 데이터 가져오기 및 보내기
        val bundle = Bundle()
        item = arguments?.getParcelable<ConsultationItem>("consultation_item")!!
        val hasConsultation = arguments?.getInt("hasConsultation")!!
        c_num = arguments?.getInt("c_num", 0)!!
        num = arguments?.getInt("h_num", 0)!!

        if (num == 0) {
            list.clear()
        } else {
            //기존 데이터 가져와서 적용
            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
            sqlitedb = dbManager.readableDatabase
            var cursor: Cursor
            val sql = "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? WHERE h.h_num = ?;"
            cursor = sqlitedb.rawQuery(sql, arrayOf(loginNum.toString(), item.h_num.toString()))
            cursor.moveToNext()
            name = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name")).toString()
            birth = cursor.getString(cursor.getColumnIndexOrThrow("h.h_birth")).toString()
            num = cursor.getInt(cursor.getColumnIndexOrThrow("h.h_num"))
            bookmark = if (cursor.getInt(cursor.getColumnIndexOrThrow("is_bookmarked")) == 1) 1 else 0

            var photoFilename: String = cursor.getString(cursor.getColumnIndexOrThrow("h_photo"))
            var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)

            cursor.close()
            sqlitedb.close()
            dbManager.close()

            list.clear()
            list.add(FaviconItem(name, birth, num, bookmark, resId))
        }

        binding.centerTextView2.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = HomelessListAdapter(requireContext(), list)
        binding.recyclerView.adapter = adapter

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)

        binding.searchButton2.setOnClickListener {

            list.clear()
            val filter = binding.searchEditText2.text.toString().trim()

            sqlitedb = dbManager.readableDatabase

            if (filter.isNotEmpty()) {
                val cursor: Cursor
                cursor = sqlitedb.rawQuery(
                    "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? WHERE h.h_name LIKE ? ORDER BY is_bookmarked DESC",
                    arrayOf(loginNum.toString(), "%$filter%")
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name")).toString()
                        birth = cursor.getString(cursor.getColumnIndexOrThrow("h.h_birth")).toString()
                        num = cursor.getInt(cursor.getColumnIndexOrThrow("h.h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndexOrThrow("is_bookmarked")) == 1) 1 else 0

                        var photoFilename: String = cursor.getString(cursor.getColumnIndexOrThrow("h_photo"))
                        var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)

                        list.add(FaviconItem(name, birth, num, bookmark, resId))
                    } while (cursor.moveToNext())
                }
                cursor?.close()

            } else {
                val cursor: Cursor
                cursor = sqlitedb.rawQuery(
                    "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? ORDER BY is_bookmarked DESC",
                    arrayOf(loginNum.toString())
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name")).toString()
                        birth = cursor.getString(cursor.getColumnIndexOrThrow("h.h_birth")).toString()
                        num = cursor.getInt(cursor.getColumnIndexOrThrow("h.h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndexOrThrow("is_bookmarked")) == 1) 1 else 0

                        var photoFilename: String = cursor.getString(cursor.getColumnIndexOrThrow("h.h_photo"))
                        var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)

                        list.add(FaviconItem(name, birth, num, bookmark, resId))
                    } while (cursor.moveToNext())
                }
                cursor?.close()
            }

            binding.centerTextView2.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE

            binding.recyclerView.layoutManager = LinearLayoutManager(context)

            adapter = HomelessListAdapter(requireContext(), list)

            binding.recyclerView.adapter = adapter

            sqlitedb.close()
        }

        dbManager.close()

        //btnBeforeHomelessList 클릭시 HomelessRevisionFragment에서 PhotoRevisionFragment로 이동
        binding.btnBeforeHomelessList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            val photoRevisionFragment = PhotoRevisionFragment()
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putInt("c_num", c_num)
            bundle.putInt("h_num", num)
            bundle.putParcelable("consultation_item", item)
            photoRevisionFragment.arguments = bundle
            parentActivity.setFragment(photoRevisionFragment)
        }

        //btnNextHomelessList 클릭시 HomelessRevisionFragment에서 LocationRevisionFragment로 이동
        binding.btnNextHomelessList.setOnClickListener{
            item.h_num = num
            val parentActivity = activity as DashboardActivity
            val locationRevisionFragment = LocationRevisionFragment()
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putInt("c_num", c_num)
            bundle.putInt("h_num", num)
            bundle.putParcelable("consultation_item", item)
            locationRevisionFragment.arguments = bundle
            parentActivity.setFragment(locationRevisionFragment)
        }

        //btnNoName 클릭시 h_num을 0으로 설정
        binding.btnNoName.setOnClickListener{
            num = 0
        }

        //btnNewHomeless 클릭시 HomelessRevisionFragment에서 ProfileAddFragment로 이동
        binding.btnNewHomeless.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ProfileAddFragment())
        }

        return binding!!.root
    }

//    private fun performSearch() {
//        val query = searchEditText.text.toString()
//        profileList.removeAllViews()
//        // 더미 데이터 사용
//        val dummyProfiles = listOf("John Doe", "Jane Smith", "Emily Johnson")
//        for (profile in dummyProfiles) {
//            if (profile.contains(query, true)) {
//                val profileView = TextView(requireContext()).apply {
//                    text = profile
//                    textSize = 18f
//                    setPadding(16, 16, 16, 16)
//                    setOnClickListener {
//                        highlightProfile(this)
//                    }
//                }
//                profileList.addView(profileView)
//            }
//        }
//    }
//
//    private fun highlightProfile(profileView: TextView) {
//        // 모든 프로필 뷰의 배경을 기본값으로 설정
//        for (i in 0 until profileList.childCount) {
//            val child = profileList.getChildAt(i)
//            if (child is TextView) {
//                child.setBackgroundResource(R.drawable.border) // 기본 테두리
//            }
//        }
//        // 클릭된 프로필 뷰의 배경을 강조된 테두리로 설정
//        profileView.setBackgroundResource(R.drawable.highlight_border) // 강조된 테두리
//    }

    private fun goToLocationRevisionFragment() {
        val fragment = LocationRevisionFragment()
        parentFragmentManager.commit {
            replace(R.id.rootLayout, fragment)
            addToBackStack(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomelessRevisionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
