import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.database.sqlite.transaction
import androidx.fragment.app.Fragment
import com.example.ruok_workers.DBManager
import com.example.ruok_workers.R
import com.example.ruok_workers.SearchFragment

class ProfileRevisionFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var etPhoneNumber: EditText

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    private var homelessId: Int = -1 // 노숙인 번호

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_revision, container, false)

        // 뷰 초기화
        etName = view.findViewById(R.id.profile_revision_Name)
        etBirthdate = view.findViewById(R.id.profile_revision_Birth)
        etPhoneNumber = view.findViewById(R.id.profile_revision_PhoneNumber)

        // 기존 데이터 가져오기
        val name = arguments?.getString("name") ?: ""
        val birth = arguments?.getString("birth") ?: ""
        val phoneNumber = arguments?.getString("phone") ?: ""

        // 화면에 기존 데이터 설정
        etName.setText(name)
        etBirthdate.setText(birth)
        etPhoneNumber.setText(phoneNumber)

        // 데이터베이스 초기화 및 열기
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.writableDatabase

        // 노숙인 번호 가져오기
        homelessId = arguments?.getInt("homelessId", -1) ?: -1

        // '수정하기' 버튼 클릭 시 이벤트 처리
        view.findViewById<Button>(R.id.profile_revieion_ok).setOnClickListener {
            updateProfile()
        }

        return view
    }

    private fun updateProfile() {
        // 수정된 데이터 가져오기
        val newName = etName.text.toString()
        val newBirth = etBirthdate.text.toString()
        val newPhoneNumber = etPhoneNumber.text.toString()

        // 데이터베이스 업데이트 쿼리 실행
        val updateQuery = "UPDATE homeless SET h_name=?, h_birth=?, h_phone=? WHERE h_num=?;"
        sqlitedb.execSQL(updateQuery, arrayOf(newName, newBirth, newPhoneNumber, homelessId.toString()))

        // 업데이트 완료 후 SearchFragment로 이동
        // SearchFragment로 이동 시 업데이트가 반영된 후로 이동하도록 수정
        sqlitedb.transaction {
            // 트랜잭션 내부에서 이동 처리
            val searchFragment = SearchFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.rootLayout, searchFragment)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String, birth: String, phone: String, homelessId: Int) =
            ProfileRevisionFragment().apply {
                arguments = Bundle().apply {
                    putString("name", name)
                    putString("birth", birth)
                    putString("phone", phone)
                    putInt("homelessId", homelessId)
                }
            }
    }
}
