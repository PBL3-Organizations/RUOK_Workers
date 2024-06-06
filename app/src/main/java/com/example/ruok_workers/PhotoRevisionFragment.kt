package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentPhotoAddBinding
import com.example.ruok_workers.databinding.FragmentPhotoRevisionBinding

class PhotoRevisionFragment : Fragment() {
    lateinit var binding : FragmentPhotoRevisionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoRevisionBinding.inflate(inflater, container, false)
        //btnPhotoRevisionBack클릭시 PhotoRevisionFragment에서 RevisionFragment로 이동
        binding.btnPhotoRevisionBack.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(RevisionFragment())
        }
        //btnPhotoRevisionNext클릭시 PhotoRevisionFragment에서 HomelessListFragment로 이동
        binding.btnPhotoRevisionNext.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(HomelessListFragment())
        }
        return this.binding.root
    }
}