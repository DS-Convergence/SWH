package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_page.*
import android.util.Log

class MyPageActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private var firestore : FirebaseFirestore? = null
    var uid : String? = null
    var nickname : String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        uid = auth.currentUser?.uid
        firestore?.collection("Users")?.document("user_${uid}")?.get()?.addOnSuccessListener { doc ->
            nickname = doc?.data?.get("nickname").toString()
            id_sign_up_txt.text = nickname
            Log.d("로그-1-success-record받기-","nickname ${nickname}")
        }

        terms_btn.setOnClickListener {
            val intent = Intent(this, TermsAndConditionsActivity::class.java)
            startActivity(intent)
        }

        user_setting_btn.setOnClickListener {
            val intent = Intent(this, UserSettingActivity::class.java)
            startActivity(intent)
        }
        back_btn.setOnClickListener {
            finish()
        }
        set_location_btn.setOnClickListener {
            val intent = Intent(this, SetLocationInformationActivity::class.java)
            startActivity(intent)
        }
        logout_btn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,LogInActivity::class.java)
            startActivity(intent)
        }
        withdrawal_btn.setOnClickListener {
            val mAlertDialog = AlertDialog.Builder(this)
            mAlertDialog.setTitle("회원탈퇴")
            mAlertDialog.setMessage("삭제된 계정은 복구할 수 없으며 해당 계정의 게시물과 정보는 완전히 삭제됩니다. " +
                    "정말 탈퇴하겠습니까?")
            mAlertDialog.setPositiveButton("Yes") { dialog, id ->
                //perform some tasks here
                deleteId()
                auth.signOut()
                val intent = Intent(this, LogInActivity :: class.java )//로그인화면으로 넘어감
                startActivity(intent)
            }
            mAlertDialog.setNegativeButton("No") { dialog, id ->
                //perform som tasks here
            }
            mAlertDialog.show()
        }
    }
    // 회원탈퇴 함수
    fun deleteId() {
        auth?.currentUser?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successful membership withdrawal", Toast.LENGTH_LONG).show()
                    }
                }
    }
}