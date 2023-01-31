package com.example.once

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.mypage_diary.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.feed.view.*
import kotlinx.android.synthetic.main.mypage_diary.view.*
import kotlinx.android.synthetic.main.item_alaram.view.*
import kotlinx.android.synthetic.main.item_feed.view.*
import kotlinx.android.synthetic.main.item_mypage_list.view.*
import java.text.SimpleDateFormat

class MyPage : Fragment() {
    private var firestore: FirebaseFirestore? = null
    private lateinit var dbRef : DatabaseReference
    private var fireAuth : FirebaseAuth? = null
    var fireStg : FirebaseStorage? = null

    var profUri: Uri? = null

    lateinit var profInfo : TextView
    lateinit var emailTxt : TextView
    lateinit var profImg : CircleImageView

    lateinit var mainActivity : MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mypage_diary, container, false)

        //파이어스토어 초기화
        firestore = FirebaseFirestore.getInstance()
        fireAuth = FirebaseAuth.getInstance()
        fireStg = FirebaseStorage.getInstance()

        val userInfo = arguments?.getString("userInfo")
//        Log.d("한줄소개 가져오기", userInfo.toString())

        dbRef = FirebaseDatabase.getInstance().reference

        //변수 초기화
        var uid = FirebaseAuth.getInstance().currentUser?.uid
        var userid = FirebaseAuth.getInstance().currentUser?.email

        profInfo = view.findViewById(R.id.mypage_profInfo)
        emailTxt = view.findViewById(R.id.mypage_profEmail)
        profImg = view.findViewById(R.id.mypage_profImage)

        //이메일
        emailTxt.text = userid.toString()

        var documentRef = firestore?.collection("users")?.document(fireAuth!!.currentUser!!.uid)
        firestore?.runTransaction { transaction ->
            var myDTO = transaction.get(documentRef!!).toObject(FeedDTO::class.java)

            Log.d("데이터 가져오기", userInfo.toString())
            // 한줄소개
            /*if(myDTO?.profInfo.toString() == null)
            {
                profInfo.text = null
            }
            else
            {
                // 한줄소개
                firestore?.collection("users")?.document(uid.toString())
                    ?.get()?.addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            val info = task.result!!["profInfo"]
                            profInfo.text = info.toString()
                        }
                    }
            }*/

            // 한줄소개
            firestore?.collection("users")?.document(uid.toString())
                ?.get()?.addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val info = task.result!!["profInfo"]
                        if(info.toString() == null)
                        {
                            profInfo.text = null
                        }
                        else
                        {
                            profInfo.text = info.toString()
                        }
                    }
                }



            // 프로필 이미지 가져와서 할당
            /*firestore?.collection("users")?.document(uid.toString())
                ?.get()?.addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val url = task.result!!["profileImageUrl"]
                        Glide.with(view.context)
                            .load(url)
                            .into(profImg)
                    }
                }*/
            // 프로필 이미지
            if(profUri != null)
            {
                profImg.setImageURI(profUri)
            }
            else
            {
                profImg.setImageResource(R.drawable.defaultprofimg)
            }
        }

        /*dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var myDTO = FeedDTO()
                // 한줄소개
                if(myDTO?.profInfo.toString() == null)
                {
                    profInfo.text = " "
                }
                else
                {
                    profInfo.text = myDTO?.profInfo.toString()
                    Log.d("들어왔니", profInfo.text.toString())
//                    FirebaseFirestore.getInstance().collection("users")
//                        .document(FirebaseAuth.getInstance().currentUser!!.uid).set(profInfo.text.toString())
                }

                firestore?.collection("users")?.document(uid.toString())
                    ?.get()?.addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            val info = task.result!!["profInfo"]
                            profInfo.text = info.toString()
                            val url = task.result!!["profileImageUrl"]
                            Glide.with(view.context)
                                .load(url)
                                .into(profImg)
                        }
                    }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })*/

        //recycler뷰 관련 설정
        view.mypageListView.layoutManager = LinearLayoutManager(activity)
        //view.mypageListView.adapter = RecyclerViewAdapter()

        //툴바 관련 설정 --->
        var toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.searchMenu -> {
                    //검색 버튼 눌렀을 때
                    Log.d("Toolbar_item: ", "검색 클릭")
                    startActivity(Intent(context, SearchActivity::class.java))
                    true
                }
                R.id.alaramMenu -> {
                    //알림 버튼 눌렀을 때
                    Log.d("Toolbar_item: ", "알림 클릭")
                    startActivity(Intent(context, AlaramAcitivity::class.java))
                    true
                }
                else -> false
            }
        }

        var setBtn: ImageButton = view.findViewById(R.id.mypageSettingBtn)
        setBtn.setOnClickListener{
            //편집 버튼 눌렀을 때
            Log.d("GotoSettingAct: ", "편집 클릭")
            startActivity(Intent(context, SettingActivity::class.java))
        }

        var capsuleBtn: Button = view.findViewById(R.id.mypage_capsuleButton)
        capsuleBtn.setOnClickListener {
            // 내 타임캡슐 버튼 눌렀을 때
            Log.d("GotoCapsuleFrag: ", "편집 클릭")
            mainActivity.loadFragment(MyPageCapsule())
        }

        return view
    }

    fun refresh(){
        (activity as MainActivity).loadFragment(MyPage())
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

    //리사이클러뷰 어댑터
    @SuppressLint("NotifyDataSetChanged")
    inner class RecyclerViewAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var mypageDTOList: ArrayList<FeedDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()

        //피드 데이터 시간에 대해 오름차순으로 정렬, 배열 비우기
        init {
            firestore?.collection("users")?.whereEqualTo("uid", FirebaseAuth.getInstance().currentUser?.uid)?.orderBy("timestamp", Query.Direction.ASCENDING)
                ?.addSnapshotListener { value, error ->
                    mypageDTOList.clear()
                    contentUidList.clear()
                    if(value == null) return@addSnapshotListener
                    for(snapshot in value!!.documents) {
                        var item = snapshot.toObject(FeedDTO::class.java)
                        mypageDTOList.add(item!!)
                        contentUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }

        //뷰홀더
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_mypage_list, parent, false)
            return CustomViewHolder (view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder.itemView

            // 마이페이지 컬렉션에 저장된 데이터 가져오기
            if(mypageDTOList!![position].feed_kind == 1) {
                view.feedFriend.setBackgroundResource(R.drawable.withfriedn)
            }
            view.mypageListItemTitle.text = mypageDTOList!![position].title //제목
            Glide.with(holder.itemView.context).load(mypageDTOList!![position].imageUrl)
                .into(view.mypageListImageView) //이미지 저장
            view.mypageListItemContent.text = mypageDTOList!![position].contents //내용
            //타임스탬프
            val timestamp = mypageDTOList[position].timestamp
            val sdf = SimpleDateFormat("yyyy.MM.dd hh시 mm분") //피드용 포맷
            val d_sdf = SimpleDateFormat("yyyy 년 MM 월 dd 일 E요일") //디테일뷰용 포맷
            val date = sdf.format(timestamp)
            val d_date = d_sdf.format(timestamp)
            view.mypageListItemDate.text = date

            //피드 더보기 버튼 클릭 시, 해당 피드데이터 디테일 액티비티로 전달
            view.mypageListBox.setOnClickListener { v->
                var intent = Intent(v.context, DetailFeedActivity::class.java)
                intent.putExtra("contentUid", contentUidList[position])
                intent.putExtra("destinationUid", mypageDTOList[position].uid)
                intent.putExtra("date", d_date)
                intent.putExtra("title", mypageDTOList[position].title)
                intent.putExtra("image", mypageDTOList[position].imageUrl)
                intent.putExtra("contents", mypageDTOList[position].contents)
                intent.putExtra("weather", mypageDTOList[position].weather_kind.toString())
                intent.putExtra("feedKind", mypageDTOList[position].feed_kind.toString())
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return mypageDTOList.size
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }
}