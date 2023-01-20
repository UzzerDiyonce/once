package com.example.once

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class AlaramAcitivity : AppCompatActivity() {

    lateinit var backBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alaram_acitivity)

        backBtn = findViewById(R.id.leftArrowBtn)
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}