package com.example.ruok_workers

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import com.example.ruok_workers.databinding.FragmentQuestionnaireBinding

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
            val onRecording = arguments?.getInt("onRecording", 0)!!
            val bundle = Bundle()
            bundle.putInt("onRecording", onRecording)
            val photoAddFragment = PhotoAddFragment()
            photoAddFragment.arguments = bundle
            DashboardActivity.setFragment(photoAddFragment)
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