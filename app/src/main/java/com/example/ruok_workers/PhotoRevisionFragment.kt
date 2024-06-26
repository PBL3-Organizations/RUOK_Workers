package com.example.ruok_workers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
import com.example.ruok_workers.databinding.FragmentPhotoRevisionBinding
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Vector

class PhotoRevisionFragment : Fragment() {
    lateinit var binding : FragmentPhotoRevisionBinding
    //갤러리 launcher
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            it -> setGallery(uri = it)
    }

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var item: ConsultationItem
    var c_num = -1
    var h_num = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoRevisionBinding.inflate(inflater, container, false)

        //전달된 가져오기
        item = arguments?.getParcelable<ConsultationItem>("consultation_item")!!
        val hasConsultation = arguments?.getInt("hasConsultation")!!
        c_num = arguments?.getInt("c_num", 0)!!
        h_num = arguments?.getInt("h_num", 0)!!

        //데이터베이스 연동 및 기존 데이터 적용
        val photoList = Vector<String>()
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        val sql = "SELECT p_filename FROM photo WHERE c_num = ?;"
        cursor = sqlitedb.rawQuery(sql, arrayOf(c_num.toString()))
        while (cursor.moveToNext()) {
            photoList.add(cursor.getString(cursor.getColumnIndexOrThrow("p_filename")))
        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()

        //기존 사진 ivPhotoRevision 넣기(마지막 사진 하나밖에 안 들어감)
        for (i in 0 until photoList.size) {
            val fileName: String = photoList.get(i)
            if (fileName.contains(".")) { //사진이 drawable에 저장된 경우
                var resId = resources.getIdentifier(photoList.get(i).substringBefore('.'), "drawable", requireContext().packageName)
                binding.ivPhotoRevision.setImageResource(resId)
            } else { //사진이 내부저장소에 저장된 경우
                val filePath = requireContext().filesDir.absolutePath + "/" + fileName

                val imgFile = File(filePath)
                if(imgFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    binding.ivPhotoRevision.setImageBitmap(bitmap)
                }
            }
        }

        //btnPhotoRevisionBack클릭시 PhotoRevisionFragment에서 RevisionFragment로 이동
        binding.btnPhotoRevisionBack.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            val revisionFragment = RevisionFragment()
            val bundle = Bundle()
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putInt("c_num", c_num)
            bundle.putInt("h_num", h_num)
            bundle.putParcelable("consultation_item", item)
            revisionFragment.arguments = bundle
            DashboardActivity.setFragment(revisionFragment)
        }
        //btnPhotoRevisionNext클릭시 PhotoRevisionFragment에서 HomelessListFragment로 이동
        binding.btnPhotoRevisionNext.setOnClickListener {
            val DashboardActivity = activity as DashboardActivity
            val homelessRevisionFragment = HomelessRevisionFragment()
            val bundle = Bundle()
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putInt("c_num", c_num)
            bundle.putInt("h_num", h_num)
            bundle.putParcelable("consultation_item", item)
            homelessRevisionFragment.arguments = bundle
            DashboardActivity.setFragment(homelessRevisionFragment)
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

            //사진 내부 저장소에 저장하기
            val currentTime = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
            val filename = c_num.toString() + "_" + sdf.format(Date(currentTime))
            saveImageToInternalStorage(imageBitmap, filename)
            item.filename = arrayOf(filename)

            binding.ivPhotoRevision.setImageBitmap(imageBitmap)
        }
    }
    //선택한 사진 ivPhotoRevision에 넣기
    fun setGallery(uri: Uri?){
        binding.ivPhotoRevision.setImageURI(uri)
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri!!)
        val imageBitmap = BitmapFactory.decodeStream(inputStream) ?: throw IllegalArgumentException(
            "이미지를 로드할 수 없습니다."
        )

        //사진 내부 저장소에 저장하기
        val currentTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
        val filename = c_num.toString() + "_" + sdf.format(Date(currentTime))
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