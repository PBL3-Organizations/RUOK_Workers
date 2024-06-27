package com.example.ruok_workers

import android.annotation.SuppressLint
import android.app.DownloadManager.Query
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FragmentUnknownHomelessBinding
import java.util.Vector

    class UnknownHomelessFragment : Fragment() {
        private lateinit var listRecyclerView: RecyclerView
        private var list = Vector<UnknownCard>()
        lateinit var binding: FragmentUnknownHomelessBinding
        lateinit var adapter: UnknownAdapter
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
            binding = FragmentUnknownHomelessBinding.inflate(inflater, container,false)

            //데이터베이스 연동
            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
            sqlitedb = dbManager.readableDatabase

            //이름을 알 수 없는 노숙인 목록 가져오기
            var unknownQuery= "SELECT c.c_time, l.l_addr, p.p_filename FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN photo p ON c.c_num = p.c_num WHERE c.h_num = '0' ORDER BY c.c_time DESC"
            var cursor: Cursor
            cursor = sqlitedb.rawQuery(unknownQuery, arrayOf())
            while (cursor.moveToNext()){
                var meet_photo: String = cursor.getString(cursor.getColumnIndexOrThrow("p_filename"))
                var meet_place: String = cursor.getString(cursor.getColumnIndexOrThrow("c_time"))
                var meeet_log: String = cursor.getString(cursor.getColumnIndexOrThrow("l_addr"))
                var resId : Int = resources.getIdentifier(meet_photo.substringBefore('.'), "drawable", requireContext().packageName)
                val item = UnknownCard(resId,meet_place, meeet_log)
                list.add(item)
            }
            cursor.close()
            sqlitedb.close()
            dbManager.close()

            var filter = 0
            //스피너 처리
            binding.spinnerUnknown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filter = position
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            //서치뷰로 필터링 구현
            binding.serchviewUnknown.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    when (filter) {
                        0 -> { //필터링 없음
                            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                            sqlitedb = dbManager.readableDatabase
                            var cursor: Cursor
                            unknownQuery = "SELECT c.c_time, l.l_addr, p.p_filename FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN photo p ON c.c_num = p.c_num WHERE c.h_num = '0' ORDER BY c.c_time DESC;"
                            cursor = sqlitedb.rawQuery(unknownQuery, arrayOf())
                            list = addToList(cursor)
                            cursor.close()
                            sqlitedb.close()
                            dbManager.close()
                            setUnknwnAdapter(list)
                        }
                        1 -> { //날짜 필터링
                            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                            sqlitedb = dbManager.readableDatabase
                            var cursor: Cursor
                            unknownQuery = "SELECT c.c_time, l.l_addr, p.p_filename FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN photo p ON c.c_num = p.c_num WHERE c.h_num = '0' AND c.c_time LIKE ?;"
                            cursor = sqlitedb.rawQuery(unknownQuery, arrayOf("%$query%"))
                            list = addToList(cursor)
                            cursor.close()
                            sqlitedb.close()
                            dbManager.close()
                            setUnknwnAdapter(list)
                        }
                        2 -> {//만난 장소 필터링
                            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                            sqlitedb = dbManager.readableDatabase
                            var cursor: Cursor
                            unknownQuery = "SELECT c.c_time, l.l_addr, p.p_filename FROM consultation c JOIN location l ON c.c_num = l.c_num JOIN photo p ON c.c_num = p.c_num WHERE c.h_num = 0 AND l.l_addr LIKE ?;"
                            cursor = sqlitedb.rawQuery(unknownQuery, arrayOf("%$query%"))
                            list = addToList(cursor)
                            cursor.close()
                            sqlitedb.close()
                            dbManager.close()
                            setUnknwnAdapter(list)
                        }

                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {return true}
            })

            var layoutManager = LinearLayoutManager(context)
            binding!!.UnknownRecyclerView.layoutManager = layoutManager

            adapter = UnknownAdapter(requireContext(),list)
            binding!!.UnknownRecyclerView.adapter = adapter

            binding.serchviewUnknown.onActionViewExpanded()

            return binding.root
        }
        private fun addToList(cursor: Cursor): Vector<UnknownCard> {
            val items = Vector<UnknownCard>()
            while(cursor.moveToNext()) {
                // 리스트에 데이터 추가
                var meet_photo: String = cursor.getString(cursor.getColumnIndexOrThrow("p_filename"))
                var meet_place: String = cursor.getString(cursor.getColumnIndexOrThrow("c_time"))
                var meeet_log: String = cursor.getString(cursor.getColumnIndexOrThrow("l_addr"))
                var resId : Int = resources.getIdentifier(meet_photo.substringBefore('.'), "drawable", requireContext().packageName)
                val item = UnknownCard(resId,meet_place, meeet_log)
                items.add(item)
            }
            return items
        }

        private fun setUnknwnAdapter(items: Vector<UnknownCard>) {
            adapter = UnknownAdapter(requireContext(),items)
            binding!!.UnknownRecyclerView.adapter = adapter

            binding.serchviewUnknown.onActionViewExpanded()
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