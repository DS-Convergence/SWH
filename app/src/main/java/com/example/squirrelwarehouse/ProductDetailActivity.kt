package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.target.Target
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class ProductDetailActivity : AppCompatActivity() {

    private lateinit var tvProdName : TextView
    private lateinit var tvProdCategory : TextView
    private lateinit var tvTime : TextView
    private lateinit var tvStatus : TextView
    private lateinit var tvUser : TextView
    private lateinit var tvUserLocation : TextView
    private lateinit var tvProdDetail : TextView
    private lateinit var tvMoney : TextView
    private var state = false;
    private lateinit var img : ImageView

    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)

        tvProdName = findViewById(R.id.tv_prodName)
        tvProdCategory = findViewById(R.id.tv_prodCategory)
        tvTime = findViewById(R.id.tv_time)
        tvStatus = findViewById(R.id.tv_time)
        tvUser = findViewById(R.id.tv_user)
        tvUserLocation = findViewById(R.id.tv_uesrLocation)
        tvProdDetail = findViewById(R.id.tv_prodDetail)
        tvMoney = findViewById(R.id.tv_money)
        img = findViewById(R.id.img_prod)

        var btnHeart : ImageButton = findViewById(R.id.btn_heart)
        val btnReport : ImageButton = findViewById(R.id.btn_report)
        val btnSubmenu : TextView = findViewById(R.id.btn_submenu)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        firestore?.collection("Product")?.document("K4cXhtjOFzbfQ2KtXGSg")?.get()?.addOnCompleteListener {
            task ->
            if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                var product = task.result.toObject(Product::class.java)
                tvProdName.text = product?.ProductName
                tvProdCategory.text = product?.Category
                tvTime.text = product?.UploadTime
                //tvStatus.text = product?
                tvUser.text = product?.UserName
                tvUserLocation.text = product?.Region.toString()
                tvProdDetail.text = product?.ProductDetail
                // 보증금
                // 대여료

                // 사진 불러오기
                var storageRef = storage?.reference?.child("product")?.child(product?.ImageURI.toString())
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                            .load(uri)
                            .into(img)
                    Log.v("IMAGE","Success")
                }?.addOnFailureListener { //이미지 로드 실패시
                    Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                    Log.v("IMAGE","failed")
                }


                // 현재 사용자가 글쓴이면 더보기 보이고 아니면 안보이도록


            }
        }



        btnHeart.setOnClickListener() {
            // 디폴트는 하얀 하트, 관심상품 등록했으면 초록 하트, 데이터베이스와 연동필요
            // Firebase로 안드로이드 sns 앱 만들기 195-197쪽 참고
            if(!state) {
                btnHeart.setImageResource(R.drawable.heart_green)
                state = true;
            }
            else {
                btnHeart.setImageResource(R.drawable.heart_white)
                state = false;
            }
        }
        val btnChat : Button = findViewById(R.id.btn_chat)

        btnReport.setOnClickListener() {
            // 신고 페이지로 이동
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        btnSubmenu.setOnClickListener {
            // ProductDetailSubmenu로 이동
            val intent = Intent(this, ProductDetailSubmenuActivity::class.java)
            startActivity(intent)
        }

    }
}