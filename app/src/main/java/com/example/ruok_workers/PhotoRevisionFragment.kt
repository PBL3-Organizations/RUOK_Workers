package com.example.ruok_workers

import android.Manifest
import android.app.Activity
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
import com.example.ruok_workers.databinding.FragmentPhotoRevisionBinding

class PhotoRevisionFragment : Fragment() {
    lateinit var binding : FragmentPhotoRevisionBinding
    //갤러리 launcher
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            it -> setGallery(uri = it)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoRevisionBinding.inflate(inflater, container, false)
        //btnPhotoRevisionBack클릭시 PhotoRevisionFragment에서 RevisionFragment로 이동
        binding.btnPhotoRevisionBack.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(RevisionFragment())
        }
        //btnPhotoRevisionNext클릭시 PhotoRevisionFragment에서 HomelessListFragment로 이동
        binding.btnPhotoRevisionNext.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            DashboardActivity.setFragment(HomelessListFragment())
        }
        //btnPhotoRevisionCamera클릭시 카메라열고 찍은 사진 ivPhotoRevision에 넣기
        binding.btnPhotoRevisionCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    //카메라 접근 권한 확인
                    requireActivity().applicationContext,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //카메라 열기
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 1000)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA), 1000
                )
            }
        }
        //btnPhotoRevisionGallery클릭시 갤러리열고 선택한 사진 ivPhotoRevision에 넣기
        binding.btnPhotoRevisionGallery.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    //갤러리 접근 권한 확인
                    requireActivity().applicationContext,
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //갤러리 열기
                launcher.launch("image/*")
            } else {
                //갤러리 접근 권한 요청
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 2000
                )
            }
        }
            return this.binding.root
    }
    //접근 권한 허용 요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //카메라 접근 권한 & 권한 요청 거부 안당했을 때 카메라 열기, 찍은 사진 imageView에 넣기
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1000)
        }
        //갤러리 접근 권한 & 권한 요청 거부 안당했을 때 카메라 열기, 찍은 사진 imageView에 넣기
        if (requestCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            launcher.launch("image/*")
        }
        //접근 요청 당했을 때
        else{
            Toast.makeText(activity, "fail", Toast.LENGTH_SHORT).show()
        }
    }
    //찍은 사진 ivPhotoRevision에 넣기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.ivPhotoRevision.setImageBitmap(imageBitmap)
        }
    }
    //선택한 사진 ivPhotoRevision에 넣기
    fun setGallery(uri: Uri?){
        binding.ivPhotoRevision.setImageURI(uri)
    }
}