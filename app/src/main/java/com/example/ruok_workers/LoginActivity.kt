package com.example.ruok_workers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginButton = findViewById<Button>(R.id.button)
        val rememberMeCheckBox = findViewById<CheckBox>(R.id.remember_me_checkbox)
        val inputIdEditText = findViewById<EditText>(R.id.input_id)
        val inputPasswordEditText = findViewById<EditText>(R.id.input_password)

        // 자동 로그인 이력이 있는 경우
        val auto = getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
        if (auto.getInt("saved_loginNum", -1) > 0) {
            val intent = Intent(this, DashboardActivity::class.java)
            val loginNum = auto.getInt("saved_loginNum", -1)
            intent.putExtra("m_num", loginNum)
            startActivity(intent)
            finish()
        }

        // 키보드 숨기기 함수
        fun hideKeyboard() {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        // 아이디 입력란에서 엔터 누르면 키보드 숨기기
        inputIdEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        // 비밀번호 입력란에서 엔터 누르면 키보드 숨기기
        inputPasswordEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        // 로그인 버튼 클릭 시 키보드 숨기고 로그인 로직 실행
        loginButton.setOnClickListener {
            hideKeyboard()

            val inputId = inputIdEditText.text.toString()
            val inputPassword = inputPasswordEditText.text.toString()

            // 데이터베이스 연동: 로그인 검사
            dbManager = DBManager(this, "RUOKsample", null, 1)
            sqlitedb = dbManager.readableDatabase
            val sql = "SELECT m_num FROM member WHERE m_id=? AND m_pw=?;"
            val cursor: Cursor = sqlitedb.rawQuery(sql, arrayOf(inputId, inputPassword))

            if (cursor.moveToNext()) { // 로그인 성공
                val num = cursor.getInt(cursor.getColumnIndex("m_num"))

                // 로그인 이력 저장 체크되었을 경우
                if (rememberMeCheckBox.isChecked) {
                    val autoLoginEdit = auto.edit()
                    autoLoginEdit.putInt("saved_loginNum", num)
                    autoLoginEdit.apply()
                }

                // 로그인 성공 시 DashboardActivity로 이동
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("m_num", num)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다", Toast.LENGTH_SHORT).show()
            }

            cursor.close()
            sqlitedb.close()
            dbManager.close()
        }
    }
}
