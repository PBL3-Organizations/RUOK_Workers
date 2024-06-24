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
    private lateinit var meet_photo: String
    private lateinit var meet_place: String
    private lateinit var meeet_log: String
    private val list = Vector<UnknownCard>()

    lateinit var binding: FragmentUnknownHomelessBinding
    lateinit var adapter: UnknownAdapter
    private val Tag = "UnknownHomelessFragment"

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
        Log.i("DB", "DB붙임")



        //이름을 알 수 없는 노숙인 목록 가져오기
//        val unknownQuery = "SELECT c.c_time, l.l_addr, p.p_filename\n" +
//                "FROM consultation c\n" +
//                "JOIN location l ON c.c_num = l.c_num\n" +
//                "JOIN photo p ON c.c_num = p.c_num\n"
//
//        var cursor: Cursor
//        Log.i("DB", "Cursor 정의")
//        cursor = sqlitedb.rawQuery(unknownQuery, arrayOf())
//        while (cursor.moveToNext()) {
//            Log.i("DB", "while")
//            meet_photo = cursor.getString(cursor.getColumnIndexOrThrow("p.p_filename")).toString()
//            meet_place = cursor.getString(cursor.getColumnIndexOrThrow("c.c_time")).toString()
//            meeet_log = cursor.getString(cursor.getColumnIndexOrThrow("l.l_addr")).toString()
//
//            val item = UnknownCard(meet_place, meeet_log)
//            list.add(item)
//        }
        var unknownQuery: String = ""
        unknownQuery += "SELECT c.c_time, l.l_addr, p.p_filename "
        unknownQuery += "FROM consultation c JOIN location l ON c.c_num = l.c_num "
        unknownQuery += "JOIN photo p ON c.c_num = p.c_num WHERE c.h_num = '0' "
        var cursor: Cursor
        cursor = sqlitedb.rawQuery(unknownQuery, arrayOf())
        while (cursor.moveToNext()){
            meet_photo = cursor.getString(cursor.getColumnIndexOrThrow("p.p_filename")).toString()
            meet_place = cursor.getString(cursor.getColumnIndexOrThrow("c.c_time")).toString()
            meeet_log = cursor.getString(cursor.getColumnIndexOrThrow("l.l_addr")).toString()
            var resId = resources.getIdentifier(meet_photo.substringBefore('.'), "drawable", requireContext().packageName)
            val item = UnknownCard(resId,meet_place, meeet_log)
            list.add(item)
        }

        cursor.close()

        binding.spinnerUnknown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.serchviewUnknown.isIconified = true
            }
        }

        binding.serchviewUnknown.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                when (binding.spinnerUnknown.selectedItemPosition){
//                    0->{
//                        var TimeQuery = unknownQuery + "AND c.c_time LIKE ?"
//                        val cursor = sqlitedb.rawQuery(TimeQuery, arrayOf("$query(YYYY-mm-dd)%"))
//                        if (cursor != null) {
//                            do {
//                                meet_photo = cursor.getString(cursor.getColumnIndexOrThrow("p.p_filename")).toString()
//                                meet_place = cursor.getString(cursor.getColumnIndexOrThrow("c.c_time")).toString()
//                                meeet_log = cursor.getString(cursor.getColumnIndexOrThrow("l.l_addr")).toString()
//                                var resId = resources.getIdentifier(meet_photo.substringBefore('.'), "drawable", requireContext().packageName)
//                                val item = UnknownCard(resId,meet_place, meeet_log)
//                                list.add(item)
//                            } while (cursor.moveToNext())
//                        }
//                        else{
//                            Toast.makeText(activity,"검색결과가 없습니다.",Toast.LENGTH_SHORT).show()
//                        }
//                        cursor?.close()
//                    }
//                    1->{
//
//                        var PlaceQuery = unknownQuery + "AND l.l_addr LIKE ?"
//                        val cursor = sqlitedb.rawQuery(PlaceQuery, arrayOf("%$query%"))
//                        if (cursor != null) {
//                            do {
//                                meet_photo = cursor.getString(cursor.getColumnIndexOrThrow("p.p_filename")).toString()
//                                meet_place = cursor.getString(cursor.getColumnIndexOrThrow("c.c_time")).toString()
//                                meeet_log = cursor.getString(cursor.getColumnIndexOrThrow("l.l_addr")).toString()
//                                var resId = resources.getIdentifier(meet_photo.substringBefore('.'), "drawable", requireContext().packageName)
//                                val item = UnknownCard(resId,meet_place, meeet_log)
//                                list.add(item)
//                            } while (cursor.moveToNext())
//                        }
//                        else{
//                            Toast.makeText(activity,"검색결과가 없습니다.",Toast.LENGTH_SHORT).show()
//                        }
//                        cursor?.close()
//                    }
                    0->{
                        Toast.makeText(activity,"0:검색결과가 없습니다.",Toast.LENGTH_SHORT).show()
                    }
                    1->{
                        Toast.makeText(activity,"1:검색결과가 없습니다.",Toast.LENGTH_SHORT).show()
                    }

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        cursor.close()

        val layoutManager = LinearLayoutManager(context)
        binding!!.UnknownRecyclerView.layoutManager = layoutManager

        adapter = UnknownAdapter(requireContext(),list)
        binding!!.UnknownRecyclerView.adapter = adapter


        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
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