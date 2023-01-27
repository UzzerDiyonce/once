package com.example.once

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailFeedActivity : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    var currentUserUid: String? = null
    var feedDTO: ArrayList<FeedDTO> = arrayListOf()
    var contentUidList: ArrayList<String> = arrayListOf()

    //피드 변수 가져오기
    var contentUid: String? = null
    var destinationUid: String? = null

    lateinit var detailBackBtn: ImageButton
    lateinit var detailLikeBtn: Button
    lateinit var detailCommentBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_feed)

        //초기화
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        detailCommentBtn = findViewById(R.id.detailCommentBtn)
        detailLikeBtn = findViewById(R.id.detailLikeBtn)
        detailBackBtn = findViewById(R.id.detailBackBtn)

        //피드 변수 가져오기
        contentUid = intent.getStringExtra("contentUid")
        destinationUid = intent.getStringExtra("destinationUid")

        //뒤로가기 버튼
        detailBackBtn.setOnClickListener {
            onBackPressed()
        }
        
        //댓글창 버튼
        detailCommentBtn.setOnClickListener { v->
            var intent = Intent(v.context, CommentActivity::class.java)
            intent.putExtra("contentUid", contentUid)
            intent.putExtra("destinationUid", destinationUid)
            startActivity(intent)
        }
    }
    //메인 돌아가기 함수
    override fun onBackPressed() {
        super.onBackPressed()
    }
}