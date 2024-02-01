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
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.a13app.databinding.FragmentHomeBinding
import java.util.Vector

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: GroupAdapter
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var login_id: String
    lateinit var login_name: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        login_id = arguments?.getString("ID").toString()
        login_name = arguments?.getString("NAME").toString()
        //Toast.makeText(this.context, login_id+", "+login_name, Toast.LENGTH_SHORT).show()

        binding.tvName.text = login_name + " 님의 모임"

        //모임목록 리사이클러뷰
        dbManager = DBManager(requireContext(), DBManager.DB_NAME,null, 1)

        sqlitedb = dbManager.readableDatabase

        //로그인한 계정이 참가한 모든 그룹 조회
        val list = Vector<Group>()
        var groupName: String = ""
        var members: String = ""
        var token: String = ""

        var groupQuery: String = ""
        groupQuery += "SELECT tb_member.token, tb_group.name "
        groupQuery += "FROM tb_member "
        groupQuery += "JOIN tb_group ON tb_member.token = tb_group.token "
        groupQuery += "WHERE tb_member.id = '$login_id' "
        groupQuery += "ORDER BY tb_member.num DESC"
        var groupCursor: Cursor
        groupCursor = sqlitedb.rawQuery(groupQuery, null)

        while (groupCursor.moveToNext()) {
            groupName = groupCursor.getString(groupCursor.getColumnIndexOrThrow("tb_group.name")).toString()
            token = groupCursor.getString(groupCursor.getColumnIndexOrThrow("tb_member.token")).toString()

            var memberQuery: String = ""
            memberQuery += "SELECT tb_account.name FROM tb_account "
            memberQuery += "JOIN tb_member ON tb_account.id = tb_member.id "
            memberQuery += "WHERE tb_member.token = '$token'"
            var memberCursor: Cursor
            memberCursor = sqlitedb.rawQuery(memberQuery, null)
            while (memberCursor.moveToNext()) {
                members += memberCursor.getString(memberCursor.getColumnIndexOrThrow("tb_account.name")).toString() + ", "
            }
            // while 루프가 끝난 후 ',' 제거
            members = members.substring(0, members.length-2)

            //리사이클러뷰 아이템 추가
            val item = Group(groupName, members, token)
            list.add(item)
            members = ""
        }

        val layoutManager = LinearLayoutManager(context)
        binding!!.recyclerView.layoutManager = layoutManager

        adapter = GroupAdapter(requireContext(), list)
        binding!!.recyclerView.adapter = adapter

        //로그아웃
        binding.btnLogout.setOnClickListener {
            var intent = Intent(requireContext(), LogoutActivity::class.java)
            intent.putExtra("ID", login_id)
            intent.putExtra("NAME", login_name)
            startActivity(intent)
        }

        return binding!!.root
    }
}