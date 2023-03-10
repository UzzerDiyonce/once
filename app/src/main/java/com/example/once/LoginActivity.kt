package com.example.once

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //google client
    private lateinit var googleSignInClient: GoogleSignInClient
    //private const val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 99
    private lateinit var btn_googleSignIn: SignInButton
    private lateinit var emailEditText: EditText
    private lateinit var pwdEditText: EditText
    private lateinit var loginBtn: Button
    private lateinit var login_layout: View
    private var REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //초기화
        login_layout = findViewById(R.id.login_layout)
        btn_googleSignIn = findViewById(R.id.btn_googleSignIn)
        emailEditText = findViewById(R.id.emailEdt)
        pwdEditText = findViewById(R.id.pwdEdt)
        loginBtn = findViewById(R.id.logBtn)

        //로그인 버튼 누르면
        loginBtn.setOnClickListener {
            var email = emailEditText.text.toString()
            var password = pwdEditText.text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password) // 회원 가입
                .addOnCompleteListener {
                        result ->
                    if(result.isSuccessful){
                        Toast.makeText(this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                        if(FirebaseAuth.getInstance().currentUser!=null){
                            var intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else if(result.exception?.message.isNullOrEmpty()){
                        Toast.makeText(this,"오류가 발생했습니다.",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        login(email,password)
                    }
                }
        }

        //logout.setOnClickListener { signOut() } //로그아웃
        //구글로그인
        btn_googleSignIn.setOnClickListener {signIn()}
        //Google 로그인 옵션 구성. requestIdToken 및 Email 요청
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //firebase auth 객체
        firebaseAuth = FirebaseAuth.getInstance()

        //카메라 및 갤러리 접근 권한 설정
        //갤러리 권한이 부여되었는지 체크
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

            //권한이 부여되지 않았다면
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //이전에 거부한 적이 있다면 설명창 띄워 다시 설정하도록 함
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("갤러리 접근 권한")
                dlg.setMessage("일기에 사진을 첨부하려면 외부 저장소 권한이 필요합니다.")
                dlg.setPositiveButton("확인"){ dialog, which -> ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE) }
                dlg.setNegativeButton("취소", null)
                dlg.show()
            } else {
                // 처음 권한 요청
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
            }

        //카메라 권한이 부여되었는지 체크
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)

            //권한이 부여되지 않았다면
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                //이전에 거부한 적이 있다면 설명창 띄워 다시 설정하도록 함
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("카메라 접근 권한")
                dlg.setMessage("일기에 사진을 첨부하려면 카메라 접근 권한이 필요합니다.")
                dlg.setPositiveButton("확인"){ dialog, which -> ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), REQUEST_READ_EXTERNAL_STORAGE) }
                dlg.setNegativeButton("취소", null)
                dlg.show()
            } else {
                // 처음 권한 요청
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), REQUEST_READ_EXTERNAL_STORAGE)
            }
    }

    fun login(email:String,password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password) // 로그인
            .addOnCompleteListener {
                    result->
                if(result.isSuccessful){
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
    }

    // onStart. 유저가 앱에 이미 구글 로그인을 했는지 확인
    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this) //구글 계정 변수
        //var feedDTO = FeedDTO()

        //계정이 등록되어있으면
        if(account != null) {
            toMainActivity(firebaseAuth.currentUser) //메인으로
//            //닉네임이 등록되어있으면
//            if(feedDTO.nickname != null) {
//                Log.d("닉네임", feedDTO.nickname.toString())
//                toMainActivity(firebaseAuth.currentUser) //메인으로
//            }
//            else {
//                toSignUpActivity(firebaseAuth.currentUser) //회원가입으로
//            }
        }
    } //onStart End

    // onActivityResult
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
    } // onActivityResult End

    // firebaseAuthWithGoogle
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + acct.id!!)

        //Google SignInAccount 객체에서 ID 토큰을 가져와서 Firebase Auth로 교환하고 Firebase에 인증
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 성공", task.exception)
                    toMainActivity(firebaseAuth?.currentUser)
                } else {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 실패", task.exception)
                    Snackbar.make(login_layout, "로그인에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }// firebaseAuthWithGoogle END

    //회원가입 액티비티로 이동
    fun toSignUpActivity(user: FirebaseUser?) {
        if(user != null) {
            var intent = Intent(this, SignupActivity::class.java)
            //startActivity(Intent(this, SignupActivity::class.java))
            //register()
            var feedDTO = FeedDTO()
            val uid = firebaseAuth.currentUser?.uid //uid 저장
            val userId = firebaseAuth.currentUser?.email //이메일 저장
            val profileImageUrl = firebaseAuth.currentUser?.photoUrl.toString() //프로필 저장
            //cloud firestore users
            //FirebaseFirestore.getInstance().collection("users")?.document(firebaseAuth!!.currentUser!!.uid)?.set(feedDTO)
            //Log.d("Save Firestore: ", "파이어스토어 저장 성공")
            //피드에 데이터 전달
            intent.putExtra("uid", uid)
            intent.putExtra("userId", userId)
            intent.putExtra("profileImageUrl", profileImageUrl)
            finish()
        }
    }
    //메인 액티비티로 이동
    fun toMainActivity(user: FirebaseUser?) {
        if(user != null) { // MainActivity 로 이동
            startActivity(Intent(this, MainActivity::class.java))
            register()
            finish()
        }
    }

    // signIn
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // signIn End

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    private fun signOut() { // 로그아웃
        // Firebase sign out
        firebaseAuth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            //updateUI(null)
        }
    }

    private fun revokeAccess() { //회원탈퇴
        // Firebase sign out
        firebaseAuth.signOut()
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
        }
    }

    //사용자 데이터 등록
    fun register() {
        var feedDTO = FeedDTO()

        feedDTO.uid = firebaseAuth.currentUser?.uid //uid 저장
        feedDTO.userId = firebaseAuth.currentUser?.email //이메일 저장
        feedDTO.profileImageUrl = firebaseAuth.currentUser?.photoUrl.toString() //프로필 저장
        //cloud firestore users
        FirebaseFirestore.getInstance().collection("users")?.document(firebaseAuth!!.currentUser!!.uid)?.set(feedDTO)
        Log.d("Save Firestore: ", "파이어스토어 저장 성공")
    }
}