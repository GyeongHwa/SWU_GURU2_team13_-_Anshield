package com.android.a13app

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.a13app.databinding.GroupItemBinding
import java.util.Vector

class GroupAdapter(private val context: Context, private val items: Vector<Group>) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GroupItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvGroupName.text = item.groupName
        holder.binding.tvMembers.text = item.members
    }

    inner class ViewHolder(var binding: GroupItemBinding) : RecyclerView.ViewHolder(binding.root)
}