package com.example.ruok_workers

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.ruok_workers.databinding.FragmentUnknownDialogBinding

class UnknownDialog(
    private val image: Int? = null,
    private val image2: Bitmap? = null,
    private val place: String,
    private val time: String
):
    DialogFragment() {
    // 뷰 바인딩 정의
    private var _binding: FragmentUnknownDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnknownDialogBinding.inflate(inflater, container, false)
        val view = binding.root

//        try {
//            image?.let { binding.ivDialog.setImageResource(it) }
//            Log.d("UnknownHomelessFragment","resid: $image")
//        } catch (e: Exception) {
//            image2?.let { binding.ivDialog.setImageBitmap(it) }
//        }

        if (image != null) {
            binding.ivDialog.setImageResource(image)
        } else if (image2 != null) {
            binding.ivDialog.setImageBitmap(image2)
        }

        binding.tvDialogPlace.text = "만난 장소 : "+place
        binding.tvDialogTime.text = "만난 날짜 : "+ time

        // x 버튼 클릭
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

