package com.android.a13app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.a13app.databinding.CalculateItemBinding
import com.android.a13app.databinding.GroupItemBinding
import java.text.NumberFormat
import java.util.Locale
import java.util.Vector

class CalculateAdapter(private val context: Context, private val calculateResult: Vector<Calculate>) :
    RecyclerView.Adapter<CalculateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //리사이클러뷰 아이템 바인딩
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CalculateItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    // 멤버별 총 지출 금액 출력
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = calculateResult[position]
        val expense: String = NumberFormat.getNumberInstance(Locale.US).format(data.expense.toInt()).toString()
        holder.binding.tvMemberExpense.text = "${data.name} 님의 지출 금액\n${expense}원"
    }

    override fun getItemCount(): Int {
        return calculateResult.size
    }

    inner class ViewHolder(var binding: CalculateItemBinding) : RecyclerView.ViewHolder(binding.root)
}