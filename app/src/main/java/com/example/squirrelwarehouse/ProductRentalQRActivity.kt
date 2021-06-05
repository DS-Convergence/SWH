package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

class ProductRentalQRActivity : AppCompatActivity() {

    private var imgQRCode: ImageView? = null
    private var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_rental_qr)

        imgQRCode = findViewById<View>(R.id.img_qrcode) as ImageView
        text = "{\"user1\":\"yeeun\",\"user2\":\"yeeun2\",\"productId\":\"pid\"}"

        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            imgQRCode!!.setImageBitmap(bitmap)

            // 찍어도 계속 남아있느게 문제
            // 시간 10초 정도 주고 이전 화면으로 되돌아가게 하거나 해야할 듯

        } catch (e: Exception) {
        }

    }
}