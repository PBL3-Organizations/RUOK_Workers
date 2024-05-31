package com.example.ruok_workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FaviconFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FaviconFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var faviconAdapter: FaviconAdapter

    //테스트 데이터셋
    private val initialData = listOf(FaviconItem("김민수", "19650315"), FaviconItem("박지영", "19620722"), FaviconItem("최영준", "19591105"), FaviconItem("이서현", "19780510"))
    private val itemList = ArrayList<FaviconItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favicon, container, false)

        recyclerView = view.findViewById(R.id.rv_favicon)

        // Initialize with initial data
        itemList.addAll(initialData)
        faviconAdapter = FaviconAdapter(itemList)
        faviconAdapter.notifyDataSetChanged()

        recyclerView.adapter = faviconAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FaviconFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FaviconFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}