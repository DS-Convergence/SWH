package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        user_setting_btn.setOnClickListener {
            val intent = Intent(this, UserSettingActivity::class.java)
            startActivity(intent)
        }
        back_btn.setOnClickListener {
            finish()
        }
        set_location_btn.setOnClickListener {
            val intent = Intent(this, SetLocationInformationActivity::class.java)
            startActivity(intent)
        }
        logout_btn.setOnClickListener {
            //TODO 로그아웃 구현하기
        }
        withdrawal_btn.setOnClickListener {
            //TODO 회원탈퇴 구현하기
        }
    }
}