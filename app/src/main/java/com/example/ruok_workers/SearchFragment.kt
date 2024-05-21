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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var centerTextView: TextView
    private lateinit var searchAdapter: SearchAdapter

    private val initialData = listOf("one", "two1", "two2", "three1", "three2", "three3", "four")
    private val searchResults = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

        searchAdapter = SearchAdapter(searchResults)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = searchAdapter

        searchButton.setOnClickListener {
            performSearch()
        }

        // Initialize with initial data
        searchResults.addAll(initialData)
        searchAdapter.notifyDataSetChanged()

        return view
    }

    private fun performSearch() {
        val query = searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            // Filter initialData based on the query
            val filteredResults = initialData.filter { it.contains(query, ignoreCase = true) }
            searchResults.clear()
            searchResults.addAll(filteredResults)
            searchAdapter.notifyDataSetChanged()

            centerTextView.text = "검색 결과: ${filteredResults.size}개"
            centerTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        } else {
            searchResults.clear()
            searchAdapter.notifyDataSetChanged()
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
