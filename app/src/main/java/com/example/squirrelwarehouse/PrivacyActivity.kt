package com.example.squirrelwarehouse

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_privacy.*

class PrivacyActivity : AppCompatActivity() {
    lateinit var webSetting : WebSettings
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        back_btn.setOnClickListener {
            finish()
        }
        web_view.webViewClient
        webSetting = web_view.settings
        webSetting.javaScriptEnabled
        web_view.loadUrl("file:///android_asset/SquirrelWareHouse_Privacy.html")

    }
}