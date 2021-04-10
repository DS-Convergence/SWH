package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainMore : AppCompatActivity() {
    lateinit var sCategory : Spinner; lateinit var sLocation : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_more)

        sCategory.findViewById<Spinner>(R.id.spnCate)
        sLocation.findViewById<Spinner>(R.id.spnLoc)

        val category = resources.getStringArray(R.array.category)
        val location = resources.getStringArray(R.array.location)

        var adapterCate: ArrayAdapter<String>
        adapterCate = ArrayAdapter(this, R.layout.main_more, category)
        sCategory.adapter = adapterCate

        var adapterLoc : ArrayAdapter<String>
        adapterLoc = ArrayAdapter(this, R.layout.main_more, location)
        sLocation.adapter = adapterLoc

        // 항목 선택하면 filtering 해서 리스트로 보여주기
    }
}