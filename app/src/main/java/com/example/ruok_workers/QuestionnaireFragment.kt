package com.example.ruok_workers

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import com.example.ruok_workers.databinding.FragmentQuestionnaireBinding
import kotlin.math.log

class QuestionnaireFragment : Fragment() {
    lateinit var binding: FragmentQuestionnaireBinding
    lateinit var cameraPermission: ActivityResultLauncher<String>

    var loginNum: Int = -1
    var health = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num", 0)!!

        //btnFinish클릭시 QuestionnaireFragment에서 PhotoAddFragment로 이동
        binding.btnFinish.setOnClickListener {
            //데이터 bundle에 저장하기
            val m_num = loginNum
            val unusual = binding.edtUnusual.text.toString()
            val measure = binding.edtMeasure.text.toString()
            val content = binding.edtContent.text.toString()

            val item = ConsultationItem(m_num, 0, "", health, unusual, measure, content, "", 0.0, 0.0, arrayOf(""))

            val DashboardActivity = activity as DashboardActivity
            val onRecording = arguments?.getInt("onRecording", 0)!!
            val bundle = Bundle()
            bundle.putInt("onRecording", onRecording)
            bundle.putInt("hasConsultation", 1)
            bundle.putParcelable("consultation_item", item)

            val photoAddFragment = PhotoAddFragment()
            photoAddFragment.arguments = bundle
            DashboardActivity.setFragment(photoAddFragment)
        }
        //건강상태 버튼 클릭시 색변경
        binding.btnQuestionGood.setOnClickListener {
            health = 1
            setFirstBtnColor()
            binding.btnQuestionGood.setBackgroundColor(Color.GREEN)
        }
        binding.btnQuestionNotbad.setOnClickListener {
            health = 2
            setFirstBtnColor()
            binding.btnQuestionNotbad.setBackgroundColor(Color.BLUE)
        }
        binding.btnQuestionBad.setOnClickListener {
            health = 3
            setFirstBtnColor()
            binding.btnQuestionBad.setBackgroundColor(Color.parseColor("#FAA900"))
        }
        binding.btnQuestionNeed.setOnClickListener {
            health = 4
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