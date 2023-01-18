package com.example.once

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = android.os.Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent) //Loagin화면을 띄운다.
            finish() //현재 액티비티 종료
        }, 2000)
    }
}