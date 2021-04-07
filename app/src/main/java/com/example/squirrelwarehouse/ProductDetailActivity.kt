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
    private var state = false;

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

        var btnHeart : ImageButton = findViewById(R.id.btn_heart)
        val btnReport : ImageButton = findViewById(R.id.btn_report)
        val btnSubmenu : TextView = findViewById(R.id.btn_submenu)


        btnHeart.setOnClickListener() {
            // 디폴트는 하얀 하트, 관심상품 등록했으면 초록 하트, 데이터베이스와 연동필요
            // Firebase로 안드로이드 sns 앱 만들기 195-197쪽 참고
            if(!state) {
                btnHeart.setImageResource(R.drawable.heart_green)
                state = true;
            }
            else {
                btnHeart.setImageResource(R.drawable.heart_white)
                state = false;
            }
        }
        val btnChat : Button = findViewById(R.id.btn_chat)

        btnReport.setOnClickListener() {
            // 신고 페이지로 이동
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        btnSubmenu.setOnClickListener {
            // ProductDetailSubmenu로 이동
            val intent = Intent(this, ProductDetailSubmenuActivity::class.java)
            startActivity(intent)
        }

    }
}