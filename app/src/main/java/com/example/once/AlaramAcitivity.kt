package com.example.once

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        backBtn = findViewById(R.id.alarmBackBtn)
        backBtn.setOnClickListener {
            onBackPressed()
            //startActivity(Intent(this, MainActivity::class.java))
        }

        //recycler뷰 관련 설정
        AlaramlistView = findViewById(R.id.AlaramlistView)
        AlaramlistView.adapter = RecyclerViewAdapter()
        AlaramlistView.layoutManager = LinearLayoutManager(this)
    }

    //메인 돌아가기 함수
    override fun onBackPressed() {
        super.onBackPressed()
    }
    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var alarmDTOList: ArrayList<AlarmDTO> = arrayListOf()

        init {
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            FirebaseFirestore.getInstance().collection("alarms").whereEqualTo("uid", uid)
                .addSnapshotListener { value, error ->
                    alarmDTOList.clear()
                    if(value == null) return@addSnapshotListener

                    for(snapshot in value.documents){
                        alarmDTOList.add(snapshot.toObject(AlarmDTO::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        //xml파일 inflate, viewHolder 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_alaram, parent, false)
            return CustomViewHolder(view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        //실제 데이터 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder.itemView

//            FirebaseFirestore.getInstance().collection("alarms").document(alarmDTOList[position].profileImage!!).get()
//                .addOnCompleteListener { task ->
//                    if(task.isSuccessful){
//                        val url = task.result!!["profileImage"]
//                        Glide.with(view.context).load(url).apply(RequestOptions().circleCrop()).into(view.findViewById(R.id.alarm_profile))
//                    }
//                }

            when(alarmDTOList[position].kind){
                0 -> {
                    val str_0 = alarmDTOList[position].fromId + "님이 일기에 도장을 찍었습니다."
                    view.alarm_comment.text = str_0
                }
                1 -> {
                    val str_0 = alarmDTOList[position].fromId + "님이 댓글을 남겼습니다."
                    view.alarm_comment.text = str_0
                }
                2 -> {
                    val str_0 = alarmDTOList[position].fromId + "님이 친구로 추가했습니다."
                    view.alarm_comment.text = str_0
                }
                3 -> {
                    val str_0 = alarmDTOList[position].fromId + "님이 회원님과 타임캡슐을 생성했습니다."
                    view.alarm_comment.text = str_0
                }
            }
            //view.alarm_comment.visibility = View.INVISIBLE
        }

        //recycler뷰 총 아이템 개수 반환
        override fun getItemCount(): Int {
            return alarmDTOList.size
        }

    }
}