// BriefingAddFragment.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.ruok_workers.R

class BriefingAddFragment : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_briefing_add, container, false)

        titleEditText = view.findViewById(R.id.edit_text_title)
        contentEditText = view.findViewById(R.id.edit_text_content)

        val publishButton = view.findViewById<Button>(R.id.btn_publish_post)
        publishButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            // 작성한 제목과 내용을 BriefingDuringFragment로 전달하여 목록에 추가
            val briefingDuringFragment = parentFragmentManager.findFragmentByTag("briefing_during") as? BriefingDuringFragment
            briefingDuringFragment?.addPost("$title\n$content")

            // 이전 화면으로 돌아가기
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
