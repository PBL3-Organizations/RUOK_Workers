package com.example.ruok_workers

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import com.example.ruok_workers.databinding.FragmentLocationTrackingBinding


class LocationTrackingFragment : Fragment() {

    enum class State {
        START, STOP, OTHER
    }

    lateinit var binding: FragmentLocationTrackingBinding
    //private var fstate: State = State.START
    private var fstate: State? = null

    companion object {
        const val ARG_STATE = "state"

        @JvmStatic
        fun newInstance(state: State) =
            LocationTrackingFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_STATE, state)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fstate = it.getSerializable(ARG_STATE, State::class.java)
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLocationTrackingBinding.inflate(inflater, container, false)

        fstate?.let { updateButtonVisibility(it) }

        //활성화 버튼 교체
        binding.btnStartOutreach.setOnClickListener {
            updateButtonVisibility(State.STOP)
        }

        binding.btnStopOutreach.setOnClickListener {
            updateButtonVisibility(State.OTHER)
        }

        binding.btnContinueOutreach.setOnClickListener {
            updateButtonVisibility(State.STOP)
        }

        //btnRecordOutreach 클릭시 LocationTrackingFragment에서 QuestionnaireFragment로 이동
        binding.btnRecordOutreach.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(QuestionnaireFragment())
        }

        //btnEndOutreach 클릭시 LocationTrackingFragment에서 ListFragment로 이동
        binding.btnEndOutreach.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ListFragment())
        }

        return binding.root
    }

    fun updateButtonVisibility(state: State) {
        when (state) {
            State.START -> {
                binding.btnStartOutreach.visibility = View.VISIBLE
                binding.btnStopOutreach.visibility = View.GONE
                binding.btnContinueOutreach.visibility = View.GONE
                binding.btnRecordOutreach.visibility = View.GONE
                binding.btnEndOutreach.visibility = View.GONE
            }
            State.STOP -> {
                binding.btnStartOutreach.visibility = View.GONE
                binding.btnStopOutreach.visibility = View.VISIBLE
                binding.btnContinueOutreach.visibility = View.GONE
                binding.btnRecordOutreach.visibility = View.GONE
                binding.btnEndOutreach.visibility = View.GONE
            }
            State.OTHER -> {
                binding.btnStartOutreach.visibility = View.GONE
                binding.btnStopOutreach.visibility = View.GONE
                binding.btnContinueOutreach.visibility = View.VISIBLE
                binding.btnRecordOutreach.visibility = View.VISIBLE
                binding.btnEndOutreach.visibility = View.VISIBLE
            }
        }
    }

}