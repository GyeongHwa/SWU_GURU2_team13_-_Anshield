package com.android.a13app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    lateinit var edtId: EditText //아이디 입력값
    lateinit var edtPassword: EditText //비밀번호 입력값
    lateinit var btnLoginForm: Button //로그인 요청 버튼
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtId = findViewById(R.id.edtId)
        edtPassword = findViewById(R.id.edtPassword)
        btnLoginForm = findViewById(R.id.btnLoginForm)

        btnLoginForm.setOnClickListener {
            var intent = Intent(this, ParentActivity::class.java)
            startActivity(intent)
        }
    }
}