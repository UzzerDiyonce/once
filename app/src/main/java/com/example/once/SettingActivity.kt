package com.example.once

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_feed.view.*

class SettingActivity : AppCompatActivity() {
    private var firestore: FirebaseFirestore? = null
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    var googleSignInClient : GoogleSignInClient?= null
    private var fireAuth : FirebaseAuth? = null
    private lateinit var dbRef : DatabaseReference

    lateinit var backBtn: ImageButton
    lateinit var alarmBtn: ImageButton
    lateinit var alarmTxt: TextView
    public var isAlarmOn : Boolean = true
    lateinit var profImgBtn : ImageButton
    lateinit var profImg : CircleImageView
    lateinit var infoTxt : TextView
    lateinit var editInfo : EditText
    lateinit var logoutBtn: Button
    lateinit var deleteMemBtn: Button
    lateinit var saveBtn: Button
    lateinit var emailTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //파이어스토어 초기화
        firestore = FirebaseFirestore.getInstance()
        fireAuth = FirebaseAuth.getInstance()

        //데이터베이스 참조
        dbRef = FirebaseDatabase.getInstance().reference

        //변수 초기화
        var user = FirebaseAuth.getInstance().currentUser
        uid = FirebaseAuth.getInstance().currentUser?.uid
        var userid = FirebaseAuth.getInstance().currentUser?.email

        var documentRef = firestore?.collection("users")?.document(FirebaseAuth.getInstance().currentUser!!.uid)

        if(user != null){
            firestore?.runTransaction{ transaction ->
                var myDTO = transaction.get(documentRef!!).toObject(FeedDTO::class.java)

                if(myDTO?.alarmSet == null)
                {
                    isAlarmOn = true
                }
                else
                {
                    isAlarmOn = myDTO?.alarmSet!!
                }
                // 한줄소개 비어있으면 비어있는 채로, 비어있지 않으면 기존 한줄소개 내용을 불러오기
                if(myDTO?.profInfo == null)
                {
                    editInfo.setText(null)
                }
                else
                {
                    editInfo.setText(myDTO?.profInfo.toString())
                }
                //피드 프로필 이미지 가져와서 할당
                FirebaseFirestore.getInstance().collection("users").document(myDTO!!.uid!!)
                    .get().addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            val url = myDTO.profileImageUrl
                            Glide.with(this)
                                .load(url)
                                .apply(RequestOptions().circleCrop())
                                .into(profImg)
                        }
                    }
            }
        }

        //이전 화면으로 돌아가기 버튼
        backBtn = findViewById(R.id.mypageSettingBackBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        // 알림 버튼 누르면 알림 이미지 및 텍스트 바꾸기
        alarmBtn = findViewById(R.id.settingAlarmBtn)
        alarmTxt = findViewById(R.id.settingAlarmBtnText)
        alarmBtn.setOnClickListener {
            if(isAlarmOn == true) {
                alarmBtn.setImageResource(R.drawable.alarm_off)
                alarmTxt.setText("꺼짐")
                isAlarmOn = false
            }
            else {
                alarmBtn.setImageResource(R.drawable.alaram_icon)
                alarmTxt.setText("켜짐")
                isAlarmOn = true
            }
        }

        // 한줄소개 글자 수 카운트
        infoTxt = findViewById(R.id.settingInfoCnt)
        editInfo = findViewById(R.id.settingInfo)
        editInfo.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var userinput = editInfo.text.toString()
                infoTxt.text = userinput.length.toString() + "/30"
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        // 연결된 이메일 계정 불러오기
        emailTxt = findViewById(R.id.settingEmail)
        emailTxt.text = userid.toString()

        // 프로필 이미지 변경
        profImg = findViewById(R.id.setting_profImage)
        profImgBtn = findViewById(R.id.settingCamerBtn)
        // 카메라 버튼 누르면 갤러리를 불러와 프로필 사진 변경
        profImgBtn.setOnClickListener{
            initImageViewProfile()
        }

        // 구글 로그아웃을 위해 로그인 세션 가져오기
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 로그아웃
        logoutBtn = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            logout()
        }

        // 회원탈퇴
        deleteMemBtn = findViewById(R.id.memLeaveBtn)
        deleteMemBtn.setOnClickListener{
            deleteMem()
        }

        // 데이터 저장
        saveBtn = findViewById(R.id.settingSaveBtn)
        saveBtn.setOnClickListener {
            var myDTO = FeedDTO()

            myDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
            myDTO.userId = FirebaseAuth.getInstance().currentUser?.email
            myDTO.alarmSet = isAlarmOn
            myDTO.profileImageUrl = profImg.toString()
            myDTO.profInfo = editInfo.text.toString()

            // 데이터 저장
            //firestore?.collection("users")?.document(fireAuth!!.currentUser!!.uid)?.set(myDTO!!)
            firestore?.collection("users")?.document(uid.toString())?.set(myDTO!!)
//            val db = firestore?.collection("users")?.document(uid.toString())
//            db?.update("alarmSet", isAlarmOn)
//            db?.update("profInfo", editInfo)
//            db?.update("profileImageUrl", profImg)
            Toast.makeText(this, "수정되었습니다.",Toast.LENGTH_SHORT).show()
        }
    }


    // 뒤로 가기
    override fun onBackPressed() {
        super.onBackPressed()
    }

    // 로그아웃
    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        googleSignInClient?.signOut()

        var logoutIntent = Intent (this, LoginActivity::class.java)
        logoutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(logoutIntent)
    }

    // 회원탈퇴
    private fun deleteMem(){
        fireAuth?.currentUser?.delete()

        val docRef = firestore?.collection("users")?.document(uid.toString())
        googleSignInClient?.revokeAccess()?.addOnCompleteListener(this) {
        }
        logout()
    }

    // 갤러리 접근
    private fun initImageViewProfile() {
        when{
            // 갤러리 접근 권한이 있는 경우
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                navigateGallery()
            }

            // 갤러리 접근 권한이 없는 경우 & 교육용 팝업을 보여줘야 하는 경우
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                showPermissionContextPopup()
            }

            // 권한 요청 하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
            else -> requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1000
            )
        }
    }
    // 권한 요청 승인 이후 실행되는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    navigateGallery()
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //
            }
        }
    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        // 가져올 컨텐츠들 중에서 Image 만을 가져온다.
        intent.type = "image/*"
        // 갤러리에서 이미지를 선택한 후, 프로필 이미지뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용한다.
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 예외처리
        if (resultCode != Activity.RESULT_OK)
            return

        when (requestCode) {
            // 2000: 이미지 컨텐츠를 가져오는 액티비티를 수행한 후 실행되는 Activity 일 때만 수행하기 위해서
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    profImg.setImageURI(selectedImageUri)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }
}