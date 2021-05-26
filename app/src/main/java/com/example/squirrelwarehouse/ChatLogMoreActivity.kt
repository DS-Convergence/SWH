package com.example.squirrelwarehouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.squirrelwarehouse.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class ChatLogMoreActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()//새로운 어뎁터
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log_more)

    }

}