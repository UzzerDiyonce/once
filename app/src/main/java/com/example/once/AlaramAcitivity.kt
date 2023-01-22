package com.example.once

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_alaram.view.*

class AlaramAcitivity : AppCompatActivity() {

    lateinit var backBtn: ImageButton
    lateinit var AlaramlistView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alaram_acitivity)

        //메인페이지로 돌아가기 버튼
        backBtn = findViewById(R.id.leftArrowBtn)
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        //recycler뷰 관련 설정
        AlaramlistView = findViewById(R.id.AlaramlistView)
        AlaramlistView.adapter = RecyclerViewAdapter()
        AlaramlistView.layoutManager = LinearLayoutManager(this)
    }
    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var alarmDTOList: ArrayList<AlarmDTO> = arrayListOf()

        init {
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            FirebaseFirestore.getInstance().collection("alarms").whereEqualTo("destinationUid", uid)
                .addSnapshotListener { value, error ->
                    alarmDTOList.clear()
                    if(value == null) return@addSnapshotListener

                    for(snapshot in value.documents){
                        alarmDTOList.add(snapshot.toObject(AlarmDTO::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_alaram, parent, false)
            return CustomViewHolder(view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder.itemView

            FirebaseFirestore.getInstance().collection("profileImages").document(alarmDTOList[position].uid!!).get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val url = task.result!!["image"]
                        Glide.with(view.context).load(url).apply(RequestOptions().circleCrop()).into(view.findViewById(R.id.alarm_profile))
                    }
                }

            when(alarmDTOList[position].kind){
                0 -> {
                    val str_0 = alarmDTOList[position].userId + " 님이 좋아요를 눌렀습니다."
                    view.alarm_comment.text = str_0
                }
                1 -> {
                    val str_0 = alarmDTOList[position].userId + " 님이 댓글을 남겼습니다."
                    view.alarm_comment.text = str_0
                }
                2 -> {
                    val str_0 = alarmDTOList[position].userId + " 님이 팔로우합니다."
                    view.alarm_comment.text = str_0
                }
            }
            view.alarm_comment.visibility = View.INVISIBLE
        }

        override fun getItemCount(): Int {
            return alarmDTOList.size
        }

    }
}