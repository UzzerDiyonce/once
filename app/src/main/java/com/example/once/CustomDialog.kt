package com.example.once


import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.once.databinding.DialogLayoutBinding

class CustomDialog : DialogFragment() {
    private var _binding: DialogLayoutBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog_fullscreen)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogLayoutBinding.inflate(inflater, container, false)
        val view = binding?.root

        //레이아웃 배경 투명
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //버튼 눌렀을 때
        binding?.dialogBack?.setOnClickListener {
            dismiss()
        }

        //binding?.dialogFriend?.text =

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.dialog_layout, container, false)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}