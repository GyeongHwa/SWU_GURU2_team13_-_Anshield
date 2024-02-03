package com.android.a13app

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.a13app.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {
    //DB연동
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var binding: FragmentCreateBinding
    lateinit var createResultFragment: CreateResultFragment //이동할 Fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        val login_id = arguments?.getString("ID")
        val login_name = arguments?.getString("NAME")

        //액션바 제목을 앱이름으로
        val actionBar = (activity as ParentActivity?)!!.supportActionBar
        actionBar!!.title = "우정!"
        actionBar.setDisplayHomeAsUpEnabled(true)

        dbManager = DBManager(requireContext(), DBManager.DB_NAME, null, 1)

        //모임생성 버튼 클릭 이벤트
        binding.btnCreate.setOnClickListener {
            var str_groupName: String = binding.edtGroupName.text.toString()
            var token: String

            if (str_groupName == "") {
                //모임 이름 미입력 시 에러 메시지 출력
                Toast.makeText(requireContext(), "모임 이름을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                //모임 이름 입력 시 DB에 저장
                sqlitedb = dbManager.writableDatabase

                //고유토큰 생성
                var cursor: Cursor
                do { //중복 확인
                    token = generateRandomString()
                    cursor = sqlitedb.rawQuery("SELECT * FROM tb_group WHERE token=?", arrayOf(token))
                } while(cursor.count != 0)

                //모임 생성(group 테이블에 그룹 삽입, 해당 group의 member로 현재 로그인한 사용자 추가)
                sqlitedb.execSQL("INSERT INTO tb_group VALUES (?, ?)", arrayOf(token, str_groupName))
                sqlitedb.execSQL("INSERT INTO tb_member(id, token) VALUES(?, ?)", arrayOf(login_id, token))
                sqlitedb.close()

                Toast.makeText(requireContext(), "모임이 생성되었습니다", Toast.LENGTH_SHORT).show()

                //모임생성결과 Fragment로 이동
                var bundle = Bundle()
                bundle.putString("ID", login_id)
                bundle.putString("NAME", login_name)
                bundle.putString("G_NAME", str_groupName)
                bundle.putString("TOKEN", token)

                createResultFragment = CreateResultFragment()
                createResultFragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, createResultFragment).commit()
            }
        }
        dbManager.close()

        return binding.root
    }

    //모임토큰 생성(랜덤 숫자 12개로 이루어진 문자열 생성)
    fun generateRandomString(): String {
        val length = 12
        val allowedChars = ('0'..'9')
        return (1..length).map { allowedChars.random() }.joinToString("")
    }
}