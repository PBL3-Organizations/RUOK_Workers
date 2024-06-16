package com.example.ruok_workers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InfoRevisionFragment : Fragment() {

    private val organizations = listOf("Lover Center", "Vision Center", "Apple Center")
    private val dummyIds = listOf("user1", "admin", "testuser") // 더미 데이터

    private var param1: String? = null
    private var param2: String? = null

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("registered_id", "")
        val userPassword = sharedPreferences.getString("registered_password", "")
        val userName = sharedPreferences.getString("user_name", "")
        val userBirth = sharedPreferences.getString("user_birth", "")
        val userOrganization = sharedPreferences.getString("user_organization", "")

        view.findViewById<EditText>(R.id.input_id).setText(userId)
        view.findViewById<EditText>(R.id.input_password).setText(userPassword)
        view.findViewById<EditText>(R.id.input_workerName).setText(userName)
        view.findViewById<EditText>(R.id.input_workerBirth).setText(userBirth)
        view.findViewById<EditText>(R.id.input_organizations).setText(userOrganization)

        val modifyButton = view.findViewById<Button>(R.id.modify_button)
        modifyButton.setOnClickListener {
            val editedUserId = view.findViewById<EditText>(R.id.input_id).text.toString()
            val editedUserPassword = view.findViewById<EditText>(R.id.input_password).text.toString()
            val editedUserName = view.findViewById<EditText>(R.id.input_workerName).text.toString()
            val editedUserBirth = view.findViewById<EditText>(R.id.input_workerBirth).text.toString()
            val editedUserOrganization = view.findViewById<EditText>(R.id.input_organizations).text.toString()

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("registered_id", editedUserId)
            editor.putString("registered_password", editedUserPassword)
            editor.putString("user_name", editedUserName)
            editor.putString("user_birth", editedUserBirth)
            editor.putString("user_organization", editedUserOrganization)
            editor.apply()
        }

        // "소속 찾기" 버튼 클릭 이벤트 핸들러 추가
        view.findViewById<Button>(R.id.find_organizations).setOnClickListener {
            showSearchDialog()
        }

        //아이디 중복여부 검사
        val checkIdDuplicateButton = view.findViewById<Button>(R.id.check_id_duplicate)
        checkIdDuplicateButton.setOnClickListener {
            checkIdDuplicate()
        }

        val modifyConfirmButton = view.findViewById<Button>(R.id.modify_button)
        modifyConfirmButton.setOnClickListener {
            // 수정하기 버튼을 클릭하면 DashboardActivity로 이동
            val intent = Intent(activity, DashboardActivity::class.java)
            startActivity(intent)
            activity?.finish() // 회원정보 수정 화면을 종료하여 뒤로 가기 버튼을 눌렀을 때 다시 회원정보 수정 화면이 나타나지 않도록 함
        }

        // "회원탈퇴" 버튼 클릭 이벤트 핸들러 추가
        val deleteButton = view.findViewById<Button>(R.id.delete_button)
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info_revision, container, false)
    }

    fun checkIdDuplicate() {
        val inputId = view?.findViewById<EditText>(R.id.input_id)?.text.toString()
        if (dummyIds.contains(inputId)) {
            showAlertDialog("중복된 아이디", "다른 아이디를 선택해주세요")
        } else {
            showAlertDialog("사용 가능", "사용 가능한 아이디입니다")
        }
    }

    fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인", null)
            .show()
    }

    // "소속 찾기" 다이얼로그 표시 함수
    private fun showSearchDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_search_organizations)

        val searchInput = dialog.findViewById<EditText>(R.id.search_input)
        val searchButton = dialog.findViewById<Button>(R.id.search_button)
        val searchResults = dialog.findViewById<ListView>(R.id.search_results)

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            val filteredOrganizations = organizations.filter { it.contains(query, ignoreCase = true) }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, filteredOrganizations)
            searchResults.adapter = adapter
        }

        searchResults.setOnItemClickListener { _, _, position, _ ->
            val selectedOrganization = searchResults.getItemAtPosition(position) as String
            val inputOrganizations = view?.findViewById<EditText>(R.id.input_organizations)
            inputOrganizations?.setText(selectedOrganization)
            dialog.dismiss()
        }

        dialog.show()
    }

    // "회원탈퇴" 확인 다이얼로그 표시 함수
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("회원탈퇴")
            .setMessage("정말로 회원탈퇴를 하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                deleteUserAccount()
            }
            .setNegativeButton("아니요", null)
            .show()
    }

    // 회원 탈퇴 처리 함수
    private fun deleteUserAccount() {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear() // 모든 데이터 삭제
        editor.apply()

        // 로그인 화면으로 이동
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish() // 현재 화면 종료
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoRevisionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
