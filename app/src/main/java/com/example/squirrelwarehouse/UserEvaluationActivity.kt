package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.user_evaluation.*

class UserEvaluationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var firestore :FirebaseFirestore = FirebaseFirestore.getInstance()
    private var ratingScore : Float = 0.0F
    private var rating : Float = 0.0F
    private var ratingCnt : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_evaluation)

        auth = FirebaseAuth.getInstance()

        rating_bar.setOnRatingBarChangeListener{ ratingBar, rating, fromUser ->
            rating_bar.rating = rating
            ratingScore = rating
        }

        firestore?.collection("Users")?.document("user_${auth?.currentUser?.uid}")
                ?.get()?.addOnSuccessListener { doc ->
                    rating = doc?.data?.get("rating").toString().toFloat()
                    ratingCnt = doc?.data?.get("ratingCnt").toString().toInt()+1
                    ratingScore += rating/ratingCnt
                    firestore.collection("Users").document("user_${auth?.currentUser?.uid}")
                            .update("rating",ratingScore,"ratingCnt",ratingCnt)
                }

        tv_close.setOnClickListener {
            // 데이터 베이스에 데이터 저장
            // QR코드 인경우 QR코드가 안없어짐. 이거 해결해야함.
            finish()
        }
    }
}