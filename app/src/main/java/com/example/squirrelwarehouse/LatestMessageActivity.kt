package com.example.squirrelwarehouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_message.*

class LatestMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)

        //build.gradle(module)에 implement한 피카소로 groupadapter만듦.
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())
        recyclerVIew_newmessage.adapter = adapter

        fetchUsers()

    }

    private fun fetchUsers(){
        //파이어 데이터 베이스에서 get reference.
        //리스너

    }
}


//recycler view(list view)를 위한 custom adapter class
class UserItem: Item<ViewHolder>(){ //class UserItem(val user: User)
    override fun bind(viewHolder: ViewHolder, position: Int) {
        // will be called in our list for each user object later on...
        //viewHolder.itemView.username_textview_new_message.text = user.username
        //이미지 올리기 프로필
        // Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_new_message)
    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}