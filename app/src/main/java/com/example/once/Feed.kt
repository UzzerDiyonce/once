package com.example.once

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.feed.view.*
import kotlinx.android.synthetic.main.item_alaram.view.*
import kotlinx.android.synthetic.main.item_feed.view.*
import java.text.SimpleDateFormat

class Feed : Fragment() {
    private var firestore: FirebaseFirestore? = null
    var uid = FirebaseAuth.getInstance().currentUser?.uid

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.feed, container, false)

        //파이어스토어 초기화
        firestore = FirebaseFirestore.getInstance()

        //변수 초기화
        uid = FirebaseAuth.getInstance().currentUser?.uid

        //recycler뷰 관련 설정
        view.FeedRecyclerView.layoutManager = LinearLayoutManager(activity)
        view.FeedRecyclerView.adapter = RecyclerViewAdapter()

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

        return view
    }

    //리사이클러뷰 어댑터
    @SuppressLint("NotifyDataSetChanged")
    inner class RecyclerViewAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var feedDTOList: ArrayList<FeedDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("feed")?.orderBy("timestamp", Query.Direction.ASCENDING)
                ?.addSnapshotListener { value, error ->
                    feedDTOList.clear()
                    contentUidList.clear()
                    if(value == null) return@addSnapshotListener
                    for(snapshot in value!!.documents) {
                        var item = snapshot.toObject(FeedDTO::class.java)
                        feedDTOList.add(item!!)
                        contentUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
//            val uid = FirebaseAuth.getInstance().currentUser?.uid
//
//            FirebaseFirestore.getInstance().collection("feed").whereEqualTo("uid", uid)
//                .addSnapshotListener { value, error ->
//                    feedDTOList.clear()
//                    if(value == null) return@addSnapshotListener
//
//                    for(snapshot in value.documents){
//                        feedDTOList.add(snapshot.toObject(FeedDTO::class.java)!!)
//                        //feedDTOList.add(snapshot.toObject(Class <feedDTOList::class.java>))
//                    }
//                    notifyDataSetChanged()
//                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
            return CustomViewHolder (view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder.itemView

            //피드 컬렉션에 저장된 데이터 가져오기
            if(feedDTOList!![position].feed_kind == 1) {
                view.feedFriend.setBackgroundResource(R.drawable.withfriedn)
            }
            view.feedItemTitle.text = feedDTOList!![position].title //제목
            view.feedItemName.text = feedDTOList!![position].userId //유저 아이디 저장
            Glide.with(holder.itemView.context).load(feedDTOList!![position].imageUrl)
                .into(view.feedImageView) //이미지 저장
            view.feedContenetView.text = feedDTOList!![position].contents //내용
            //타임스탬프
            val timestamp = feedDTOList[position].timestamp
            val sdf = SimpleDateFormat("yyyy.MM.dd hh시 mm분")
            val date = sdf.format(timestamp)
            view.feedDateView.text = date
            //피드 더보기 버튼 클릭
            view.feedDetailBtn.setOnClickListener { v->
                var intent = Intent(v.context, DetailFeedActivity::class.java)
                intent.putExtra("contentUid", contentUidList[position])
                intent.putExtra("destinationUid", feedDTOList[position].uid)
                startActivity(intent)
            }
            //피드 프로필 이미지
            FirebaseFirestore.getInstance().collection("users").document(feedDTOList[position].uid!!)
                .get().addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val url = task.result!!["profileImageUrl"]
                        Glide.with(holder.itemView.context).load(url)
                            .apply(RequestOptions().circleCrop())
                            .into(view.feedItemProfile)
                    }
                }
        }

        override fun getItemCount(): Int {
            return feedDTOList.size
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }
}