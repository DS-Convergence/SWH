package com.example.squirrelwarehouse

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_form)

        etProdName = findViewById(R.id.et_prodName)
        etCategory = findViewById(R.id.et_category)
        etProdDetail = findViewById(R.id.et_prodDetail)
        etRentalFee = findViewById(R.id.et_rentalFee)
        etDeposit = findViewById(R.id.et_deposit)

        cbRentalFee = findViewById(R.id.cb_rentalFee)
        cbDeposit = findViewById(R.id.cb_deposit)
        cbLocation = findViewById(R.id.cb_location)

        map = findViewById(R.id.layout_map)

        val btnUpload : Button = findViewById(R.id.btn_upload)
        val backButton : TextView = findViewById(R.id.back_btn)

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

        backButton.setOnClickListener() {
            finish()
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