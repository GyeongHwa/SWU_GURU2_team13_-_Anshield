package com.android.a13app

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.a13app.databinding.FragmentExpenseBinding
import java.util.Vector

class ExpenseFragment : Fragment() {
    lateinit var binding: FragmentExpenseBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var detailsFragment: DetailsFragment
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

        dbManager = DBManager(requireContext(), DBManager.DB_NAME, null, 1)

        //결제한 사람 드롭다운 메뉴
        sqlitedb = dbManager.readableDatabase

        var payerCursor: Cursor
        payerCursor = sqlitedb.rawQuery("SELECT id FROM tb_member WHERE token= '$token'", null)
        val items: Array<String> = Array(payerCursor.count) {index ->
            payerCursor.moveToNext()
            payerCursor.getString(payerCursor.getColumnIndexOrThrow("id")).toString()
        }
        val memberAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerPayer.adapter = memberAdapter

        binding.spinnerPayer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                payer = items[position].toString()
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

                //primary key 생성
                var cursor: Cursor
                var num: Int = 0
                cursor = sqlitedb.rawQuery("SELECT num FROM tb_expense ORDER BY num DESC", null)
                if (cursor.count != 0) {
                    cursor.moveToFirst()
                    num = cursor.getInt(cursor.getColumnIndexOrThrow("num")) + 1
                }

                sqlitedb.execSQL("INSERT INTO tb_expense VALUES ($num, '$token', '$str_payer', '$str_expense', '$str_location', '$str_date')")
                sqlitedb.close()

                Toast.makeText(requireContext(), "지출항목이 추가되었습니다", Toast.LENGTH_SHORT).show()

                var bundle = Bundle()
                bundle.putString("ID", login_id)
                bundle.putString("NAME", login_name)
                bundle.putString("TOKEN", token)

                detailsFragment = DetailsFragment()
                detailsFragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, detailsFragment).commit()
            }
        }
        dbManager.close()

        return binding.root
    }
}