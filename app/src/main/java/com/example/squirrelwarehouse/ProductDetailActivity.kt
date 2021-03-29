package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var tvProdName : TextView
    private lateinit var tvProdCategory : TextView
    private lateinit var tvTime : TextView
    private lateinit var tvStatus : TextView
    private lateinit var tvUser : TextView
    private lateinit var tvUserLocation : TextView
    private lateinit var tvProdDetail : TextView
    private lateinit var tvMoney : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)

        tvProdName = findViewById(R.id.tv_prodName)
        tvProdCategory = findViewById(R.id.tv_prodCategory)
        tvTime = findViewById(R.id.tv_time)
        tvStatus = findViewById(R.id.tv_time)
        tvUser = findViewById(R.id.tv_user)
        tvUserLocation = findViewById(R.id.tv_uesrLocation)

        tvProdDetail = findViewById(R.id.tv_prodDetail)

        tvMoney = findViewById(R.id.tv_money)
        val btnHeart : ImageButton = findViewById(R.id.btn_heart)
        val btnChat : Button = findViewById(R.id.btn_chat)

        val btnReport : ImageButton = findViewById(R.id.btn_report)

        val btnSubmenu : TextView = findViewById(R.id.btn_submenu)
        btnSubmenu.setOnClickListener {
            // ProductDetailSubmenu로 이동
            val intent = Intent(this, ProductDetailSubmenuActivity::class.java)
            startActivity(intent)
        }

    }
}