package com.example.ruok_workers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InfoRevisionFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var loginNum: Int = -1

    lateinit var editUserId: EditText
    lateinit var editUserPassword: EditText
    lateinit var editUserPasswordCheck: EditText
    lateinit var editUserName: EditText
    lateinit var editUserOrg: EditText
    lateinit var editUserOrgNum: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("CutPasteId", "Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        //위젯 연결
        editUserId = view.findViewById(R.id.input_id)
        editUserPassword = view.findViewById(R.id.input_password)
        editUserPasswordCheck = view.findViewById(R.id.input_check_password)
        editUserName = view.findViewById(R.id.input_workerName)
        editUserOrg = view.findViewById(R.id.input_organizations)
        editUserOrgNum = view.findViewById(R.id.organization_num)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        val sql = "SELECT m.m_name, m.m_id, m.m_pw, m.wf_num, w.wf_name FROM member m JOIN welfare_facilities w ON m.wf_num = w.wf_num WHERE m.m_num = ?;"
        cursor = sqlitedb.rawQuery(sql, arrayOf(loginNum.toString()))
        cursor.moveToNext()
        var userId = cursor.getString(cursor.getColumnIndex("m.m_id"))
        var userPassword = cursor.getString(cursor.getColumnIndex("m.m_pw"))
        var userName = cursor.getString(cursor.getColumnIndex("m.m_name"))
        var userOrg = cursor.getString(cursor.getColumnIndex("w.wf_name"))
        var userOrgNum = cursor.getInt(cursor.getColumnIndex("m.wf_num"))

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        editUserId.setText(userId)
        editUserPassword.setText(userPassword)
        editUserPasswordCheck.setText(userPassword)
        editUserName.setText(userName)
        editUserOrg.setText(userOrg)
        editUserOrgNum.setText(userOrgNum.toString())

        val modifyButton = view.findViewById<Button>(R.id.modify_button)
        modifyButton.setOnClickListener {
            userId = editUserId.text.toString()
            userPassword = editUserPassword.text.toString()
            val userPasswordCheck = editUserPasswordCheck.text.toString()
            userName = editUserName.text.toString()
            userOrg = editUserOrg.text.toString()
            userOrgNum = editUserOrgNum.text.toString().toInt()

            // 데이터베이스 연동: 아이디 중복검사
            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
            sqlitedb = dbManager.readableDatabase
            val pstmt = "SELECT m_id FROM member WHERE m_id=? AND m_num!=?;"
            val cursor = sqlitedb.rawQuery(pstmt, arrayOf(userId, loginNum.toString()))

            val count = cursor.count

            cursor.close()
            sqlitedb.close()
            dbManager.close()

            if (userId.isBlank() || userPassword.isBlank() || userPasswordCheck.isBlank() || userName.isBlank() || userOrg.isBlank()) {
                Toast.makeText(requireContext(), "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (!isPasswordValid(userPassword)) {
                Toast.makeText(
                    requireContext(),
                    "비밀번호는 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (userPassword != userPasswordCheck) {
                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            } else if (count > 0) {
                Toast.makeText(requireContext(), "중복된 아이디입니다", Toast.LENGTH_SHORT).show()
            } else {
                // 데이터베이스 연동: 회원정보 수정
                dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
                sqlitedb = dbManager.writableDatabase
                val sql = "UPDATE member SET m_name=?, m_id=?, m_pw=?, wf_num=? WHERE m_num = ?;"
                sqlitedb.execSQL(sql, arrayOf(userName, userId, userPassword, userOrgNum, loginNum))

                sqlitedb.close()
                dbManager.close()

                Toast.makeText(requireContext(), "회원정보 수정!", Toast.LENGTH_SHORT).show()

                // 수정하기 버튼을 클릭하면 DashboardActivity로 이동
                val intent = Intent(activity, DashboardActivity::class.java)
                intent.putExtra("m_num", loginNum)
                startActivity(intent)
                activity?.finish() // 회원정보 수정 화면을 종료하여 뒤로 가기 버튼을 눌렀을 때 다시 회원정보 수정 화면이 나타나지 않도록 함
            }
        }


        // 엔터 누를 시 키보드 숨기기 처리
        editUserId.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        editUserPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        editUserPasswordCheck.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        editUserName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        editUserOrg.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
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
        //데이터베이스 연동: 아이디 중복검사
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        val sql = "SELECT m_id FROM member WHERE m_id=? AND m_num!=?;"
        cursor = sqlitedb.rawQuery(sql, arrayOf(editUserId.text.toString(), loginNum.toString()))

        val count = cursor.count

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        if (count > 0) {
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
    @SuppressLint("Range")
    private fun showSearchDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_search_organizations)

        val searchInput = dialog.findViewById<EditText>(R.id.search_input)
        val searchButton = dialog.findViewById<Button>(R.id.search_button)

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()

            //데이터베이스 연동: 복지시설 정보 가져오기
            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
            sqlitedb = dbManager.readableDatabase
            var cursor: Cursor
            val pstmt = "SELECT * FROM welfare_facilities WHERE wf_name LIKE ?;";
            cursor = sqlitedb.rawQuery(pstmt, arrayOf("%$query%"))

            var orgList = ArrayList<OrganizationItem>()
            while (cursor.moveToNext()) {
                val wfNum = cursor.getInt(cursor.getColumnIndex("wf_num"))
                val wfName = cursor.getString(cursor.getColumnIndex("wf_name")).toString()
                val wfAddr  = cursor.getString(cursor.getColumnIndex("wf_addr")).toString()
                orgList.add(OrganizationItem(wfNum, wfName, wfAddr))
            }
            cursor.close()
            sqlitedb.close()
            dbManager.close()

            recyclerView = dialog.findViewById(R.id.rvOrg)
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = OrganizationAdapter(requireContext(), orgList, dialog, editUserOrg, editUserOrgNum)
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
        //데이터베이스 연동: 회원정보 삭제
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.writableDatabase
        val sql = "DELETE FROM member WHERE m_num = ?;"
        sqlitedb.execSQL(sql, arrayOf(loginNum))
        sqlitedb.close()
        dbManager.close()

        // 로그인 화면으로 이동
        val intent = Intent(activity, MainActivity::class.java)
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

    // 키보드를 숨기는 함수
    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun isPasswordValid(password: String): Boolean {
        // 최소 길이 체크
        if (password.length < 8) return false

        // 대문자 포함 체크
        if (!password.any { it.isUpperCase() }) return false

        // 소문자 포함 체크
        if (!password.any { it.isLowerCase() }) return false

        // 숫자 포함 체크
        if (!password.any { it.isDigit() }) return false

        // 특수 문자 포함 체크
        if (!password.any { "!@#$%^&*()_+[]{}|;:,.<>?/".contains(it) }) return false

        return true
    }
}
