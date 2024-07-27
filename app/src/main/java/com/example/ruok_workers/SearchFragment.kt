package com.example.ruok_workers

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var faviconAdapter: FaviconAdapter

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

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)

        var itemList = ArrayList<FaviconItem>()
        var name: String
        var birth: String
        var bookmark: Int
        var num: Int

        binding.searchButton.setOnClickListener {

            itemList.clear()
            val filter = binding.searchEditText.text.toString().trim()

            sqlitedb = dbManager.readableDatabase

            if (filter.isNotEmpty()) {
                val cursor: Cursor
                cursor = sqlitedb.rawQuery(
                    "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? WHERE h.h_name LIKE ? ORDER BY is_bookmarked DESC, h.h_name ASC",
                    arrayOf(loginNum.toString(), "%$filter%")
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndexOrThrow("h_name")).toString()
                        birth = cursor.getString(cursor.getColumnIndexOrThrow("h_birth")).toString()
                        num = cursor.getInt(cursor.getColumnIndex("h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndex("is_bookmarked")) == 1) 1 else 0

                        var photoFilename: String = cursor.getString(cursor.getColumnIndex("h_photo"))
                        var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)

                        itemList.add(FaviconItem(name, birth, num, bookmark, resId, loginNum))
                    } while (cursor.moveToNext())
                }
                cursor?.close()

            } else {
                val cursor: Cursor
                cursor = sqlitedb.rawQuery(
                    "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? ORDER BY is_bookmarked DESC, h.h_name ASC",
                    arrayOf(loginNum.toString())
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndexOrThrow("h_name")).toString()
                        birth = cursor.getString(cursor.getColumnIndexOrThrow("h_birth")).toString()
                        num = cursor.getInt(cursor.getColumnIndex("h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndex("is_bookmarked")) == 1) 1 else 0

                        var photoFilename: String = cursor.getString(cursor.getColumnIndex("h_photo"))
                        var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)

                        itemList.add(FaviconItem(name, birth, num, bookmark, resId, loginNum))
                    } while (cursor.moveToNext())
                }
                cursor?.close()
            }

            binding.centerTextView.text = if (itemList.isEmpty()) "검색 결과가 없습니다." else "검색 결과: ${itemList.size}개"
            binding.centerTextView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE

            binding.recyclerView.layoutManager = LinearLayoutManager(context)

            faviconAdapter = FaviconAdapter(requireContext(), itemList)

            binding.recyclerView.adapter = faviconAdapter

            sqlitedb.close()
        }


        dbManager.close()

        binding.addNewProfile.setOnClickListener {
            val profileAddFragment = ProfileAddFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.rootLayout, profileAddFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
