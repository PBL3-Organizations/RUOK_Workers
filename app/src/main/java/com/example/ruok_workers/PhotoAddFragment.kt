package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentPhotoAddBinding
import com.example.ruok_workers.databinding.FragmentQuestionnaireBinding


class PhotoAddFragment : Fragment() {
    lateinit var binding : FragmentPhotoAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoAddBinding.inflate(inflater, container, false)
        //btnPhotoAddBack클릭시 PhotoAddFragment에서 QuestionnaireFragment로 이동
        binding.btnPhotoAddBack.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(QuestionnaireFragment())
        }
        //btnPhotoAddNext클릭시 PhotoAddFragment에서 HomelessListFragment로 이동
        binding.btnPhotoAddNext.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(HomelessListFragment())
        }
        return this.binding.root
    }
}