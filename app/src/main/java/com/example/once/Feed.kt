package com.example.once

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
import kotlinx.android.synthetic.main.feed.view.*
import kotlinx.android.synthetic.main.item_alaram.view.*
import kotlinx.android.synthetic.main.item_feed.view.*

class Feed : Fragment() {
    private var firestore: FirebaseFirestore? = null

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

    inner class RecyclerViewAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var feedDTOList: ArrayList<FeedDTO> = arrayListOf()

        init {
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            FirebaseFirestore.getInstance().collection("feed").whereEqualTo("uid", uid)
                .addSnapshotListener { value, error ->
                    feedDTOList.clear()
                    if(value == null) return@addSnapshotListener

                    for(snapshot in value.documents){
                        feedDTOList.add(snapshot.toObject(FeedDTO::class.java)!!)
                        //feedDTOList.add(snapshot.toObject(Class <feedDTOList::class.java>))
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
            return CustomViewHolder (view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder.itemView

            //피드 컬렉션에 저장된 데이터 가져오기
            view.feedItemTitle.text = feedDTOList!![position].title //제목
            view.feedContenetView.text = feedDTOList!![position].contents //내용
            view.feedItemName.text = feedDTOList!![position].userId //이메일

            view.feedDateView.setOnClickListener {
                //디테일 화면 레이아웃
            }
//            view.feedItemProfile
//            view.feedImageView


//            FirebaseFirestore.getInstance().collection("feed").document(feedDTOList[position].uid!!).get()
//                .addOnCompleteListener { task ->
//                    if(task.isSuccessful){
//                        val url = task.result!!["profileImageUrl"]
//                        Glide.with(view.context).load(url).apply(RequestOptions().circleCrop()).into(view.findViewById(R.id.feedItemProfile))
//                    }
//                }
//
//            when(feedDTOList[position].weather_kind){
//                0 -> {
//                    val str_0 = feedDTOList[position].userId + ""
//                    view.feedContenetView.text = str_0
//                }
//                1 -> {
//                    val str_0 = feedDTOList[position].userId + ""
//                    view.feedItemTitle.text = str_0
//                }
//            }
//            view.alarm_comment.visibility = View.INVISIBLE
        }

        override fun getItemCount(): Int {
            return feedDTOList.size
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }
}