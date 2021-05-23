package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

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

        // Listener 연결
        sCategory.setSelection(1)
        sCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "카테고리를 선택해주세요.", Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // TODO: 항목 선택하면 filtering 해서 리스트로 보여주기
            }
        }

        sLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "지역을 선택해주세요.", Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // TODO: 항목 선택하면 filtering 해서 리스트로 보여주기
            }
        }
    }


}