package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentQuestionnaireBinding
import com.example.ruok_workers.databinding.FragmentRevisionBinding

class QuestionnaireFragment : Fragment() {
    lateinit var binding: FragmentQuestionnaireBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireBinding.inflate(inflater, container, false)
        //btnFinish클릭시 QuestionnaireFragment에서 ListFragment로 이동
        binding.btnFinish.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(ListFragment())
        }
        return binding!!.root
    }
}