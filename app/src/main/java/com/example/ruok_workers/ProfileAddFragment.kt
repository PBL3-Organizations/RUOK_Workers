package com.example.ruok_workers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileAddFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    private val GALLERY_REQUEST_CODE = 1001
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_add, container, false)

        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.writableDatabase

        val saveButton = view.findViewById<Button>(R.id.save_button_in_add_profile)
        val profileImageView = view.findViewById<ImageView>(R.id.profile_image)

        // 프로필 이미지 클릭 시 갤러리에서 이미지 선택
        profileImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // URI 읽기 권한 부여
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }

        saveButton.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.name_input).text.toString()
            val birth = view.findViewById<EditText>(R.id.birth_input_profile_add).text.toString()
            val phone = view.findViewById<EditText>(R.id.phone_input).text.toString()
            val specialNote = view.findViewById<EditText>(R.id.special_note_input).text.toString()

            // 모든 필드가 비어있는지 확인
            if (name.isEmpty() || birth.isEmpty() || phone.isEmpty() || specialNote.isEmpty() || selectedImageUri == null) {
                Toast.makeText(requireContext(), "모든 항목을 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                // 이미지 URI와 함께 데이터베이스에 저장
                val query = "INSERT INTO homeless (h_name, h_birth, h_phone, h_unusual, h_photo) " +
                        "VALUES ('$name', '$birth', '$phone', '$specialNote', '${selectedImageUri.toString()}')"
                sqlitedb.execSQL(query)

                // 성공 메시지
                Toast.makeText(requireContext(), "프로필이 저장되었습니다.", Toast.LENGTH_SHORT).show()

                // 키보드 숨기기
                hideKeyboard()

                // 검색 화면(SearchFragment)으로 이동
                val parentActivity = activity as DashboardActivity
                parentActivity.setFragment(SearchFragment())
            }
        }

        // EditText에서 포커스가 벗어날 때 키보드 숨기기
        val editTextList = listOf(
            view.findViewById<EditText>(R.id.name_input),
            view.findViewById<EditText>(R.id.birth_input_profile_add),
            view.findViewById<EditText>(R.id.phone_input),
            view.findViewById<EditText>(R.id.special_note_input)
        )

        for (editText in editTextList) {
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
        }

        return view
    }

    // 갤러리에서 이미지 선택 후 URI 저장
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                selectedImageUri = imageUri
                val profileImageView = requireView().findViewById<ImageView>(R.id.profile_image)
                profileImageView.setImageURI(imageUri) // 선택한 이미지를 ImageView에 표시
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileAddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
