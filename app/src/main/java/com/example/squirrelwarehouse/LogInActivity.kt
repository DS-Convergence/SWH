package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        auth = FirebaseAuth.getInstance()
        val email = findViewById<EditText>(R.id.id_login)
        val password = findViewById<EditText>(R.id.pw_login)

        log_in_btn.setOnClickListener {
            if(email.text.toString().isEmpty() || password.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter the email or password", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            //임시로 메인 엑티비티 넘어가게 코드짬
                            val intent = Intent(this, MyPageActivity::class.java)
                            startActivityForResult(intent, 0)

                            /*아래는 이메일 인증 부분 주석처리 해둔 것*/
                            //회원가입한 유저라는 것 확인됐음. 이제 이메일 인증을 완료한 유저인지 확인.
                            /*
                            if (auth?.currentUser?.isEmailVerified!!) {
                                //맞다면, 로그인 계속 진행.
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser
                                /*아래는 이메일 인증 부분 주석처리 해둔 것*/
                            } else {
                                //회원가입은 했으나 아직 이메일 인증을 하지 않은 사용자임. 로그인 못함. 토스트띄움
                                Toast.makeText(
                                    this,
                                    "Please verify your email address.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }*/

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "로그인 실패. 아이디나 비밀번호를 확인하세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        go_sign_up_btn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }
}