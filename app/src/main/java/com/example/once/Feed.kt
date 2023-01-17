package com.example.once

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView

class Feed : Fragment() {

    //lateinit var myHelper: myDBHelper //mainactivity에서 생성한 db이름

    lateinit var feedScrollView: ScrollView //피드 페이지의 스크롤뷰
    lateinit var feedNameText: TextView //피드 페이지의 닉네임 텍스트뷰
    lateinit var feedImageButton: ImageButton //피드 페이지의 이미지버튼
    lateinit var feedLikeButton: ImageButton //피드 페이지의 좋아요 이미지버튼
    lateinit var feedTotalLikeText: TextView //피드 페이지의 좋아요 합계 텍스트뷰

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.feed, container, false)

        feedScrollView = view.findViewById(R.id.feed_scrollView)
        feedNameText = view.findViewById(R.id.feed_nickTxt)
        feedImageButton = view.findViewById(R.id.feed_imageBtn)
        feedLikeButton = view.findViewById(R.id.feed_likeBtn)
        feedTotalLikeText = view.findViewById(R.id.feed_totalLikeTxt)
        //return inflater.inflate(R.layout.feed, container, false)

        //db연결
        //sqlDB = myHelper.readableDatabase
        //var cursor: Cursor
        //cursor = sqlDB.rawQuery("SELECT * FROM diaryTBL;", null)
        //var strNames = "닉네임" + "\r\n" + "--------" + "\r\n"
        //var strLikes = "좋아요" + "\r\n" + "--------" + "\r\n"
        //var strImage = "이미지" + "\r\n" + "--------" + "\r\n"
        //var strContents = "내용" + "\r\n" + "--------" + "\r\n"
        //while (cursor.moveToNext()) {
            //strNames += cursor.getString(0) + "\r\n"
            //strNumbers += cursor.getString(1) + "\r\n"
        //}
        //feedNameText.setText(strNames)
        //feedTotalLikeText.setText(strLikes)
        //feedImageButton.setText(strImage)
        //cursor.close()
        //sqlDB.close()

        //피드에서 일기의 이미지를 클릭하면
        feedImageButton.setOnClickListener{
            //특정 일기 fragment로 이동
        }

        return view
    }

}