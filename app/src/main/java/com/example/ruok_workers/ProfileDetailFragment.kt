import android.app.AlertDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ruok_workers.DBManager
import com.example.ruok_workers.ProfileRevisionFragment
import com.example.ruok_workers.QuestionnaireFragment
import com.example.ruok_workers.R
import com.example.ruok_workers.SearchFragment

class ProfileDetailFragment : Fragment() {

    private lateinit var btnSurvey: Button

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_detail, container, false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        btnSurvey = view.findViewById(R.id.btn_EditProfile)
        btnSurvey.setOnClickListener {
            loadProfileRevisionFragment()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_EditProfile).setOnClickListener {
            loadProfileRevisionFragment()
        }

        view.findViewById<Button>(R.id.btn_goTolist).setOnClickListener {
            loadSearchFragment()
        }

        view.findViewById<Button>(R.id.btn_removeProfile).setOnClickListener {
            showRemoveProfileDialog()
        }
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

    private fun loadProfileRevisionFragment() {
        // ProfileRevisionFragment 의 인스턴스를 생성
        val profileRevisionFragment = ProfileRevisionFragment()

        // FragmentManager 를 사용하여 트랜잭션을 시작
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // 현재 프래그먼트를 ProfileRevisionFragment 로 교체
        fragmentTransaction.replace(R.id.rootLayout, profileRevisionFragment)
        // 트랜잭션을 백 스택에 추가
        fragmentTransaction.addToBackStack(null)
        // 트랜잭션을 커밋
        fragmentTransaction.commit()
    }

    private fun loadSearchFragment() {
        val searchFragment = SearchFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.rootLayout, searchFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun showRemoveProfileDialog() {
        // AlertDialog를 사용하여 사용자에게 프로필 삭제 여부를 묻는 대화 상자를 표시
        AlertDialog.Builder(requireContext())
            .setTitle("Remove Profile")
            .setMessage("본 프로필을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { dialog, _ ->
                // "Yes" 버튼을 클릭하면 프로필 삭제 처리를 수행
                //데이터베이스 연동
                dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                dbManager.close()

                // 여기서는 SearchFragment로 이동
                loadSearchFragment()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                // "No" 버튼을 클릭하면 대화 상자를 닫기
                dialog.dismiss()
            }
            .show()
    }
}
