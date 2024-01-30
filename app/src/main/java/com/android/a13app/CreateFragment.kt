package com.android.a13app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.a13app.databinding.FragmentCreateBinding
import kotlin.random.Random

class CreateFragment : Fragment() {
    lateinit var binding: FragmentCreateBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var createResultFragment: CreateResultFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        val login_id = arguments?.getString("ID")
        val login_name = arguments?.getString("NAME")

        dbManager = DBManager(requireContext(), DBManager.DB_NAME, null, 1)

        binding.btnCreate.setOnClickListener {
            var str_groupName: String = binding.edtGroupName.text.toString()
            var token: String

            if (str_groupName == "") {
                Toast.makeText(requireContext(), "모임 이름을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                sqlitedb = dbManager.writableDatabase

                //토큰 생성
                var cursor: Cursor
                do { //중복 확인
                    token = generateRandomString()
                    cursor = sqlitedb.rawQuery("SELECT * FROM tb_group WHERE token='$token'", null)
                } while(cursor.count != 0)

                //모임 생성
                sqlitedb.execSQL("INSERT INTO tb_group VALUES ('$token', '$str_groupName')")
                sqlitedb.execSQL("INSERT INTO tb_member(id, token) VALUES('$login_id', '$token')")
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

    fun generateRandomString(): String {
        val length = 12
        val allowedChars = ('0'..'9')
        return (1..length).map { allowedChars.random() }.joinToString("")
    }
}