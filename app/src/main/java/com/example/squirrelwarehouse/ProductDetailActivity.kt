package com.example.squirrelwarehouse

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Favorite
import com.example.squirrelwarehouse.models.Product
import com.example.squirrelwarehouse.models.StayTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.product_detail.*
import kotlinx.coroutines.internal.AddLastDesc
import java.text.SimpleDateFormat
import kotlin.math.ln


class ProductDetailActivity : AppCompatActivity() {

    private lateinit var tvProdName : TextView
    private lateinit var tvProdCategory : TextView
    private lateinit var tvProdCategoryHobby : TextView
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

    private lateinit var prod : String  // 물건 id

    private lateinit var prodUser : String  // 물건 주인 id

    private var start: Long = 0
    private var stop: Long = 0
    private var total: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)

        tvProdName = findViewById(R.id.tv_prodName)
        tvProdCategory = findViewById(R.id.tv_prodCategory)
        tvProdCategoryHobby = findViewById(R.id.tv_prodCategoryHobby)
        tvTime = findViewById(R.id.tv_time)
        tvStatus = findViewById(R.id.tv_status)
        tvUser = findViewById(R.id.tv_user)
        tvUserLocation = findViewById(R.id.tv_userLocation)
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
        val intent = intent
        prod = intent.getStringExtra("data").toString()


        firestore?.collection("Product")?.document(prod)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                task ->
            if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                var product = task.result.toObject(Product::class.java)
                tv_prodName.text = product?.productName
                tv_prodCategory.text = product?.category
                tv_prodCategoryHobby.text = product?.categoryHobby
                tv_status.text = product?.status
                tv_user.text = product?.userName
                tv_userLocation.text = "위치 정보 없음"
                tv_prodDetail.text = product?.productDetail

                tv_bar_prodName.text = product?.productName

                // 시간 데이터 형식 변경
                var sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
                var date = sdf.parse(product?.uploadTime)
                sdf = SimpleDateFormat("yy년 MM월 dd일 HH시 mm분")
                var dateStr = sdf.format(date)
                tv_time.text = dateStr

                var geocoder = Geocoder(this)
                var list : List<Address>? = null
                if(product?.region != null)
                    list = geocoder.getFromLocation(product!!.region!!.latitude, product!!.region!!.longitude, 10)

                if(list != null) {
                    if(list.size!=0) {
                        // https://bitsoul.tistory.com/135 주소 참고
                        //tv_userLocation.text = list.get(0).getAddressLine(0).toString()  // 주소 전체
                        //tv_userLocation.text = list.get(0).adminArea.toString() + " " + list.get(0).locality.toString()+ " " + list.get(0).thoroughfare.toString()
                        Log.v("region",list.get(0).getAddressLine(0).toString())
                        Log.v("region",list.get(0).toString())

                        var str = list.get(0).toString().split(" ")
                        tv_userLocation.text = str[1] + " " + str[2] + " " + str[3]
                    }
                }

                // 보증금
                if(product?.deposit.equals("")) { // 체크가 안되어 있거나
                    tv_deposit.text = "보증금: 0원"
                }
                else {
                    tv_deposit.text = "보증금: " + product?.deposit + "원"
                }

                // 대여료
                if(product?.rentalFee.equals("")) {
                    tv_rentalfee.text = "대여료: 0원"
                }
                else {
                    tv_rentalfee.text = "대여료: " + product?.rentalFee + "원"
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
                // 더보기, 찜하기, 채팅하기, 신고버튼
                if(product?.userId.equals(auth.currentUser!!.uid)) { //
                    btnSubmenu.visibility = View.VISIBLE
                    btnHeart.visibility = View.GONE
                    btnChat.visibility = View.GONE
                    btn_report.visibility = View.GONE
                }
                else {
                    btnSubmenu.visibility = View.GONE
                    btnHeart.visibility = View.VISIBLE
                    btnChat.visibility = View.VISIBLE
                    btn_report.visibility = View.VISIBLE
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

        tvUser.setOnClickListener {
            val intent = Intent(this, UserInfoActivity::class.java)
            intent.putExtra("UserId",userid)
            startActivity(intent)
        }

        back_btn.setOnClickListener {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //finish()
        // 변경된 데이터를 불러오기 위해 자신의 액티비티를 다시 호출
        /*
        var intent = Intent(this, ProductDetailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
*/
        if(requestCode==0) {
            if(resultCode == Activity.RESULT_OK) {
                val intent = Intent()
                //prod = data!!.getStringExtra("TextOut").toString()
                var intent2 = Intent(this, ProductDetailActivity::class.java)
                intent2.putExtra("data",data!!.getStringExtra("TextOut").toString())
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent2)
                finish()
            }
            else if(resultCode == Activity.CONTEXT_INCLUDE_CODE) {
                finish()
            }
            else {
                //finish()
            }
        }


    }

    override fun onStart() {
        super.onStart()
        start = System.currentTimeMillis()
        Log.v("MainActivity", "onStart() "+start)
    }

    override fun onStop() {
        super.onStop()
        stop = System.currentTimeMillis()
        total += (stop - start)
        Log.v("MainActivity", "onStop() "+(stop - start))

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("MainActivity", "onDestroy() " + (total/1000) + "초")

        //auth = FirebaseAuth.getInstance()
        //var map = mutableMapOf<String,Int>()
        // prod 오류나면 여기서 정의 해줘야함.
        //map.put(prod, (total/1000).toInt())
        //var staytime = StayTime(map)

        // 데이터 베이스에 물건 본 시간 넣는 코드
        firestore?.collection("StayTime")?.document(auth.currentUser!!.uid)?.get()?.addOnCompleteListener {
            task ->
            if(task.isSuccessful) {

                var staytime = task.result.toObject(StayTime::class.java)
                var stmap = (staytime?.products as Map<String,Int>).toMutableMap()
                var trmap = (staytime?.transform as Map<String,Int>).toMutableMap()
                if(stmap.containsKey(prod)) { // 이미 있는 물건, 즉 이미 본 물건이라면 본 시간을 업데이트
                    stmap[prod] = stmap.get(prod)!!.toInt() + (total/1000).toInt()
                }
                else { // 처음보는 물건은 그냥 추가
                    if((total/1000).toInt() >= 1)   // 1초이상인 경우에만 데이터베이스에 넣기
                        stmap.put(prod, (total/1000).toInt())
                }

                var map = mutableMapOf<String,Any>()
                map["products"] = stmap


                // 원데이터를 로그변환하는 코드
                if(stmap.containsKey(prod)) { // 원데이터가 있는 경우에만, 1이상인 경우에만 변환적용
                    trmap[prod] = ln(stmap.get(prod)!!.toDouble()).toInt()
                    map["transform"] = trmap
                }


                /*
                // 로그변환 위한 코드임. 일시적으로 필요한 코드. 사용 후 삭제
                var trans = mutableMapOf<String,Int>()
                for(p in stmap) {
                    trans.put(p.key, ln(p.value.toDouble()).toInt())
                    Log.v("trans",p.key + " " + ln(p.value.toDouble()).toInt())
                }

                map["transform"] = trans
                // 사용후 삭제
                */


                firestore?.collection("StayTime")?.document(auth.currentUser!!.uid)?.update(map)?.addOnCompleteListener {
                    task ->
                    if(task.isSuccessful) {
                        Log.v("staytime","Update Success")
                    }
                    else {
                        Log.v("staytime","Update Failed")
                    }
                }
            }
        }


    }
}