package com.android.a13app

import android.app.ActionBar
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
    lateinit var binding: FragmentDetailsBinding
    lateinit var adapter: ExpenseCardAdapter
    lateinit var memberAdapter: MemberAdapter
    lateinit var parentActivity: ParentActivity
    //버튼 클릭 이벤트 처리
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //btnAddExpense클릭시 DatailsFragment에서 ExpenseFragment로 이동 및 ID,NAME전달
        binding.btnAddExpense.setOnClickListener {
            val parentActivity = activity as ParentActivity
            parentActivity.setFragment(ExpenseFragment())
        }
        //btnCalculate클릭시 DatailsFragment에서 CalculateFragment로 이동 및 ID,NAME전달
        binding.btnCalculate.setOnClickListener {
            val parentActivity = activity as ParentActivity
            parentActivity.setFragment(CalculateFragment())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        //title변경
        val actionBar = (activity as ParentActivity?)!!.supportActionBar
        actionBar!!.title = "모임이름"
        actionBar.setDisplayHomeAsUpEnabled(true)

        //지출항목목록 리사이클러뷰
        val list = Vector<ExpenseCard>()

        val item1 = ExpenseCard("별하", "40,000원", "부산", "2024.01.28")
        val item2 = ExpenseCard("효림", "400,000원", "서울", "2024.01.29")
        val item3 = ExpenseCard("경화", "600,000원", "서울", "2024.01.30")

        list.add(item1)
        list.add(item2)
        list.add(item3)

        val layoutManager = LinearLayoutManager(context)
        binding!!.expenseRecyclerView.layoutManager = layoutManager

        adapter = ExpenseCardAdapter(requireContext(), list)
        binding!!.expenseRecyclerView.adapter = adapter

        //멤버 리사이클러뷰
        val memberList = Vector<Member>()

        val m_item1 = Member("별하")
        val m_item2 = Member("효림")
        val m_item3 = Member("효림")
        val m_item4 = Member("효림")
        val m_item5 = Member("효림")
        val m_item6 = Member("효림")

        memberList.add(m_item1)
        memberList.add(m_item2)
        memberList.add(m_item3)
        memberList.add(m_item4)
        memberList.add(m_item5)
        memberList.add(m_item6)

        val memberLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding!!.membersRecyclerView.layoutManager = memberLayoutManager

        memberAdapter = MemberAdapter(requireContext(), memberList)
        binding!!.membersRecyclerView.adapter = memberAdapter

        return binding!!.root
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}