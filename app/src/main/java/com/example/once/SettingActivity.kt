package com.example.once

import android.content.Intent
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.TextView
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {

    lateinit var backBtn: ImageButton
    lateinit var alarmBtn: ImageButton
    lateinit var alarmTxt: TextView
    public var isAlarmOn : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //이전 화면으로 돌아가기 버튼
        backBtn = findViewById(R.id.mypageSettingBackBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        // 알람 버튼 누르면 이미지 바꾸기
        alarmBtn = findViewById(R.id.settingAlarmBtn)
        alarmBtn.setOnClickListener {
            if(isAlarmOn == true) {
                alarmBtn.setImageResource(R.drawable.alarm_off)
                alarmTxt.setText("꺼짐")
            }
            else {
                alarmBtn.setImageResource(R.drawable.alaram_icon)
                alarmTxt.setText("켜짐")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}