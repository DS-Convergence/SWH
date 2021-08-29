package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_info.*
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage


class UserInfoActivity : AppCompatActivity() {
    private var firestore : FirebaseFirestore? = null
    var storage : FirebaseStorage?=null
    var uid : String? = null
    var nickname : String? = null
    var introduce : String? = null
    var rating : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        uid = intent.getStringExtra("UserId")
        if (uid != null) {
            Log.d("유저아이디", uid!!)
        }

        report_btn.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
        back_btn.setOnClickListener {
            finish()
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.setReverseLayout(true)
        layoutManager.setStackFromEnd(true)
        user_listview.layoutManager = layoutManager

        user_listview.adapter = ItemAdapter2(uid,this) //uid 넘겨주기

        firestore?.collection("Users")?.document("user_${uid}")?.get()?.addOnSuccessListener { doc ->
            nickname = doc?.data?.get("nickname").toString()
            introduce = doc?.data?.get("introduce").toString()
            rating = doc?.data?.get("rating").toString()
            usernickname_txt.text = nickname
            user_writing_list_tv.text = nickname + " 님의 게시글"
            userintroduce_txt.text = introduce
            userrating_txt.text = rating
            var storageRef = storage?.reference?.child("images")?.child(doc?.data?.get("userProPic").toString())
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                        .load(uri)
                        .into(user_propic_img)
                Log.v("IMAGE","Success")
            }?.addOnFailureListener { //이미지 로드 실패시
                Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                Log.v("IMAGE","failed")

            }
        }

    }



}