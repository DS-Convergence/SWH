package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

    private lateinit var userid : String

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
        val btnChat : Button = findViewById(R.id.btn_chat)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()


        // intent로 물건 id 정보 넘겨 받아야함!

        var prod = "LciNSbXgkv7TQq3gDrj4"


        firestore?.collection("Product")?.document(prod)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                task ->
            if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                var product = task.result.toObject(Product::class.java)
                tvProdName.text = product?.productName
                tvProdCategory.text = product?.category
                tvTime.text = product?.uploadTime
                //tvStatus.text = product?
                tvUser.text = product?.userName
                tvUserLocation.text = product?.region.toString()
                tvProdDetail.text = product?.productDetail
                // 보증금
                // 대여료

                userid = product?.userId.toString()

                // 사진 불러오기
                var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
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
                if(product?.userId.equals(auth.currentUser!!.uid)) { //
                    btnSubmenu.visibility = View.VISIBLE
                }
                else {
                    btnSubmenu.visibility = View.GONE
                }


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


        btnReport.setOnClickListener() {
            // 신고 페이지로 이동
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        btnSubmenu.setOnClickListener {
            // ProductDetailSubmenu로 이동
            val intent = Intent(this, ProductDetailSubmenuActivity::class.java)
            intent.putExtra("ProductID", prod)
            startActivityForResult(intent, 0)
        }

        btnChat.setOnClickListener {
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra("ProductID",prod)
            intent.putExtra("UserId",userid)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //finish()
        // 변경된 데이터를 불러오기 위해 자신의 액티비티를 다시 호출
        var intent = Intent(this, ProductDetailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()


    }

}