package com.example.ruok_workers

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.max


class ServiceAgreeActivity : AppCompatActivity() {

    private lateinit var pdfImageView: ImageView
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //액션바 제목 변경
        supportActionBar?.setTitle("RUOK?")

        setContentView(R.layout.activity_service_agree)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        pdfImageView = findViewById(R.id.pdfImageView)
//
//        // PDF 파일을 assets에서 읽고 화면에 표시하는 함수 호출
//        try {
//            openPdfFromAssets("서비스 이용약관_v1.0_241013.pdf")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }

        // RecyclerView 설정
        val pdfRecyclerView = findViewById<RecyclerView>(R.id.pdfRecyclerView)
        pdfRecyclerView.layoutManager = LinearLayoutManager(this)

        // PDF 파일 로드
        try {
            openPdfFromAssets("서비스 이용약관_v1.0_241013.pdf")?.let { renderer ->
                pdfRenderer = renderer
                val adapter = ServiceAgreeAdapter(pdfRenderer!!)
                pdfRecyclerView.adapter = adapter
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun openPdfFromAssets(fileName: String): PdfRenderer? {
        // assets에서 PDF 파일을 내부 저장소에 복사
        val file = File(cacheDir, fileName)
        if (!file.exists()) {
            assets.open(fileName).use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

//        // PdfRenderer를 통해 PDF 파일을 표시
//        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
//        pdfRenderer = PdfRenderer(fileDescriptor)
//    }
//        // 첫 번째 페이지를 이미지로 변환하여 ImageView에 설정
//        currentPage = pdfRenderer?.openPage(0)
//        currentPage?.let { page ->
//            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
//            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//            pdfImageView.setImageBitmap(bitmap)
//        }

        // PdfRenderer를 반환
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        return PdfRenderer(fileDescriptor)

    }

    override fun onDestroy() {
        super.onDestroy()
//        currentPage?.close()
        pdfRenderer?.close()

    }
}