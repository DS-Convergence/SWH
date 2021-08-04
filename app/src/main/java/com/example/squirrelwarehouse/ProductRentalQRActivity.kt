package com.example.squirrelwarehouse

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.squirrelwarehouse.models.Rental
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.*

class ProductRentalQRActivity : AppCompatActivity() {

    private var imgQRCode: ImageView? = null
    private var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_rental_qr)

        var intent = intent
        var user1 = intent.getStringExtra("userId1").toString()
        var user2 = intent.getStringExtra("userId2").toString()
        var prodId = intent.getStringExtra("productId").toString()

        var firestore = FirebaseFirestore.getInstance()

        imgQRCode = findViewById<View>(R.id.img_qrcode) as ImageView
        //text = "{\"user1\":\"yeeun\",\"user2\":\"yeeun2\",\"productId\":\"pid\"}"

        text = "{"
        text += "\"user1\":\"" + user1 + "\","
        text += "\"user2\":\"" + user2 + "\","
        text += "\"productId\":\"" + prodId + "\""
        text += "}"

        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            imgQRCode!!.setImageBitmap(bitmap)

        } catch (e: Exception) {

        }


        // 찍어도 계속 남아있느게 문제
        // 시간 10초 정도 주고 이전 화면으로 되돌아가게 하거나 해야할 듯
        GlobalScope.launch {
            delay(10000L) // 10초

            // 물건을 반납할 땐 물건 평점 화면으로 넘어가도록
            firestore?.collection("Rental")?.document(user1+user2+prodId)?.get()?.addOnCompleteListener {
                task ->
                if(task.isSuccessful) {
                    var rental = task.result.toObject(Rental::class.java)
                    var rtime= rental?.returnTime // 그냥 이 데이터가 있는 지 알면, 이게 처음 찍는게 아니라는 것을 의미.
                    if(rtime != null) {
                        val intent = Intent(this@ProductRentalQRActivity, UserEvaluationActivity::class.java)
                        startActivityForResult(intent, 0)
                    }
                    else {
                        finish()
                    }
                }
            }
            
        }
        
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 평점페이지에서 다시 돌아오면 finish()
        finish()
    }

}