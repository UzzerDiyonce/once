package com.example.once

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailFeedActivity : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    var currentUserUid: String? = null
    var feedDTO: ArrayList<FeedDTO> = arrayListOf()
    var contentUidList: ArrayList<String> = arrayListOf()

    lateinit var detailLikeBtn: Button
    lateinit var detailCommentBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_feed)

        //초기화
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        //댓글창 버튼
        detailCommentBtn.setOnClickListener { 

        }
    }
}