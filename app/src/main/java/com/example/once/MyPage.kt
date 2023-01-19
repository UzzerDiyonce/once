package com.example.once

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

class MyPage : Fragment() {

    lateinit var mypageDibtn : Button
    lateinit var mypageCapbtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //하단 네비게이션 바 구현
        setMypageFragment(MyPage_Diary())

        mypageDibtn.setOnClickListener() {
            setMypageFragment(MyPage_Diary())
        }
        mypageCapbtn.setOnClickListener(){
            setMypageFragment(MyPage_Capsule())
        }
    }

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mypage_view = inflater.inflate(R.layout.my_page, container, false)

        return mypage_view
    }

    public fun setMypageFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.mypage_container, fragment)
        transaction.commit()
    }
}