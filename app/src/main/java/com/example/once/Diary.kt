package com.example.once

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File

class Diary : AppCompatActivity() {

    lateinit var uploadBtn: Button
    lateinit var imgView: ImageView

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.diary)

        uploadBtn = findViewById(R.id.drawing)
        imgView = findViewById(R.id.pic)

        uploadBtn.setOnClickListener(View.OnClickListener {
            fun onClick(){
                
            }
        });
    }
}