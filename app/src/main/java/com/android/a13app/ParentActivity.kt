package com.android.a13app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.android.a13app.databinding.ActivityMainBinding
import com.android.a13app.databinding.ActivityParentBinding

class ParentActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityParentBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var createFragment: CreateFragment
    private lateinit var joinFragment: JoinFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //네비게이션 드로어 바인딩
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.app_name)
        toggle.syncState()

        title = "우정!"

        //로그인 정보 받아오기
        var intent = intent
        var login_id = intent.getStringExtra("ID")
        var login_name = intent.getStringExtra("NAME")
        //Toast.makeText(this, login_name  + ", " + login_id, Toast.LENGTH_SHORT).show()

        var bundle = Bundle()
        bundle.putString("ID", login_id)
        bundle.putString("NAME", login_name)

        homeFragment = HomeFragment()
        createFragment = CreateFragment()
        joinFragment = JoinFragment()

        homeFragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.rootLayout, homeFragment).commit()

        //네비게이션 드로어 선택하면 해당 Fragment로 전환
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabHome -> {
                    homeFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.rootLayout, homeFragment).commit()
                }
                R.id.tabCreate -> {
                    createFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.rootLayout, createFragment).commit()
                }
                R.id.tabJoin -> {
                    joinFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.rootLayout, joinFragment).commit()
                }
            }
            binding.drawerLayout.closeDrawers()
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}