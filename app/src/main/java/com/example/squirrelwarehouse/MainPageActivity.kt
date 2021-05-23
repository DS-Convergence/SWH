package com.example.squirrelwarehouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.listview_form.*
import kotlinx.android.synthetic.main.main_page.*

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        updateView.layoutManager = LinearLayoutManager(this)
        cateView.layoutManager = LinearLayoutManager(this)
        rcmdView.layoutManager = LinearLayoutManager(this)

        val adapter = ListAdapter()
        // TODO: 메인페이지 리사이클러뷰 내용 넣기
        //adapter.add()
        updateView.adapter = adapter

        //adapter.add()
        cateView.adapter = adapter

        //adapter.add()
        rcmdView.adapter = adapter
    }
}