package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.listview_form.*

class NoticeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)

        page_title.text = "알림"

        // 알림 보여주기
    }
}