package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.squirrelwarehouse.models.Product
import com.example.squirrelwarehouse.models.Rental
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class TempActivity : AppCompatActivity() {

    private var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        firestore = FirebaseFirestore.getInstance()

        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setPrompt("")
        intentIntegrator.setBeepEnabled(true)
        intentIntegrator.captureActivity = ProductRentalCameraActivity::class.java
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //qrcode 가 없으면
            if (result.contents == null) {
                Toast.makeText(this@TempActivity, "취소!", Toast.LENGTH_SHORT).show()
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
                                    }
                                }

                            }
                            else { // 데이터가 없는 경우.
                                var rental = Rental(user1, user2, productId, timeStamp, null)

                                firestore?.collection("Rental")?.document(user1+user2+productId)?.set(rental)?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.v("RENTAL","Success")
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
    }
}