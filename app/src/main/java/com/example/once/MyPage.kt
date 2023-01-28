package com.example.once

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.mypage_diary.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import com.example.once.databinding.MypageDiaryBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.mypage_diary.view.*
import kotlinx.android.synthetic.main.item_alaram.view.*
import kotlinx.android.synthetic.main.item_mypage_list.view.*

class MyPage : Fragment() {
    private var firestore: FirebaseFirestore? = null
    var uid = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var bind : MypageDiaryBinding

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mypage_diary, container, false)
        bind = MypageDiaryBinding.inflate(layoutInflater)

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

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener(){
        val btnSequence = bind.mypageDiaryContainer.children
    }
}