package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileAddFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        // 프래그먼트의 레이아웃을 인플레이트합니다.
        val view = inflater.inflate(R.layout.fragment_profile_add, container, false)

        // 데이터베이스와 연결합니다.
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.writableDatabase

        // 저장 버튼을 찾습니다.
        val saveButton = view.findViewById<Button>(R.id.save_button_in_add_profile)

        // 저장 버튼 클릭 이벤트를 설정합니다.
        saveButton.setOnClickListener {
            // 입력 필드에서 사용자 입력을 가져옵니다.
            val name = view.findViewById<EditText>(R.id.name_input).text.toString()
            val birth = view.findViewById<EditText>(R.id.birth_input_profile_add).text.toString()
            val phone = view.findViewById<EditText>(R.id.phone_input).text.toString()

            // SQL 쿼리를 생성하여 실행합니다.
            val query = "INSERT INTO homeless (h_name, h_birth, h_phone, h_photo) " +
                    "VALUES ('$name', '$birth', '$phone', 'default.jpeg')"
            sqlitedb.execSQL(query)

            // 저장 후에는 검색 화면(SearchFragment)으로 이동합니다.
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.rootLayout, SearchFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileAddFragment.
         */
        // TODO: Rename and change types and number of parameters
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