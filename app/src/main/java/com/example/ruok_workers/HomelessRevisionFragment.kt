package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomelessRevisionFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var searchEditText: EditText
    private lateinit var profileList: LinearLayout

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

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
        val view = inflater.inflate(R.layout.fragment_homeless_revision, container, false)
        searchEditText = view.findViewById(R.id.search_edit_text)
        profileList = view.findViewById(R.id.profile_list)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        val searchButton: Button = view.findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            performSearch()
        }

        val nextButton: Button = view.findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            goToLocationRevisionFragment()
        }

        return view
    }

    private fun performSearch() {
        val query = searchEditText.text.toString()
        profileList.removeAllViews()
        // 더미 데이터 사용
        val dummyProfiles = listOf("John Doe", "Jane Smith", "Emily Johnson")
        for (profile in dummyProfiles) {
            if (profile.contains(query, true)) {
                val profileView = TextView(requireContext()).apply {
                    text = profile
                    textSize = 18f
                    setPadding(16, 16, 16, 16)
                    setOnClickListener {
                        highlightProfile(this)
                    }
                }
                profileList.addView(profileView)
            }
        }
    }

    private fun highlightProfile(profileView: TextView) {
        // 모든 프로필 뷰의 배경을 기본값으로 설정
        for (i in 0 until profileList.childCount) {
            val child = profileList.getChildAt(i)
            if (child is TextView) {
                child.setBackgroundResource(R.drawable.border) // 기본 테두리
            }
        }
        // 클릭된 프로필 뷰의 배경을 강조된 테두리로 설정
        profileView.setBackgroundResource(R.drawable.highlight_border) // 강조된 테두리
    }

    private fun goToLocationRevisionFragment() {
        val fragment = LocationRevisionFragment()
        parentFragmentManager.commit {
            replace(R.id.rootLayout, fragment)
            addToBackStack(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomelessRevisionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
