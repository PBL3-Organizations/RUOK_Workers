package com.example.ruok_workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.ruok_workers.R

class BriefingAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 해당 프래그먼트의 레이아웃을 인플레이트
        val view = inflater.inflate(R.layout.fragment_briefing_add, container, false)

        val submitButton = view.findViewById<Button>(R.id.button_submit_briefing)

        submitButton.setOnClickListener {
            // 저장 버튼 클릭 처리

            // 현재 프래그먼트 감추기
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
