import android.annotation.SuppressLint
import android.app.AlertDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.ruok_workers.BriefingDetailFragment
import com.example.ruok_workers.DBManager
import com.example.ruok_workers.DashboardActivity
import com.example.ruok_workers.ProfileRevisionFragment
import com.example.ruok_workers.R
import com.example.ruok_workers.SearchFragment

class ProfileDetailFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvBirthdate: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvSpecialNote: TextView
    private lateinit var btnRemoveProfile: Button
    private lateinit var btnGoToList: Button
    private lateinit var ivProfiledetail:ImageView

    private lateinit var dbManager: DBManager
    private lateinit var sqlitedb: SQLiteDatabase

    private var homelessId: Int = -1 // 노숙인 번호

    @SuppressLint("Range", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_detail, container, false)

        // 뷰 초기화
        tvName = view.findViewById(R.id.tvName)
        tvBirthdate = view.findViewById(R.id.tvBirthdate)
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber)
        tvSpecialNote = view.findViewById(R.id.tvSpecialNote)
        btnRemoveProfile = view.findViewById(R.id.btn_removeProfile)
        btnGoToList = view.findViewById(R.id.btn_goTolist)
        ivProfiledetail = view.findViewById(R.id.ivProfiledetail)

        // FaviconAdapter에서 전달받은 데이터
        val name = arguments?.getString("name") ?: ""
        val birth = arguments?.getString("birth") ?: ""

        // 데이터베이스 초기화 및 쿼리 실행
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        val cursor = sqlitedb.rawQuery("SELECT * FROM homeless WHERE h_name = ? AND h_birth = ?", arrayOf(name, birth))
        if (cursor.moveToFirst()) {
            homelessId = cursor.getInt(cursor.getColumnIndex("h_num"))
            val phoneNumber = cursor.getString(cursor.getColumnIndex("h_phone"))
            var specialNote = cursor.getString(cursor.getColumnIndex("h_unusual"))
            var photoFilename: String = cursor.getString(cursor.getColumnIndex("h_photo"))
            var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)

            // TextView에 데이터 표시
            tvName.text = name
            tvBirthdate.text = birth
            tvPhoneNumber.text = phoneNumber
            tvSpecialNote.text = specialNote
            ivProfiledetail.setImageResource(resId)
        }
        cursor.close()

        // '프로필 수정' 버튼 클릭 이벤트 처리
        view.findViewById<Button>(R.id.btn_EditProfile).setOnClickListener {
            val phoneNumber = tvPhoneNumber.text.toString()
            val specialNote = tvSpecialNote.text.toString()
            navigateToProfileRevisionFragment(name, birth, phoneNumber, specialNote)
        }

        // btnGoToList 버튼 클릭 리스너 설정
        btnGoToList.setOnClickListener {
            loadSearchFragment()
        }

        // 프로필 삭제 버튼 클릭 리스너 설정
        btnRemoveProfile.setOnClickListener {
            showRemoveProfileDialog()
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }

    private fun navigateToProfileRevisionFragment(name: String, birth: String, phone: String, specialNote: String) {
        // ProfileRevisionFragment로 이동
        val fragment = ProfileRevisionFragment()
        val args = Bundle()
        args.putString("name", name)
        args.putString("birth", birth)
        args.putString("phone", phone)
        args.putString("specialNote", specialNote)
        fragment.arguments = args

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.rootLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showRemoveProfileDialog() {
        // AlertDialog를 사용하여 사용자에게 프로필 삭제 여부를 묻는 대화 상자를 표시
        AlertDialog.Builder(requireContext())
            .setTitle("프로필 삭제")
            .setMessage("본 프로필을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { dialog, _ ->
                // "삭제" 버튼을 클릭하면 프로필 삭제 처리를 수행
                deleteProfile()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                // "취소" 버튼을 클릭하면 대화 상자를 닫기
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteProfile() {
        // 데이터베이스에서 노숙인 및 즐겨찾기 삭제
        sqlitedb.beginTransaction()
        try {
            // 노숙인 삭제
            sqlitedb.delete("homeless", "h_num = ?", arrayOf(homelessId.toString()))

            // 즐겨찾기 삭제
            sqlitedb.delete("bookmark", "h_num = ?", arrayOf(homelessId.toString()))

            sqlitedb.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            sqlitedb.endTransaction()
        }

        // 삭제 후 처리
        showToast("프로필이 삭제되었습니다.")

        // SearchFragment로 이동
        loadSearchFragment()
    }

    private fun loadSearchFragment() {
//        val searchFragment = SearchFragment()
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.rootLayout, searchFragment)
//            .commit()
        val parentActivity = activity as DashboardActivity
        parentActivity.setFragment(SearchFragment())
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // onViewCreated 메서드 추가
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼 비활성화 콜백 설정
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 버튼을 막고, 원하는 경우 메시지를 표시
                // showToast("뒤로가기가 비활성화되었습니다.")
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
