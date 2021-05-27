package com.example.squirrelwarehouse

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.protobuf.LazyStringArrayList
import kotlinx.android.synthetic.main.main_more.*

class MainMore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_more)

        // spinner 변수 선언, 아이디 연결
        var sCategory:Spinner = spnCate
        var sCateHob:Spinner = spnHobby
        var sLocation:Spinner = spnLoc
        var sLocDet:Spinner = spnLocDetail
        //sCategory.findViewById<Spinner>(R.id.spnCate)
        //sLocation.findViewById<Spinner>(R.id.spnLoc)

        // 변수에 배열 저장
        val category = resources.getStringArray(R.array.category)
        val catehobby = resources.getStringArray(R.array.cate_hobby)
        val location = resources.getStringArray(R.array.location)

        // adapter 연결
        var adapterCate: ArrayAdapter<String>
        adapterCate = ArrayAdapter(this, R.layout.main_more, category)
        sCategory.adapter = adapterCate

        var adapterHob:ArrayAdapter<String>
        adapterHob = ArrayAdapter(this, R.layout.main_more, catehobby)
        sCateHob.adapter = adapterHob

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

        sCateHob.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        var adapterLocDe: ArrayAdapter<String>
        lateinit var locdetail : Array<String>
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
                val text: String = sLocation.getSelectedItem().toString()
                when(text) {
                    "특별시" -> { locdetail = resources.getStringArray(R.array.loc_special) }
                    "광역시" -> { locdetail = resources.getStringArray(R.array.loc_gycity) }
                    "강원도" -> { locdetail = resources.getStringArray(R.array.loc_gwd) }
                    "경기도" -> { locdetail = resources.getStringArray(R.array.loc_ggd) }
                    "경상북도" -> { locdetail = resources.getStringArray(R.array.loc_gsbd) }
                    "경상남도" -> { locdetail = resources.getStringArray(R.array.loc_gsnd) }
                    "전라북도" -> { locdetail = resources.getStringArray(R.array.loc_jlbd) }
                    "전라남도" -> { locdetail = resources.getStringArray(R.array.loc_jlnd) }
                    "충청북도" -> { locdetail = resources.getStringArray(R.array.loc_ccbd) }
                    "충청남도" -> { locdetail = resources.getStringArray(R.array.loc_ccnd) }
                    "제주특별자치도" -> { locdetail = resources.getStringArray(R.array.loc_jjd) }
                }
            }
        }

        adapterLocDe = ArrayAdapter(this, R.layout.main_more, locdetail)
        sLocDet.adapter = adapterLocDe
    }
}