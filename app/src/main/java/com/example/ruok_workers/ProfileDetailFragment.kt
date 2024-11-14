import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ruok_workers.BriefingDetailFragment
import com.example.ruok_workers.DBManager
import com.example.ruok_workers.DashboardActivity
import com.example.ruok_workers.HomelessQuestionListFragment
import com.example.ruok_workers.DashboardFragment
import com.example.ruok_workers.ProfileRevisionFragment
import com.example.ruok_workers.QuestionnaireFragment
import com.example.ruok_workers.R
import com.example.ruok_workers.SearchFragment
import java.io.File
import java.io.InputStream

class ProfileDetailFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvBirthdate: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvSpecialNote: TextView
    private lateinit var btnRemoveProfile: Button
    private lateinit var btnGoToList: Button
    private lateinit var ivProfiledetail:ImageView
    private lateinit var buttonConnection: Button

    private lateinit var dbManager: DBManager
    private lateinit var sqlitedb: SQLiteDatabase

    private var homelessId: Int = -1 // 노숙인 번호
    var m_num = ""
    var bookmark= -1

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
        buttonConnection = view.findViewById(R.id.buttonConnection)

        // FaviconAdapter에서 전달받은 데이터
        val name = arguments?.getString("name") ?: ""
        val birth = arguments?.getString("birth") ?: ""
        m_num = arguments?.getString("m_num")?:""
        bookmark = arguments?.getInt("bookmark",0)!!

        // 데이터베이스 초기화 및 쿼리 실행
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        val cursor = sqlitedb.rawQuery("SELECT * FROM homeless WHERE h_name = ? AND h_birth = ?", arrayOf(name, birth))
        if (cursor.moveToFirst()) {
            homelessId = cursor.getInt(cursor.getColumnIndex("h_num"))
            val phoneNumber = cursor.getString(cursor.getColumnIndex("h_phone"))
            var specialNote = cursor.getString(cursor.getColumnIndex("h_unusual"))
            val photoPath: String = cursor.getString(cursor.getColumnIndex("h_photo"))

            // TextView에 데이터 표시
            tvName.text = name
            tvBirthdate.text = birth
            tvPhoneNumber.text = phoneNumber
            tvSpecialNote.text = specialNote

            if (photoPath.startsWith("/")) {
                // 내부 저장소 경로에서 이미지 불러오기
                val file = File(photoPath)
                if (file.exists()) {
                    // Bitmap으로 변환하여 ImageView에 설정
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    ivProfiledetail.setImageBitmap(bitmap)
                } else {
                    // 파일이 없을 경우 기본 이미지 설정
                    ivProfiledetail.setImageResource(R.drawable.aegis_logo)
                }
            } else {
                // drawable 이미지 불러오기
                val resId = resources.getIdentifier(photoPath.substringBefore('.'), "drawable", requireContext().packageName)
                if (resId != 0) {
                    ivProfiledetail.setImageResource(resId)
                } else {
                    // 이미지가 없는 경우 기본 이미지 표시
                    ivProfiledetail.setImageResource(R.drawable.aegis_logo)
                }
            }
        }
        cursor.close()

        // '프로필 수정' 버튼 클릭 이벤트 처리
        view.findViewById<Button>(R.id.btn_EditProfile).setOnClickListener {
            val phoneNumber = tvPhoneNumber.text.toString()
            val specialNote = tvSpecialNote.text.toString()
            navigateToProfileRevisionFragment(name, birth, phoneNumber, specialNote, homelessId)
        }

        // btnGoToList 버튼 클릭 리스너 설정
        btnGoToList.setOnClickListener {
            loadSearchFragment()
        }

        // 프로필 삭제 버튼 클릭 리스너 설정
        btnRemoveProfile.setOnClickListener {
            showRemoveProfileDialog()
        }
        // 상담내역 보기 버튼 클릭 리스너 설정
        buttonConnection.setOnClickListener {
            navigateToHomelessQuestionListFragment(homelessId)
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }

    private fun navigateToProfileRevisionFragment(name: String, birth: String, phone: String, specialNote: String,h_num : Int) {
        // ProfileRevisionFragment로 이동
        val fragment = ProfileRevisionFragment()
        val args = Bundle()
        args.putString("name", name)
        args.putString("birth", birth)
        args.putString("phone", phone)
        args.putString("specialNote", specialNote)
        args.putString("m_num",m_num)
        args.putInt("bookmark", bookmark)
        args.putInt("HomelessId", h_num)
        fragment.arguments = args

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.rootLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun navigateToHomelessQuestionListFragment(h_num : Int) {
        // HomelessQuestionListFragment로 이동
        val fragment = HomelessQuestionListFragment()
        val args = Bundle()
        args.putInt("HomelessId", h_num)
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

//    // onViewCreated 메서드 추가
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // 뒤로가기 버튼 비활성화 콜백 설정
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                // 뒤로가기 버튼을 막고, 원하는 경우 메시지를 표시
//                // showToast("뒤로가기가 비활성화되었습니다.")
//            }
//        }
//
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뒤로가기 버튼을 눌렀을 때 백스택을 비우고 DashboardFragment로 이동
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 백스택을 전부 비움
                    parentFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    // DashboardFragment로 이동
                    val parentActivity = activity as DashboardActivity
                    parentActivity.setFragment(DashboardFragment())
                }
            }
        )
    }
}