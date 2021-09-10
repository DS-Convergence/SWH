package com.example.squirrelwarehouse

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        back_btn.setOnClickListener {
            finish()
        }
        //신고 좀 더 보완해야 할거 같음
        report_form_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/yKHEgjgP3AxHxSqf8"))
            startActivity(intent)
        }
    }
}