package com.example.ruok_workers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileAddFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_profile_add, container, false)

        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.writableDatabase

        val saveButton = view.findViewById<Button>(R.id.save_button_in_add_profile)

        saveButton.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.name_input).text.toString()
            val birth = view.findViewById<EditText>(R.id.birth_input_profile_add).text.toString()
            val phone = view.findViewById<EditText>(R.id.phone_input).text.toString()
            val specialNote = view.findViewById<EditText>(R.id.special_note_input).text.toString()

            val query = "INSERT INTO homeless (h_name, h_birth, h_phone, h_unusual, h_photo) " +
                    "VALUES ('$name', '$birth', '$phone', '$specialNote', 'default.jpeg')"
            sqlitedb.execSQL(query)

            // 키보드 숨기기
            hideKeyboard()

            // 검색 화면(SearchFragment)으로 이동
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(SearchFragment())
        }

        // EditText에서 포커스가 벗어날 때 키보드 숨기기
        val editTextList = listOf(
            view.findViewById<EditText>(R.id.name_input),
            view.findViewById<EditText>(R.id.birth_input_profile_add),
            view.findViewById<EditText>(R.id.phone_input),
            view.findViewById<EditText>(R.id.special_note_input)
        )

        for (editText in editTextList) {
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
        }

        return view
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileAddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
