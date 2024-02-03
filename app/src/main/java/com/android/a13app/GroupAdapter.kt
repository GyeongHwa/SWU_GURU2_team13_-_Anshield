package com.android.a13app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.a13app.databinding.GroupItemBinding
import java.util.Vector

class GroupAdapter(private val context: Context, private val items: Vector<Group>) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
    lateinit var detailsFragment: DetailsFragment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GroupItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //모임목록의 아이템 구성
        val item = items[position]
        holder.binding.tvGroupName.text = "모임명: " + item.groupName
        holder.binding.tvMembers.text = "멤버: " + item.members

        holder.binding.root.setOnClickListener {
            detailsFragment = DetailsFragment()

            var bundle = Bundle()
            bundle.putString("TOKEN", item.token)
            bundle.putString("G_NAME", item.groupName)

            detailsFragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout, detailsFragment).commit()
        }
    }

    inner class ViewHolder(var binding: GroupItemBinding) : RecyclerView.ViewHolder(binding.root)
}