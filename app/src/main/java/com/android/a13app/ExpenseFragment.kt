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
    //DB 연동
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var binding: FragmentExpenseBinding

    lateinit var mapFragment: MapFragment //이동할 프래그먼트
    lateinit var detailsFragment: DetailsFragment //이동할 프래그먼트
    lateinit var bundle: Bundle //프래그먼트 이동시 데이터 전달
    lateinit var items: Array<String> //멤버 드롭다운 메뉴에 들어갈 멤버 아이디를 담는 Array
    lateinit var payer: String //결제한 사람 아이디

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseBinding.inflate(inflater, container, false)

        //로그인 및 모임 정보 가져오기
        val login_id = arguments?.getString("ID")
        val login_name = arguments?.getString("NAME")
        val groupName = arguments?.getString("G_NAME")
        val token = arguments?.getString("TOKEN")

        //MapFragment에서 돌아왔을 때 이전 값 유지
        binding.edtExpense.setText(arguments?.getString("EXPENSE"))
        binding.edtLocation.setText(arguments?.getString("LOCATION"))
        binding.edtDate.setText(arguments?.getString("DATE"))

        //Fragment로 전달할 값
        bundle = Bundle()
        bundle.putString("ID", login_id)
        bundle.putString("NAME", login_name)
        bundle.putString("TOKEN", token)
        bundle.putString("G_NAME", groupName)

        dbManager = DBManager(requireContext(), DBManager.DB_NAME, null, 1)

        //결제한 사람 드롭다운 메뉴
        sqlitedb = dbManager.readableDatabase

        //해당 모임에 참가한 멤버 아이디 조회
        var payerCursor: Cursor
        payerCursor = sqlitedb.rawQuery("SELECT id FROM tb_member WHERE token= ?", arrayOf(token))
        items = Array(payerCursor.count) {index ->
            payerCursor.moveToNext()
            payerCursor.getString(payerCursor.getColumnIndexOrThrow("id")).toString()
        }

        //드롭다운 메뉴에 아이템 추가
        val memberAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPayer.adapter = memberAdapter

        //MapFragment에서 돌아왔을 때 이전 값 유지
        if (arguments?.getString("PAYER") != null) {
            val indexOfSelectedItem = items.indexOf(arguments?.getString("PAYER"))
            binding.spinnerPayer.setSelection(indexOfSelectedItem)
        }

        //드롭다운 메뉴에서 선택된 결제한 사람을 payer에 저장
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
        sqlitedb.close()

        //지출항목 저장
        binding.btnExAdd.setOnClickListener {
            var str_payer = payer
            var str_expense = binding.edtExpense.text.toString()
            var str_location = binding.edtLocation.text.toString()
            var str_date = binding.edtDate.text.toString()

            if (str_payer == "") {
                //결제한 사람 입력 필수
                Toast.makeText(requireContext(), "결제한 사람을 입력하세요", Toast.LENGTH_SHORT).show()
            } else if (str_expense == "") {
                //결제 금액 입력 필수
                Toast.makeText(requireContext(), "결제한 금액을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                //지출항목 테이블에 삽입
                sqlitedb = dbManager.writableDatabase

                sqlitedb.execSQL("INSERT INTO tb_expense(token, payer, expense, location, date) VALUES (?, ?, ?, ?, ?)"
                    , arrayOf(token, str_payer, str_expense, str_location, str_date))
                sqlitedb.close()

                Toast.makeText(requireContext(), "지출항목이 추가되었습니다", Toast.LENGTH_SHORT).show()

                //모임상세 프래그먼트로 이동
                val parentActivity = activity as ParentActivity
                parentActivity.setFragment(DetailsFragment(), groupName, token)
            }
        }

        //지도API 호출
        binding.btnMap.setOnClickListener {
            //사용자 입력값 저장
            bundle.putString("PAYER", payer)
            bundle.putString("EXPENSE", binding.edtExpense.text.toString())
            bundle.putString("LOCATION", binding.edtLocation.text.toString())
            bundle.putString("DATE", binding.edtDate.text.toString())

            //지도 프래그먼트로 전환
            mapFragment = MapFragment()
            mapFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, mapFragment).commit()
        }
        dbManager.close()

        return binding.root
    }
}