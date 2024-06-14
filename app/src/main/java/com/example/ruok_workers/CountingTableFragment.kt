package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ruok_workers.databinding.FragmentCountingAddBinding
import com.example.ruok_workers.databinding.FragmentCountingTableBinding


class CountingTableFragment : Fragment() {
    lateinit var binding: FragmentCountingTableBinding

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
        binding = FragmentCountingTableBinding.inflate(inflater, container, false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        binding.btnSaveCountingTable.setOnClickListener {
            val parentActivity = activity as DashboardActivity

            // AlertDialog 빌더 생성
            val builder = AlertDialog.Builder(parentActivity)
            builder.setTitle("몇 차 카운팅 업무인가요?")

            // 숫자를 입력받기 위한 EditText 생성
            val input = EditText(parentActivity)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            input.hint = "0"
            builder.setView(input)

            // 확인 버튼 설정
            builder.setPositiveButton("확인") { dialog, which ->
                // 입력된 숫자 가져오기
                val inputValue = input.text.toString().toInt()

                // 프래그먼트로 숫자 값 전달 및 이동
                val bundle = Bundle().apply {
                    putInt("counting_value", inputValue)
                }
                val countingListFragment = CountingListFragment().apply {
                    arguments = bundle
                }
                parentActivity.setFragment(CountingListFragment())
                Toast.makeText(requireContext(), "카운팅 저장!", Toast.LENGTH_SHORT).show()
            }

            // 취소 버튼 설정
            builder.setNegativeButton("취소") { dialog, which ->
                dialog.cancel()
            }

            // AlertDialog 보여주기
            builder.show()
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingTableFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}