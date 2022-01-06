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
import java.io.IOException
import java.text.SimpleDateFormat
import kotlin.math.ln


class ProductDetailActivity : AppCompatActivity() {

    private var state = false; // 물건 거래 상황

    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    private lateinit var auth: FirebaseAuth

    private lateinit var userid : String

    private lateinit var prod : String  // 물건 id

    private var start: Long = 0
    private var stop: Long = 0
    private var total: Long = 0

    var viewcount : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()


        // intent로 물건 id 정보 넘겨 받아야함
        val intent = intent
        prod = intent.getStringExtra("data").toString()


        // 넘겨 받은 물건 id를 통해 데이터 가져오기
        firestore?.collection("Product")?.document(prod)?.get()?.addOnCompleteListener {
                task ->
            if(task.isSuccessful) { // 데이터 가져오기를 성공하면

                // product_detail.xml에 데이터 넣기
                var product = task.result.toObject(Product::class.java)
                tv_prodName.text = product?.productName
                tv_prodCategory.text = product?.category
                tv_prodCategoryHobby.text = product?.categoryHobby
                tv_status.text = product?.status
                tv_user.text = product?.userName
                tv_userLocation.text = "위치 정보 오류"
                tv_prodDetail.text = product?.productDetail
                tv_bar_prodName.text = product?.productName


                // 물건 주인 id 저장
                userid = product?.userId.toString()


                // 조회수 가져오기
                viewcount = product?.view


                // 시간 데이터 형식 변경
                var sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
                var date = sdf.parse(product?.uploadTime)
                sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")
                var dateStr = sdf.format(date)
                tv_time.text = dateStr


                // 위도, 경도 데이터 > 한글 주소
                var geocoder = Geocoder(this)
                var list : List<Address>? = null
                if(product?.region != null) {
                    try {
                        list = geocoder.getFromLocation(product!!.region!!.latitude, product!!.region!!.longitude, 10)
                    }
                    catch (e: IOException) {
                        list = null
                    }
                }

                if(list != null) {
                    if(list.size!=0) {
                        // https://bitsoul.tistory.com/135 주소 참고
                        //tv_userLocation.text = list.get(0).getAddressLine(0).toString()  // 주소 전체
                        //tv_userLocation.text = list.get(0).adminArea.toString() + " " + list.get(0).locality.toString()+ " " + list.get(0).thoroughfare.toString()
                        Log.v("region",list.get(0).getAddressLine(0).toString())
                        Log.v("region",list.get(0).toString())

                        var str = list.get(0).toString().split(" ")
                        //var str2 = str[1] + " " + str[2] + " " + str[3]
                        var str2 = ""
                        if(str.size >= 2) str2 += str[1]
                        if(str.size >= 3) str2 += " " + str[2]
                        if(str.size >= 4) str2 += " " + str[3]
                        Log.v("location", str2)

                        if(str2.length <= 40)  // 가끔가다 list.get(0) 전부가 뜨는 경우가 있음. 그것을 방지하기 위함
                            tv_userLocation.text = str2
                    }
                }


                // 보증금
                if(product?.deposit.equals("")) tv_deposit.text = "보증금: 0원"   // 값이 비어있으면 0원
                else tv_deposit.text = "보증금: " + product?.deposit + "원"


                // 대여료
                if(product?.rentalFee.equals("")) tv_rentalfee.text = "대여료: 0원"  // 값이 비어있으면 0원
                else tv_rentalfee.text = "대여료: " + product?.rentalFee + "원"


                // 사진 불러오기
                var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                        .load(uri)
                        .into(img_prod)
                    Log.v("IMAGE","Success")


                    // 좋아요 리스트에 있는지 데이터 가져옴
                    firestore?.collection("Favorite")?.document(auth.currentUser!!.uid)?.get()?.addOnCompleteListener {
                            task ->
                        if(task.isSuccessful){
                            var favorite = task.result.toObject(Favorite::class.java)
                            var array = favorite?.products

                            if (array != null) { // array가 null이 아니면
                                if(array.contains(prod)) { // array에 현재 보고 있는 물건이 있는지 판단
                                    state = true;
                                    btn_heart.setImageResource(R.drawable.heart_green) // array에 있으면 초록하트로.
                                }
                            }
                            Log.v("HEART","Success"+ state.toString())
                        }else {
                            Log.v("HEART","Failed")
                        }
                    }
                }?.addOnFailureListener {  //이미지 로드 실패시
                    Log.v("IMAGE","failed")
                }


                // 더보기, 찜하기, 채팅하기, 신고버튼
                // 현재 사용자가 글쓴이면 더보기 보이고 아니면 안보이도록
                if(product?.userId.equals(auth.currentUser!!.uid)) { //
                    btn_submenu.visibility = View.VISIBLE
                    btn_heart.visibility = View.GONE
                    btn_chat.visibility = View.GONE
                    btn_report.visibility = View.GONE
                }
                else {
                    btn_submenu.visibility = View.GONE
                    btn_heart.visibility = View.VISIBLE
                    btn_chat.visibility = View.VISIBLE
                    btn_report.visibility = View.VISIBLE
                }


                // 대여완료된 물건이면 채팅하기 버튼 숨김
                if(product?.status.equals("대여 종료")) {
                    btn_chat.visibility = View.INVISIBLE
                    //btn_heart.visibility = View.GONE
                    tv_closed.visibility = View.VISIBLE
                }

            }
        }


        // 찜버튼
        btn_heart.setOnClickListener() {
            // 디폴트는 하얀 하트, 관심상품 등록했으면 초록 하트, 데이터베이스와 연동필요
            // Firebase로 안드로이드 sns 앱 만들기 195-197쪽 참고

            if(!state) { // 물건을 찜할 경우
                btn_heart.setImageResource(R.drawable.heart_green)
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
                    }
                }

            }
            else { // 이미 찜한 물건을 해제할 경우
                btn_heart.setImageResource(R.drawable.heart_white)
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
                    }
                }
            }
        }


        // 신고버튼
        btn_report.setOnClickListener() {
            val intent = Intent(this, ReportActivity::class.java)  // 신고 페이지로 이동
            startActivity(intent)
        }


        // 서브메뉴버튼
        btn_submenu.setOnClickListener {
            val intent = Intent(this, ProductDetailSubmenuActivity::class.java)  // ProductDetailSubmenu로 이동
            intent.putExtra("ProductID", prod)
            startActivityForResult(intent, 0)
        }


        // 채팅버튼
        btn_chat.setOnClickListener {
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra("ProductID",prod)
            intent.putExtra("UserId",userid)
            startActivity(intent)
        }


        // 유저이름
        tv_user.setOnClickListener {
            val intent = Intent(this, UserInfoActivity::class.java)  // 물건주인의 마이페이지로 이동
            intent.putExtra("UserId",userid)
            startActivity(intent)
        }


        // 뒤로가기 버튼
        back_btn.setOnClickListener {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0) {
            if(resultCode == Activity.RESULT_OK) {  // 게시글 수정 후 실행되는 코드
                // 변경된 데이터를 불러오기 위해 자신의 액티비티를 다시 호출
                var intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("data",data!!.getStringExtra("TextOut").toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            else if(resultCode == Activity.CONTEXT_INCLUDE_CODE) {  // 게시글 삭제 후 실행되는 코드
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // 페이지에 처음 들어온 시각 저장
        start = System.currentTimeMillis()
        Log.v("MainActivity", "onStart() "+start)
    }

    override fun onStop() {
        super.onStop()

        // 페이지가 꺼지거나 다른 화면에 의해 가려진 시각 저장
        stop = System.currentTimeMillis()
        // 페이지 본 시간 저장
        total += (stop - start)
        Log.v("MainActivity", "onStop() "+(stop - start))

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("MainActivity", "onDestroy() " + (total/1000) + "초")

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
                if(stmap.containsKey(prod)) { // 원데이터가 있는 경우에만, 데이터는 1이상인 데이터만 존재하기 때문에 음수값 나오지 않음.
                    var transValue = ln(stmap.get(prod)!!.toDouble()).toInt()
                    if(transValue > 10)  // 변환된 데이터가 10이 넘어가면 그냥 10을 준다.
                        trmap[prod] = 10
                    else
                        trmap[prod] = transValue
                    map["transform"] = trmap
                }


                // 머무른 시간과 조회수 적용
                if(!auth.currentUser!!.uid.equals(userid)){  // 물건 주인이 현재 사용자이면 머무른 시간과 조회수 모두 적용 x
                    firestore?.collection("StayTime")?.document(auth.currentUser!!.uid)?.update(map)?.addOnCompleteListener {
                        task ->
                        if(task.isSuccessful) {
                            Log.v("staytime","Update Success")

                            var map = mutableMapOf<String?,Any?>()
                            map["view"] = viewcount!!.toInt() + 1  // 조회수 1 추가
                            firestore?.collection("Product")?.document(prod)?.update(map)?.addOnCompleteListener {
                                task ->
                                if(task.isSuccessful) {
                                    Log.v("viewcount","Update Success")
                                }
                            }
                        }
                        else {
                            Log.v("staytime","Update Failed")
                        }
                    }
                }

            }
        }


    }
}