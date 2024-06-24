package com.example.ruok_workers

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ruok_workers.databinding.FragmentPhotoAddBinding
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PhotoAddFragment : Fragment() {
    lateinit var binding : FragmentPhotoAddBinding
    //갤러리 launcher
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            it -> setGallery(uri = it)
    }

    var loginNum = -1
    lateinit var item: ConsultationItem
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoAddBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num", 0)!!

        val onRecording = arguments?.getInt("onRecording", 0)!!
        val bundle = Bundle()
        bundle.putInt("onRecording", onRecording)

        item = arguments?.getParcelable<ConsultationItem>("consultation_item")!!
        val hasConsultation = arguments?.getInt("hasConsultation")!!

        //btnPhotoAddBack클릭시 PhotoAddFragment에서 QuestionnaireFragment로 이동
        binding.btnPhotoAddBack.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            val questionnaireFragment = QuestionnaireFragment()
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putParcelable("consultation_item", item)
            questionnaireFragment.arguments = bundle
            DashboardActivity.setFragment(questionnaireFragment)
        }
        //btnPhotoAddNext클릭시 PhotoAddFragment에서 HomelessListFragment로 이동
        binding.btnPhotoAddNext.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            val homelessListFragment = HomelessListFragment()
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putParcelable("consultation_item", item)
            homelessListFragment.arguments = bundle
            DashboardActivity.setFragment(homelessListFragment)
        }
        //btnPhotoAddCamera클릭시 카메라열고 찍은 사진 ivPhotoAdd에 넣기
        binding.btnPhotoAddCamera.setOnClickListener{
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
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),1000)
            }
        }
        //btnPhotoAddGallery클릭시 갤러리열고 선택한 사진 ivPhotoAdd에 넣기
        binding.btnPhotoAddGallery.setOnClickListener{
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
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),2000)
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
    //찍은 사진 ivPhotoAdd에 넣기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1000){
            val imageBitmap = data?.extras?.get("data") as Bitmap

            //사진 내부 저장소에 저장하기
            val currentTime = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
            val filename = loginNum.toString() + "_" + sdf.format(Date(currentTime))
            saveImageToInternalStorage(imageBitmap, filename)
            item.filename = arrayOf(filename)

            binding.ivPhotoAdd.setImageBitmap(imageBitmap)
        }
    }
    //선택한 사진 ivPhotoAdd에 넣기
    fun setGallery(uri: Uri?) {
        binding.ivPhotoAdd.setImageURI(uri)
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri!!)
        val imageBitmap = BitmapFactory.decodeStream(inputStream) ?: throw IllegalArgumentException(
            "이미지를 로드할 수 없습니다."
        )

        //사진 내부 저장소에 저장하기
        val currentTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
        val filename = loginNum.toString() + "_" + sdf.format(Date(currentTime))
        saveImageToInternalStorage(imageBitmap, filename)
        item.filename = arrayOf(filename)
    }

    //사진 내부 저장소에 저장하기
    private fun saveImageToInternalStorage(bitmap: Bitmap, filename: String) {
        try {
            val fileOutputStream = requireContext().openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}