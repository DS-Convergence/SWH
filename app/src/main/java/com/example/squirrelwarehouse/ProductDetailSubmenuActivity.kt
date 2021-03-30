package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class ProductDetailSubmenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail_submenu)

        val modify : TextView = findViewById(R.id.tv_modify)
        val remove : TextView = findViewById(R.id.tv_remove)
        val main : View = findViewById(R.id.main)

        modify.setOnClickListener {
            // 글쓰기 화면으로. 칸은 정보로 채워져있는 상태
            val intent = Intent(this, ProductFormActivity::class.java)
            startActivity(intent)
        }

        remove.setOnClickListener {
            // 삭제
        }

        main.setOnClickListener() {
            finish()
        }

    }
}