package com.example.once

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.once.databinding.MypageCapsuleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.item_feed.view.*
import kotlinx.android.synthetic.main.item_mypage_list.view.*
import kotlinx.android.synthetic.main.mypage_diary.view.*
import java.text.SimpleDateFormat

class MyPageCapsule : Fragment() {
    private var firestore: FirebaseFirestore? = null
    var uid = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var bind : MypageCapsuleBinding

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
        val view = inflater.inflate(R.layout.mypage_capsule, container, false)
        bind = MypageCapsuleBinding.inflate(layoutInflater)

        //파이어스토어 초기화
        firestore = FirebaseFirestore.getInstance()

        //변수 초기화
        uid = FirebaseAuth.getInstance().currentUser?.uid

        //recycler뷰 관련 설정
        view.mypageListView.layoutManager = LinearLayoutManager(activity)
        view.mypageListView.adapter = RecyclerViewAdapter()

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

        var diaryBtn: Button = view.findViewById(R.id.mypage_diaryButton)
        diaryBtn.setOnClickListener {
            // 내 타임캡슐 버튼 눌렀을 때
            Log.d("GotoDiaryFrag: ", "편집 클릭")
            mainActivity.loadFragment(MyPage())
        }

        return view
    }

    //리사이클러뷰 어댑터
    @SuppressLint("NotifyDataSetChanged")
    inner class RecyclerViewAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var mypageDTOList: ArrayList<MypageDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()

        //피드 데이터 시간에 대해 오름차순으로 정렬, 배열 비우기
        init {
            firestore?.collection("mypage")?.orderBy("timestamp", Query.Direction.ASCENDING)
                ?.addSnapshotListener { value, error ->
                    mypageDTOList.clear()
                    contentUidList.clear()
                    if(value == null) return@addSnapshotListener
                    for(snapshot in value!!.documents) {
                        var item = snapshot.toObject(MypageDTO::class.java)
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

            //피드 컬렉션에 저장된 데이터 가져오기
            if(mypageDTOList!![position].feed_kind == 0) {
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