package com.android.a13app

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.a13app.databinding.FragmentHomeBinding
import java.util.Vector

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        val login_id = arguments?.getString("ID")
        val login_name = arguments?.getString("NAME")
        //Toast.makeText(this.context, login_id+", "+login_name, Toast.LENGTH_SHORT).show()

        binding.tvName.text = login_name + "님의 모임"

        //모임목록 리사이클러뷰
        val list = Vector<Group>()

        val item1 = Group("부산여행 가는 모임", "별하, 효림, 경화")
        val item2 = Group("1월20일 회식정산", "별하, 효림, 경화, 아림")

        list.add(item1)
        list.add(item2)

        val layoutManager = LinearLayoutManager(context)
        binding!!.recyclerView.layoutManager = layoutManager

        adapter = GroupAdapter(requireContext(), list)
        binding!!.recyclerView.adapter = adapter

        return binding!!.root
    }
}