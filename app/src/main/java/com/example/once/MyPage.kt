package com.example.once

import android.content.Intent
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.mypage_diary.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.once.databinding.MypageDiaryBinding

class MyPage : Fragment() {

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind = MypageDiaryBinding.inflate(layoutInflater)
        val myDiary = MyPage()
        val myCapsule = MyPageCapsule()
        bind.mypageDiaryButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.mypage_diaryContainer, myDiary, MyPage::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }

        bind.mypageCapsuleButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.mypage_diaryContainer, myCapsule, MyPageCapsule::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }



        return bind.root
    }
}