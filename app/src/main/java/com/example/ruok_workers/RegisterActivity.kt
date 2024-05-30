package com.example.ruok_workers

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    private val organizations = listOf("Lover Center", "Vision Center", "Apple Center")
    private val dummyIds = listOf("user1", "admin", "testuser") // 더미 데이터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signUpButton = findViewById<Button>(R.id.signUp_button2)
        signUpButton.setOnClickListener {
            val inputId = findViewById<EditText>(R.id.input_id).text.toString()
            val inputPassword = findViewById<EditText>(R.id.input_password).text.toString()
            val inputCheckPassword = findViewById<EditText>(R.id.input_check_password).text.toString()
            val inputName = findViewById<EditText>(R.id.input_workerName).text.toString() // 사용자 이름 입력
            val inputBirth = findViewById<EditText>(R.id.input_workerBirth).text.toString() // 사용자 생년월일 입력
            val inputOrganization = findViewById<EditText>(R.id.input_organizations).text.toString() // 사용자 소속 입력

            if (inputId.isBlank() || inputPassword.isBlank() || inputCheckPassword.isBlank() || inputName.isBlank() || inputBirth.isBlank() || inputOrganization.isBlank()) {
                Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (inputPassword != inputCheckPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            } else if (dummyIds.contains(inputId)) {
                Toast.makeText(this, "중복된 아이디입니다", Toast.LENGTH_SHORT).show()
            } else {
                // 회원 정보를 SharedPreferences에 저장
                val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("registered_id", inputId)
                    putString("registered_password", inputPassword)
                    putString("user_name", inputName) // 사용자 이름 저장
                    putString("user_birth", inputBirth) // 사용자 생년월일 저장
                    putString("user_organization", inputOrganization) // 사용자 소속 저장
                    apply()
                }
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
    }

    fun showSearchDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_organizations)

        val searchInput = dialog.findViewById<EditText>(R.id.search_input)
        val searchButton = dialog.findViewById<Button>(R.id.search_button)
        val searchResults = dialog.findViewById<ListView>(R.id.search_results)

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            val filteredOrganizations = organizations.filter { it.contains(query, ignoreCase = true) }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredOrganizations)
            searchResults.adapter = adapter
        }

        searchResults.setOnItemClickListener { _, _, position, _ ->
            val selectedOrganization = searchResults.getItemAtPosition(position) as String
            val inputOrganizations = findViewById<EditText>(R.id.input_organizations)
            inputOrganizations.setText(selectedOrganization)
            dialog.dismiss()
        }

        dialog.show()
    }

    fun checkIdDuplicate() {
        val inputId = findViewById<EditText>(R.id.input_id).text.toString()
        if (dummyIds.contains(inputId)) {
            showAlertDialog("중복된 아이디", "다른 아이디를 선택해주세요")
        } else {
            showAlertDialog("사용 가능", "사용 가능한 아이디입니다")
        }
    }

    fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인", null)
            .show()
    }
}
