package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        auth = FirebaseAuth.getInstance()
        id_sign_up_txt.text = auth.currentUser.toString()//이후에 사용자 닉네임 넣는 걸로 바꾸기
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
            //TODO 회원탈퇴 구현하기
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