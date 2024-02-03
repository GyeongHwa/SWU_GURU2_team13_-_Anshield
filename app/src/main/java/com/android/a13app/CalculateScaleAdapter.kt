package com.android.a13app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.a13app.databinding.CalculateScaleItemBinding
import java.text.NumberFormat
import java.util.Locale
import java.util.Vector

class CalculateScaleAdapter(private val context: Context, private val calculateScaleResult: Vector<CalculateScale>) :
    RecyclerView.Adapter<CalculateScaleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //리사이클러뷰 아이템 바인딩
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CalculateScaleItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    // 정산 결과 출력
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = calculateScaleResult[position]

        //정산결과 반영
        if(data.amount.toInt() != 0){
            val amount: String = NumberFormat.getNumberInstance(Locale.US).format(data.amount.toInt()).toString()
            holder.binding.tvCalculateExpense.text = "${data.payer} 님 -> ${data.receiver} 님\n${amount}원 송금해주시면 됩니다"
        } else{
            holder.binding.cvCalculateExpense.visibility = View.GONE
            holder.binding.llCalculateExpense.visibility = View.GONE
            holder.binding.tvCalculateExpense.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return calculateScaleResult.size
    }

    inner class ViewHolder(var binding: CalculateScaleItemBinding) : RecyclerView.ViewHolder(binding.root)
}