package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import com.example.ruok_workers.databinding.FragmentHomelessListBinding


class HomelessListFragment : Fragment() {
    lateinit var binding: FragmentHomelessListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomelessListBinding.inflate(inflater, container, false)

        //btnBeforeHomelessList 클릭시 HomelessListFragment에서 PhotoAddFragment로 이동
        binding.btnBeforeHomelessList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(PhotoAddFragment())
        }

        //btnNextHomelessList 클릭시 HomelessListFragment에서 LocationAddFragment로 이동
        binding.btnNextHomelessList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(LocationAddFragment())
        }

        //btnNoName 클릭시 HomelessListFragment에서 ProfileAddFragment로 이동
        binding.btnNoName.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ProfileAddFragment())
        }

        //btnNewHomeless 클릭시 HomelessListFragment에서 ProfileAddFragment로 이동
        binding.btnNewHomeless.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ProfileAddFragment())
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomelessListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}