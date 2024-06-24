package com.example.ruok_workers

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentHomelessListBinding


class HomelessListFragment : Fragment() {
    lateinit var binding: FragmentHomelessListBinding
    lateinit var adapter: HomelessListAdapter

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var loginNum: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomelessListBinding.inflate(inflater, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)

        var list = ArrayList<FaviconItem>()
        var name: String
        var birth: String
        var bookmark: Int
        var num: Int

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
                        num = cursor.getInt(cursor.getColumnIndex("h.h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndex("is_bookmarked")) == 1) 1 else 0

                        var photoFilename: String = cursor.getString(cursor.getColumnIndex("h_photo"))
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
                        num = cursor.getInt(cursor.getColumnIndex("h.h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndex("is_bookmarked")) == 1) 1 else 0

                        var photoFilename: String = cursor.getString(cursor.getColumnIndex("h.h_photo"))
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

        val onRecording = arguments?.getInt("onRecording", 0)!!
        val bundle = Bundle()
        bundle.putInt("onRecording", onRecording)

        //btnBeforeHomelessList 클릭시 HomelessListFragment에서 PhotoAddFragment로 이동
        binding.btnBeforeHomelessList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            val photoAddFragment = PhotoAddFragment()
            photoAddFragment.arguments = bundle
            parentActivity.setFragment(photoAddFragment)
        }

        //btnNextHomelessList 클릭시 HomelessListFragment에서 LocationAddFragment로 이동
        binding.btnNextHomelessList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            val locationAddFragment = LocationAddFragment()
            locationAddFragment.arguments = bundle
            parentActivity.setFragment(locationAddFragment)
        }

        //btnNoName 클릭시 HomelessListFragment에서 UnknownHomelessFragment로 이동
        binding.btnNoName.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(UnknownHomelessFragment())
        }

        //btnNewHomeless 클릭시 HomelessListFragment에서 ProfileAddFragment로 이동
        binding.btnNewHomeless.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ProfileAddFragment())
        }

        return binding!!.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomelessListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}