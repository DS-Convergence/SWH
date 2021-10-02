package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.user_evaluation.*
import android.util.Log

class UserEvaluationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var firestore :FirebaseFirestore = FirebaseFirestore.getInstance()
    private var ratingScore : Float = 0.0F
    private var rating : Float = 0.0F
    private var ratingCnt : Int = 0
    private var otherUser : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_evaluation)
        var user1 = intent.getStringExtra("user1").toString() //물건 주인
        var user2 = intent.getStringExtra("user2").toString() //대여원하는 사람
        auth = FirebaseAuth.getInstance()

        rating_bar.setOnRatingBarChangeListener{ ratingBar, rating, fromUser ->
            rating_bar.rating = rating
            ratingScore = rating
            Log.d("점수","${ratingScore}")
        }



        tv_close.setOnClickListener {
            // 데이터 베이스에 데이터 저장
            // QR코드 인경우 QR코드가 안없어짐. 이거 해결해야함.
            if(auth?.currentUser?.uid.equals(user1)){
                otherUser = user2
            }
            else if(auth?.currentUser?.uid.equals(user2)){
                otherUser = user1
            }
            Log.d("점수","$otherUser")
            firestore?.collection("Users")?.document("user_${otherUser}")
                    ?.get()?.addOnSuccessListener { doc ->
                        rating = doc?.data?.get("rating").toString().toFloat()
                        ratingCnt = doc?.data?.get("ratingCnt").toString().toInt()
                        ratingScore = (ratingScore+(rating*ratingCnt))/(ratingCnt+1)
                        ratingCnt += 1
                        firestore.collection("Users").document("user_${otherUser}")
                                .update("rating",ratingScore,"ratingCnt",ratingCnt)
                    }
            val intent = Intent(this,ChatLogActivity::class.java)
            startActivity(intent)
            finish()
            //TODO 실행시켜보기
        }
    }
}