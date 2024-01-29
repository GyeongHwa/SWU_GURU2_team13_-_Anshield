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

    lateinit var login_id: String
    lateinit var login_name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        login_id = intent.getStringExtra("ID").toString()
        login_name = intent.getStringExtra("NAME").toString()

        tvName = findViewById(R.id.tvName)
        btnYes = findViewById(R.id.btnYes)
        btnNo = findViewById(R.id.btnNo)

        tvName.text = login_name + tvName.text

        btnYes.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnNo.setOnClickListener {
            intent = Intent(this, ParentActivity::class.java)
            intent.putExtra("ID", login_id)
            intent.putExtra("NAME", login_name)
            startActivity(intent)
        }
    }
}