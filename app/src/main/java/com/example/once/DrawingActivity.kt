package com.example.once

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.once.CanvasView.Companion.currentColor

class DrawingActivity : AppCompatActivity() {

    //상단 바
    lateinit var backBtn: ImageButton

    //본문: 상단
    lateinit var galleryBtn: Button
    lateinit var pencilBtn: RadioButton
    lateinit var eraserBtn: RadioButton
    lateinit var drawingView: LinearLayout

    private var imageUri: Uri? = null

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private var REQUEST_READ_EXTERNAL_STORAGE = 1000

    companion object {
        var path = Path()
        var paintBrush = Paint()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val canvasView = CanvasView(this)
        setContentView(R.layout.activity_drawing)

        backBtn = findViewById(R.id.leftArrowBtn)
        galleryBtn = findViewById(R.id.galleryBtn)
        pencilBtn = findViewById(R.id.pencilBtn)
        eraserBtn = findViewById(R.id.eraserBtn)

        drawingView = findViewById(R.id.drawing_field)

        //갤러리에서 사진 선택
        galleryBtn.setOnClickListener {
            checkGallAuthority()
            selectGallery()
        }

        //뒤로 가기 버튼
        backBtn.setOnClickListener {
            onBackPressed()
        }
        //그리기 버튼
        pencilBtn.setOnClickListener {
            Toast.makeText(this, "그리기", Toast.LENGTH_SHORT).show()
            usingBrush(paintBrush.color)
        }
        //지우기 버튼
        eraserBtn.setOnClickListener {
            Toast.makeText(this, "지우기", Toast.LENGTH_SHORT).show()
        }
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
        }
    }

    private fun usingBrush(color: Int){
        currentColor = color
        path = Path()
    }
}
