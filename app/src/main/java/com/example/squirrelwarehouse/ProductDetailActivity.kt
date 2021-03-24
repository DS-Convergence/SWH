package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)

        var tvProdName : TextView = findViewById(R.id.tv_prodName)
        var tvProdCategory : TextView = findViewById(R.id.tv_prodCategory)
        var tvTime : TextView = findViewById(R.id.tv_time)
        var tvStatus : TextView = findViewById(R.id.tv_time)
        var tvUser : TextView = findViewById(R.id.tv_user)
        var tvUserLocation : TextView = findViewById(R.id.tv_uesrLocation)

        var tvProdDetail : TextView = findViewById(R.id.tv_prodDetail)

        var tvMoney : TextView = findViewById(R.id.tv_money)
        val btnHeart : ImageButton = findViewById(R.id.btn_heart)
        val btnChat : Button = findViewById(R.id.btn_chat)

        val btnReport : ImageButton = findViewById(R.id.btn_report)

        val btnSubmenu : TextView = findViewById(R.id.btn_submenu)
        btnSubmenu.setOnClickListener {
            startActivity(Intent(this, ProductDetailSubmenuActivity::class.java))
        }
    }
}