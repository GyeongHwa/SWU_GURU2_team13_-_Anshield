package com.android.a13app

import android.app.ActionBar
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.a13app.databinding.FragmentDetailsBinding
import java.util.Vector

class DetailsFragment : Fragment(), View.OnClickListener {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var binding: FragmentDetailsBinding
    lateinit var adapter: ExpenseCardAdapter
    lateinit var memberAdapter: MemberAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        //모임정보 가져오기
        val groupName: String = arguments?.getString("G_NAME").toString()
        val token: String = arguments?.getString("TOKEN").toString()

        //title변경
        val actionBar = (activity as ParentActivity?)!!.supportActionBar
        actionBar!!.title = groupName
        actionBar.setDisplayHomeAsUpEnabled(true)

        //tvToken 변경
        binding.tvToken.text = "참가 토큰 : " + token

        //DB연동
        dbManager = DBManager(requireContext(), DBManager.DB_NAME,null, 1)
        sqlitedb = dbManager.readableDatabase

        //지출항목목록 리사이클러뷰
        val list = Vector<ExpenseCard>()

        //지출항목 조회하는 SELECT문
        var expenseQuery: String = ""
        expenseQuery += "SELECT tb_account.name, tb_expense.expense, tb_expense.location, tb_expense.date FROM tb_expense "
        expenseQuery += "JOIN tb_account ON tb_account.id = tb_expense.payer "
        expenseQuery += "WHERE tb_expense.token='$token' "
        expenseQuery += "ORDER BY tb_expense.date DESC"
        var cursor: Cursor
        cursor = sqlitedb.rawQuery(expenseQuery, null)
        while (cursor.moveToNext()) {
            val str_payer = cursor.getString(0).toString()
            val str_expense = cursor.getString(1).toString()
            val str_location = cursor.getString(2).toString()
            val str_date = cursor.getString(3).toString()

            val item = ExpenseCard(str_payer, str_expense, str_location, str_date)
            list.add(item)
        }

        val layoutManager = LinearLayoutManager(context)
        binding!!.expenseRecyclerView.layoutManager = layoutManager

        adapter = ExpenseCardAdapter(requireContext(), list)
        binding!!.expenseRecyclerView.adapter = adapter

        //총 금액 출력
        var sumExpense: Int = 0
        for (item in list) {
            val expense = item.expenseMoney.toInt()
            sumExpense += expense
        }
        binding!!.tvSumExpense.text = "총 금액 : " + sumExpense.toString()

        //멤버 리사이클러뷰
        val memberList = Vector<Member>()

        var memberQuery = ""
        memberQuery += "SELECT tb_account.name FROM tb_account "
        memberQuery += "JOIN tb_member ON tb_account.id = tb_member.id "
        memberQuery += "WHERE tb_member.token = '$token'"
        var memberCursor: Cursor
        memberCursor = sqlitedb.rawQuery(memberQuery, null)

        while (memberCursor.moveToNext()) {
            val memberItem = Member(
                memberCursor.getString(memberCursor.getColumnIndexOrThrow("tb_account.name"))
                    .toString()
            )
            memberList.add(memberItem)
        }

        val memberLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding!!.membersRecyclerView.layoutManager = memberLayoutManager

        memberAdapter = MemberAdapter(requireContext(), memberList)
        binding!!.membersRecyclerView.adapter = memberAdapter

        //btnAddExpense클릭시 DatailsFragment에서 ExpenseFragment로 이동 및 ID,NAME전달
        binding.btnAddExpense.setOnClickListener {
            val parentActivity = activity as ParentActivity
            parentActivity.setFragment(ExpenseFragment(), groupName, token)
        }

        //btnCalculate클릭시 DatailsFragment에서 CalculateFragment로 이동 및 ID,NAME전달
        binding.btnCalculate.setOnClickListener {
            val parentActivity = activity as ParentActivity
            parentActivity.setFragment(CalculateFragment(), groupName, token)
        }

        return binding!!.root
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}