package com.example.ruok_workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)


        //btnList 클릭시 DashboardFragment에서 ListFragment로 이동
        binding.btnList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ListFragment())
        }

        //btnLocation 클릭시 DashboardFragment에서 LocationTrackingFragment로 이동
        binding.btnLocation.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(LocationTrackingFragment())
        }

        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

}