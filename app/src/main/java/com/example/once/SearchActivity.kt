package com.example.once

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_feed.view.*

class SearchActivity : AppCompatActivity() {
    lateinit var searchBackBtn: ImageButton
    lateinit var searchListView: RecyclerView
    lateinit var searchBtn: ImageButton
    lateinit var searchEditText: EditText
    private var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //메인페이지로 돌아가기 버튼
        searchBackBtn = findViewById(R.id.searchBack)
        searchBackBtn.setOnClickListener {
            onBackPressed()
        }

        //검색어 변수 초기화
        searchEditText = findViewById(R.id.searchEditTxt)
        var searchWord = searchEditText.text.toString()

//        //recycler뷰 관련 설정
        searchListView = findViewById(R.id.SearchListView)
        searchListView.adapter = RecyclerViewAdapter()
        searchListView.layoutManager = LinearLayoutManager(this)

        //검색버튼&기능
        searchBtn = findViewById(R.id.searchBtn)
        searchBtn.setOnClickListener {
            (searchListView.adapter as RecyclerViewAdapter).search(searchWord)
            //파이어스토어 변수 초기화
            firestore = FirebaseFirestore.getInstance()
        }
    }

    //메인 돌아가기 함수
    override fun onBackPressed() {
        super.onBackPressed()
    }

    //리사이클러뷰 어댑터
    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var searchDTO: ArrayList<SearchDTO> = arrayListOf()

        //피드 데이터 가져오기
        init {
            firestore?.collection("feed")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                searchDTO.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(SearchDTO::class.java)
                    searchDTO.add(item!!)
                }
                notifyDataSetChanged()
            }
        }

        //xml파일 inflate, viewHolder 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
            return CustomViewHolder(view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        //실제 데이터 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder.itemView

            view.feedItemTitle.text = searchDTO!![position].title //제목
            view.feedContenetView.text = searchDTO!![position].contents //내용
            view.feedItemName.text = searchDTO!![position].userId //이메일
            view.feedImageView.setImageResource(0)
            view.feedDateView.text = ""
        }

        //recycler뷰 총 아이템 개수 반환
        override fun getItemCount(): Int {
            return searchDTO.size
        }

        // 파이어스토어에서 데이터를 불러와서 검색어가 있는지 판단
        fun search(searchWord : String) {
            firestore?.collection("feed")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                searchDTO.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.getString("title")!!.contains(searchWord)) {
                        var item = snapshot.toObject(SearchDTO::class.java)
                        searchDTO.add(item!!)
                    }
                    if (snapshot.getString("contents")!!.contains(searchWord)) {
                        var item = snapshot.toObject(SearchDTO::class.java)
                        searchDTO.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
        }

    }
}