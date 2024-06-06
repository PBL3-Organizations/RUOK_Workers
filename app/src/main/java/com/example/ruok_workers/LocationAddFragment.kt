package com.example.ruok_workers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import com.example.ruok_workers.databinding.FragmentLocationAddBinding


class LocationAddFragment : Fragment() {
    lateinit var binding: FragmentLocationAddBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationAddBinding.inflate(inflater, container, false)

        //btnCompleteLocationAdd 클릭시 LocationAddFragment에서 LocationTrackingFragment로 이동
        binding.btnCompleteLocationAdd.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(LocationTrackingFragment.newInstance(LocationTrackingFragment.State.OTHER))
            Toast.makeText(context, "상담내역 저장!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LocationAddFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}