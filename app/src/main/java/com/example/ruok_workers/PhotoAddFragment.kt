package com.example.ruok_workers

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ruok_workers.databinding.FragmentPhotoAddBinding


class PhotoAddFragment : Fragment() {
    lateinit var binding : FragmentPhotoAddBinding
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        it -> setGallery(uri = it)
    }
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
        binding.btnPhotoAddCamera.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 1000)
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),1000)
            }
        }
        binding.btnPhotoAddGallery.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext,
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch("image/*")
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),2000)
            }
        }
        return this.binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1000)
        }
        if (requestCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            launcher.launch("image/*")
        }
        else{
            Toast.makeText(activity, "fail", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1000){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.ivPhotoAdd.setImageBitmap(imageBitmap)
        }
    }
    fun setGallery(uri: Uri?){
        binding.ivPhotoAdd.setImageURI(uri)
    }

}