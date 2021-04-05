package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.back_btn

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var firestore :FirebaseFirestore = FirebaseFirestore.getInstance()
    private val TAG : String = "CreateAccount"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.id_sign_up)
        val password = findViewById<EditText>(R.id.pw_sign_up)
        val passwordCheck = findViewById<EditText>(R.id.pw_sign_up_check)
        val nickname = findViewById<EditText>(R.id.nick_sign_up)

        //이메일 혈식 체크
        val email_pattern = android.util.Patterns.EMAIL_ADDRESS
        sign_up_btn.setOnClickListener {
            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()){
                Toast.makeText(this, "이메일, 닉네임 또는 비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
            else if(password.text.toString().length < 6){
                Toast.makeText(this, "비밀번호는 6자리 이상 이어야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else if(!email_pattern.matcher(email.text.toString()).matches()){
                Toast.makeText(this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
            }
            else if(password.text.toString() != passwordCheck.text.toString()){
                Toast.makeText(this, "비밀번호를 다시한번 확인해주세요.",Toast.LENGTH_SHORT).show()
            }
            else {
                //회원 가입 완료처리....-> 이후 이메일 인증 구현하기(주석 해제)
                //회원 가입과 동시에 모델유저에 uid, nickname, 등 마이페이지에 들어가야할 정보 넣기(하면 표시하기)
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                //유저를 서버에 등록 성공했음. 이제 유저의 이메일로 인증 메일을 발송함.
                                /*아래는 이메일 인증 부분 주석처리 해둔 것*/
                                /*auth.currentUser?.sendEmailVerification()
                                        ?.addOnCompleteListener {task2 ->
                                            if (task2.isSuccessful){
                                                //이메일을 보냈으니 확인하라는 토스트메세지 띄움.
                                                Toast.makeText(this, "Registered successfully. Please check your email for verification.",Toast.LENGTH_LONG).show()
                                                //Log.d("로그-success-인증 메일 전송","성공")
                                            }
                                            else{
                                                Log.d("로그-fail-인증 메일 전송","실패 . . . . "+task2.exception?.message)
                                            }
                                        }*/


                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                //updateUI(user)
                                // 아니면 액티비티를 닫아 버린다.
                                //finish()
                                //overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                        baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                ).show()
                                //updateUI(null)
                            //입력필드 초기화
                            email?.setText("")
                            password?.setText("")
                            email.requestFocus()
                            }
                        }
            }
        }
        back_btn.setOnClickListener {
            finish()
        }
    }
}