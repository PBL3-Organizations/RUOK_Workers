package com.example.ruok_workers

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentBriefingAfterBinding
import java.util.Vector

class BriefingAfterFragment : Fragment() {

    private lateinit var dbManager: DBManager
    private lateinit var sqlitedb: SQLiteDatabase
    private lateinit var listView: ListView
    private lateinit var binding: FragmentBriefingAfterBinding
    private lateinit var adapter: BriefingAfterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBriefingAfterBinding.inflate(inflater,container,false)
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        val briefingsAfter = Vector<BriefingAfterCard>()

        val cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM briefing WHERE b_type = 3 ORDER BY b_notice DESC, b_time DESC", null)
        while (cursor.moveToNext()) {
            var bNum: Int = cursor.getInt(cursor.getColumnIndexOrThrow("b_num"))
            val title:String = cursor.getString(cursor.getColumnIndexOrThrow("b_title"))
            val bTime:String = cursor.getString(cursor.getColumnIndexOrThrow("b_time"))
            val isNotice:Int = cursor.getInt(cursor.getColumnIndexOrThrow("b_notice"))

            val displayTitle = if (isNotice == 1) {
                "\uD83D\uDD34 $title"  // Red circle emoji
            } else {
                title
            }
            briefingsAfter.add(BriefingAfterCard(bNum,title,bTime))
        }

        cursor.close()

        val layoutManager = LinearLayoutManager(context)
        binding!!.listViewBriefingAfter.layoutManager = layoutManager

        adapter = BriefingAfterAdapter(requireContext(),briefingsAfter)
        binding!!.listViewBriefingAfter.adapter = adapter

        sqlitedb.close()
        dbManager.close()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("Range")
    private fun displayBriefings() {

    }
}
