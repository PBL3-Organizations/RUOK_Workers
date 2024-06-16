package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentDetailsBinding
import com.example.ruok_workers.databinding.FragmentRevisionBinding

class RevisionFragment : Fragment() {
    lateinit var binding: FragmentRevisionBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRevisionBinding.inflate(inflater, container, false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        //btnRevision클릭시 RevisionFragment에서 PhotoRevisionFragment로 이동
        binding.btnRevisionNext.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(PhotoRevisionFragment())
        }
        //건강상태 버튼 클릭시 색변경
        binding.btnRevisionGood.setOnClickListener {
            setFirstBtnColor()
            binding.btnRevisionGood.setBackgroundColor(Color.GREEN)
        }
        binding.btnRevisionNotbad.setOnClickListener {
            setFirstBtnColor()
            binding.btnRevisionNotbad.setBackgroundColor(Color.BLUE)
        }
        binding.btnRevisionBad.setOnClickListener {
            setFirstBtnColor()
            binding.btnRevisionBad.setBackgroundColor(Color.parseColor("#FAA900"))
        }
        binding.btnRevisionNeed.setOnClickListener {
            setFirstBtnColor()
            binding.btnRevisionNeed.setBackgroundColor(Color.RED)
        }
        return this.binding.root
    }
    private fun setFirstBtnColor(){
        binding.btnRevisionGood.setBackgroundColor(Color.parseColor("#8F9090"))
        binding.btnRevisionNotbad.setBackgroundColor(Color.parseColor("#8F9090"))
        binding.btnRevisionBad.setBackgroundColor(Color.parseColor("#8F9090"))
        binding.btnRevisionNeed.setBackgroundColor(Color.parseColor("#8F9090"))
    }
}