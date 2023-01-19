package com.example.once

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView           //하단 네비게이션 바 변수
    lateinit var diary: FloatingActionButton
    lateinit var timecapsule: FloatingActionButton

    private var REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //하단 네비게이션 바 구현
        setContentView(R.layout.activity_main)
        loadFragment(Feed())                                //처음 실행 시 피드 메뉴 띄우게 함

        bottomNav = findViewById(R.id.bottomNavi)           //하단 네비게이션 바 불러오기
        diary = findViewById(R.id.diary)
        timecapsule = findViewById(R.id.timeCapsule)

        diary.setVisibility(View.INVISIBLE)
        timecapsule.setVisibility(View.INVISIBLE)

        //앱에 이미지 접근 권한을 부여하였는지 확인
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

            //권한이 허용되지 않았다면
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //이전에 거부한 적이 있다면 권한이 필요한 이유를 설명한다.
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("갤러리 접근 권한 필요")
                dlg.setMessage("갤러리에서 사진을 첨부하고자 할 때 외부 저장소 권한이 필수로 필요합니다.")
                dlg.setPositiveButton("확인"){ dialog, which -> ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE) }
                dlg.setNegativeButton("취소", null)
                dlg.show()
            } else {
                // 처음 권한 요청
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
            }

        //네비게이션 바 메뉴 누를 시 액션
        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.item_feed -> {                         //피드 메뉴를 누르면 피드 메뉴로 넘어감
                    loadFragment(Feed())
                    true
                }
                R.id.item_record -> {                       //기록 메뉴를 누르면 그림일기, 타임캡슐 버튼 두개가 활성화 됨
                    diary.setVisibility(View.VISIBLE)
                    timecapsule.setVisibility(View.VISIBLE)
                    diary.setOnClickListener {
                        loadFragment(Diary())
                        diary.setVisibility(View.INVISIBLE)
                        timecapsule.setVisibility(View.INVISIBLE)
                    }
                    timecapsule.setOnClickListener {
                        loadFragment(TimeCapsule())
                        diary.setVisibility(View.INVISIBLE)
                        timecapsule.setVisibility(View.INVISIBLE)
                    }
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