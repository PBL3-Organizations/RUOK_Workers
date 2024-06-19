package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.example.ruok_workers.databinding.FragmentUnknownDialogBinding

class UnknownDialog (place:String, time:String):
    DialogFragment() {
    // 뷰 바인딩 정의
    private var _binding: FragmentUnknownDialogBinding? = null
    private val binding get() = _binding!!

    private var place: String? = null
    private var time: String? = null

    init {
        this.place = place
        this.time = time
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnknownDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        // 제목
        binding.tvDialogPlace.text = "만난 장소 : "+place

        // 확인 버튼 텍스트
        binding.tvDialogPlace.text = "만난 날짜 : "+ time

        // 취소 버튼 클릭
        binding.btnDialogBack.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}