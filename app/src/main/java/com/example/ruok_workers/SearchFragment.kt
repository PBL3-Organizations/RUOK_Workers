package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var centerTextView: TextView
    private lateinit var faviconAdapter: FaviconAdapter

    //테스트 데이터셋
    private val initialData = listOf(FaviconItem("김민수", "19650315"), FaviconItem("박지영", "19620722"), FaviconItem("최영준", "19591105"), FaviconItem("이서현", "19780510"), FaviconItem("Jenny", "19780510"), FaviconItem("Lisa", "19780510"), FaviconItem("Rose", "19780510"), FaviconItem("Jisoo", "19780510"))
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

        // Initialize with initial data
        itemList.addAll(initialData)
        faviconAdapter = FaviconAdapter(itemList)
        faviconAdapter.notifyDataSetChanged()

        recyclerView.adapter = faviconAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        searchButton.setOnClickListener {
            performSearch()
        }

        // add_new_profile 버튼을 찾기
        val addNewProfileButton: Button = view.findViewById(R.id.add_new_profile)

        // 버튼에 OnClickListener 설정
        addNewProfileButton.setOnClickListener {
            // ProfileAddFragment로 이동
            val profileAddFragment = ProfileAddFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.rootLayout, profileAddFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    private fun performSearch() {
        val query = searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            // Filter initialData based on the query
            val filteredResults = initialData.filter { it.name.contains(query, ignoreCase = true) }
            itemList.clear()
            itemList.addAll(filteredResults)
            faviconAdapter.notifyDataSetChanged()

            centerTextView.text = "검색 결과: ${filteredResults.size}개"
            centerTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        } else {
            itemList.clear()
            faviconAdapter.notifyDataSetChanged()
            centerTextView.text = "검색 결과가 없습니다."
            centerTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
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
