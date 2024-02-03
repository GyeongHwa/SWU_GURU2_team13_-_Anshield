package com.android.a13app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.a13app.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var binding: FragmentMapBinding

    lateinit var expenseFragment: ExpenseFragment //이동할 프래그먼트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        //ExpenseFragment에서 입력했던 값과 로그인 정보 보존
        var bundle = Bundle()
        bundle.putString("PAYER", arguments?.getString("PAYER").toString())
        bundle.putString("EXPENSE", arguments?.getString("EXPENSE").toString())
        bundle.putString("LOCATION", arguments?.getString("LOCATION").toString())
        bundle.putString("DATE", arguments?.getString("DATE").toString())

        bundle.putString("ID", arguments?.getString("ID").toString())
        bundle.putString("NAME", arguments?.getString("NAME").toString())
        bundle.putString("G_NAME", arguments?.getString("G_NAME").toString())
        bundle.putString("TOKEN", arguments?.getString("TOKEN").toString())

        //지도 불러오기
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //돌아가기 버튼에 대한 클릭 이벤트(이전 프래그먼트로 이동)
        binding.btnReturn.setOnClickListener {
            expenseFragment = ExpenseFragment()
            expenseFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, expenseFragment).commit()
        }

        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        val seoul = LatLng(37.556, 126.97)

        //초점 맞출 지점 설정
        val markerOptions = MarkerOptions()
        markerOptions.position(seoul)
        markerOptions.title("서울")
        markerOptions.snippet("한국 수도")

        mMap.addMarker(markerOptions)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10f))
    }
}