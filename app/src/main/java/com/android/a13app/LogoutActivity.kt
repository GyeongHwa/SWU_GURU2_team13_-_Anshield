package com.android.a13app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LogoutActivity : AppCompatActivity() {
    lateinit var tvName: TextView
    lateinit var btnYes: Button
    lateinit var btnNo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        //로그인 정보 가져오기
        val login_id = intent.getStringExtra("ID").toString()
        val login_name = intent.getStringExtra("NAME").toString()

        tvName = findViewById(R.id.tvName)
        btnYes = findViewById(R.id.btnYes)
        btnNo = findViewById(R.id.btnNo)

        tvName.text = login_name + tvName.text

        //로그아웃 '네' 버튼 클릭 시 로그아웃
        btnYes.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //로그아웃 '아니오' 버튼 클릭 시 다시 홈으로 이동
        btnNo.setOnClickListener {
            intent = Intent(this, ParentActivity::class.java)
            intent.putExtra("ID", login_id)
            intent.putExtra("NAME", login_name)
            startActivity(intent)
        }
    }
}