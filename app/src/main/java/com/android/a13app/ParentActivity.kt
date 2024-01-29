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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.app_name)
        toggle.syncState()

        title = "우정!"

        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(R.id.rootLayout, homeFragment).commit()
        createFragment = CreateFragment()
        joinFragment = JoinFragment()

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabHome -> {
                    supportFragmentManager.beginTransaction().replace(R.id.rootLayout, homeFragment).commit()
                }
                R.id.tabCreate -> {
                    supportFragmentManager.beginTransaction().replace(R.id.rootLayout, createFragment).commit()
                }
                R.id.tabJoin -> {
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