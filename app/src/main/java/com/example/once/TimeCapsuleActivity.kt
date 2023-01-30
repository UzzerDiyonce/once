package com.example.once

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class TimeCapsuleActivity : AppCompatActivity() {
    //상단 바
    lateinit var backBtn: ImageButton

    //본문: 상단
    lateinit var dateView: Button

    //본문: 이미지 삽입
    lateinit var galleryBtn: Button
    lateinit var drawingBtn: Button
    lateinit var imgView: ImageView
    //lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    //본문: 글

    //하단: 완료 버튼

    private var REQUEST_READ_EXTERNAL_STORAGE = 1000

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        backBtn = findViewById(R.id.leftArrowBtn)

        dateView = findViewById(R.id.dateView)

        galleryBtn = findViewById(R.id.galleryBtn)
        drawingBtn = findViewById(R.id.drawing_Btn)
        imgView = findViewById(R.id.imgView)

        //날짜 선택
        val calendar = Calendar.getInstance()
        val datePick = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(calendar)
        }

        //뒤로가기 버튼 눌렀을 때 메인 액티비티로 돌아감
        backBtn.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //날짜 선택 버튼을 눌렀을 때 팝업창 띄움
        dateView.setOnClickListener{
            DatePickerDialog(this, datePick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        galleryBtn.setOnClickListener {
            checkGallAuthority()
            selectGallery()
        }

        drawingBtn.setOnClickListener {
            val intent = Intent(this, DrawingActivity::class.java);
            startActivity(intent)
        }
    }

    //날짜 출력
    private fun updateDate(calendar: Calendar){
        val myFormat = "YYYY년 MM월 DD일 E요일"
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                //부여 되었다면 갤러리 호출
                selectGallery()
            }
            else {
                //처음 권한 요청
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE)
            }
    }

    private fun selectGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_READ_EXTERNAL_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            imageUri = data?.data
            imgView.setImageURI(imageUri)
        }
    }
}