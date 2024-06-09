package com.example.ruok_workers

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ruok_workers.databinding.FragmentPhotoAddBinding
import com.example.ruok_workers.databinding.FragmentQuestionnaireBinding
import java.net.URI


class PhotoAddFragment : Fragment() {
    lateinit var binding : FragmentPhotoAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoAddBinding.inflate(inflater, container, false)
        //btnPhotoAddBack클릭시 PhotoAddFragment에서 QuestionnaireFragment로 이동
        binding.btnPhotoAddBack.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(QuestionnaireFragment())
        }
        //btnPhotoAddNext클릭시 PhotoAddFragment에서 HomelessListFragment로 이동
        binding.btnPhotoAddNext.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(HomelessListFragment())
        }
        return this.binding.root
    }
}