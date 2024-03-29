package com.android.a13app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.android.a13app.databinding.ActivityParentBinding

class ParentActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityParentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //네비게이션 드로어 바인딩
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.app_name)
        toggle.syncState()

        title = "우정!"

        //첫 화면을 홈프래그먼트로 설정
        setFragment(HomeFragment(), null, null)

        //네비게이션 드로어 선택하면 해당 Fragment로 전환
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabHome -> setFragment(HomeFragment(), null, null)
                R.id.tabCreate -> setFragment(CreateFragment(), null, null)
                R.id.tabJoin -> setFragment(JoinFragment(), null, null)
            }
            binding.drawerLayout.closeDrawers()
            false
        }
    }

    //입력받은 Fragment로 화면 전환 및 데이터 전달 함수
    fun setFragment(fragment: Fragment, groupName: String?, token: String?){
        //ID와 NAME 정보가져오기
        var intent = intent
        var id = intent.getStringExtra("ID")
        var name = intent.getStringExtra("NAME")
        //bundle에 값 저장 및 전달
        var bundle = Bundle()
        bundle.putString("ID", id)
        bundle.putString("NAME", name)
        bundle.putString("G_NAME",groupName)
        bundle.putString("TOKEN", token)

        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.rootLayout, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}