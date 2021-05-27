package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_page.*

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        // 버튼 기능 구현
        var gotoMore = gotomore
        gotoMore.setOnClickListener {
            val intent = Intent(this, MainMore::class.java)
            startActivityForResult(intent, 0)
        }

        var gotoChat = gotochat
        gotoChat.setOnClickListener {
            val intent = Intent(this, LatestMessageActivity::class.java)
            startActivityForResult(intent, 0)
        }
        var gotoMap = gotomap
        gotoMap.setOnClickListener {
            val intent = Intent(this, MainMapActivity::class.java)
            startActivityForResult(intent, 0)
        }

        var searchBar = searchBar
        searchBar.setOnClickListener {

        }

        var moreUpdate = moreUpdate
        moreUpdate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // listView로 연결하기
            }
        })

        var moreCate = moreCate
        moreCate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // listView로 연결
            }
        })

        var moreRcmd = moreRcmd
        moreRcmd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // listView로 연결
            }
        })

        var writeBtn = writeBtn
        writeBtn.setOnClickListener {
            val intent = Intent(this, ProductFormActivity::class.java)
            startActivityForResult(intent, 0)
        }


        // 메인페이지 물품 보기
        updateView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        cateView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rcmdView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        // TODO: 메인페이지 리사이클러뷰 내용 넣기
        // https://hijjang2.tistory.com/313
        lateinit var item: MainItem

        var updateList:ArrayList<MainItem> = arrayListOf()
        for(i in 1..5) {
            item = MainItem("","최신"+i)
            updateList.add(item)
        }
        updateView.adapter = MainViewAdapter(updateList)

        var cateList:ArrayList<MainItem> = arrayListOf()
        for(i in 1..5) {
            item = MainItem("","카테고리"+i)
            cateList.add(item)
        }
        cateView.adapter = MainViewAdapter(cateList)

        var rcmdList:ArrayList<MainItem> = arrayListOf()
        for(i in 1..5) {
            item = MainItem("","추천"+i)
            rcmdList.add(item)
        }
        rcmdView.adapter = MainViewAdapter(rcmdList)
    }
}