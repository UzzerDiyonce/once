package com.example.once

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class DiaryActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    //상단 바
    lateinit var backBtn: ImageButton

    //본문: 상단
    lateinit var datePickBtn: ImageButton
    lateinit var dateView: TextView

    //본문: 이미지 삽입
    lateinit var galleryBtn: Button
    lateinit var drawingBtn: Button
    lateinit var imgView: ImageView
    //본문: 글

    //하단: 완료 버튼

    private var REQUEST_READ_EXTERNAL_STORAGE = 1000

    var year = 0
    var month = 0
    var day = 0

    companion object{
        const val PARAM_KEY_IMAGE = "image"
        const val PARAM_KEY_PRODUCT_ID
    }
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        backBtn = findViewById(R.id.leftArrowBtn)

        datePickBtn = findViewById(R.id.dateBtn)
        dateView = findViewById(R.id.dateView)

        galleryBtn = findViewById(R.id.galleryBtn)
        drawingBtn = findViewById(R.id.pictureBtn)
        imgView = findViewById(R.id.imgView)

        //날짜 선택
        val calendar = Calendar.getInstance()
        val datePick = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(calendar)
        }

        //뒤로가기 버튼 눌렀을 때 메인 액티비티로 돌아감
        backBtn.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //날짜 선택 버튼을 눌렀을 때 팝업창 띄움
        datePickBtn.setOnClickListener{
            DatePickerDialog(this, datePick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        galleryBtn.setOnClickListener {
            checkGallAuthority()

        }
    }

    //날짜 출력
    private fun updateLabel(calendar: Calendar){
        val myFormat = "YYYY년\nMM월 DD일"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.KOREA)
        dateView.setText((simpleDateFormat.format(calendar.time)))
    }

    private fun checkGallAuthority() {
        //갤러리 권한이 부여되었는지 체크
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

            //권한이 없다면
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //이전에 거부한 적이 있다면 설명창 띄워 다시 설정하도록 함
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("갤러리 접근 권한")
                dlg.setMessage("일기에 사진을 첨부하려면 외부 저장소 권한이 필요합니다.")
                dlg.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE)
                }
                dlg.setNegativeButton("취소", null)
                dlg.show()
            } else {
                //처음 권한 요청
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE)
            }
        //부여 되었다면 갤러리 호출
        else
            selectGallery()
    }

    private fun selectGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }
}