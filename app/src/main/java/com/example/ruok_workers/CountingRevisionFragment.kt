package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentCountingRevisionBinding
import java.util.Vector


class CountingRevisionFragment : Fragment() {
    lateinit var binding: FragmentCountingRevisionBinding

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
        binding = FragmentCountingRevisionBinding.inflate(inflater, container, false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        var list = Vector<CountingRevisionItem>()
        var place: String = ""
        var worker: String = ""
        var women: Int = 0
        var men: Int = 0

        var query: String = ""

        var cursor: Cursor
        cursor = sqlitedb.rawQuery(query, arrayOf())

        while (cursor.moveToNext()){

            var item = CountingRevisionItem(place, worker, women, men)
            list.add(item)
        }

        binding.btnCountingRevision.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(CountingDetailFragment())
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingRevisionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}