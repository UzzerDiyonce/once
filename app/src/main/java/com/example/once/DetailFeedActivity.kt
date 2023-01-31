package com.example.once

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_feed.*
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.item_feed.view.*

class DetailFeedActivity : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    var uid = FirebaseAuth.getInstance().currentUser?.uid

    //피드 변수 가져오기
    var contentUid: String? = null
    var destinationUid: String? = null
    var date: String? = null
    var title: String? = null
    var Image: String? = null
    var contents: String? = null

    lateinit var detailBackBtn: ImageButton
    lateinit var detailLikeBtn: Button
    lateinit var detailCommentBtn: Button
    lateinit var detailStamp: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_feed)

        //초기화
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        detailCommentBtn = findViewById(R.id.detailCommentBtn)
        detailLikeBtn = findViewById(R.id.detailLikeBtn)
        detailBackBtn = findViewById(R.id.detailBackBtn)
        detailStamp = findViewById(R.id.detailStamp)

        //피드 변수 가져오기
        contentUid = intent.getStringExtra("contentUid")
        destinationUid = intent.getStringExtra("destinationUid")
        date = intent.getStringExtra("date")
        title = intent.getStringExtra("title")
        Image = intent.getStringExtra("image")
        contents = intent.getStringExtra("contents")
        var weather = intent.getStringExtra("weather")?.toInt()
        var feedKind = intent.getStringExtra("feedKind")?.toInt()
//        var friends = intent.getSerializableExtra("friends") as ArrayList<String>

        detailTitleView.text = title //제목
        detailDateView.text = date //날짜
        Glide.with(this).load(Image).into(detailImageView) //이미지
        detailContentView.text = contents //내용
        //날씨
        if(weather == 0) {
            detailSun.setImageResource(R.drawable.sunny_colored)
        }
        if(weather == 1) {
            detailCloud.setImageResource(R.drawable.cloudy_colored)
        }
        if(weather == 2) {
            detailRain.setImageResource(R.drawable.rain_colored)
        }
        if(weather == 3) {
            detailSnow.setImageResource(R.drawable.snow_colored)
        }
        //피드 종류
        if(feedKind == 1) {
            var friends = intent.getSerializableExtra("friends") as ArrayList<String>

            detailFriendIcon.setImageResource(R.drawable.withfriedn)
            detailFriend.text = "함께한 친구"

            //함께한 친구 버튼을 눌렀을 때 다이얼로그 띄우기
            detailFriend.setOnClickListener {
                val dialog = CustomDialog()
                dialog.show(supportFragmentManager, "CustomDialog")

                var bundle = Bundle()
                bundle.putStringArrayList("d_friends", friends)
                dialog.arguments = bundle

//                var len = friends.size
//                for(i in 0..len-1)
//                {
//                    //Log.d("함께한 친구", friends[i])
//                    dialogFriend.text = friends[i] + "\n"
//                }
            }
        }

        //뒤로가기 버튼
        detailBackBtn.setOnClickListener {
            onBackPressed()
        }
        
        //댓글창 버튼, 댓글 액티비티로 데이터 전달
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

            //이미 좋아요 한 경우, 피드 데이터에서 like 카운트 -1, 레이아웃 수정
            if(feedDTO!!.likers.containsKey(uid)) {
                feedDTO?.likeCount = feedDTO?.likeCount!! - 1
                feedDTO?.likers!!.remove(uid)
                Log.d("좋아요 취소 경우:", feedDTO?.likeCount.toString())
                detailLikeBtn.text = "도장 꾹"
                detailLikeBtn.setTextColor(Color.parseColor("#2A1D17"))
                detailStamp.setImageBitmap(null)
            }
            //아니면 좋아요, 피드 데이터에서 like 카운트 +1, 레이아웃 수정
            else {
                feedDTO?.likeCount = feedDTO?.likeCount!! + 1
                feedDTO?.likers!![uid.toString()] = true
                //likeAlarm(feedDTO.uid!!)
                likeAlarm(destinationUid!!)
                Log.d("좋아요 누른 경우:", feedDTO?.likeCount.toString())
                detailLikeBtn.text = "눌렀다!"
                detailLikeBtn.setTextColor(Color.parseColor("#8A7267"))
                detailStamp.setImageResource(R.drawable.stamp)
            }
            transaction.set(tsDoc, feedDTO)
        }
    }
    //좋아요 알림 -> 알림액티비티에서 사용
    fun likeAlarm(destinationUid: String) {
        val alarmDTO = AlarmDTO()
        alarmDTO.fromUid = destinationUid
        alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
        alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
        alarmDTO.kind = 0
        alarmDTO.timestamp = System.currentTimeMillis()
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
    }
}