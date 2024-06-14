package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckPwFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckPwFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_check_pw, container, false)

        // pwModifyConfirm 버튼을 찾아 클릭 리스너를 설정합니다.
        val pwModifyConfirmButton = view.findViewById<Button>(R.id.pwModifyConfirm)
        pwModifyConfirmButton.setOnClickListener {

            //데이터베이스 연동
            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
            dbManager.close()

            // InfoRevisionFragment로 전환합니다.
            val infoRevisionFragment = InfoRevisionFragment.newInstance("param1", "param2")
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.rootLayout, infoRevisionFragment)
            transaction.addToBackStack(null) // 뒤로 가기 버튼을 눌렀을 때 이전 프래그먼트로 돌아갈 수 있도록 스택에 추가합니다.
            transaction.commit()
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
         * @return A new instance of fragment CheckPwFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckPwFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}