package com.android.a13app

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.a13app.databinding.ExpensecardItemBinding
import com.android.a13app.databinding.GroupItemBinding
import java.util.Vector

class ExpenseCardAdapter(private val context: Context, private val items: Vector<ExpenseCard>) : RecyclerView.Adapter<ExpenseCardAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ExpensecardItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvExpensePerson.text = item.expensePerson
        holder.binding.tvExpenseMomey.text = item.expenseMomey
        holder.binding.tvExpensePlace.text = item.expensePlace
        holder.binding.tvExpensedDate.text = item.expenseDate
    }

    inner class ViewHolder(var binding: ExpensecardItemBinding) : RecyclerView.ViewHolder(binding.root)
}