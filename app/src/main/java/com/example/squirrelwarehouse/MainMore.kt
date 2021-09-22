package com.example.squirrelwarehouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_more.*

class MainMore : AppCompatActivity() {
    private var valList = Array<String>(3) {i->""}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_more)


        // spinner 변수 선언, 아이디 연결
        var sCategory:Spinner = spnCate
        var sCateHob:Spinner = spnHobby
        var sLocation:Spinner = spnLoc
        var sLocDet:Spinner = spnLocDetail
        sLocDet.visibility = View.INVISIBLE

        // var valList = Array<String>(3) {i->""}

        // 메인으로 돌아가기 버튼
        var gotomain = moreBtn
        gotomain.setOnClickListener {
            finish()
        }

        // 알림 버튼
        var noticebtn = noticeBtn
        noticebtn.setOnClickListener {
            // TODO: notice -> listview 로 보여주기
            //startActivityForResult(intent, 0)
        }

        // 설정 버튼
        var settingbtn = settingBtn
        settingbtn.setOnClickListener {
            val intent = Intent(this, MainSettingsActivity::class.java)
            startActivityForResult(intent, 0)
        }

        // 마이페이지 버튼
        var mypagebtn = mypageBtn
        mypagebtn.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivityForResult(intent, 0)
        }

        /*
        var hobtestbtn = hobbytestBtn
        hobtestbtn.setOnClickListener {
            // TODO: 취미테스트 구현
        }
         */

        /*
        // 적용 버튼
        var applybtn = applyBtn
        applybtn.setOnClickListener {
            // spinner 값 적용해서 listview로 보여주기
            var intent = Intent(this, FilteringResult::class.java)
            intent.putExtra("valList", valList)
            startActivityForResult(intent, 0)
            valList.clear()
            finish()
        }
         */



        // 변수에 배열 저장
        val category = resources.getStringArray(R.array.category)
        val catehobby = resources.getStringArray(R.array.cate_hobby)
        val location = resources.getStringArray(R.array.location)

        // adapter 연결
        var adapterCate : ArrayAdapter<String>
        adapterCate = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, category)
        sCategory.adapter = adapterCate

        var adapterHob : ArrayAdapter<String>
        adapterHob = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, catehobby)
        sCateHob.adapter = adapterHob

        var adapterLoc : ArrayAdapter<String>
        adapterLoc = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, location)
        sLocation.adapter = adapterLoc


        // Listener 연결
        sCategory.setSelection(0)
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
                // 항목 선택하면 filtering 해서 리스트로 보여주기
                if(sCategory.selectedItemPosition!=0) {
                    Toast.makeText(applicationContext, sCategory.selectedItem.toString(), Toast.LENGTH_LONG).show()
                    valList[0] = sCategory.selectedItem.toString()
                }
            }
        }

        sCateHob.setSelection(0)
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
                // 항목 선택하면 filtering 해서 리스트로 보여주기
                if(sCateHob.selectedItemPosition!=0) {
                    Toast.makeText(applicationContext, sCateHob.selectedItem.toString(), Toast.LENGTH_LONG).show()
                    valList[1] = sCateHob.selectedItem.toString()
                }
            }
        }

        sLocation.setSelection(0)
        sLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "지역을 선택하세요.", Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(sLocation.selectedItemPosition!=0) {
                    var strLoc = sLocation.selectedItem.toString()
                    // Toast.makeText(applicationContext, strLoc, Toast.LENGTH_LONG).show()
                    // 이 안에서 바꾸는 strarr 은 반영이 안됨
                    var locdetre = setAdapter(strLoc)
                    // valList[2] = locdetre
                }
            }
        }


        // 적용 버튼
        var applybtn = applyBtn
        applybtn.setOnClickListener {
            // spinner 값 적용해서 listview로 보여주기
            var intent = Intent(this, FilteringResult::class.java)
            intent.putExtra("valList", valList)
            startActivityForResult(intent, 0)
            // valList.clear()
            finish()
        }
    }


    fun setAdapter(str:String) : String {
        var sLocDet:Spinner = spnLocDetail
        var adapterLocDe : ArrayAdapter<String>
        var strarr = resources.getStringArray(R.array.loc_special)
        when(str) {
            "특별시" -> { strarr = resources.getStringArray(R.array.loc_special) }
            "광역시" -> { strarr = resources.getStringArray(R.array.loc_gycity) }
            "강원도" -> { strarr = resources.getStringArray(R.array.loc_gwd) }
            "경기도" -> { strarr = resources.getStringArray(R.array.loc_ggd) }
            "경상북도" -> { strarr = resources.getStringArray(R.array.loc_gsbd) }
            "경상남도" -> { strarr = resources.getStringArray(R.array.loc_gsnd) }
            "전라북도" -> { strarr = resources.getStringArray(R.array.loc_jlbd) }
            "전라남도" -> { strarr = resources.getStringArray(R.array.loc_jlnd) }
            "충청북도" -> { strarr = resources.getStringArray(R.array.loc_ccbd) }
            "충청남도" -> { strarr = resources.getStringArray(R.array.loc_ccnd) }
            "제주특별자치도" -> { strarr = resources.getStringArray(R.array.loc_jjd) }
        }
        adapterLocDe = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, strarr)
        sLocDet.adapter = adapterLocDe
        tvLocDetail.visibility = View.VISIBLE
        sLocDet.visibility = View.VISIBLE
        sLocDet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(applicationContext, sLocDet.selectedItem.toString(), Toast.LENGTH_LONG).show()
                valList[2] = sLocDet.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return sLocDet.selectedItem.toString()
    }
}