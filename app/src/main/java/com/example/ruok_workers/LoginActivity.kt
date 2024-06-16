package com.example.ruok_workers

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
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

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // 로그인 이력 저장 체크되었을 경우
        val savedId = sharedPreferences.getString("saved_id", null)
        val savedPassword = sharedPreferences.getString("saved_password", null)
        val rememberMeChecked = sharedPreferences.getBoolean("remember_me", false)

        if (rememberMeChecked) {
            inputIdEditText.setText(savedId)
            inputPasswordEditText.setText(savedPassword)
            rememberMeCheckBox.isChecked = true
        }

        loginButton.setOnClickListener {
            val inputId = inputIdEditText.text.toString()
            val inputPassword = inputPasswordEditText.text.toString()

            //데이터베이스 연동
            dbManager = DBManager(this, "RUOKsample", null, 1)
            dbManager.close()

            val registeredId = sharedPreferences.getString("registered_id", null)
            val registeredPassword = sharedPreferences.getString("registered_password", null)

            if (inputId == registeredId && inputPassword == registeredPassword) {
                // 로그인 저장 클릭된 경우 --> 로그인 창 갔을 때 사용자 정보 자동 불러오기
                val editor = sharedPreferences.edit()
                if (rememberMeCheckBox.isChecked) {
                    editor.putString("saved_id", inputId)
                    editor.putString("saved_password", inputPassword)
                    editor.putBoolean("remember_me", true)
                } else {
                    editor.remove("saved_id")
                    editor.remove("saved_password")
                    editor.putBoolean("remember_me", false)
                }
                editor.apply()

                // 로그인 성공 시 DashboardActivity로 이동
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish() // 로그인 화면을 종료하여 뒤로 가기 버튼을 눌렀을 때 다시 로그인 화면이 나타나지 않도록 함
            } else {
                Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
