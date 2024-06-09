package com.example.ruok_workers

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signUpButton = findViewById<Button>(R.id.signup_button)
        signUpButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        dbManager = DBManager(this, "RUOKsample", null, 1)

        //데이터 삽입 테스트
        sqlitedb = dbManager.writableDatabase
        sqlitedb.execSQL("INSERT INTO welfare_facilities VALUES (101, '다시서기종합지원센터', '서울 용산구 한강대로92길 6 갈월동빌딩');")
        sqlitedb.close()

        //데이터 조회 테스트
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM welfare_facilities;", null)
        if(cursor.moveToNext()) {
            val wf_num = cursor.getInt(cursor.getColumnIndex("wf_num")).toString() + "\n"
            val wf_name = cursor.getString(cursor.getColumnIndex("wf_name")).toString() + "\n"
            val wf_addr = cursor.getString(cursor.getColumnIndex("wf_addr")).toString()
            var selected_data = wf_num + wf_name + wf_addr
            Toast.makeText(this, selected_data, Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()
    }
}
