package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.squirrelwarehouse.models.Product
import com.example.squirrelwarehouse.models.Rental
import com.example.squirrelwarehouse.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log_more.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ChatLogMoreActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()//새로운 어뎁터
    var toUser: User? = null

    private lateinit var auth: FirebaseAuth
    private var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log_more)

        val main : View = findViewById(R.id.main)   // 예은 추가

        var intent = intent
        var user1 = intent.getStringExtra("userId1").toString() // 빌려주는 사람. QR코드 제공
        var user2 = intent.getStringExtra("userId2").toString()  // 빌리는 사람. 카메라 제공
        var prod = intent.getStringExtra("productId")

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        textView_camera_chat_log_more.setOnClickListener {
            //val intent = Intent(this, ::class.java)
            //startActivityForResult(intent, 0)
        }

        textView_return_chat_log_more.setOnClickListener {

            var temp = user1

            // 이미 저장된 정보라면
            // touserId와 fromId가 바뀌어야함.
            firestore?.collection("Rental")?.document(user1+user2+prod)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                task ->
                if (task.isSuccessful) {
                    var rental = task.result.toObject(Rental::class.java)
                    if(rental?.userId1 != null) {
                        temp = user2
                        Log.v("RENTAL",temp)
                    }
                    else {
                        Log.v("RENTAL","실패")
                    }

                    // 현재 유저가 빌려주는 사람인 경우 QR코드로. touserid
                    if(auth.currentUser!!.uid.equals(temp)) {
                        //예은이 코드로 정보 넘겨주기_ProductRentalQRActivity
                        val intent = Intent(this, ProductRentalQRActivity::class.java)
                        intent.putExtra("userId1", user1) //빌려주는 사람_게시글 올린 사람_QR코드 띄우기
                        Log.d("CHECK_ChatLogMore", "userId1 " + user1)
                        intent.putExtra("userId2", user2) //빌리는 사람_카메라 띄우기
                        Log.d("CHECK_ChatLogMore", "userId2 " + user2)
                        intent.putExtra("productId", prod)
                        Log.d("CHECK_ChatLogMore", "productId " + prod)
                        startActivityForResult(intent, 0)
                    }

                    // 현재 유저가 빌리는 사람인 경우 카메라로. fromId
                    else {
                        val intentIntegrator = IntentIntegrator(this)
                        intentIntegrator.setPrompt("")
                        intentIntegrator.setBeepEnabled(true)
                        intentIntegrator.captureActivity = ProductRentalCameraActivity::class.java
                        intentIntegrator.initiateScan()
                    }


                }
                else {
                    Log.v("RENTAL","실패")
                }
            }





        }

        // 예은 추가
        main.setOnClickListener() {
            finish()
        }

    }

    // 예은 코드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //qrcode 가 없으면
            if (result.contents == null) {
                Toast.makeText(this@ChatLogMoreActivity, "취소!", Toast.LENGTH_SHORT).show()
            } else {
                //qrcode 결과가 있으면
                try {
                    //data를 json으로 변환
                    val obj = JSONObject(result.contents)
                    var user1 = obj.getString("user1")
                    var user2 = obj.getString("user2")
                    var productId = obj.getString("productId")
                    var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

                    firestore?.collection("Rental")?.document(user1+user2+productId)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                        task ->
                        if(task.isSuccessful) {
                            var rental = task.result.toObject(Rental::class.java)
                            var id= rental?.userId1
                            if (id != null) {   // 데이터가 데이터베이스에 있는 경우. returnTime만 업데이트
                                var map = mutableMapOf<String,Any>()
                                map["returnTime"] = timeStamp

                                firestore?.collection("Rental")?.document(user1+user2+productId)?.update(map)?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.v("RENTAL", "UpdateSuccess")
                                        // 다시 채팅창으로 돌아가야함.
                                        // finish()
                                    }
                                }

                            }
                            else { // 데이터가 없는 경우.
                                var rental = Rental(user1, user2, productId, timeStamp, null)

                                firestore?.collection("Rental")?.document(user1+user2+productId)?.set(rental)?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.v("RENTAL","Success")
                                        // 다시 채팅창으로 돌아가야함.
                                        // finish()
                                    }
                                }
                            }
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        Toast.makeText(this,"반납완료", Toast.LENGTH_LONG).show();
        finish()
    }



}