package com.android.a13app

import android.content.Intent
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.a13app.databinding.FragmentHomeBinding
import com.android.a13app.databinding.FragmentJoinBinding

class JoinFragment : Fragment() {
    lateinit var binding: FragmentJoinBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        val login_id = arguments?.getString("ID")
        val login_name = arguments?.getString("NAME")

        dbManager = DBManager(requireContext(), DBManager.DB_NAME, null, 1)

        binding.joinButton.setOnClickListener {
            val enteredToken = binding.joinEditText.text.toString()

            // 입력받은 토큰이 tb_group 테이블에 존재하는지 확인하는 함수
            fun isTokenValid(enteredToken: String): Boolean {
                val dbManager = DBManager(context, DBManager.DB_NAME, null, 1)
                sqlitedb = dbManager.readableDatabase

                val query = "SELECT * FROM tb_group WHERE token = '$enteredToken'"
                val cursor:Cursor
                cursor = sqlitedb.rawQuery(query, null)

                val isValid = cursor.count > 0

                cursor.close()
                sqlitedb.close()
                dbManager.close()

                return isValid
            }

            // tb_member 테이블에 사용자 ID와 토큰 추가하는 함수
            fun addMemberToTeam(login_id: String, enteredToken: String) {
                val dbManager = DBManager(context, DBManager.DB_NAME, null, 1)
                val sqlitedb = dbManager.writableDatabase

                sqlitedb.execSQL("INSERT INTO tb_member(id, token) VALUES('$login_id', '$enteredToken');")

                sqlitedb.close()
                dbManager.close()
            }

            // tb_team 테이블에서 입력받은 토큰과 일치하는 모임이 있는지 확인
            if (isTokenValid(enteredToken)) {
                // tb_member 테이블에 로그인 중인 사용자의 ID와 입력받은 토큰 추가
                addMemberToTeam(login_id.toString(), enteredToken)

                // 모임의 멤버가 되었습니다! 메시지 출력
                Toast.makeText(context, "모임의 멤버가 되었습니다!", Toast.LENGTH_SHORT).show()
            } else {
                // 입력한 토큰이 유효하지 않을 경우에 대한 처리
                Toast.makeText(context, "입력한 토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}
