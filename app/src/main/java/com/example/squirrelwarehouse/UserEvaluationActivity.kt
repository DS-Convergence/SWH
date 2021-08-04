package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import kotlinx.android.synthetic.main.user_evaluation.*

class UserEvaluationActivity : AppCompatActivity() {

    private lateinit var imgAcorn1 : ImageButton
    private lateinit var imgAcorn2 : ImageButton
    private lateinit var imgAcorn3 : ImageButton
    private lateinit var imgAcorn4 : ImageButton
    private lateinit var imgAcorn5 : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_evaluation)
/*
        imgAcorn1 = findViewById(R.id.img_acorn1)
        imgAcorn2 = findViewById(R.id.img_acorn2)
        imgAcorn3 = findViewById(R.id.img_acorn3)
        imgAcorn4 = findViewById(R.id.img_acorn4)
        imgAcorn5 = findViewById(R.id.img_acorn5)
*/

        tv_close.setOnClickListener {
            // 데이터 베이스에 데이터 저장
            // QR코드 인경우 QR코드가 안없어짐. 이거 해결해야함.
            finish()
        }
    }
}