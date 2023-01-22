package com.example.once

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DiaryActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    //상단 바
    lateinit var backBtn: ImageButton

    //본문: 상단
    lateinit var datePickBtn: ImageButton
    lateinit var dateView: TextView

    //본문: 이미지 삽입
    lateinit var galleryBtn: Button
    lateinit var drawingBtn: Button
    lateinit var imgView: ImageView
    //본문: 글

    //하단: 완료 버튼

    private var REQUEST_READ_EXTERNAL_STORAGE = 1000

    var year = 0
    var month = 0
    var day = 0

    var savedYear = 0
    var savedMonth = 0
    var savedDay = 0

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        backBtn = findViewById(R.id.leftArrowBtn)

        datePickBtn = findViewById(R.id.dateBtn)
        dateView = findViewById(R.id.dateView)

        //뒤로가기 버튼 눌렀을 때 메인 액티비티로 돌아감
        backBtn.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //날짜 선택 버튼을 눌렀을 때 팝업창 띄움
        datePickBtn.setOnClickListener{
            getDateCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    private fun getDateCalendar(){
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedYear = year
        savedMonth = month
        savedDay = dayOfMonth

        getDateCalendar()

    }
}