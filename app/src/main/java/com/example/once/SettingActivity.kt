package com.example.once

import android.Manifest
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.TextView
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {

    lateinit var backBtn: ImageButton
    lateinit var alarmBtn: ImageButton
    lateinit var alarmTxt: TextView
    public var isAlarmOn : Boolean = true
    lateinit var profImgBtn : ImageButton

    private var REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //이전 화면으로 돌아가기 버튼
        backBtn = findViewById(R.id.mypageSettingBackBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        // 알림 버튼 누르면 알림 이미지 및 텍스트 바꾸기
        alarmBtn = findViewById(R.id.settingAlarmBtn)
        alarmTxt = findViewById(R.id.settingAlarmBtnText)
        alarmBtn.setOnClickListener {
            if(isAlarmOn == true) {
                alarmBtn.setImageResource(R.drawable.alarm_off)
                alarmTxt.setText("꺼짐")
                isAlarmOn = false
            }
            else {
                alarmBtn.setImageResource(R.drawable.alaram_icon)
                alarmTxt.setText("켜짐")
                isAlarmOn = true
            }
        }

        // 카메라 버튼 누르면 갤러리를 불러와 프로필 사진 변경
        profImgBtn.setOnClickListener{

        }
    }

    // 뒤로 가기 버튼
    override fun onBackPressed() {
        super.onBackPressed()
    }


}