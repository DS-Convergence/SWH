package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.listview.*
import kotlinx.android.synthetic.main.listview_form.*

class SearchResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)

        page_title.text = "검색 결과"

        back_btn.setOnClickListener {
            finish()
        }
    }
}