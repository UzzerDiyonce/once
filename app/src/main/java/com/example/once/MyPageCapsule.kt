package com.example.once

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import com.example.once.databinding.MypageCapsuleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPageCapsule : Fragment() {
    private var firestore: FirebaseFirestore? = null
    var uid = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var bind : MypageCapsuleBinding

    lateinit var mainActivity : MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mypage_capsule, container, false)
        bind = MypageCapsuleBinding.inflate(layoutInflater)

        //파이어스토어 초기화
        firestore = FirebaseFirestore.getInstance()

        //툴바 관련 설정 --->
        var toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.searchMenu -> {
                    //검색 버튼 눌렀을 때
                    Log.d("Toolbar_item: ", "검색 클릭")
                    startActivity(Intent(context, SearchActivity::class.java))
                    true
                }
                R.id.alaramMenu -> {
                    //알림 버튼 눌렀을 때
                    Log.d("Toolbar_item: ", "알림 클릭")
                    startActivity(Intent(context, AlaramAcitivity::class.java))
                    true
                }
                else -> false
            }
        }

        var setBtn: ImageButton = view.findViewById(R.id.mypageSettingBtn)
        setBtn.setOnClickListener{
            //편집 버튼 눌렀을 때
            Log.d("GotoSettingAct: ", "편집 클릭")
            startActivity(Intent(context, SettingActivity::class.java))
        }

        var diaryBtn: Button = view.findViewById(R.id.mypage_diaryButton)
        diaryBtn.setOnClickListener {
            // 내 타임캡슐 버튼 눌렀을 때
            Log.d("GotoDiaryFrag: ", "편집 클릭")
            mainActivity.loadFragment(MyPage())
        }

        return view
    }
}