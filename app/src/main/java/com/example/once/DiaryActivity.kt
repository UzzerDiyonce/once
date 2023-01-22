package com.example.once

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DiaryActivity : AppCompatActivity() {

    lateinit var uploadBtn: Button
    lateinit var imgView: ImageView

    private var REQUEST_READ_EXTERNAL_STORAGE = 1000

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)
    }


}