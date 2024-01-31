package com.android.a13app

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.a13app.databinding.FragmentExpenseBinding

class ExpenseFragment : Fragment() {
    lateinit var binding: FragmentExpenseBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var bundle: Bundle
    lateinit var mapFragment: MapFragment
    lateinit var detailsFragment: DetailsFragment
    lateinit var items: Array<String>
    lateinit var payer: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseBinding.inflate(inflater, container, false)

        //로그인 및 모임 정보 가져오기
        val login_id = arguments?.getString("ID")
        val login_name = arguments?.getString("NAME")
        //val token = arguments?.getString("TOKEN")
        val token = "890123456789"

        //MapFragment에서 돌아왔을 때 이전 값 유지
        binding.edtExpense.setText(arguments?.getString("EXPENSE"))
        binding.edtLocation.setText(arguments?.getString("LOCATION"))
        binding.edtDate.setText(arguments?.getString("DATE"))

        //Fragment로 전달할 값
        bundle = Bundle()
        bundle.putString("ID", login_id)
        bundle.putString("NAME", login_name)
        bundle.putString("TOKEN", token)

        dbManager = DBManager(requireContext(), DBManager.DB_NAME, null, 1)

        //결제한 사람 드롭다운 메뉴
        sqlitedb = dbManager.readableDatabase

        var payerCursor: Cursor
        payerCursor = sqlitedb.rawQuery("SELECT id FROM tb_member WHERE token= '$token'", null)
        items = Array(payerCursor.count) {index ->
            payerCursor.moveToNext()
            payerCursor.getString(payerCursor.getColumnIndexOrThrow("id")).toString()
        }
        val memberAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerPayer.adapter = memberAdapter

        if (arguments?.getString("PAYER") != null) {
            val indexOfSelectedItem = items.indexOf(arguments?.getString("PAYER"))
            binding.spinnerPayer.setSelection(indexOfSelectedItem)
        }

        binding.spinnerPayer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                payer = items[position]
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        //지출항목 저장
        binding.btnExAdd.setOnClickListener {
            var str_payer = payer?:null
            var str_expense = binding.edtExpense.text.toString()
            var str_location = binding.edtLocation.text.toString()
            var str_date = binding.edtDate.text.toString()

            if (str_payer == "") {
                Toast.makeText(requireContext(), "결제한 사람을 입력하세요", Toast.LENGTH_SHORT).show()
            } else if (str_expense == "") {
                Toast.makeText(requireContext(), "결제한 금액을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                sqlitedb = dbManager.writableDatabase

                sqlitedb.execSQL("INSERT INTO tb_expense(token, payer, expense, location, date) VALUES ('$token', '$str_payer', $str_expense, '$str_location', '$str_date')")
                sqlitedb.close()

                Toast.makeText(requireContext(), "지출항목이 추가되었습니다", Toast.LENGTH_SHORT).show()

                detailsFragment = DetailsFragment()
                detailsFragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, detailsFragment).commit()
            }
        }

        //지도API 호출
        binding.btnMap.setOnClickListener {
            //사용자 입력값 저장
            bundle.putString("PAYER", payer)
            bundle.putString("EXPENSE", binding.edtExpense.text.toString())
            bundle.putString("LOCATION", binding.edtLocation.text.toString())
            bundle.putString("DATE", binding.edtDate.text.toString())

            mapFragment = MapFragment()
            mapFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, mapFragment).commit()
        }
        dbManager.close()

        return binding.root
    }
}