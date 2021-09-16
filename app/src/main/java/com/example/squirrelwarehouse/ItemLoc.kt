package com.example.squirrelwarehouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_item_loc.*

class ItemLoc : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_loc)

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
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.65186,127.01615),15f)) // 내 위치로 설정, 현재 덕성여대
        // 내 근처의 아이템 찾아서 표시하기, 참고: https://steemit.com/kr-dev/@gbgg/firebase-5-firebase-1

        /* 모앱코드 참고
        mMap.setOnMapClickListener { point ->
            videoMark = GroundOverlayOptions().image(
                BitmapDescriptorFactory.fromResource(R.drawable.videomark))
                .position(point,100f,100f)
            mMap.addGroundOverlay(videoMark)
        }*/
    }
}
