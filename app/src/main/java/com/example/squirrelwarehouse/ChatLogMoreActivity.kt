package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.squirrelwarehouse.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log_more.*


class ChatLogMoreActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()//새로운 어뎁터
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log_more)
        var intent = intent
        var touserid = intent.getStringExtra("userId1").toString()
        var fromId = intent.getStringExtra("userId2").toString()
        var prod = intent.getStringExtra("productId")


        textView_camera_chat_log_more.setOnClickListener {
            //val intent = Intent(this, ::class.java)
            //startActivityForResult(intent, 0)
        }

        textView_return_chat_log_more.setOnClickListener {
            //예은이 코드로 정보 넘겨주기_ProductRentalQRActivity
            val intent = Intent(this, ProductRentalQRActivity::class.java)
            intent.putExtra("userId1", touserid) //빌려주는 사람_게시글 올린 사람_QR코드 띄우기
            Log.d("CHECK_ChatLogMore", "userId1 " + touserid)
            intent.putExtra("userId2", fromId) //빌리는 사람_카메라 띄우기
            Log.d("CHECK_ChatLogMore", "userId2 " + fromId)
            intent.putExtra("productId", prod)
            Log.d("CHECK_ChatLogMore", "productId " + prod)
            startActivityForResult(intent, 0)
        }


    }

}