package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentDetailsBinding
import com.example.ruok_workers.databinding.FragmentRevisionBinding

class RevisionFragment : Fragment() {
    lateinit var binding: FragmentRevisionBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRevisionBinding.inflate(inflater, container, false)
        //btnRevision클릭시 RevisionFragment에서 ListFragment로 이동
        binding.btnRevision.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(ListFragment())
        }
        return this.binding.root
    }

}