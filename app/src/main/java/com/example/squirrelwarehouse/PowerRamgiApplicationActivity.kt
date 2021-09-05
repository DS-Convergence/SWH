package com.example.squirrelwarehouse

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_power_ramgi_application.*

class PowerRamgiApplicationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_power_ramgi_application)
        back_btn.setOnClickListener {
            finish()
        }
        powerRamgi_submit_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/ybZ1ZtLXfVDkJV8e8") )
            startActivity(intent)
        }
    }

}