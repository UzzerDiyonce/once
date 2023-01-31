package com.example.once

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import java.text.SimpleDateFormat
import java.util.*

class SetTimeCapsuleOpenDate : AppCompatActivity() {
    //상단 바
    lateinit var backBtn: ImageButton
    lateinit var dateOpenView: Button

    private var REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_time_capsule_open_date)

        backBtn = findViewById(R.id.leftArrowBtn)
        dateOpenView = findViewById(R.id.dateView)

        //날짜 선택
        val calendar = Calendar.getInstance()
        val datePick = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(calendar)
        }

        //뒤로가기 버튼 눌렀을 때 메인 액티비티로 돌아감
        backBtn.setOnClickListener{
            onBackPressed()
        }

        //날짜 선택 버튼을 눌렀을 때 팝업창 띄움
        dateOpenView.setOnClickListener{
            DatePickerDialog(this, datePick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    //날짜 출력
    private fun updateDate(calendar: Calendar){
        val myFormat = "YYYY년 MM월 DD일 E요일"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.KOREA)
        dateOpenView.setText((simpleDateFormat.format(calendar.time)))
    }
}