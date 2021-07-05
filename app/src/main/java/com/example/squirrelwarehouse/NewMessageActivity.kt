package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.squirrelwarehouse.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*


class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        fetchUser() //Database에 있는 user들을 fetch하는 것

    }
    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUser(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()


                //파이어 베이스에서 데이터 모두 가져오기
                p0.children.forEach{
                    Log.d("NewMessage",it.toString())
                    //it 오브젝트를 user 클래스 우리가 만든거로 바꾸기
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }

                adapter.setOnItemClickListener{item, view ->

                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatLogListActivity::class.java)
                    //                   intent.putExtra(USER_KEY,item.user.username)
                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)
                    finish() //다시 리스트로 안돌아가고 메인 으로 돌아가는
                }
                recyclerview_newmessage_morefirends.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}


//recycler view(list view)를 위한 custom adapter class
class UserItem(val user: User): Item<ViewHolder>(){
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