package com.example.ruok_workers

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
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

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var centerTextView: TextView
    private lateinit var faviconAdapter: FaviconAdapter

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    private val itemList = ArrayList<FaviconItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchEditText = view.findViewById(R.id.search_edit_text)
        searchButton = view.findViewById(R.id.search_button)
        recyclerView = view.findViewById(R.id.recycler_view)
        centerTextView = view.findViewById(R.id.center_text_view)

        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        faviconAdapter = FaviconAdapter(requireContext(), itemList)
        recyclerView.adapter = faviconAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        searchButton.setOnClickListener {
            performSearch()
        }

        val addNewProfileButton: Button = view.findViewById(R.id.add_new_profile)
        addNewProfileButton.setOnClickListener {
            val profileAddFragment = ProfileAddFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.rootLayout, profileAddFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        centerTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE

        return view
    }

    @SuppressLint("Range", "NotifyDataSetChanged")
    private fun performSearch() {
        val query = searchEditText.text.toString().trim()
        itemList.clear()

        if (query.isNotEmpty()) {
            val cursor = sqlitedb.rawQuery("SELECT * FROM homeless WHERE h_name LIKE ?", arrayOf("%$query%"))
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex("h_name"))
                    val birth = cursor.getString(cursor.getColumnIndex("h_birth"))
                    itemList.add(FaviconItem(name, birth))
                } while (cursor.moveToNext())
            }
            cursor?.close()
        } else {
            val cursor = sqlitedb.rawQuery("SELECT * FROM homeless", null)
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex("h_name"))
                    val birth = cursor.getString(cursor.getColumnIndex("h_birth"))
                    itemList.add(FaviconItem(name, birth))
                } while (cursor.moveToNext())
            }
            cursor?.close()
        }

        faviconAdapter.notifyDataSetChanged()
        centerTextView.text = if (itemList.isEmpty()) "검색 결과가 없습니다." else "검색 결과: ${itemList.size}개"
        centerTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE
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
