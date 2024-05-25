import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ruok_workers.QuestionnaireFragment
import com.example.ruok_workers.R

class ProfileDetailFragment : Fragment() {

    private lateinit var btnSurvey: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_detail, container, false)

        // 버튼을 레이아웃에서 찾아옴
        btnSurvey = view.findViewById(R.id.btnSurvey)

        // 버튼에 클릭 리스너 추가
        btnSurvey.setOnClickListener {
            // QuestionnaireFragment를 불러옴
            loadQuestionnaireFragment()
        }

        return view
    }

    private fun loadQuestionnaireFragment() {
        // QuestionnaireFragment를 인스턴스화
        val questionnaireFragment = QuestionnaireFragment()

        // FragmentManager를 사용하여 Transaction 시작
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // QuestionnaireFragment를 불러와서 트랜잭션에 추가
        fragmentTransaction.replace(R.id.rootLayout, questionnaireFragment)
        // Transaction 커밋
        fragmentTransaction.commit()
    }
}
