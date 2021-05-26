package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProductDetailSubmenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail_submenu)

        val modify : TextView = findViewById(R.id.tv_modify)
        val remove : TextView = findViewById(R.id.tv_remove)
        val main : View = findViewById(R.id.main)

        val intent = intent
        val text = intent.getStringExtra("ProductID")

        modify.setOnClickListener {
            // 글쓰기 화면으로. 칸은 정보로 채워져있는 상태
            val intent = Intent(this, ProductFormActivity::class.java)
            intent.putExtra("ModifyProduct", text)
            startActivityForResult(intent, 0)
        }

        remove.setOnClickListener {
            // 삭제
        }

        main.setOnClickListener() {
            finish()
        }

    }
}