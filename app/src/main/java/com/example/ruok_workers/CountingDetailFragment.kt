package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ruok_workers.databinding.FragmentCountingAddBinding
import com.example.ruok_workers.databinding.FragmentCountingDetailBinding


class CountingDetailFragment : Fragment() {
    lateinit var binding: FragmentCountingDetailBinding

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
        binding = FragmentCountingDetailBinding.inflate(inflater, container, false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        binding.btnRevisionCountingDetail.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(CountingRevisionFragment())
        }

        binding.btnListCountingDetail.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(CountingListFragment())
        }

        binding.btnDeleteCountingDetail.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage("본 카운팅 테이블을 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, which ->
                    //데이터베이스 연동
                    dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                    dbManager.close()

                    val DashboardActivity = activity as DashboardActivity
                    DashboardActivity.setFragment(CountingListFragment())
                    Toast.makeText(requireContext(), "카운팅 삭제!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("취소", null)
                .show()
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}