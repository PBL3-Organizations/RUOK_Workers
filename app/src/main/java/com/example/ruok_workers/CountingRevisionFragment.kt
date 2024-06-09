package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentCountingRevisionBinding


class CountingRevisionFragment : Fragment() {
    lateinit var binding: FragmentCountingRevisionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountingRevisionBinding.inflate(inflater, container, false)

        binding.btnCountingRevision.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(CountingDetailFragment())
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingRevisionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}