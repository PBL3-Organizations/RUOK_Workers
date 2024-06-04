import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.ruok_workers.BriefingAddFragment
import com.example.ruok_workers.R

class BriefingDuringFragment : Fragment() {

    private lateinit var addPostButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_briefing_during, container, false)

        // '새 글 작성' 버튼 찾기
        addPostButton = view.findViewById<Button>(R.id.btn_add_post2)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // '새 글 작성' 버튼을 항상 보여주기(디폴트값)
        addPostButton.visibility = View.VISIBLE

        // '새 글 작성' 버튼에 클릭 리스너 설정
        addPostButton.setOnClickListener {
            // '새 글 작성' 버튼을 클릭 시 BriefingAddFragment로 이동
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, BriefingAddFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            // 새 Fragment로 이동할 때 '새 글 작성' 버튼 숨기기
            addPostButton.visibility = View.GONE
        }
    }
}
