package com.example.ruok_workers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
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

            //데이터베이스 연동: 로그인 검사
            dbManager = DBManager(this, "RUOKsample", null, 1)
            sqlitedb = dbManager.readableDatabase
            val sql = "SELECT m_num FROM member WHERE m_id=? AND m_pw=?;"
            var cursor: Cursor
            cursor = sqlitedb.rawQuery(sql, arrayOf(inputId, inputPassword))

            if (cursor.count > 0) {
                // 로그인 성공 시 DashboardActivity로 이동
                val intent = Intent(this, DashboardActivity::class.java)
                cursor.moveToNext()
                val num = cursor.getInt(cursor.getColumnIndex("m_num"))
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
