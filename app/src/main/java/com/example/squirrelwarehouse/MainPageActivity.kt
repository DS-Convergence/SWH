package com.example.squirrelwarehouse

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.listview_form.*
import kotlinx.android.synthetic.main.main_page.*

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        // 버튼 기능 구현

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