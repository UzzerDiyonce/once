package com.example.once

<<<<<<< Updated upstream
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
=======
import android.os.Bundle
import android.os.PersistableBundle
>>>>>>> Stashed changes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
<<<<<<< Updated upstream
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
=======
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
>>>>>>> Stashed changes

class Diary : Fragment() {

    lateinit var uploadBtn: Button                      //이미지 업로드 버튼
    lateinit var imgView: ImageView                     //이미지 미리보기 뷰
    private var uri: String? = null                     //이미지 경로

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< Updated upstream


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.diary, container, false)
=======

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

>>>>>>> Stashed changes
    }
}