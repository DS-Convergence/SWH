package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.listview_form.*

class RcmdMore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)
        page_title.text = "추천"

        var intent = intent
        var arr = intent.getSerializableExtra("rcmdList") as ArrayList<String>
        Log.v("RcmdList", "추천 페이지에서 배열 크기: "+arr.size)
    }
}