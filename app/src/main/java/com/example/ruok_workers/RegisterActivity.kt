package com.example.ruok_workers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RegisterActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var input_workerName: EditText
    lateinit var input_workerBirth: EditText
    lateinit var input_id: EditText
    lateinit var input_password: EditText
    lateinit var input_check_password: EditText
    lateinit var input_organization: EditText
    lateinit var organization_number: TextView

    lateinit var checkBoxRegisterAll: CheckBox
    lateinit var checkBoxRegister1: CheckBox
    lateinit var checkBoxRegister2: CheckBox
    lateinit var signUpButton: Button
    lateinit var tvRegisterAgree1: TextView
    lateinit var tvRegisterAgree2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        //액션바 제목 변경
        supportActionBar?.setTitle("RUOK?")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 체크박스 및 버튼 초기화
        checkBoxRegisterAll = findViewById(R.id.checkBoxRegisterAll)
        checkBoxRegister1 = findViewById(R.id.checkBoxRegister1)
        checkBoxRegister2 = findViewById(R.id.checkBoxRegister2)
        signUpButton = findViewById(R.id.signUp_button2)
        tvRegisterAgree1 = findViewById(R.id.tvRegisterAgree1)
        tvRegisterAgree2 = findViewById(R.id.tvRegisterAgree2)

        // '전체 동의' 체크박스 클릭 이벤트
        checkBoxRegisterAll.setOnCheckedChangeListener { _, isChecked ->
            checkBoxRegister1.isChecked = isChecked
            checkBoxRegister2.isChecked = isChecked
        }

        // 각각의 필수 약관 체크박스 클릭 이벤트
        checkBoxRegister1.setOnCheckedChangeListener { _, _ ->
            syncCheckAll()
        }

        checkBoxRegister2.setOnCheckedChangeListener { _, _ ->
            syncCheckAll()
        }

        // '서비스 이용약관 보기' 버튼 클릭 이벤트
        tvRegisterAgree1.setOnClickListener{
            val intent = Intent(this, ServiceAgreeActivity::class.java)
            startActivity(intent)
        }

//        tvRegisterAgree1.setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.main, ServiceAgreeFragment())
//                .addToBackStack(null)
//                .commit()
//        }

        // '개인정보 수집 및 이용 보기' 버튼 클릭 이벤트
        tvRegisterAgree2.setOnClickListener{
            val intent = Intent(this, PrivacyAgreeActivity::class.java)
            startActivity(intent)
        }

//        tvRegisterAgree2.setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.main, PrivacyAgreeFragment())
//                .addToBackStack(null)
//                .commit()
//        }

        input_workerName = findViewById(R.id.input_workerName)
        input_workerBirth = findViewById(R.id.input_workerBirth)
        input_id = findViewById(R.id.input_id)
        input_password = findViewById(R.id.input_password)
        input_check_password = findViewById(R.id.input_check_password)
        input_organization = findViewById(R.id.input_organizations)
        organization_number = findViewById(R.id.organization_num)

        val signUpButton = findViewById<Button>(R.id.signUp_button2)
        signUpButton.setOnClickListener {
            val inputId = input_id.text.toString()
            val inputPassword = input_password.text.toString()
            val inputCheckPassword = input_check_password.text.toString()
            val inputName = input_workerName.text.toString() // 사용자 이름 입력
            val inputBirth = input_workerBirth.text.toString() // 사용자 생년월일 입력
            val inputOrganization = input_organization.text.toString() // 사용자 소속 입력
            val organization_num = organization_number.text.toString().toInt()

            // 데이터베이스 연동: 아이디 중복검사
            dbManager = DBManager(this, "RUOKsample", null, 1)
            sqlitedb = dbManager.readableDatabase
            var cursor: Cursor
            val pstmt = "SELECT m_id FROM member WHERE m_id=?;"
            cursor = sqlitedb.rawQuery(pstmt, arrayOf(inputId))

            val count = cursor.count

            cursor.close()
            sqlitedb.close()
            dbManager.close()

            if (!checkBoxRegister1.isChecked || !checkBoxRegister2.isChecked) {
                Toast.makeText(this, "필수 약관에 동의해주세요.", Toast.LENGTH_SHORT).show()
            } else if (inputId.isBlank() || inputPassword.isBlank() || inputCheckPassword.isBlank() || inputName.isBlank() || inputBirth.isBlank() || inputOrganization.isBlank()) {
                Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (inputPassword != inputCheckPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            } else if (!isPasswordValid(inputPassword)) {
                Toast.makeText(this, "비밀번호는 최소 8자리 이상이어야 하며 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
            } else if (count > 0) {
                Toast.makeText(this, "중복된 아이디입니다", Toast.LENGTH_SHORT).show()
            } else if (inputBirth.length != 8) {
                Toast.makeText(this, "생년월일을 YYYYMMDD 형식으로 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // 데이터베이스 연동: 회원정보 저장
                dbManager = DBManager(this, "RUOKsample", null, 1)
                sqlitedb = dbManager.writableDatabase
                val sql = "INSERT INTO member (m_name, m_id, m_pw, m_birth, m_type, m_photo, wf_num) VALUES (?, ?, ?, ?, 1, 'default.jpeg', ?);"
                sqlitedb.execSQL(sql, arrayOf(inputName, inputId, inputPassword, inputBirth, organization_num))

                sqlitedb.close()
                dbManager.close()

                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val findOrganizationsButton = findViewById<Button>(R.id.find_organizations)
        findOrganizationsButton.setOnClickListener {
            showSearchDialog()
        }

        val checkIdButton = findViewById<Button>(R.id.check_id_duplicate)
        checkIdButton.setOnClickListener {
            checkIdDuplicate()
        }

        // Enter 키 입력 후 키보드 숨기기
        input_workerName.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                hideKeyboard(input_workerName)
                true
            } else {
                false
            }
        }

        input_workerBirth.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                hideKeyboard(input_workerBirth)
                true
            } else {
                false
            }
        }

        input_id.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                hideKeyboard(input_id)
                true
            } else {
                false
            }
        }

        input_password.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                hideKeyboard(input_password)
                true
            } else {
                false
            }
        }

        input_check_password.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                hideKeyboard(input_check_password)
                true
            } else {
                false
            }
        }

        input_organization.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                hideKeyboard(input_organization)
                true
            } else {
                false
            }
        }
    }

    // checkBoxRegisterAll 상태를 동기화하는 함수
    private fun syncCheckAll() {
        checkBoxRegisterAll.isChecked = checkBoxRegister1.isChecked && checkBoxRegister2.isChecked
    }

    @SuppressLint("Range", "ResourceType")
    fun showSearchDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_organizations)

        val searchInput = dialog.findViewById<EditText>(R.id.search_input)
        val searchButton = dialog.findViewById<Button>(R.id.search_button)

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()

            // 데이터베이스 연동: 복지시설 정보 가져오기
            dbManager = DBManager(this, "RUOKsample", null, 1)
            sqlitedb = dbManager.readableDatabase
            var cursor: Cursor
            val pstmt = "SELECT * FROM welfare_facilities WHERE wf_name LIKE ?;"
            cursor = sqlitedb.rawQuery(pstmt, arrayOf("%$query%"))

            val orgList = ArrayList<OrganizationItem>()
            while (cursor.moveToNext()) {
                val wfNum = cursor.getInt(cursor.getColumnIndex("wf_num"))
                val wfName = cursor.getString(cursor.getColumnIndex("wf_name")).toString()
                val wfAddr = cursor.getString(cursor.getColumnIndex("wf_addr")).toString()
                orgList.add(OrganizationItem(wfNum, wfName, wfAddr))
            }
            cursor.close()
            sqlitedb.close()
            dbManager.close()

            recyclerView = dialog.findViewById(R.id.rvOrg)
            recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = OrganizationAdapter(applicationContext, orgList, dialog, input_organization, organization_number)
        }

        dialog.show()
    }

    fun checkIdDuplicate() {
        val inputId = findViewById<EditText>(R.id.input_id).text.toString()

        // 데이터베이스 연동: 기존 아이디 리스트 가져오기
        dbManager = DBManager(this, "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        val pstmt = "SELECT m_id FROM member WHERE m_id=?;"
        cursor = sqlitedb.rawQuery(pstmt, arrayOf(inputId))

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

    fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인", null)
            .show()
    }

    // 키보드 숨기기 메서드
    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
