package com.example.squirrelwarehouse

import android.util.Log
import com.example.squirrelwarehouse.models.ChatMessage
import com.example.squirrelwarehouse.models.Product
import com.example.squirrelwarehouse.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.user_row_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>(){ //최근 받은 메세지 창들
    var chatPartnerUser: User? =null
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    private lateinit var auth: FirebaseAuth

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.LastMessage_textview_new_message.text = chatMessage.text

        val chatPartnerId: String
        if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessage.toId
        }else{
            chatPartnerId = chatMessage.fromId
        }
        var prod = chatMessage.id
        Log.d("CHECK_LATES","chatMessage.id : " + chatMessage.id)


        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                //최근 보낸 메세지 창에 username설정하기
                viewHolder.itemView.username_textview_new_message.text = chatPartnerUser?.username

                //viewHolder.itemView.chat_log_textview_productname.text = chatMessage.id
                //이미지 설정하기
                val targetImageView = viewHolder.itemView.imageView_new_message
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)
                firestore = FirebaseFirestore.getInstance()
                firestore?.collection("Product")?.document(chatMessage.id)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                        task ->
                    Log.d("CheckEunbae","성공")
                    if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                        var product = task.result.toObject(Product::class.java)
                        viewHolder.itemView.chat_log_textview_productname.text = product?.productName
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}