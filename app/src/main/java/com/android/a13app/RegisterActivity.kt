package com.android.a13app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

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

        dbManager = DBManager(this, DBManager.DB_NAME, null, 1)

        btnRegisterForm.setOnClickListener {
            var str_id: String = edtId.text.toString()
            var str_pw: String = edtPassword.text.toString()
            var str_name: String = edtName.text.toString()

            if (str_id == "") { //아이디 입력필수
                Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show()
            } else if (str_pw == "") { //비밀번호 입력필수
                Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            } else if (str_name == "") { //이름 입력필수
                Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                sqlitedb = dbManager.writableDatabase

                //아이디 중복확인
                var cursor: Cursor
                cursor = sqlitedb.rawQuery("SELECT * FROM tb_account WHERE id='"+str_id+"'", null)
                if (cursor.count > 0) {
                    Toast.makeText(this, "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show()
                } else if (cursor.count == 0) { //중복되는 아이디가 없으면 회원정보 DB에 INSERT
                    sqlitedb.execSQL("INSERT INTO tb_account VALUES ('$str_id', '$str_pw', '$str_name')")
                    sqlitedb.close()

                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                    //로그인 페이지로 이동
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            dbManager.close()
        }
    }
}