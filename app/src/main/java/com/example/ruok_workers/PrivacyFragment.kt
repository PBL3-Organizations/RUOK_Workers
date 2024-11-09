package com.example.ruok_workers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.noties.markwon.Markwon
import org.apache.logging.log4j.CloseableThreadContext.put
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.max
import kotlin.math.min

class PrivacyFragment : Fragment() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var markdownTextView: TextView
    private var fontSize = 16f // 기본 폰트 크기 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_privacy, container, false)
        val downloadButton: Button = view.findViewById(R.id.buttonPrivacyDownload)

        // PDF 다운로드 버튼 클릭 리스너 설정
        downloadButton.setOnClickListener {
            downloadPdf("RUOK_개인정보처리방침_v2.0_241105.pdf")
        }

        markdownTextView = view.findViewById(R.id.markdownTextView3)
        markdownTextView.textSize = fontSize

        // 전문 마크다운 코드
        val markdownContent = """
            # RUOK 개인정보처리방침

            ### 제1조(목적)
            RUOK(이하 ‘어플리케이션’)에서 제공하는 서비스(이하 ‘RUOK 서비스’)를 이용하는 개인(이하 ‘이용자’ 또는 ‘개인’)의 개인정보를 보호하기 위해, 개인정보보호법, 정보통신망 이용촉진 및 정보보호 등에 관한 법률(이하 '정보통신망법') 등 관련 법령을 준수하고, 개인정보 보호 관련 고충을 신속하고 원활하게 처리하기 위하여 본 방침을 수립합니다.

            ### 제2조(개인정보 처리의 원칙)
            어플리케이션은 법령 및 본 방침에 따라 이용자 및 서비스 이해관계자의 개인정보를 수집할 수 있으며, 수집된 개인정보는 원칙적으로 제3자에게 제공되지 않습니다. 단, 법령에 따라 강제되는 경우 사전 동의 없이 제3자에게 제공할 수 있습니다.

            ### 제3조(본 방침의 공개)
            1. 이용자가 쉽게 본 방침을 확인할 수 있도록 어플리케이션의 첫 화면 또는 메인 대시보드에 공개합니다.
            2. 글자 크기, 색상 등을 활용하여 이용자가 본 방침을 쉽게 확인할 수 있도록 합니다.

            ### 제4조(본 방침의 변경)
            1. 개인정보 관련 법령, 지침, 정부 또는 서비스 정책 변경에 따라 개정될 수 있습니다.
            2. 변경 사항은 첫 화면 공지, 전자우편 등으로 최소 7일, 중요한 변경 시 최소 30일 전에 공지합니다.

            ### 제5조(회원 가입을 위한 정보)
            회원 가입을 위해 다음과 같은 정보를 수집합니다:
            - 필수: 성명, 전화번호, 생년월일, 소속 기관, 아이디, 비밀번호, 위치정보
            - 선택: 없음

            ### 제6조(RUOK 서비스 제공을 위한 정보)
            RUOK 서비스를 제공하기 위해 다음 정보를 수집합니다:
            1. 권한: 카메라, 위치, 지도, 파일 읽기/쓰기/열람/다운로드
            2. 개인정보 수집 및 이용이 필요한 기능

            ### 제7조(개인정보 수집 방법)
            1. 이용자가 직접 입력하는 방식
            2. 개인정보 수집/처리 동의 후 타인의 개인정보를 입력하는 방식

            ### 제8조(개인정보의 이용)
            1. 공지 전달, 문의 회신, 서비스 제공 등 운영 목적
            2. 이용 제한, 부정 이용 방지 및 제재 목적
            3. 인구통계학적 분석, 방문 기록 분석 등

            ### 제9조(개인정보의 보유 및 이용기간)
            개인정보는 수집 목적 달성을 위한 기간 동안 보유 및 이용됩니다.

            ### 제10조(법령에 따른 보유 및 이용기간)
            1. 위치정보 보호법: 개인 위치정보 기록 6개월
            2. 통신비밀보호법: 서비스 이용 개인정보 3개월

            ### 제11조(개인정보의 파기원칙)
            원칙적으로 필요하지 않은 경우 지체 없이 파기합니다.

            ### 제12조(개인정보파기절차)
            1. 입력한 정보는 처리 목적 달성 후 내부 방침에 따라 파기됩니다.
            2. 보유 및 이용기간은 제9조 참고

            ### 제13조(개인정보파기방법)
            전자 파일은 복구 불가능한 방법으로 삭제, 종이 문서는 분쇄기로 파기합니다.

            ### 제14조(광고성 정보의 전송 조치)
            어플리케이션은 영리 목적의 광고성 정보를 전송하지 않습니다.

            ### 제15조(아동의 개인정보보호)
            만 14세 이상의 이용자에 한해 회원가입을 허용합니다.

            ### 제16조(개인정보 조회 및 수집동의 철회)
            1. 언제든지 개인정보 조회/수정을 요청할 수 있습니다.
            2. ‘회원탈퇴’ 기능을 통해 개인정보 삭제와 동의 철회를 진행할 수 있습니다.

            ### 제17조(개인정보 정보변경 등)
            개인정보 오류에 대한 정정을 요청할 수 있으며, 정정 완료 전까지 이용하지 않습니다.

            ### 제18조(이용자의 의무)
            1. 최신 상태의 개인정보 유지 책임은 이용자에게 있습니다.
            2. 타인의 개인정보 도용 시 법적 책임이 따릅니다.

            ### 제19조(개인정보 유출 등에 대한 조치)
            개인정보 유출 시 해당 사항을 이용자와 관계 기관에 지체 없이 알립니다.

            ### 제20조(개인정보 유출 등에 대한 조치의 예외)
            연락처를 알 수 없는 경우 메인 대시보드에 30일 이상 게시하여 대체할 수 있습니다.

            ### 제21조(개인정보 자동 수집 장치의 설치·운영 및 거부)
            쿠키를 사용하지 않습니다.

            ### 제22조(권익침해에 대한 구제방법)
            개인정보 침해에 대한 구제 방법으로 개인정보분쟁조정위원회, 한국인터넷진흥원 등에 신청할 수 있습니다.

            ### 제23조(개인정보보호 책임자 및 연락처)
            1. 개인정보보호 책임자: 개인정보보호 책임자명
            2. 연락처: 전화번호 및 이메일 주소

            ### 제24조(본 개인정보처리방침의 적용 범위)
            RUOK 어플리케이션 및 관련 서비스에 적용되며, 다른 앱에서는 별도의 방침이 적용될 수 있습니다.

            ### 제25조(개정 전 고지 의무)
            개정 사항은 최소 7일 전에 사전 공지하며, 중요한 변경 시 최소 30일 전에 공지합니다.

            ---

            부칙: 본 방침은 2024.10.11.부터 시행됩니다.
            

            """.trimIndent()

        val markwon = Markwon.create(requireContext())
        markwon.setMarkdown(markdownTextView, markdownContent)

        scaleGestureDetector = ScaleGestureDetector(requireContext(), object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                fontSize *= detector.scaleFactor
                fontSize = max(10f, min(fontSize, 50f))
                markdownTextView.textSize = fontSize
                return true
            }
        })

        markdownTextView.setOnTouchListener { _, event ->
            if (event.pointerCount == 2) {
                scaleGestureDetector.onTouchEvent(event)
                true
            } else {
                false
            }
        }

        return view
    }

    // PDF 다운로드 메서드
    private fun downloadPdf(fileName: String) {
        try {
            val inputStream: InputStream = requireContext().assets.open(fileName)

            // Android 10 이상 버전에서 MediaStore를 사용하여 파일 저장
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                getDownloadUri(requireContext(), fileName)
            } else {
                // Android 10 미만에서는 일반 파일 접근 사용
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                Uri.fromFile(file)
            }

            // URI가 유효한 경우 파일을 쓰기
            if (uri != null) {
                requireContext().contentResolver.openOutputStream(uri)?.use { outputStream ->
                    copyFile(inputStream, outputStream)
                    Toast.makeText(context, "개인정보 처리방침 다운로드 완료", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(context, "개인정보 처리방침 다운로드 실패", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "개인정보 처리방침 다운로드 실패", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "개인정보 처리방침 다운로드 실패", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getDownloadUri(context: Context, fileName: String): Uri? {
        val contentResolver = context.contentResolver
        val collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI

        // 이미 존재하는 파일이 있다면 삭제
        contentResolver.delete(collection, "${MediaStore.MediaColumns.DISPLAY_NAME}=?", arrayOf(fileName))

        // 새 파일 추가
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        return contentResolver.insert(collection, contentValues)
    }

    // 파일 복사 메서드
    private fun copyFile(input: InputStream, output: OutputStream) {
        input.use { inputStream ->
            output.use { outputStream ->
                val buffer = ByteArray(1024)
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PrivacyFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}