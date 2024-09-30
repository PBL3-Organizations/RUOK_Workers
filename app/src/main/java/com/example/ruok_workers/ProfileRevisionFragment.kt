package com.example.ruok_workers

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProfileRevisionFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etSpecialNote: EditText
    private lateinit var ivProfileRevision:ImageView

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    var resId = -1
    @SuppressLint("Range", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_revision, container, false)

        // 뷰 초기화
        etName = view.findViewById(R.id.profile_revision_Name)
        etBirthdate = view.findViewById(R.id.profile_revision_Birth)
        etPhoneNumber = view.findViewById(R.id.profile_revision_PhoneNumber)
        etSpecialNote = view.findViewById(R.id.profile_revision_SpecialNote)
        ivProfileRevision = view.findViewById(R.id.imageView_profile_revison)

        // 기존 데이터 가져오기
        val name = arguments?.getString("name") ?: ""
        val birth = arguments?.getString("birth") ?: ""
        val phoneNumber = arguments?.getString("phone") ?: ""
        val specialNote = arguments?.getString("specialNote") ?: ""

        // 화면에 기존 데이터 설정
        etName.setText(name)
        etBirthdate.setText(birth)
        etPhoneNumber.setText(phoneNumber)
        etSpecialNote.setText(specialNote)

        // 데이터베이스 초기화 및 열기
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.writableDatabase

        val cursor = sqlitedb.rawQuery("SELECT * FROM homeless WHERE h_name = ? AND h_birth = ?", arrayOf(name, birth))
        while (cursor.moveToNext()) {
            var photoFilename: String = cursor.getString(cursor.getColumnIndex("h_photo"))
            resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
            // TextView에 데이터 표시
            ivProfileRevision.setImageResource(resId)
        }
        cursor.close()

        // 엔터 누를 시 키보드 숨기기 처리
        etName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        etBirthdate.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        etPhoneNumber.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        etSpecialNote.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        // '수정하기' 버튼 클릭 시 이벤트 처리
        view.findViewById<Button>(R.id.profile_revieion_ok).setOnClickListener {
            updateProfile(name, birth, phoneNumber, specialNote)
        }

        return view
    }

    private fun updateProfile(name: String, birth: String, phoneNumber: String, specialNote: String) {
        // 수정된 데이터 가져오기
        val newName = etName.text.toString()
        val newBirth = etBirthdate.text.toString()
        val newPhoneNumber = etPhoneNumber.text.toString()
        val newSpecialNote = etSpecialNote.text.toString()

        if (newName.isBlank() || newBirth.isBlank() || newPhoneNumber.isBlank()) {
            Toast.makeText(context, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            sqlitedb.beginTransaction()
            try {
                // 기존 데이터를 삭제합니다.
                val deleteQuery = "DELETE FROM homeless WHERE h_name=? AND h_birth=? AND h_phone=? AND h_unusual=?;"
                sqlitedb.execSQL(deleteQuery, arrayOf(name, birth, phoneNumber, specialNote))

                // 새 데이터를 추가합니다.
                val insertQuery =
                    "INSERT INTO homeless (h_name, h_birth, h_phone, h_unusual, h_photo) VALUES (?, ?, ?, ?, ?);"
                sqlitedb.execSQL(insertQuery, arrayOf(newName, newBirth, newPhoneNumber, newSpecialNote, resId.toString()))

                sqlitedb.setTransactionSuccessful()
            } finally {
                sqlitedb.endTransaction()
            }

            // 업데이트 완료 후 SearchFragment로 이동
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(SearchFragment())

            Toast.makeText(context, "프로필이 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ProfileRevisionFragment", "Profile update failed", e)
            Toast.makeText(context, "프로필 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }

    // 키보드를 숨기는 함수
    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String, birth: String, phone: String, specialNote: String) =
            ProfileRevisionFragment().apply {
                arguments = Bundle().apply {
                    putString("name", name)
                    putString("birth", birth)
                    putString("phone", phone)
                    putString("specialNote", specialNote)
                }
            }
    }
}
