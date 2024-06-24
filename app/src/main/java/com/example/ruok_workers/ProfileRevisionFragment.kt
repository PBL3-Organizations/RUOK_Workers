package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProfileRevisionFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var etPhoneNumber: EditText

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

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

        // '수정하기' 버튼 클릭 시 이벤트 처리
        view.findViewById<Button>(R.id.profile_revieion_ok).setOnClickListener {
            updateProfile(name, birth, phoneNumber)
        }

        return view
    }

    private fun updateProfile(name: String, birth: String, phoneNumber: String) {
        // 수정된 데이터 가져오기
        val newName = etName.text.toString()
        val newBirth = etBirthdate.text.toString()
        val newPhoneNumber = etPhoneNumber.text.toString()

        if (newName.isBlank() || newBirth.isBlank() || newPhoneNumber.isBlank()) {
            Toast.makeText(context, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            sqlitedb.beginTransaction()
            try {
                // 기존 데이터를 삭제합니다.
                val deleteQuery = "DELETE FROM homeless WHERE h_name=? AND h_birth=? AND h_phone=?;"
                sqlitedb.execSQL(deleteQuery, arrayOf(name, birth, phoneNumber))

                // 새 데이터를 추가합니다.
                val insertQuery =
                    "INSERT INTO homeless (h_name, h_birth, h_phone, h_photo) VALUES (?, ?, ?, 'default.jpeg');"
                sqlitedb.execSQL(insertQuery, arrayOf(newName, newBirth, newPhoneNumber))

                sqlitedb.setTransactionSuccessful()
            } finally {
                sqlitedb.endTransaction()
            }

            // 업데이트 완료 후 SearchFragment로 이동
            val searchFragment = SearchFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.rootLayout, searchFragment)
                .commit()

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

    companion object {
        @JvmStatic
        fun newInstance(name: String, birth: String, phone: String) =
            ProfileRevisionFragment().apply {
                arguments = Bundle().apply {
                    putString("name", name)
                    putString("birth", birth)
                    putString("phone", phone)
                }
            }
    }
}
