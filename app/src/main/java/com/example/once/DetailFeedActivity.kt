package com.example.once

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            Log.d("목적지Uid: ", destinationUid.toString())
            startActivity(intent)
        }

        //도장(좋아요) 버튼
        detailLikeBtn.setOnClickListener {
            likeEvent()
        }
    }
    //메인 돌아가기 함수
    override fun onBackPressed() {
        super.onBackPressed()
    }
    //도장(좋아요) 함수
    fun likeEvent() {
        var tsDoc = firestore?.collection("feed")?.document(contentUid.toString())
        firestore?.runTransaction { transaction ->
            var feedDTO = transaction.get(tsDoc!!).toObject(FeedDTO::class.java)

            //이미 좋아요 한 경우
            if(feedDTO!!.likers.containsKey(uid)) {
                feedDTO?.likeCount = feedDTO?.likeCount!! - 1
                feedDTO?.likers!!.remove(uid)
                Log.d("좋아요 취소 경우:", feedDTO?.likeCount.toString())
            }
            //아니면 좋아요
            else {
                feedDTO?.likeCount = feedDTO?.likeCount!! + 1
                feedDTO?.likers!![uid.toString()] = true
                //likeAlarm(feedDTO.uid!!)
                likeAlarm(destinationUid!!)
                Log.d("좋아요 누른 경우:", feedDTO?.likeCount.toString())
            }
            transaction.set(tsDoc, feedDTO)
        }
    }
    //좋아요 알림
    fun likeAlarm(destinationUid: String) {
        val alarmDTO = AlarmDTO()
        alarmDTO.fromUid = destinationUid
        alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
        alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
        alarmDTO.kind = 0
        alarmDTO.timestamp = System.currentTimeMillis()
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
//        var message = FirebaseAuth.getInstance()?.currentUser?.email + "님이 좋아요를 눌렀습니다."
//        FcmPush.instance.
    }
}