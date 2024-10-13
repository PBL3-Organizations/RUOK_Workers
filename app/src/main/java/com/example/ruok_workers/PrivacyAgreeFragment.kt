package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentPrivacyAgreeBinding


class PrivacyAgreeFragment : Fragment() {
    lateinit var binding: FragmentPrivacyAgreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrivacyAgreeBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_privacy_agree, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PrivacyAgreeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}