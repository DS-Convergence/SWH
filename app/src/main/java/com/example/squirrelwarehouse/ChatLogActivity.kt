package com.example.squirrelwarehouse

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class ChatLogActivity : AppCompatActivity() {
    companion object {
        val TAG = "chatLog"
    }

    //val adapter = GroupAdapter<ViewHolder>()//새로운 어뎁터
    //var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
       // recyclerView_chat_log.adapter = adapter //새로운 object를 add할 수 있게 해주고 그럴 때마다 새롭게 refresh해줌.

        //상단 바이름 설정
        //NewMessageActivity에서 받아온 키 값으로 username 받아오기
        //val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        //username 뿐만아니라 전체 user받아올 수 있음
        //toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        // supportActionBar?.title = toUser?.username //optional이니까 ?써줘야함.

        //listenForMessages()

        //보내기 버튼 누리면 보내지게
       // send_button_chat_log.setOnClickListener {
       //     Log.d(TAG, " Attempt to send message.....")
            //performSendMessage() // 새로운 메소드. 어떻게 firebase의 메세지를 보낼지
      //  }

    }

}