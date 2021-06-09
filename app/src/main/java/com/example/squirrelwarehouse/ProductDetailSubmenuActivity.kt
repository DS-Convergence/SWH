package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.squirrelwarehouse.models.Product
import com.example.squirrelwarehouse.models.UserModelFS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProductDetailSubmenuActivity : AppCompatActivity() {

    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail_submenu)

        val modify : TextView = findViewById(R.id.tv_modify)
        val remove : TextView = findViewById(R.id.tv_remove)
        val main : View = findViewById(R.id.main)

        val intent = intent
        val text = intent.getStringExtra("ProductID")

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        modify.setOnClickListener {
            // 글쓰기 화면으로. 칸은 정보로 채워져있는 상태
            val intent = Intent(this, ProductFormActivity::class.java)
            intent.putExtra("ModifyProduct", text)
            startActivityForResult(intent, 0)
        }

        remove.setOnClickListener {
            // 삭제

            // 일단 현재 데이터 가져옴.
            firestore?.collection("Product")?.document(text.toString())?.get()?.addOnCompleteListener {  // Product에서 현재 물건 imageURI 가져옴
                    task ->
                if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                    var product = task.result.toObject(Product::class.java)
                    var image = product?.imageURI.toString()

                    // Product 데이터 삭제
                    firestore?.collection("Product")?.document(text.toString())?.delete()?.addOnCompleteListener {
                            task ->
                        if(task.isSuccessful) {

                            // Storage에 있는 사진 데이터 삭제
                            var storageRef = storage?.reference?.child("product")?.child(image)
                            storageRef?.delete()?.addOnCompleteListener {
                                task ->
                                if(task.isSuccessful) {
                                    Log.v("DELETE","Success")
                                    finish()
                                    // 메인화면으로 넘어가야함.
                                }
                            }
                        }
                    }
                }
            }

        }

        main.setOnClickListener() {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        finish()

        // 물건을 수정하고는 ProductDetialActivity로 가야하고
        // 물건을 삭제할때는 메인화면으로 돌아가야 함.

    }
}