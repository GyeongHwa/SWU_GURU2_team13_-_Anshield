package com.android.a13app

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.a13app.databinding.MemberItemBinding
import java.util.Vector

class MemberAdapter(private val context: Context, private val items: Vector<Member>) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MemberItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //리사이클러뷰 아이템에 멤버 이름 출력
        val item = items[position]
        holder.binding.btnName.text = item.name
    }

    inner class ViewHolder(var binding: MemberItemBinding) : RecyclerView.ViewHolder(binding.root)
}