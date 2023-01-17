package com.example.once

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView           //하단 네비게이션 바 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //하단 네비게이션 바 구현
        setContentView(R.layout.activity_main)
        loadFragment(Feed())                                //처음 실행 시 피드 메뉴 띄우게 함
        bottomNav = findViewById(R.id.bottomNavi)           //하단 네비게이션 바 불러오기

        //네비게이션 바 메뉴 누를 시 액션
        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.item_feed -> {                         //피드 메뉴를 누르면 피드 메뉴로 넘어감
                    loadFragment(Feed())
                    true
                }
                R.id.item_record -> {                       //기록 메뉴를 누르면 그림일기, 타임캡슐 버튼 두개가 활성화 됨
                    loadFragment(Record())
                    true
                }
                R.id.item_mypage -> {                       //마이페이지 아이콘을 누르면 마이페이지
                    loadFragment(MyPage())
                    true
                }
                else -> {
                    true
                }
            }
        }

    }
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.commit()
    }
}