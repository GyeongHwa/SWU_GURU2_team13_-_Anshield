package com.android.a13app

import android.os.Bundle
import android.util.Log
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

    lateinit var expenseFragment: ExpenseFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        //ExpenseFragment에서 입력했던 값 보존
        var bundle = Bundle()
        bundle.putString("PAYER", arguments?.getString("PAYER").toString())
        bundle.putString("EXPENSE", arguments?.getString("EXPENSE").toString())
        bundle.putString("LOCATION", arguments?.getString("LOCATION").toString())
        bundle.putString("DATE", arguments?.getString("DATE").toString())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnReturn.setOnClickListener {
            expenseFragment = ExpenseFragment()
            expenseFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, expenseFragment).commit()
        }

        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        Log.i("MAP", "onMapReady()...")
        mMap = p0

        val seoul = LatLng(37.556, 126.97)

        val markerOptions = MarkerOptions()
        markerOptions.position(seoul)
        markerOptions.title("서울")
        markerOptions.snippet("한국 수도")

        mMap.addMarker(markerOptions)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10f))
    }
}