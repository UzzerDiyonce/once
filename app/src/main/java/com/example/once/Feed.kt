package com.example.once

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

        return view
    }
}