package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class LatestMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)

        //build.gradle(module)에 implement한 피카소로 groupadapter만듦.
        val adapter = GroupAdapter<ViewHolder>()
        recyclerView_newmessage.adapter = adapter

        fetchUsers() //파이어베이스의 user들과 fetch하기
    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers(){
        //파이어 데이터 베이스에서 get reference.
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                //파이어 베이스에서 데이터 모두 가져오기
                snapshot.children.forEach{
                    Log.d("NewMassage",it.toString())
                    //it 오브젝트를 user 클래스 우리가 만든거로 바꾸기
                    val user = it.getValue(com.example.squirrelwarehouse.models.User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }

                adapter.setOnItemClickListener{item, view ->
                    //클릭하면 채팅창 띄우기
                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    //                   intent.putExtra(USER_KEY,item.user.username)
                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)

                    finish() //다시 리스트로 안돌아가고 메인 으로 돌아가는
                }
                recyclerView_newmessage.adapter = adapter


            }
            override fun onCancelled(error: DatabaseError) {

            }


        })

    }
}

//recycler view(list view)를 위한 custom adapter class
class UserItem(val user: com.example.squirrelwarehouse.models.User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        // will be called in our list for each user object later on...
        viewHolder.itemView.username_textview_new_message.text = user.username

        //이미지 올리기 프로필
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_new_message)
    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}
