package com.example.ruok_workers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentLogoutBinding

class LogoutFragment : Fragment() {

    private lateinit var binding: FragmentLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle fragment arguments if any
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)

        // Retrieve the logged-in user's ID from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("registered_id", "User")

        // Set the text with the user ID
        binding.tvName.text = "$userId\n님 로그아웃\n하시겠습니까?"

        // btnYes 클릭시 LogoutFragment에서 MainActivity로 이동
        binding.btnYes.setOnClickListener {
            clearSavedCredentials()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish() // Finish current activity to prevent going back
        }

        // btnNo 클릭시 LogoutFragment에서 DashboardFragment로 이동
        binding.btnNo.setOnClickListener {
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(DashboardFragment())
        }

        return binding.root
    }

    private fun clearSavedCredentials() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("saved_id")
            remove("saved_password")
            putBoolean("remember_me", false)
            apply()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogoutFragment().apply {
                arguments = Bundle().apply {
                    // Add any required arguments here
                }
            }
    }
}
