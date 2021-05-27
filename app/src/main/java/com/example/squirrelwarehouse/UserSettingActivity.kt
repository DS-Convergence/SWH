package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.activity_user_setting.back_btn

class UserSettingActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private var firestore : FirebaseFirestore? = null
    var uid : String? = null
    var nickname : String? = null
    var email : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        uid = auth.currentUser?.uid
        firestore?.collection("Users")?.document("user_${uid}")?.get()?.addOnSuccessListener { doc ->
            nickname = doc?.data?.get("nickname").toString()
            email = doc?.data?.get("email").toString()
            nick_edit_tv.setText(nickname)
            email_edit_tv.text = email
            Log.d("로그-1-success-record받기-","nickname ${nickname}")
            Log.d("로그-1-success-record받기-","email ${email}")
        }
        back_btn.setOnClickListener {
            finish()
        }

    }
}