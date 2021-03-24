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

    }
}