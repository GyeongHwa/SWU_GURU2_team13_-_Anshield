package com.android.a13app

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.a13app.databinding.FragmentCalculateBinding
import java.util.Vector

class CalculateFragment : Fragment() {
    //DB연동
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var binding: FragmentCalculateBinding

    lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalculateBinding.inflate(inflater, container, false)

        // 로그인 및 모임 정보 가져오기
        val login_id = arguments?.getString("ID")
        val login_name = arguments?.getString("NAME")
        val token = arguments?.getString("TOKEN")
        val groupName: String = arguments?.getString("G_NAME").toString()

        // 액션바 제목을 모임이름으로
        val actionBar = (activity as ParentActivity?)!!.supportActionBar
        actionBar.run {
            this!!.title = groupName
            setDisplayHomeAsUpEnabled(true)
        }

        // 모임 멤버별 지출 금액 가져오기
        val members = Vector<Calculate>()
        val memberScale = Vector<CalculateScale>()

        dbManager = DBManager(requireContext(), DBManager.DB_NAME, null, 1)
        sqlitedb = dbManager.readableDatabase

        val cursor: Cursor = sqlitedb.rawQuery(
            "SELECT tb_account.id, tb_account.name FROM tb_account JOIN tb_member ON tb_account.id = tb_member.id WHERE tb_member.token = ?",
            arrayOf(token)
        )

        while (cursor.moveToNext()) {
            val memberId = cursor.getString(0)
            val memberName = cursor.getString(1)

            val cursor2: Cursor = sqlitedb.rawQuery(
                "SELECT COALESCE(SUM(expense), 0) FROM tb_expense WHERE token = '$token' AND payer = '$memberId'", null
            )

            while (cursor2.moveToNext()) {
                val expenseAmount = cursor2.getDouble(0)
                members.add(Calculate(memberId, memberName, expenseAmount, 0.0))
            }

            cursor2.close()
        }

        cursor.close()
        sqlitedb.close()

        // RecyclerView 초기화
        binding!!.calculateRecyclerView.layoutManager = LinearLayoutManager(context)

        // CalculateAdapter 생성 및 연결
        val adapter = CalculateAdapter(requireContext(), members)
        binding!!.calculateRecyclerView.adapter = adapter


        // 총 지출 금액 계산
        val totalSpent = members.sumByDouble { it.expense }

        // 평균 금액 계산
        val average = totalSpent / members.size

        // 차액 계산
        for (item in members) {
            item.difference = item.expense - average
        }

        // 정산 결과를 출력
        for ( i in 0..(members.size - 1)) {
            for ( j  in 0..(members.size - 1)) {

                var payer = members[i]
                var receiver = members[j]

                if (payer.difference < 0 && receiver.difference > 0) {
                    var amount = Math.min(Math.abs(payer.difference), Math.abs(receiver.difference))

                    payer.difference = payer.difference + amount
                    receiver.difference = receiver.difference - amount

                    memberScale.add(CalculateScale(payer.name, receiver.name, amount))
                }
            }
        }

        // RecyclerView 초기화
        binding!!.calculateScaleRecyclerView.layoutManager = LinearLayoutManager(context)

        // CalculateAdapter 생성 및 연결
        val adapterScale = CalculateScaleAdapter(requireContext(), memberScale)
        binding!!.calculateScaleRecyclerView.adapter = adapterScale

        Toast.makeText(requireContext(), login_name + "님, 즐거운 정산 되세요!", Toast.LENGTH_SHORT).show()

        dbManager.close()

        return binding.root
    }
}