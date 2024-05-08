package com.example.ruok_workers

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
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
        loginButton.setOnClickListener {
            // 로그인 버튼을 클릭하면 DashboardActivity로 이동
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish() // 로그인 화면을 종료하여 뒤로 가기 버튼을 눌렀을 때 다시 로그인 화면이 나타나지 않도록 함
        }
    }
}
