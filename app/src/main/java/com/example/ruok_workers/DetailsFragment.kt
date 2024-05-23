package com.example.ruok_workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.ActivityDashboardBinding
import com.example.ruok_workers.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
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
        return binding!!.root
    }

}