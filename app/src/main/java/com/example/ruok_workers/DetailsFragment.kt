package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.ActivityDashboardBinding
import com.example.ruok_workers.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        //btnGoRevision클릭시 DatailsFragment에서 RevisionFragment로 이동
        binding.btnGoRevision.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(RevisionFragment())
        }
        //btnGoList클릭시 DatailsFragment에서 ListFragment로 이동
        binding.btnGoList.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(ListFragment())
        }
        //btnDelete클릭시 AlertDialog 생성
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage("본 게시물을 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, which ->
                    val DashboardActivity = activity as DashboardActivity
                    DashboardActivity.setFragment(ListFragment())
                    Toast.makeText(requireContext(), "상담내역 삭제!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("취소", null)
                .show()

        }

        return this.binding.root
    }

}