package com.example.squirrelwarehouse

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_item_loc.*
import kotlinx.android.synthetic.main.activity_item_loc.back_btn
import kotlinx.android.synthetic.main.product_detail.*
import java.text.SimpleDateFormat

class ItemLoc : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null

    private var data : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_loc)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        back_btn.setOnClickListener {
            finish()
        }

        // https://webnautes.tistory.com/647 구글지도
        // https://andro-jinu.tistory.com/entry/studio2 네이버지도

        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.main_map, mapFragment)
                .commit()
        /*val mapFragment = supportFragmentManager
            .findFragmentById(R.id.main_map) as SupportMapFragment?*/
        mapFragment!!.getMapAsync(this)

        layout_info.setOnClickListener {
            var intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("data",data)
            startActivityForResult(intent, 0)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.54662807656252, 126.96465941773634), 14.5f)) // 내 위치로 설정, 현재 서울역
        // 내 근처의 아이템 찾아서 표시하기, 참고: https://steemit.com/kr-dev/@gbgg/firebase-5-firebase-1

        /* 모앱코드 참고
        mMap.setOnMapClickListener { point ->
            videoMark = GroundOverlayOptions().image(
                BitmapDescriptorFactory.fromResource(R.drawable.videomark))
                .position(point,100f,100f)
            mMap.addGroundOverlay(videoMark)
        }*/


        // 현재 위치 받아오기
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        //매니페스트에 권한이 추가되어 있다해도 여기서 다시 한번 확인해야함
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@ItemLoc, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            when { //프로바이더 제공자 활성화 여부 체크
                isNetworkEnabled -> {
                    val location =
                            lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음
                    //getLongitude = location?.longitude!!
                    //getLatitude = location.latitude
                    //Toast.makeText(applicationContext, getLongitude.toString()+","+getLatitude.toString(), Toast.LENGTH_SHORT).show()
                    mMap!!.isMyLocationEnabled = true

                }
                isGPSEnabled -> {
                    val location =
                            lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음
                    //getLongitude = location?.longitude!!
                    //getLatitude = location.latitude
                    //Toast.makeText(applicationContext, getLongitude.toString()+","+getLatitude.toString(), Toast.LENGTH_SHORT).show()
                    mMap!!.isMyLocationEnabled = true
                }
                else -> {
                }
            }
            //몇초 간격과 몇미터를 이동했을시에 호출되는 부분 - 주기적으로 위치 업데이트를 하고 싶다면 사용
            // ****주기적 업데이트를 사용하다가 사용안할시에는 반드시 해제 필요****
            /*lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, //몇초
                    1F,   //몇미터
                    gpsLocationListener)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1F,
                    gpsLocationListener)
            //해제부분. 상황에 맞게 잘 구현하자
            lm.removeUpdates(gpsLocationListener)*/

        }
        lm.removeUpdates(gpsLocationListener)


        // 마커
        firestore?.collection("Product")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (querySnapshot == null) return@addSnapshotListener

            // 데이터 받아오기
            for (snapshot in querySnapshot!!.documents) {
                var item = snapshot.toObject(Product::class.java)
                if (item != null) {
                    if(item.region!=null) {
                        var latLng = LatLng(item!!.region!!.latitude, item!!.region!!.longitude)
                        //mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        var marker = mMap!!.addMarker(MarkerOptions().position(latLng))
                        marker.tag = item!!.userId.toString() +"_" + item!!.uploadTime.toString()

                    }

                }

            }

        }


        // 마커 클릭 리스너
        mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker?): Boolean {
                //Toast.makeText(applicationContext,p0!!.tag.toString(),Toast.LENGTH_SHORT).show()

                // 데이터 가져오기
                firestore?.collection("Product")?.document(p0!!.tag.toString())?.get()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var product = task.result.toObject(Product::class.java)

                        layout_info.visibility = View.VISIBLE
                        tv_info_prodName.text = product!!.productName
                        tv_info_status.text = product!!.status
                        tv_info_detail.text = product!!.productDetail

                        // 시간 데이터 형식 변경
                        var sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
                        var date = sdf.parse(product!!.uploadTime)
                        sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")
                        var dateStr = sdf.format(date)
                        tv_info_time.text = dateStr

                        // 인텐트로 넘겨줄 데이터
                        data = product!!.userId.toString() +"_" + product!!.uploadTime.toString()

                        // 사진 불러오기
                        var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(img_info_product)
                            Log.v("IMAGE", "Success")
                        }

                        // 거래상태 배경설정
                        if(product!!.status.equals("대여 전")) {
                            tv_info_status.setTextColor(ContextCompat.getColor(applicationContext,R.color.mocha_brown))
                            tv_info_status.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.rice_white))
                            layout_info.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.asparagus_green))
                        } else if(product!!.status.equals("대여 중")) {
                            tv_info_status.setTextColor(ContextCompat.getColor(applicationContext,R.color.white))
                            tv_info_status.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.asparagus_green))
                            layout_info.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.asparagus_green))
                        } else if(product!!.status.equals("대여 종료")) {
                            tv_info_status.setTextColor(ContextCompat.getColor(applicationContext,R.color.dark_grey))
                            tv_info_status.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.grey))
                            layout_info.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.dark_grey))
                        }
                    }
                }

                return false
            }

        })

        mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng?) {
                layout_info.visibility = View.INVISIBLE
            }

        })

    }

    val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val provider: String = location.provider
            val longitude: Double = location.longitude
            val latitude: Double = location.latitude
            val altitude: Double = location.altitude
        }
        //아래 3개함수는 형식상 필수부분
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }




}
