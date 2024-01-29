package com.android.a13app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.a13app.databinding.FragmentCreateBinding
import com.android.a13app.databinding.FragmentCreateResultBinding

class CreateResultFragment : Fragment() {
    lateinit var binding: FragmentCreateResultBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateResultBinding.inflate(inflater, container, false)

        //모임 생성 정보 가져오기
        val groupName = arguments?.getString("G_NAME").toString()
        val token = arguments?.getString("TOKEN").toString()

        binding.tvGroupName.text = groupName + "\n생성 완료!"
        binding.tvToken.text = "멤버를 초대해보세요!\n참가 토큰 : " + token

        return binding.root
    }
}