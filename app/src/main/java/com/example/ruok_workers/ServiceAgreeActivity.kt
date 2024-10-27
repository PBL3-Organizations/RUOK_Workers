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
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.noties.markwon.Markwon
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.max
import kotlin.math.min


class ServiceAgreeActivity : AppCompatActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var markdownTextView: TextView
    private var fontSize = 16f // 기본 폰트 크기 설정

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

        // TextView 초기화
//        val markdownTextView = findViewById<TextView>(R.id.markdownTextView)
        markdownTextView = findViewById<TextView>(R.id.markdownTextView)

        // XML에서 초기 폰트 크기를 가져와 변수로 설정
//        fontSize = markdownTextView.textSize / resources.displayMetrics.scaledDensity
        markdownTextView.textSize = fontSize // 초기 폰트 크기 설정

        val markdown = """
            # R U OK? 서비스 이용약관

            ## 제 1조 (목적)
            본 약관은 'R U OK?' 서비스(이하 '서비스')를 제공함에 있어 서비스 이용 조건 및 절차, 이용자의 권리와 의무, 책임사항 등을 규정함을 목적으로 합니다.

            ## 제 2조 (정의)
            1. '서비스'란 'R U OK?'가 제공하는 노숙인 아웃리치 관리 애플리케이션을 말합니다.  
            2. '이용자'란 본 약관에 따라 서비스를 제공받는 사회복지사 및 기타 사용자들을 말합니다.  
            3. '아웃리치'란 노숙인을 대상으로 사회복지사가 현장 방문, 상담, 지원 등의 서비스를 제공하는 활동을 의미합니다.

            ## 제 3조 (이용약관의 효력 및 변경)
            1. 본 약관은 서비스를 이용하고자 하는 모든 이용자에게 적용됩니다.  
            2. 본 약관은 서비스 화면에 공지하거나 기타의 방법으로 이용자에게 통지함으로써 효력이 발생합니다.  
            3. 서비스 운영자는 필요에 따라 약관을 변경할 수 있으며, 변경된 약관은 위 2항의 방법으로 공지 또는 통지합니다. 이용자가 변경된 약관에 동의하지 않을 경우, 서비스 이용을 중단하고 탈퇴할 수 있습니다.

            ## 제 4조 (서비스의 제공 및 변경)
            1. 서비스는 상담 내역 기록, 노숙인 인원 카운팅, 노숙인 프로필 관리, 즐겨찾기 기능, 이름을 알 수 없는 노숙인 모음, 검색 필터 등 다양한 기능을 제공합니다.  
            2. 운영자는 서비스의 기술적 필요 및 정책적 이유로 서비스 내용 일부 또는 전부를 변경할 수 있으며, 이러한 변경 사항은 사전에 이용자에게 공지합니다.

            ## 제 5조 (서비스의 중단)
            1. 운영자는 다음의 경우 서비스 제공을 일시적으로 중단할 수 있습니다.  
               - (1) 시스템 점검, 유지보수 또는 서비스 개선을 위한 경우  
               - (2) 전기통신설비의 장애, 정전 등의 불가피한 사유가 발생한 경우  
               - (3) 기타 천재지변, 국가비상사태 등 불가항력적인 경우  

            2. 서비스 중단 시 운영자는 사전에 공지하며, 부득이한 경우 사후에 이를 공지할 수 있습니다.

            ## 제 6조 (이용자의 의무)
            1. 이용자는 본 약관 및 관련 법령을 준수하며, 서비스의 원활한 운영을 방해하는 행위를 해서는 안 됩니다.  
            2. 이용자는 타인의 개인정보를 무단으로 수집, 이용하거나 서비스 이용 과정에서 허위 정보를 제공해서는 안 됩니다.  
            3. 이용자는 서비스 이용 시 제공되는 모든 정보를 정확하고 성실하게 입력해야 하며, 서비스 내 노숙인 상담 기록 및 관련 정보는 보안과 프라이버시 보호를 위하여 신중하게 다뤄야 합니다.

            ## 제 7조 (개인정보의 보호)
            1. 운영자는 '개인정보 보호법' 등 관련 법령을 준수하며, 이용자의 개인정보를 보호하기 위해 최선을 다합니다.  
            2. 운영자는 이용자가 제공한 개인정보를 본 서비스 운영 이외의 목적으로 사용하지 않으며, 필요한 경우 관련 법령에 따라 별도의 동의를 얻습니다.  
            3. 이용자의 개인정보 보호에 관한 자세한 사항은 서비스 내 제공되는 '개인정보 처리방침'에 따릅니다.

            ## 제 8조 (책임 제한)
            1. 운영자는 천재지변, 국가 비상사태, 네트워크 장애 등 불가항력으로 인해 서비스를 제공할 수 없는 경우, 그에 대한 책임을 지지 않습니다.  
            2. 운영자는 이용자가 서비스 내에서 제공된 정보를 잘못 사용하거나, 부정확한 정보로 인해 발생한 손해에 대해서는 책임을 지지 않습니다.

            ## 제 9조 (이용자의 권리)
            1. 이용자는 서비스 내 제공된 기능을 통해 노숙인 상담 내역, 사진, 위치정보 등을 기록, 관리할 수 있으며, 검색 필터를 이용하여 데이터를 효율적으로 조회할 수 있습니다.  
            2. 이용자는 필요에 따라 본인의 계정을 탈퇴할 수 있으며, 탈퇴 시 서비스 내 모든 데이터는 복구할 수 없습니다.

            ## 제 10조 (분쟁 해결 및 관할 법원)
            1. 본 약관과 관련하여 운영자와 이용자 간에 발생한 분쟁은 상호 협의에 의해 해결하도록 노력합니다.  
            2. 협의가 원만하지 않을 경우, 대한민국 법률에 따라 민사소송을 제기할 수 있으며, 관할 법원은 운영자의 본사 소재지를 따릅니다.

            ---

            부칙  
            본 약관은 2024년 10월 11일부터 시행됩니다.

        """.trimIndent()

        // Initialize Markwon
        val markwon = Markwon.create(this)

        // Set markdown content to TextView
        markwon.setMarkdown(markdownTextView, markdown)

        // ScaleGestureDetector 초기화
        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                fontSize *= detector.scaleFactor
                fontSize = max(10f, min(fontSize, 50f)) // 폰트 크기의 최소/최대값 설정
                markdownTextView.textSize = fontSize
                return true
            }
        })

        // TextView에 터치 리스너 추가
        markdownTextView.setOnTouchListener { _, event ->
            if (event.pointerCount == 2) {
                // 두 손가락으로 폰트 크기 조절
                scaleGestureDetector.onTouchEvent(event)
                true
            } else {
                // 한 손가락일 경우 스크롤 동작 유지
                false
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // ScaleGestureDetector에 터치 이벤트 전달
        scaleGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}