package com.android.a13app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RegisterActivity : AppCompatActivity() {
    lateinit var edtId: EditText //아이디 입력값
    lateinit var edtPassword: EditText //비밀번호 입력값
    lateinit var edtName: EditText //사용자 이름 입력값
    lateinit var btnRegisterForm: Button //회원가입 요청 버튼
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtId = findViewById(R.id.edtId)
        edtPassword = findViewById(R.id.edtPassword)
        edtName = findViewById(R.id.edtName)
        btnRegisterForm = findViewById(R.id.btnRegisterForm)

        btnRegisterForm.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}