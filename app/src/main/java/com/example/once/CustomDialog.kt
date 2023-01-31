package com.example.once

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.once.databinding.DialogLayoutBinding
import kotlinx.android.synthetic.main.dialog_layout.*

class CustomDialog : DialogFragment() {
    private var _binding: DialogLayoutBinding? = null
    private val binding get() = _binding
    var len: Int = 0
    lateinit var friendTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //풀스크린으로 다이얼로그 설정
        setStyle(STYLE_NO_TITLE, R.style.dialog_fullscreen)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogLayoutBinding.inflate(inflater, container, false)
        val view = binding?.root

        //val _view = inflater.inflate(R.layout.dialog_layout, container, false)
        friendTxt = view!!.findViewById(R.id.dialogFriend)

        //함꼐한 친구 데이터 받기
        val d_friends = arguments?.getStringArrayList("d_friends")
        len = d_friends!!.size
        friendTxt.text = ""
        for(i in 0..len - 1)
        {
            Log.d("함께한 친구", d_friends!![i])
            friendTxt.text = friendTxt.text.toString() + d_friends!![i] + "\n"
        }

        //레이아웃 배경 투명
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //버튼 눌렀을 때
        binding?.dialogBack?.setOnClickListener {
            dismiss() //닫기
        }

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.dialog_layout, container, false)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}