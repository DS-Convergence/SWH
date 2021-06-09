package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Favorite
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.product_form.*


class ProductDetailActivity : AppCompatActivity() {

    private lateinit var tvProdName : TextView
    private lateinit var tvProdCategory : TextView
    private lateinit var tvTime : TextView
    private lateinit var tvStatus : TextView
    private lateinit var tvUser : TextView
    private lateinit var tvUserLocation : TextView
    private lateinit var tvProdDetail : TextView
    private lateinit var tvDeposit : TextView
    private lateinit var tvRentalfee : TextView
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
        tvDeposit = findViewById(R.id.tv_deposit)
        tvRentalfee = findViewById(R.id.tv_rentalfee)
        img = findViewById(R.id.img_prod)

        var btnHeart : ImageButton = findViewById(R.id.btn_heart)
        val btnReport : ImageButton = findViewById(R.id.btn_report)
        val btnSubmenu : TextView = findViewById(R.id.btn_submenu)
        val btnChat : Button = findViewById(R.id.btn_chat)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()


        // intent로 물건 id 정보 넘겨 받아야함!

        var prod = "93rEd9K64U6qLghEq0A8"


        firestore?.collection("Product")?.document(prod)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                task ->
            if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                var product = task.result.toObject(Product::class.java)
                tvProdName.text = product?.productName
                tvProdCategory.text = product?.category
                tvTime.text = product?.uploadTime
                tvStatus.text = product?.status
                tvUser.text = product?.userName
                tvUserLocation.text = product?.region.toString()
                tvProdDetail.text = product?.productDetail

                // 보증금
                if(product?.deposit.equals("")) { // 체크가 안되어 있거나
                    tvDeposit.text = "보증금: 0원"
                }
                else {
                    tvDeposit.text = "보증금: " + product?.deposit + "원"
                }

                // 대여료
                if(product?.rentalFee.equals("")) {
                    tvRentalfee.text = "대여료: 0원"
                }
                else {
                    tvRentalfee.text = "대여료: " + product?.rentalFee + "원"
                }


                userid = product?.userId.toString()

                // 사진 불러오기
                var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                        .load(uri)
                        .into(img)
                    Log.v("IMAGE","Success")

                    // 좋아요 리스트에 있는지 데이터 가져옴
                    firestore?.collection("Favorite")?.document(auth.currentUser!!.uid)?.get()?.addOnCompleteListener {
                            task ->
                        if(task.isSuccessful){
                            var favorite = task.result.toObject(Favorite::class.java)
                            var array = favorite?.products
                            if (array != null) { // array가 null이 아니면
                                for(product in array) { // array에 현재 보고 있는 물건이 있는지 판단한다.
                                    if(product.equals(prod)) {
                                        state = true;
                                        btnHeart.setImageResource(R.drawable.heart_green) // array에 있으면 초록하트로.
                                    }
                                    Log.v("PRODUCTS",product)
                                }

                            }
                            Log.v("HEART","Success"+ state.toString())
                        }else {
                            Log.v("HEART","Failed")
                        }
                    }

                }?.addOnFailureListener { //이미지 로드 실패시
                    Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                    Log.v("IMAGE","failed")
                }


                // 현재 사용자가 글쓴이면 더보기 보이고 아니면 안보이도록
                if(product?.userId.equals(auth.currentUser!!.uid)) { //
                    btnSubmenu.visibility = View.VISIBLE
                    btnHeart.visibility = View.GONE
                    btnChat.visibility = View.GONE
                }
                else {
                    btnSubmenu.visibility = View.GONE
                    btnHeart.visibility = View.VISIBLE
                    btnChat.visibility = View.VISIBLE
                }

            }
        }



        btnHeart.setOnClickListener() {
            // 디폴트는 하얀 하트, 관심상품 등록했으면 초록 하트, 데이터베이스와 연동필요
            // Firebase로 안드로이드 sns 앱 만들기 195-197쪽 참고
            // 맨처음에 계정을 만들 때 Favorite 데이터도 함께 생성시켜야할 것 같음.
            if(!state) {
                btnHeart.setImageResource(R.drawable.heart_green)
                state = true;

                // Favorite 컬렉션에 있는 uid를 이용
                // 하얀색 하트를 누르면 초록색 하트로 변함과 동시에
                // 데이터 베이스에 하트 누른 물건 추가
                firestore?.collection("Favorite")?.document(auth.currentUser!!.uid)?.get()?.addOnCompleteListener {
                        task ->
                    if(task.isSuccessful){
                        var favorite = task.result.toObject(Favorite::class.java)
                        var array : ArrayList<String> = favorite?.products as ArrayList<String>
                        array.add(prod)

                        var map = mutableMapOf<String,Any>()
                        map["products"] = array

                        firestore?.collection("Favorite")?.document(auth.currentUser!!.uid)?.update(map)?.addOnCompleteListener {
                                task ->
                            if(task.isSuccessful) {
                                Log.v("HEART","Update Success")
                                Toast.makeText(applicationContext,"관심물품 추가",Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Log.v("HEART","Update Failed")
                            }
                        }


                        //Log.v("HEART","Success"+ state.toString())
                    }else {
                        //Log.v("HEART","Failed")
                    }
                }

            }
            else {
                btnHeart.setImageResource(R.drawable.heart_white)
                state = false;

                firestore?.collection("Favorite")?.document(auth.currentUser!!.uid)?.get()?.addOnCompleteListener {
                        task ->
                    if(task.isSuccessful){
                        var favorite = task.result.toObject(Favorite::class.java)
                        var array : ArrayList<String> = favorite?.products as ArrayList<String>
                        array.remove(prod)

                        var map = mutableMapOf<String,Any>()
                        map["products"] = array

                        firestore?.collection("Favorite")?.document(auth.currentUser!!.uid)?.update(map)?.addOnCompleteListener {
                                task ->
                            if(task.isSuccessful) {
                                Log.v("HEART","Update Success")
                                Toast.makeText(applicationContext,"관심물품 삭제",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Log.v("HEART","Update Failed")
                            }
                        }


                        //Log.v("HEART","Success"+ state.toString())
                    }else {
                        //Log.v("HEART","Failed")
                    }
                }

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