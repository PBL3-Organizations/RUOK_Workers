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
            # 개인정보처리방침

            ## 제1조(목적)

            RUOK(이하 ’어플리케이션‘)에서 제공하고자 하는 서비스(이하 ‘RUOK 서비스’)를 이용하는 개인(이하 ‘이용자’ 또는 ‘개인’)의 정보(이하 ‘개인정보’)를 보호하기 위해, 개인정보보호법, 정보통신망 이용촉진 및 정보보호 등에 관한 법률(이하 '정보통신망법') 등 관련 법령을 준수하고, 서비스 이해관계자의 개인정보 보호 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보처리방침(이하 ‘본 방침’)을 수립합니다.

            ## 제2조(개인정보 처리의 원칙)

            개인정보 관련 법령 및 본 방침에 따라 어플리케이션은 이용자 및 서비스 이해관계자의 개인정보를 수집할 수 있으며 수집된 개인정보는 원칙적으로 제3자에게 제공되지 않습니다. 단, 법령의 규정 등에 의해 적법하게 강제되는 경우 어플리케이션은 수집한 서비스 이해관계자의 개인정보를 사전에 개인의 동의 없이 제3자에게 제공할 수도 있습니다.

            ## 제3조(본 방침의 공개)

            1. 어플리케이션에서는 이용자가 언제든지 쉽게 본 방침을 확인할 수 있도록 어플리케이션의 첫 화면 또는 메인 대시보드를 통해 본 방침을 공개하고 있습니다.
            2. 어플리케이션에서 제1항에 따라 본 방침을 공개하는 경우 글자 크기, 색상 등을 활용하여 이용자가 본 방침을 쉽게 확인할 수 있도록 합니다.

            ## 제4조(본 방침의 변경)

            1. 본 방침은 개인정보 관련 법령, 지침, 고시 또는 정부나 RUOK 서비스의 정책이나 내용의 변경에 따라 개정될 수 있습니다.
            2. 어플리케이션은 제1항에 따라 본 방침을 개정하는 경우 다음 각 호 하나 이상의 방법으로 공지합니다.
               - 어플리케이션의 첫 화면 또는 별도의 창을 통하여 공지하는 방법
               - 서면·모사전송·전자우편 또는 이와 비슷한 방법으로 이용자에게 공지하는 방법
            3. 어플리케이션은 제2항의 공지는 본 방침 개정의 시행일로부터 최소 7일 이전에 공지합니다. 다만, 이용자 권리의 중요한 변경이 있을 경우에는 최소 30일 전에 공지합니다.

            ## 제5조(회원 가입을 위한 정보)

            본 어플리케이션은 이용자의 RUOK 서비스에 대한 회원가입을 위하여 다음과 같은 정보를 수집합니다.
            1. 필수 수집 정보: 성명, 개인 연락처(전화번호), 생년월일, 소속 기관, 아이디, 비밀번호, 위치정보
            2. 선택 수집 정보: 없음

            ## 제6조(RUOK 서비스 제공을 위한 정보)

            본 어플리케이션은 이용자에게 RUOK 어플의 서비스를 제공하기 위하여 다음과 같은 정보를 수집하며, 어플에서 사용하는 권한은 다음과 같습니다.
            1. 어플에서 사용하는 권한: 카메라, 위치, 지도, 파일 읽기, 파일 쓰기, 파일 열람, 파일 다운로드
            2. 개인정보 수집 및 이용이 필요한 기타 어플리케이션 서비스(기능)

            - 기능별 서비스 제공을 위한 필수 개인정보
            
                - 상담 내역 기록: 성명, 개인 연락처(전화번호), 생년월일, 건강 및 가족관계 등 민감정보, 얼굴 사진, 위치정보
                - 노숙인 프로필 목록 열람·추가·수정·삭제: 성명, 개인 연락처(전화번호), 생년월일, 건강 및 가족관계 등 민감정보, 얼굴 사진, 위치정보
                - 노숙인 즐겨찾기: 성명, 개인 연락처(전화번호), 생년월일, 건강 및 가족관계 등 민감정보, 얼굴 사진, 위치정보
                - 검색 필터: 성명, 개인 연락처(전화번호), 생년월일, 건강 및 가족관계 등 민감정보, 얼굴 사진, 위치정보
                - 이름을 알 수 없는 노숙인 모음: 얼굴 사진, 위치정보, 건강 및 가족관계 등 민감정보

            ## 제7조(개인정보 수집 방법)

            본 어플리케이션은 다음과 같은 방법으로 이용자의 개인정보를 수집합니다.
            1. 어플리케이션이 제공하는 서비스를 통해 이용자가 자신의 개인정보를 입력하는 방식
            2. 이용자가 개인정보 수집/처리 동의를 받은 상태로 타 정보주체의 개인정보를 입력하는 방식

            ## 제8조(개인정보의 이용)

            어플리케이션은 개인정보를 다음 각 호의 경우에 이용합니다.
            1. 공지사항의 전달 등 어플리케이션 운영에 필요한 경우
            2. 이용문의에 대한 회신, 불만의 처리 등 이용자에 대한 서비스 개선을 위한 경우
            3. 어플리케이션의 서비스를 제공하기 위한 경우
            4. 법령 및 회사 약관을 위반하는 회원에 대한 이용 제한 조치, 부정 이용 행위를 포함하여 서비스의 원활한 운영에 지장을 주는 행위에 대한 방지 및 제재를 위한 경우
            5. 인구통계학적 분석, 서비스 방문 및 이용기록의 분석을 위한 경우
            6. 개인정보 및 관심에 기반한 이용자간 관계의 형성을 위한 경우
            7. 사용자(사회복지사)의 '아웃리치' 업무 진행에 있어 필요한 경우

            ## 제9조(개인정보의 보유 및 이용기간)

            어플리케이션은 이용자의 개인정보에 대해 개인정보의 수집·이용 목적 달성을 위한 기간 동안 개인정보를 보유 및 이용합니다.

            ## 제10조(법령에 따른 개인정보의 보유 및 이용기간)

            어플리케이션은 관계법령에 따라 다음과 같이 개인정보를 보유 및 이용합니다.

            1. 위치정보의 보호 및 이용 등에 관한 법률
               - 개인위치정보에 관한 기록: 6개월
            2. 통신비밀보호법
               - 서비스 이용 관련 개인정보: 3개월

            ## 제11조(개인정보의 파기원칙)

            어플리케이션은 원칙적으로 이용자의 개인정보 처리 목적의 달성, 보유·이용기간의 경과 등 개인정보가 필요하지 않을 경우에는 해당 정보를 지체 없이 파기합니다.

            ## 제12조(개인정보파기절차)

            1. 이용자가 회원가입 등을 위해 입력한 정보는 개인정보 처리 목적이 달성된 후 내부 방침 및 기타 관련 법령에 의한 정보보호 사유에 따라 파기되어집니다.
            2. 내부 방침 및 기타 관련 법령에 의한 정보보호 사유 상세는 본 개인정보처리방침의 제9조 개인정보의 보유 및 이용기간을 참고할 수 있습니다.

            ## 제13조(개인정보파기방법)

            어플리케이션은 전자적 파일형태로 저장된 개인정보는 기록을 재생할 수 없는 기술적 방법을 사용하여 삭제하며, 종이로 출력된 개인정보는 분쇄기로 분쇄하거나 소각 등을 통하여 파기합니다.

            ## 제14조(광고성 정보의 전송 조치)

            본 어플리케이션은 전자적 전송매체를 이용하여 영리목적의 광고성 정보를 전송하지 않습니다.

            ## 제15조(아동의 개인정보보호)

            1. 본 어플리케이션에서는 만 14세 미만 아동의 개인정보 보호를 위하여 만 14세 이상의 이용자에 한하여 회원가입을 허용합니다.
            
            ## 제16조(개인정보 조회 및 수집동의 철회)

            1. 이용자는 언제든지 등록되어 있는 자신의 개인정보를 조회하거나 수정할 수 있으며 개인정보 수집 동의 철회를 요청할 수 있습니다.
            2. 이용자는 어플리케이션 내 ‘회원탈퇴’ 기능을 통해 개인정보 삭제와 동시에 개인정보 수집 동의를 철회할 수 있습니다.
            3. 이용자는 이 외에도 본 개인정보처리방침의 제23조에 기재된 개인정보보호 책임자 및 유관부서의 연락처를 활용하여 개인정보 수집동의를 철회하거나 조회할 수 있습니다.

            ## 제17조(개인정보 정보변경 등)

            1. 이용자는 어플리케이션의 관리자에게 전조(제16조 개인정보 조회 및 수집동의 철회)의 방법을 통해 개인정보의 오류에 대한 정정을 요청할 수 있습니다.
            2. 어플리케이션은 전항의 경우에 개인정보의 정정을 완료하기 전까지 개인정보를 이용하지 않습니다.

            ## 제18조(이용자의 의무)

            1. 이용자는 자신의 개인정보를 최신의 상태로 유지해야 하며, 이용자의 부정확한 정보 입력으로 발생하는 문제의 책임은 이용자 자신에게 있습니다.
            2. 타인의 개인정보를 도용한 회원가입의 경우 이용자 자격을 상실하거나 관련 개인정보보호 법령에 의해 처벌받을 수 있습니다.
            3. 이용자는 비밀번호 등에 대한 보안을 유지할 책임이 있으며 제3자에게 이를 양도하거나 대여할 수 없습니다.

            ## 제19조(개인정보 유출 등에 대한 조치)

            어플리케이션 관리자는 개인정보의 분실·도난·유출(이하 "유출 등"이라 한다) 사실을 알게 되었을 때에는 지체 없이 다음 각 호의 모든 사항을 해당 이용자에게 알리고 방송통신위원회 또는 한국인터넷진흥원에 신고합니다.
            1. 유출 등이 된 개인정보 항목
            2. 유출 등이 발생한 시점
            3. 이용자가 취할 수 있는 조치
            4. 정보통신서비스 제공자 등의 대응 조치
            5. 이용자가 상담 등을 접수할 수 있는 기관 및 연락처

            ## 제20조(개인정보 유출 등에 대한 조치의 예외)

            어플리케이션 관리자는 전조에도 불구하고 이용자의 연락처를 알 수 없는 등 정당한 사유가 있는 경우에는 어플의 메인 대시보드나 첫 화면에 해당 내용을 30일 이상 게시하는 방법으로 전조의 통지를 갈음하는 조치를 취할 수 있습니다.

            ## 제21조(개인정보 자동 수집 장치의 설치·운영 및 거부에 관한 사항)

            본 어플리케이션은 이용자에게 개별적인 맞춤 서비스를 제공하기 위해 이용 정보를 저장하고 수시로 불러오는 개인정보 자동 수집장치(이하 '쿠키')를 사용하지 않습니다.

            ## 제22조(권익침해에 대한 구제방법)

            1. 정보주체는 개인정보침해로 인한 구제를 받기 위하여 개인정보분쟁조정위원회, 한국인터넷진흥원 개인정보침해신고센터 등에 분쟁해결이나 상담 등을 신청할 수 있습니다. 이 밖에 기타 개인정보침해의 신고, 상담에 대하여는 아래의 기관에 문의하시기 바랍니다.
               - 개인정보분쟁조정위원회 : (국번없이) 1833-6972 (www.kopico.go.kr)
               - 개인정보침해신고센터 : (국번없이) 118 (privacy.kisa.or.kr)
               - 대검찰청 : (국번없이) 1301 (www.spo.go.kr)
               - 경찰청 : (국번없이) 182 (ecrm.cyber.go.kr)
            2. 본 어플리케이션은 정보주체의 개인정보자기결정권을 보장하고, 개인정보침해로 인한 상담 및 피해 구제를 위해 노력하고 있으며, 신고나 상담이 필요한 경우 제1항의 담당부서로 연락해주시기 바랍니다.
            3. 개인정보 보호법 제35조(개인정보의 열람), 제36조(개인정보의 정정·삭제), 제37조(개인정보의 처리정지 등)의 규정에 의한 요구에 대하여 공공기관의 장이 행한 처분 또는 부작위로 인하여 권리 또는 이익의 침해를 받은 자는 행정심판법이 정하는 바에 따라 행정심판을 청구할 수 있습니다.
               - 중앙행정심판위원회 : (국번없이) 110 (www.simpan.go.kr)

            ## 제23조(개인정보보호 책임자 및 연락처)

            1. 개인정보보호 책임자: 개인정보보호 책임자명
            2. 개인정보보호 책임자 연락처: 전화번호 및 이메일 주소
            3. 개인정보와 관련된 문의 사항의 경우 상단의 개인정보보호 책임자에게 연락하여 처리할 수 있습니다.

            ## 제24조(본 개인정보처리방침의 적용 범위)

            1. 본 개인정보처리방침은 RUOK 어플리케이션 및 관련 제반 서비스에 적용되며, RUOK를 제외한 다른 어플리케이션에서 제공되는 서비스에 대해서는 별개의 개인정보처리방침이 적용될 수 있습니다.

            ## 제25조(개정 전 고지 의무)

            1. 본 개인정보처리방침의 내용 추가, 삭제 및 수정이 있을 경우 개정 최소 7일 전에 ‘어플리케이션의 메인 대시보드’를 통해 사전 공지를 할 것입니다.
            2. 다만, 수집하는 개인정보의 항목, 이용목적의 변경 등과 같이 이용자 권리의 중대한 변경이 발생할 때에는 최소 30일 전에 공지하며, 필요 시 이용자 동의를 본 개인정보처리방침의 제7조에 기재된 방법과 동일한 방법으로 다시 받을 수도 있습니다.
            
            ---

            부칙  
            제1조 본 방침은 2024.10.11.부터 시행됩니다.

            
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