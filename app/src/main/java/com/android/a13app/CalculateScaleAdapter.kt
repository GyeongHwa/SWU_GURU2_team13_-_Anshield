package com.android.a13app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.a13app.databinding.CalculateScaleItemBinding
import com.android.a13app.databinding.GroupItemBinding
import java.util.Vector

class CalculateScaleAdapter(private val context: Context, private val calculateScaleResult: Vector<CalculateScale>) :
    RecyclerView.Adapter<CalculateScaleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CalculateScaleItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    // 정산 결과 출력
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = calculateScaleResult[position]

        if(data.amount != 0.0){
            holder.binding.tvCalculateExpense.text = "${data.name} 님 -> ${data.receiver} 님: ${data.amount.toInt()} 원 송금해주시면 됩니다"
        } else{
            holder.binding.cvCalculateExpense.visibility = android.view.View.GONE
        }
    }

    override fun getItemCount(): Int {
        return calculateScaleResult.size
    }

    inner class ViewHolder(var binding: CalculateScaleItemBinding) : RecyclerView.ViewHolder(binding.root)
}