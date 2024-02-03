package com.android.a13app

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.a13app.databinding.FragmentJoinBinding

class JoinFragment : Fragment() {
    //DB 연동
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var binding: FragmentJoinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        val login_id = arguments?.getString("ID")
        val login_name = arguments?.getString("NAME")

        //액션바 제목을 앱 이름으로
        val actionBar = (activity as ParentActivity?)!!.supportActionBar
        actionBar!!.title = "우정!"
        actionBar.setDisplayHomeAsUpEnabled(true)

        dbManager = DBManager(requireContext(), DBManager.DB_NAME, null, 1)

        //입력된 모임토큰에 해당하는 모임이름 출력
        binding.joinEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?:0
                if (textLength == 12) {
                    //모임이름 조회
                    sqlitedb = dbManager.readableDatabase

                    val query = "SELECT * FROM tb_group WHERE token = ?"
                    val cursorGroupName:Cursor
                    cursorGroupName = sqlitedb.rawQuery(query, arrayOf(s.toString()))

                    //해당 토큰을 가지는 모임이름 출력
                    if (cursorGroupName.count > 0) {
                        cursorGroupName.moveToFirst()
                        val gName = cursorGroupName.getString(cursorGroupName.getColumnIndexOrThrow("name")).toString()
                        binding.tvGName.text = "$gName 에\n참가하시겠습니까?"
                    } else {
                        binding.tvGName.text = "유효하지 않은 토큰입니다"
                    }

                    cursorGroupName.close()
                    sqlitedb.close()
                } else {
                    //유효하지 않은 토큰값
                    binding.tvGName.text = "유효하지 않은 토큰입니다"
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //모임참가 버튼에 대한 클릭 이벤트 처리
        binding.joinButton.setOnClickListener {
            val enteredToken = binding.joinEditText.text.toString()

            // 입력받은 토큰이 tb_group 테이블에 존재하는지 확인하는 함수
            fun isTokenValid(enteredToken: String): Boolean {
                sqlitedb = dbManager.readableDatabase

                val query = "SELECT * FROM tb_group WHERE token = ?"
                val cursor:Cursor
                cursor = sqlitedb.rawQuery(query, arrayOf(enteredToken))

                val isValid = cursor.count > 0

                cursor.close()
                sqlitedb.close()

                return isValid
            }

            //이미 모임의 멤버인지 확인
            fun isAlreadyJoined(enteredToken: String): Boolean {
                sqlitedb = dbManager.readableDatabase

                val query = "SELECT * FROM tb_member WHERE token = ? AND id = ?"
                val cursor:Cursor
                cursor = sqlitedb.rawQuery(query, arrayOf(enteredToken, login_id))

                val isJoined = cursor.count > 0

                cursor.close()
                sqlitedb.close()

                return isJoined
            }

            // tb_member 테이블에 사용자 ID와 토큰 추가하는 함수
            fun addMemberToTeam(login_id: String, enteredToken: String) {
                val sqlitedb = dbManager.writableDatabase

                sqlitedb.execSQL("INSERT INTO tb_member(id, token) VALUES(?, ?)", arrayOf(login_id, enteredToken))

                sqlitedb.close()
            }

            // tb_group 테이블에서 입력받은 토큰과 일치하는 모임이 있는지 확인
            if (isTokenValid(enteredToken) && !isAlreadyJoined(enteredToken)) {
                // tb_member 테이블에 로그인 중인 사용자의 ID와 입력받은 토큰 추가
                addMemberToTeam(login_id.toString(), enteredToken)

                // 모임의 멤버가 되었습니다! 메시지 출력
                Toast.makeText(context, "모임의 멤버가 되었습니다!", Toast.LENGTH_SHORT).show()

                val parentActivity = activity as ParentActivity
                parentActivity.setFragment(HomeFragment(), null, null)
            } else if (isAlreadyJoined(enteredToken)) {
                // 이미 모임에 참여 중입니다. 메시지 출력
                Toast.makeText(context, "이미 모임에 참여 중입니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 입력한 토큰이 유효하지 않을 경우에 대한 처리
                Toast.makeText(context, "입력한 토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        dbManager.close()

        return binding.root
    }
}
