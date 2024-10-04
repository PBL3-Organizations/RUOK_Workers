package com.example.ruok_workers

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentQuestionnaireBinding

class QuestionnaireFragment : Fragment() {
    lateinit var binding: FragmentQuestionnaireBinding
    lateinit var cameraPermission: ActivityResultLauncher<String>

    var loginNum: Int = -1
    lateinit var item: ConsultationItem
    var health = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num", 0)!!

        val onRecording = arguments?.getInt("onRecording", 0)!!
        val bundle = Bundle()
        bundle.putInt("onRecording", onRecording)

        item = arguments?.getParcelable<ConsultationItem>("consultation_item")!!
        val hasConsultation = arguments?.getInt("hasConsultation")!!

        // 엔터 누를 시 키보드 숨기기 처리
        binding.edtUnusual.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.edtMeasure.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        //btnBeforeQuestionnaire 클릭시 QuestionnaireFragment에서 HomelessListFragment로 이동
        binding.btnBeforeQuestionnaire.setOnClickListener{
            item.unusual = binding.edtUnusual.text.toString()
            item.measure = binding.edtMeasure.text.toString()
            item.content = binding.edtContent.text.toString()
            item.health = health

            val parentActivity = activity as DashboardActivity
            val HomelessListFragment = HomelessListFragment()
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putParcelable("consultation_item", item)
            HomelessListFragment.arguments = bundle
            parentActivity.setFragment(HomelessListFragment)
        }
        //btnNextQuestionnaire클릭시 QuestionnaireFragment에서 PhotoAddFragment로 이동
        binding.btnNextQuestionnaire.setOnClickListener {
            item.unusual = binding.edtUnusual.text.toString()
            item.measure = binding.edtMeasure.text.toString()
            item.content = binding.edtContent.text.toString()
            item.health = health

            val parentActivity = activity as DashboardActivity
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putParcelable("consultation_item", item)
            val photoAddFragment = PhotoAddFragment()
            photoAddFragment.arguments = bundle
            parentActivity.setFragment(photoAddFragment)
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

    // 키보드를 숨기는 함수
    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}