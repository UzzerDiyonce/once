package com.example.once

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    lateinit var signNick: TextView
    lateinit var signBtn: Button
    lateinit var signMent: TextView

    var nickList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //초기화
        signNick = findViewById(R.id.sign_nick)
        signBtn = findViewById(R.id.sign_next)
        signMent = findViewById(R.id.sign_ment)

        //닉네임 변수
        var nick = signNick.text.toString()

        //다음 버튼 눌렀을 때
        signBtn.setOnClickListener {
            if(!nickCheck(nick)) {
                nickSave(nick)
            }
            else {
                signMent.text = "닉네임은 최대 7글자이며 특수문자는 불가능합니다."
                signMent.setTextColor(Color.parseColor("#760505"))
            }
        }
    }

    private fun nickCheck (input: String): Boolean {
        val nickname = input?.trim().toString()
        val exp = Regex("^[가-힣]{1,7}$")

        Log.d("빈칸이아닌가", nickname.isNullOrEmpty().toString())
        Log.d("정규식인가", exp.matches(nickname).toString())
        return !nickname.isNullOrEmpty() && exp.matches(nickname)
    }

    private fun nickSave (nick: String) {
        var feedDTO = FeedDTO()

        feedDTO.uid = intent.getStringExtra("uid")
        feedDTO.userId = intent.getStringExtra("userId")
        feedDTO.profileImageUrl = intent.getStringExtra("profileImageUrl")
        feedDTO.nickname = nick
        FirebaseFirestore.getInstance().collection("users")?.document(FirebaseAuth.getInstance().currentUser!!.uid)?.set(feedDTO)
        Log.d("Save Firestore: ", "파이어스토어 저장 성공")

        //파이어 스토어 닉네임 목록
//        FirebaseFirestore.getInstance().collection("users")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//            //리스트 클리어
//            nickList.clear()
//            for (snapshot in querySnapshot!!.documents) {
//                if (snapshot.getString("nickname")!!.contains(nick)) {
//                    signMent.text = "이미 사용 중인 닉네임입니다."
//                    signMent.setTextColor(Color.parseColor("#760505"))
//                    feedDTO.nickname = null
//                    FirebaseFirestore.getInstance().collection("users")?.document(FirebaseAuth.getInstance().currentUser!!.uid)?.set(feedDTO)
//                }
//                else {
//                    signMent.text = "사용 가능한 닉네임입니다."
//                    signMent.setTextColor(Color.parseColor("#4C6701"))
//                    //feedDTO.nickname = nick
//                    //FirebaseFirestore.getInstance().collection("users")?.document(FirebaseAuth.getInstance().currentUser!!.uid)?.set(feedDTO)
//                }
//            }
//        }
    }
}