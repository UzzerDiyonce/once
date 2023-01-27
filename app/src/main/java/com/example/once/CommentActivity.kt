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
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_comment.view.*
import kotlinx.android.synthetic.main.feed.view.*
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentActivity : AppCompatActivity() {
    lateinit var commentBackBtn: ImageButton
    lateinit var commentBtn: Button
    lateinit var commentTxt: EditText
    lateinit var commentRecyclerView: RecyclerView

    var contentUid: String? = null
    //var destinationUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        //메인페이지로 돌아가기 버튼
        commentBackBtn = findViewById(R.id.commentBack)
        commentBackBtn.setOnClickListener {
            onBackPressed()
        }

        //변수 설정
        contentUid = intent.getStringExtra("contentUid")
        //destinationUid = intent.getStringExtra("destinationUid")

        //recycler뷰 관련 설정
        commentRecyclerView = findViewById(R.id.commentRecyclerView)
        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentRecyclerView.adapter = RecyclerViewAdapter()

        //댓글쓰기 버튼
        commentBtn = findViewById(R.id.commentBtn)
        commentBtn.setOnClickListener {

            //DTO 초기화
            var comment = FeedDTO.Comment()
            comment.userId = FirebaseAuth.getInstance().currentUser?.email
            comment.uid = FirebaseAuth.getInstance().currentUser?.uid
            comment.comment = commentTxt.text.toString()
            comment.timestamp = System.currentTimeMillis()

            //db 저장
            FirebaseFirestore.getInstance().collection("feed").document(contentUid!!).collection("comments").document().set(comment)
            //Log.d("contentUid: ", contentUid.toString())
            commentTxt.setText("")
        }
    }
    //메인 돌아가기 함수
    override fun onBackPressed() {
        super.onBackPressed()
    }

    //리사이클러뷰 어댑터
    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var comments: ArrayList<FeedDTO.Comment> = arrayListOf()

        init {
            FirebaseFirestore.getInstance().collection("feed")
                .document(contentUid!!).collection("comments")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    comments.clear()
                    if(value == null) return@addSnapshotListener
                    for(snapshot in value.documents) {
                        comments.add(snapshot.toObject(FeedDTO.Comment::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
            return CustomViewHolder(view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder.itemView
            view.com_text.text = comments[position].comment
            view.com_nick.text = comments[position].userId

            FirebaseFirestore.getInstance().collection("users")
                .document(comments[position].uid!!).get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val url = task.result!!["profileImage"]
                        Glide.with(holder.itemView.context).load(url).apply(RequestOptions()).into(view.com_profile)
                    }
                }
        }

        override fun getItemCount(): Int {
            return comments.size
        }

    }
}