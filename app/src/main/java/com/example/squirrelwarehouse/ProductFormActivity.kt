package com.example.squirrelwarehouse

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

class ProductFormActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var etProdName : EditText
    private lateinit var etCategory : EditText
    private lateinit var etProdDetail : EditText
    private lateinit var etRentalFee : EditText
    private lateinit var etDeposit : EditText

    private lateinit var cbRentalFee : CheckBox
    private lateinit var cbDeposit : CheckBox
    private lateinit var cbLocation : CheckBox

    private lateinit var map : FrameLayout
    private lateinit var img : ImageView

    private var mMap: GoogleMap? = null

    private var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_form)

        // 변수 초기화
        etProdName = findViewById(R.id.et_prodName)
        etCategory = findViewById(R.id.et_category)
        etProdDetail = findViewById(R.id.et_prodDetail)
        etRentalFee = findViewById(R.id.et_rentalFee)
        etDeposit = findViewById(R.id.et_deposit)

        cbRentalFee = findViewById(R.id.cb_rentalFee)
        cbDeposit = findViewById(R.id.cb_deposit)
        cbLocation = findViewById(R.id.cb_location)

        map = findViewById(R.id.layout_map)
        img = findViewById(R.id.img)

        firestore = FirebaseFirestore.getInstance()

        val btnUpload : Button = findViewById(R.id.btn_upload)
        val btnBack : TextView = findViewById(R.id.back_btn)
        val btnImg : Button = findViewById(R.id.btn_img)


        // 지도
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


        // 지도안에서 터치가 일어나면 스크롤뷰는 움직이지 않도록 설정함.
        val sv = findViewById<ScrollView>(R.id.sv)
        val containerMap = findViewById<TouchFrameLayout>(R.id.layout_map)
        containerMap.setTouchListener(object : TouchFrameLayout.OnTouchListener {
            override fun onTouch() {
                sv.requestDisallowInterceptTouchEvent(true)
            }
        })


        // 대여료 체크박스가 체크되어야 금액 작성 가능.
        cbRentalFee.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) etRentalFee.isEnabled = true
            else etRentalFee.isEnabled = false
        }

        // 보증금 체크박스가 체크되어야 금액 작성 가능
        cbDeposit.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) etDeposit.isEnabled = true
            else etDeposit.isEnabled = false
        }

        // 위치 체크박스가 체크되어야 지도 보임.
        cbLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) map.visibility = View.VISIBLE
            else map.visibility = View.GONE

        }

        // 뒤로가기 버튼
        btnBack.setOnClickListener() {
            finish()
        }

        // 사진을 가져오는 버튼
        btnImg.setOnClickListener {
            //이미지 불러오기기(갤러리 접근)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, 0) //PICK_IMAGE에는 본인이 원하는 상수넣으면된다.
        }

        // 글 업로드 버튼
        btnUpload.setOnClickListener {
            var pName = etProdName.text.toString()
            var pCate = etCategory.text.toString()
            var pDetail = etProdDetail.text.toString()
            var pDeposit = etDeposit.text.toString()
            var pRental = etRentalFee.text.toString()


            var product = Product("userid","userName",pName, pCate, pDetail, null, pDeposit, pRental, null,null)
            firestore?.collection("Product")?.document()?.set(product)?.addOnCompleteListener {
                task ->
                if(task.isSuccessful) {
                    // 프로세스가 성공했을 경우 코드 입력
                }
            }

        }


    }

    //갤러리에서 이미지 불러온 후 행동
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                try {
                    // 선택한 이미지를 가져옴.
                    // 사진이 돌아가는 문제가 발생하여 Glide를 이용함.
                    val uri = data!!.data
                    Glide.with(this).load(uri).into(img)
                    img.visibility = View.VISIBLE

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val SEOUL = LatLng(37.56, 126.97)
        val markerOptions = MarkerOptions()
        markerOptions.position(SEOUL)
        markerOptions.title("서울")
        markerOptions.snippet("한국의 수도")
        mMap!!.addMarker(markerOptions)


        // 기존에 사용하던 다음 2줄은 문제가 있습니다.

        // CameraUpdateFactory.zoomTo가 오동작하네요.
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10f))
    }
}


// 지도를 확대하는데 스크롤이 움직이는 현상을 막기 위함.
class TouchFrameLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var listener: OnTouchListener? = null

    interface OnTouchListener {
        fun onTouch()
    }

    fun setTouchListener(listener: OnTouchListener) {
        this.listener = listener
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> listener?.onTouch()
        }
        return super.dispatchTouchEvent(event)
    }
}