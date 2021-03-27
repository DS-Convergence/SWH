package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProductDetailSubmenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail_submenu)

        val modify : TextView = findViewById(R.id.tv_modify)
        val remove : TextView = findViewById(R.id.tv_remove)

        modify.setOnClickListener {
            // 글쓰기 화면으로. 칸은 정보로 채워져있는 상태
        }

        remove.setOnClickListener {
            // 삭제
        }

    }
}