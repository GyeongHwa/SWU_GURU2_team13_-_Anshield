package com.android.a13app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.a13app.databinding.FragmentDetailsBinding
import java.util.Vector

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DetailsFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentDetailsBinding
    lateinit var adapter: ExpenseCardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }
    private fun setOnClickListener() {
        val btnSequence = binding.layoutDatails.children
        btnSequence.forEach { btn ->
            btn.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
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

        return binding!!.root
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.btnAddExpense -> {
                val parentActivity = activity as ParentActivity
                parentActivity.setFragment(CalculateFragment())
            }
            R.id.btnCalculate ->{
                val parentActivity = activity as ParentActivity
                parentActivity.setFragment(ExpenseFragment())
            }
        }
    }
}