package com.example.once

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class CommentActivity : AppCompatActivity() {
    lateinit var commentBackBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        //메인페이지로 돌아가기 버튼
        commentBackBtn = findViewById(R.id.commentBack)
        commentBackBtn.setOnClickListener {
            onBackPressed()
        }

    }
    //메인 돌아가기 함수
    override fun onBackPressed() {
        super.onBackPressed()
    }
}