package com.android.a13app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.a13app.databinding.CalculateItemBinding
import com.android.a13app.databinding.GroupItemBinding
import java.util.Vector

class CalculateAdapter(private val context: Context, private val calculateResult: Vector<Calculate>) :
    RecyclerView.Adapter<CalculateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CalculateItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    // 멤버별 총 지출 금액 출력
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = calculateResult[position]
        holder.binding.tvMemberExpense.text = "${data.name} 님의 지출 금액\n${data.expense.toInt()} 원"
    }

    override fun getItemCount(): Int {
        return calculateResult.size
    }

    inner class ViewHolder(var binding: CalculateItemBinding) : RecyclerView.ViewHolder(binding.root)
}