package com.example.squirrelwarehouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_settings.*

class MainSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_settings)

        var noticeSet = notice_set
        var darkSet = dark_set

        noticeSet.setOnCheckedChangeListener { buttonView, isChecked ->
            // 알림 켜기
        }

        darkSet.setOnCheckedChangeListener { buttonView, isChecked ->
            // 다크모드 켜기
        }
    }
}