package com.example.once

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_comment.view.*
import kotlinx.android.synthetic.main.feed.view.*
import kotlinx.android.synthetic.main.item_comment.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_feed.view.*
import java.text.SimpleDateFormat
import java.util.Date

class CommentActivity : AppCompatActivity() {
    lateinit var commentBackBtn: ImageButton
    lateinit var commentBtn: Button
    lateinit var commentTxt: EditText
    lateinit var commentRecyclerView: RecyclerView

    //피드 변수 코멘트에서 받기
    var contentUidForCom: String? = null
    var destinationUidForCom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        //초기화
        commentTxt = findViewById(R.id.commentText)

        //메인페이지로 돌아가기 버튼
        commentBackBtn = findViewById(R.id.commentBack)
        commentBackBtn.setOnClickListener {
            onBackPressed()
        }

        //변수 설정
        contentUidForCom = intent.getStringExtra("contentUid")
        destinationUidForCom = intent.getStringExtra("destinationUid")

        //recycler뷰 관련 설정
        commentRecyclerView = findViewById(R.id.commentRecyclerView)
        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentRecyclerView.adapter = RecyclerViewAdapter()

        //댓글쓰기 버튼
        commentBtn = findViewById(R.id.commentBtn)
        commentBtn.setOnClickListener {

            //DTO 초기화
            var feedDTO = FeedDTO()
            var comment = FeedDTO.Comment()
            comment.userId = FirebaseAuth.getInstance().currentUser?.email
            comment.uid = FirebaseAuth.getInstance().currentUser?.uid
            comment.comment = commentTxt.text.toString()
            comment.timestamp = System.currentTimeMillis()

            //db 저장
            FirebaseFirestore.getInstance().collection("feed").document(contentUidForCom!!).collection("comments").document().set(comment)
            //Log.d("contentUid: ", contentUid.toString())
            commentTxt.setText("")
        }
    }
    //돌아가기 함수
    override fun onBackPressed() {
        super.onBackPressed()
    }

    //리사이클러뷰 어댑터
    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var comments: ArrayList<FeedDTO.Comment> = arrayListOf()

        //피드 데이터 시간에 대해 오름차순으로 정렬
        init {
            FirebaseFirestore.getInstance().collection("feed")
                .document(contentUidForCom!!).collection("comments")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { value, error ->
                    comments.clear()
                    if(value == null) return@addSnapshotListener
                    for(snapshot in value.documents) {
                        comments.add(snapshot.toObject(FeedDTO.Comment::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        //뷰홀더
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
            return CustomViewHolder(view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            //데이터 할당
            var view = holder.itemView
            view.com_text.text = comments[position].comment
            view.com_nick.text = comments[position].userId

            //타임스탬프 변수, Date 형식으로 변환
            val timestamp = comments[position].timestamp //작성시간
            val currentTime = System.currentTimeMillis() //현재시간
            val timeDiffer = currentTime - timestamp!! //시간차이
            var sdf = SimpleDateFormat("h시간 전") //시간포맷
            var testTime = SimpleDateFormat("yyyy.MM.dd hh시 mm분") //테스트용 시간포맷
            var date = sdf.format(timeDiffer)
            //현재 시간과 차이가 1시간 초과일 경우
            if(timeDiffer > 3600000) {
                view.com_time.text = date
                Log.d("현재시간: ", testTime.format(currentTime))
                Log.d("작성시간: ", testTime.format(timestamp))
            }
            //그 이하일 경우
            else {
                sdf = SimpleDateFormat("m분 전")
                date = sdf.format(timeDiffer)
                view.com_time.text = date
                Log.d("현재시간: ", testTime.format(currentTime))
                Log.d("작성시간: ", testTime.format(timestamp))
            }

            //유저 데이터의 프로필 이미지 가져와서 댓글 프로필 저장
            FirebaseFirestore.getInstance().collection("users").document(comments[position].uid!!)
                .get().addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val url = task.result!!["profileImageUrl"]
                        Glide.with(holder.itemView.context).load(url)
                            .apply(RequestOptions().circleCrop())
                            .into(view.com_profile)
                    }
                }
        }

        override fun getItemCount(): Int {
            return comments.size
        }

    }
}