package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.squirrelwarehouse.models.ChatMessage
import com.example.squirrelwarehouse.models.Product
import com.example.squirrelwarehouse.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_message.*
import kotlinx.android.synthetic.main.user_row_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import kotlin.collections.HashMap

class LatestMessageActivity : AppCompatActivity() {
    private var firestore : FirebaseFirestore? = null
    private var chatmessage :ChatMessage? = null
        companion object {
        var currentUser: User? = null
        val TAG = "LatestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)
        recyclerView_newmessage.adapter = adapter
        //recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL)) //구분선
        //클릭하면 (최근 메세지) 채팅으로 들어가게
        //set item click listener on your adapter

        latest_message_more_users.setOnClickListener {
            val intent = Intent(this, NewMessageActivity::class.java)
            startActivity(intent) //NewMessageActivity창 뜸.
        }

        adapter.setOnItemClickListener { item,view->
            Log.d(TAG,"123")
            val intent = Intent(this, ChatLogListActivity::class.java)
            //we are missing the caht partner user
            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
           Log.d("TEST","view.chat_log_textview_productname" + item.chatMessage.id)

            intent.putExtra("prod", item.chatMessage.id)
            startActivity(intent)
        }

        // setupDummyRows()
        listenForLatestMessage()
        fetchCurrentUser()
        verifyUserIsLoggedIn() //user이 로그인 하고 있으면 앱 시작 했을 때 다시 로그인 페이지 안뜨게
    }

    val latestMessageMap = HashMap<String, ChatMessage>() //hashmap으로 바로 통보해서 적용하도록. 새로운 메세지가

    private fun refreshRecyclerViewMessges(){
        adapter.clear()
        latestMessageMap.values.forEach{
            adapter.add(LatestMessageRow(it))

        }
    }
    private fun listenForLatestMessage(){
        //최근 메세제 읽어오는 리스너
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //새로운 child있을 떄마다 notify.
                val chatMessage = snapshot.getValue(ChatMessage::class.java)?:return

                latestMessageMap[snapshot.key!!] = chatMessage //actual value는 chatMessage
                refreshRecyclerViewMessges()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //파이어 베이스에서 변경되었으면 UI화면에 반영
                //메세지 cahging있을 때 마다
                val chatMessage = snapshot.getValue(ChatMessage::class.java)?:return
                latestMessageMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessges()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {            }
            override fun onCancelled(error: DatabaseError) {            }

        })
    }

    val adapter = GroupAdapter<ViewHolder>()

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                currentUser = snapshot.getValue(User::class.java)
                // Log.d(TAG,"Current user ${currentUser?.profileImageUrl}")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun verifyUserIsLoggedIn() {
        //user이 로그인 하고 있으면 앱 시작 했을 때 다시 로그인 페이지 안뜨게
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) { //user가 없으면 그 로그인 창 액티비티가 뜸.
            val intent = Intent(this, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent) //RegisterActivityt보여주기.
        }
    }

}