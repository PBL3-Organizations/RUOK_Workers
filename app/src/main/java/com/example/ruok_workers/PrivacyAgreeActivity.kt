package com.example.ruok_workers

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.noties.markwon.Markwon
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

class PrivacyAgreeActivity : AppCompatActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var markdownTextView: TextView
    private var fontSize = 16f // 기본 폰트 크기 설정

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //액션바 제목 변경
        supportActionBar?.setTitle("RUOK?")

        setContentView(R.layout.activity_privacy_agree)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // TextView 초기화
        markdownTextView = findViewById<TextView>(R.id.markdownTextView2)
//        val markdownTextView = findViewById<TextView>(R.id.markdownTextView2)
        markdownTextView.textSize = fontSize // 초기 폰트 크기 설정

        val markdown = """
            # 개인정보처리동의서 (복지사)

            RUOK(이하 '어플리케이션'이라고 합니다)은(는) 개인정보보호법 등 관련 법령상의 개인정보보호 규정을 준수하며 귀하의 개인정보 보호에 최선을 다하고 있습니다. 본 어플리케이션은 개인정보보호법에 근거하여 다음과 같은 내용으로 개인정보를 수집 및 처리하고자 합니다.

            다음의 내용을 자세히 읽어보시고 모든 내용을 이해하신 후에 동의 여부를 결정해주시기 바랍니다.

            ---

            ### 제1조(개인정보 수집 및 이용 목적)
            이용자가 제공한 모든 정보는 다음의 목적을 위해 활용하며, 목적 이외의 용도로는 사용되지 않습니다.
            - 회원가입, 로그인, 회원정보 수정, 로그아웃, 회원탈퇴 시 개인식별
            - 기타 어플리케이션 서비스 제공
            - 개인정보 수집 및 이용이 필요한 기타 어플리케이션 서비스(기능) : 상담 내역 기록, 카운팅 테이블, 노숙인 프로필 목록 열람·추가·수정·삭제, 노숙인 즐겨찾기, 검색 필터, 이름을 알 수 없는 노숙인 모음

            ### 제2조(개인정보 수집 및 이용 항목)
            어플리케이션은 개인정보 수집 및 이용 목적을 위하여 다음과 같은 정보를 수집합니다.
            - 필수 수집 및 처리 항목 : 성명, 개인 연락처(전화번호), 생년월일, 소속 기관, 아이디, 비밀번호, 위치정보
            - 선택 수집 항목 : 없음

            ### 제3조(개인정보 보유 및 이용 기간)
            1. 수집한 개인정보는 수집·이용 동의일로부터 사용자가 회원탈퇴를 하기 전 시점까지 보관 및 이용합니다.
            2. 사용자가 회원탈퇴하여 더 이상 서비스 운용에 필요한 개인정보가 불필요하게 되었을 때에는 지체없이 해당 개인정보를 파기합니다.
            3. 개인정보는 전자적 파일의 경우 기술적으로 복구할 수 없는 방식으로 삭제하고, 문서 형태의 개인정보는 분쇄하거나 소각하여 파기합니다.

            ### 제4조(동의 거부 관리)
            귀하는 본 안내에 따른 개인정보 수집·이용에 대하여 동의를 거부할 권리가 있습니다. 다만, 귀하가 개인정보 동의를 거부하시는 경우에 회원가입 불가로 인한 서비스 이용 제한의 불이익이 발생할 수 있음을 알려드립니다.
            - 이용이 제한되는 서비스 목록 : 회원가입, 로그인, 회원정보 수정, 로그아웃, 회원탈퇴, 상담 내역 기록, 카운팅 테이블, 노숙인 프로필 목록 열람·추가·수정·삭제, 노숙인 즐겨찾기, 검색 필터, 이름을 알 수 없는 노숙인 모음

            ### 제5조(개인정보보호 책임자 및 연락처)
            1. 개인정보보호 책임자: 개인정보보호 책임자명
            2. 개인정보보호 책임자 연락처: 전화번호 및 이메일 주소
            3. 개인정보와 관련된 문의 사항은 상단의 개인정보보호책임자에게 연락하여 처리할 수 있습니다.

            ---

            본인은 위의 동의서 내용을 충분히 숙지하였으며, 위와 같이 개인정보를 수집·이용하는데 동의합니다.
            
            
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
        // Activity에서도 터치 이벤트 전달
        scaleGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}