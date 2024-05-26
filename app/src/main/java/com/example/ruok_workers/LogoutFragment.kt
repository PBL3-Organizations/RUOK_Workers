package com.example.ruok_workers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import com.example.ruok_workers.databinding.FragmentLogoutBinding

class LogoutFragment : Fragment() {
    lateinit var binding: FragmentLogoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)

        //btnYes 클릭시 LogoutFragment에서 MainActivity로 이동
        binding.btnYes.setOnClickListener {
            var intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        //btnNo 클릭시 LogoutFragment에서 DashboardFragment로 이동
        binding.btnNo.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(DashboardFragment())
        }

        return binding.root

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogoutFragment.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogoutFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}