package com.android.a13app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var edtId: EditText //아이디 입력값
    lateinit var edtPassword: EditText //비밀번호 입력값
    lateinit var btnLoginForm: Button //로그인 요청 버튼
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtId = findViewById(R.id.edtId)
        edtPassword = findViewById(R.id.edtPassword)
        btnLoginForm = findViewById(R.id.btnLoginForm)

        //DB 생성(friendshipDB)
        dbManager = DBManager(this, "friendshipDB",null, 1)

        btnLoginForm.setOnClickListener {
            //사용자 입력값 가져오기
            var str_id: String = edtId.text.toString()
            var str_pw: String = edtPassword.text.toString()

            //로그인 처리
            sqlitedb = dbManager.readableDatabase

            var cursor: Cursor
            cursor = sqlitedb.rawQuery("SELECT * FROM tb_account WHERE id='"+str_id+"' AND pw='"+str_pw+"'", null)
            if (cursor.count == 1) {
                var intent = Intent(this, ParentActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}