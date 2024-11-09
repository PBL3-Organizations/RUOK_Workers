package com.example.ruok_workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.example.ruok_workers.databinding.FragmentAgreeBinding
import com.example.ruok_workers.databinding.FragmentSearchBinding
import io.noties.markwon.Markwon
import kotlin.math.max
import kotlin.math.min

class AgreeFragment : Fragment() {

    lateinit var binding: FragmentAgreeBinding

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var markdownTextView: TextView
    private var fontSize = 16f // 기본 폰트 크기 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agree, container, false)

        binding = FragmentAgreeBinding.inflate(inflater, container, false)

        // TextView 초기화
        markdownTextView = binding.markdownTextView4 // binding을 통해 접근
        markdownTextView.textSize = fontSize // 초기 폰트 크기 설정

        val markdown = """
            # 개인정보처리동의서 (노숙인)

            RUOK(이하 '어플리케이션'라고 합니다)은(는) 개인정보보호법 등 관련 법령상의 개인정보보호 규정을 준수하며 귀하의 개인정보보호에 최선을 다하고 있습니다. 본 어플리케이션은 개인정보보호법에 근거하여 다음과 같은 내용으로 개인정보를 수집 및 처리하고자 합니다.

            다음의 내용을 자세히 읽어보시고 모든 내용을 이해하신 후에 동의 여부를 결정해주시기 바랍니다.

            ## 제1조(개인정보 수집 및 이용 목적)
            이용자가 제공한 모든 정보는 다음의 목적을 위해 활용하며, 목적 이외의 용도로는 사용되지 않습니다.
            
            - 상담 내역 기록, 노숙인 프로필 열람·추가·수정·삭제, 노숙인 즐겨찾기, 검색 필터, 이름을 알 수 없는 노숙인 모음 기능을 통한 맞춤형 복지 서비스 제공

            ## 제2조(개인정보 수집 및 이용 항목)
            어플리케이션은 개인정보 수집 및 이용 목적을 위하여 다음과 같은 정보를 수집합니다.
            
            - 필수 수집 & 처리하는 개인정보 항목: 성명, 개인 연락처(전화번호), 생년월일, 건강 및 가족관계 등 민감정보, 얼굴 사진, 위치정보

            ## 제3조(개인정보 보유 및 이용 기간)
            수집한 개인정보는 수집·이용 동의일로부터 사용자가 정보 삭제 요청을 하기 전 및 노숙인 상태를 벗어난 시점까지 보관 및 이용합니다.

            ## 제4조(동의 거부 관리)
            귀하는 본 안내에 따른 개인정보 수집·이용에 대하여 동의를 거부할 권리가 있습니다. 다만, 귀하가 개인정보 동의를 거부하시는 경우에 서비스 이용 제한의 불이익이 발생할 수 있음을 알려드립니다.

            ## 제5조(개인정보보호 책임자 및 연락처)
            개인정보보호 책임자명 및 연락처: 

            본인은 위의 동의서 내용을 충분히 숙지하였으며, 위와 같이 개인정보를 수집·이용하는데 동의합니다.
            
        """.trimIndent()

        // Initialize Markwon
        val markwon = Markwon.create(requireContext())

        // Set markdown content to TextView
        markwon.setMarkdown(markdownTextView, markdown)

        // ScaleGestureDetector 초기화
        scaleGestureDetector = ScaleGestureDetector(requireContext(), object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
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

        val agreeCheckBox = binding.AgreeCheckBox // 체크박스 참조

        // 다음 버튼 클릭 시 동의 체크 확인 후 이동 또는 메시지 표시
        binding.buttonAgreeNext.setOnClickListener {
            if (agreeCheckBox.isChecked) {
                // CheckBox가 체크된 경우 ProfileAddFragment로 이동
                val profileAddFragment = ProfileAddFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.rootLayout, profileAddFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            } else {
                // 체크되지 않은 경우 토스트 메시지 표시
                Toast.makeText(requireContext(), "개인정보 수집 및 이용에 동의해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뒤로가기 버튼을 눌렀을 때 백스택을 비우고 SearchFragment로 이동
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 백스택을 전부 비움
                    parentFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    // /SearchFragment로 이동
                    val parentActivity = activity as DashboardActivity
                    parentActivity.setFragment(SearchFragment())
                }
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgreeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}