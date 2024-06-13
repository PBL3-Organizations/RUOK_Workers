package com.example.ruok_workers

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.PowerManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import com.example.ruok_workers.databinding.FragmentQuestionnaireBinding
import com.example.ruok_workers.databinding.FragmentRevisionBinding

class QuestionnaireFragment : Fragment() {
    lateinit var binding: FragmentQuestionnaireBinding
    lateinit var cameraPermission: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireBinding.inflate(inflater, container, false)
        //btnFinish클릭시 QuestionnaireFragment에서 PhotoAddFragment로 이동
        binding.btnFinish.setOnClickListener {
                val DashboardActivity = activity as DashboardActivity
                DashboardActivity.setFragment(PhotoAddFragment())
            }
        //건강상태 버튼 클릭시 색변경
        binding.btnQuestionGood.setOnClickListener {
            setFirstBtnColor()
            binding.btnQuestionGood.setBackgroundColor(Color.GREEN)
        }
        binding.btnQuestionNotbad.setOnClickListener {
            setFirstBtnColor()
            binding.btnQuestionNotbad.setBackgroundColor(Color.BLUE)
        }
        binding.btnQuestionBad.setOnClickListener {
            setFirstBtnColor()
            binding.btnQuestionBad.setBackgroundColor(Color.parseColor("#FAA900"))
        }
        binding.btnQuestionNeed.setOnClickListener {
            setFirstBtnColor()
            binding.btnQuestionNeed.setBackgroundColor(Color.RED)
        }
        return this.binding.root
    }
    private fun setFirstBtnColor(){
        binding.btnQuestionGood.setBackgroundColor(Color.parseColor("#8F9090"))
        binding.btnQuestionNotbad.setBackgroundColor(Color.parseColor("#8F9090"))
        binding.btnQuestionBad.setBackgroundColor(Color.parseColor("#8F9090"))
        binding.btnQuestionNeed.setBackgroundColor(Color.parseColor("#8F9090"))
    }
}