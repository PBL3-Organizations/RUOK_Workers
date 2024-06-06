package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ruok_workers.databinding.FragmentLocationAddBinding
import com.example.ruok_workers.databinding.FragmentLocationRevisionBinding


class LocationRevisionFragment : Fragment() {
    lateinit var binding: FragmentLocationRevisionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationRevisionBinding.inflate(inflater, container, false)

        //btnCompleteLocationRevision 클릭시 LocationRevisionFragment에서 DetailFragment로 이동
        binding.btnCompleteLocationRevision.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(DetailsFragment())
            Toast.makeText(context, "상담내역 수정!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LocationRevisionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}