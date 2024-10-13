package com.example.ruok_workers

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PrivacyAgreeActivity : AppCompatActivity() {

    private lateinit var pdfImageView: ImageView
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_privacy_agree)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pdfImageView = findViewById(R.id.pdfImageView2)

        // PDF 파일을 assets에서 읽고 화면에 표시하는 함수 호출
        try {
            openPdfFromAssets("개인정보처리동의서_v1.0_241012.pdf")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun openPdfFromAssets(fileName: String) {
        // assets에서 PDF 파일을 내부 저장소에 복사
        val file = File(cacheDir, fileName)
        if (!file.exists()) {
            assets.open(fileName).use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        // PdfRenderer를 통해 PDF 파일을 표시
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer = PdfRenderer(fileDescriptor)

        // 첫 번째 페이지를 이미지로 변환하여 ImageView에 설정
        currentPage = pdfRenderer?.openPage(0)
        currentPage?.let { page ->
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pdfImageView.setImageBitmap(bitmap)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPage?.close()
        pdfRenderer?.close()
    }
}