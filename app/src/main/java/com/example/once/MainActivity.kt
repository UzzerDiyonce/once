package com.example.once

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(Fragment1())
        bottomNav = findViewById(R.id.bottomNavi) as BottomNavigationView

        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.item_feed -> {
                    loadFragment(Fragment1())
                    true
                }
                R.id.item_record -> {
                    loadFragment(Fragment2())
                    true
                }
                R.id.item_mypage -> {
                    loadFragment(Fragment3())
                    true
                }
                R.id.item_search -> {
                    loadFragment(Fragment4())
                    true
                }
                else -> {
                    true
                }
            }
        }

    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.commit()
    }
}