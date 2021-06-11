package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.main_itemview.*
import kotlinx.android.synthetic.main.main_page.*


class MainPageActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        // 버튼 기능 구현
        var gotoMore = gotomore
        gotoMore.setOnClickListener {
            val intent = Intent(this, MainMore::class.java)
            startActivityForResult(intent, 0)
        }

        var gotoChat = gotochat
        gotoChat.setOnClickListener {
            val intent = Intent(this, LatestMessageActivity::class.java)
            startActivityForResult(intent, 0)
        }

        var gotoMap = gotomap
        gotoMap.setOnClickListener {
            val intent = Intent(this, ItemLoc::class.java)
            startActivityForResult(intent, 0)
        }

        var writeBtn = writeBtn
        writeBtn.setOnClickListener {
            val intent = Intent(this, ProductFormActivity::class.java)
            startActivityForResult(intent, 0)
        }

        var searchBar = searchBar
        searchBar.setOnClickListener {
            searchBar.queryHint = "검색어를 입력하세요."

        }
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchItem(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })



        // 메인페이지 물품 보기
        // TODO: 메인페이지 리사이클러뷰 내용 넣기
        // https://hijjang2.tistory.com/313
        // https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=cosmosjs&logNo=221050368244

        // 업데이트
        // 리사이클러뷰:
/*
        updateView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        var updateAdapter : ItemAdapter = ItemAdapter(this)
        var updateList : ArrayList<HashMap<String, String>> = dataInArray()

        var updateList:ArrayList<MainItem> = arrayListOf()
        for(i in 1..5) {
            item = MainItem("","최신"+i)
            updateList.add(item)
        }
        updateView.adapter = MainViewAdapter(updateList)

        // updateAdapter.items.clear()
        // updateView.adapter = updateAdapter

        // 업데이트 - 더보기
        var moreUpdate = moreUpdate
        moreUpdate.setOnClickListener {
            val intent = Intent(this, UpdateMoreActivity::class.java)
            intent.putExtra("updateList",updateList)
            startActivityForResult(intent, 0)
        }
 */
        // 업데이트
        // 리사이클러뷰: updateView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        // var updateAdapter : MainViewAdapter = MainViewAdapter()
        var updateList : ArrayList<HashMap<String, String>> = dataInArray()

        // updateAdapter.items.clear()
        // updateView.adapter = updateAdapter

        // 업데이트 - 더보기
        var moreUpdate = moreUpdate
        moreUpdate.setOnClickListener {
            val intent = Intent(this, UpdateMoreActivity::class.java)
            intent.putExtra("updateList",updateList)
            startActivityForResult(intent, 0)
        }


        // 카테고리 : 지정 카테고리 항목 보여주기
        /*var usrCategory:String = "운동"  // 추천 알고리즘 결과 상위 카테고리로 설정
        cateTextView.text = "#"+usrCategory
        //cateView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        var cateAdapter : MainViewAdapter = MainViewAdapter()
        cateAdapter.items.clear()
        var cateList:ArrayList<MainItem> = arrayListOf()
        /*for(i in 1..5) {
            item = MainItem("","카테고리"+i)
            // item = MainItem(지정 카테고리 상위 아이템 3개 받아서 저장)
            cateList.add(item)
        }*/
        //lateinit var item : MainItem
        var prodId = "93rEd9K64U6qLghEq0A8"
        lateinit var prdName : String
        firestore?.collection("Product")?.document(prodId)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
            task ->
            if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                var product = task.result.toObject(Product::class.java)
                item.name = product?.productName.toString()

                // 사진 불러오기
                var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                        .load(uri)
                        .into(thumb)
                    Log.v("IMAGE","Success")
                }?.addOnFailureListener { //이미지 로드 실패시
                    Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                    Log.v("IMAGE","failed")
                }
            }
            else {
                Toast.makeText(applicationContext,"데이터 로드 실패", Toast.LENGTH_LONG).show()
            }
        }
        cateAdapter.items.add(item)
        cateAdapter.items.add(MainItem("","카테고리2"))
        cateAdapter.items.add(MainItem("","카테고리3"))
        //cateView.adapter = cateAdapter
        */

        // 카테고리 - 더보기
        var moreCate = moreCate
        moreCate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // listView로 연결

            }
        })


        // 추천
        /*//rcmdView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        var rcmdAdapter : MainViewAdapter = MainViewAdapter()
        //var rcmdList:ArrayList<MainItem> = arrayListOf()
        rcmdAdapter.items.clear()
        for(i in 1..5) {
            item = MainItem("","추천"+i)
            // item = MainItem(추천 알고리즘 결과의 상위 아이템 3개 받아서 저장)
            //rcmdList.add(item)
            rcmdAdapter.items.add(item)
        }
        //rcmdView.adapter = rcmdAdapter
        */

        // 추천 - 더보기
        var moreRcmd = moreRcmd
        moreRcmd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // listView로 연결
            }
        })
    }

    private fun searchItem(query: String) {}

    fun dataInArray(): ArrayList<HashMap<String, String>> {
        firestore = FirebaseFirestore.getInstance()
        val data = firestore?.collection("Product")
        val itemList = arrayListOf<HashMap<String, String>>()

        itemList.clear()
        if (data != null) {
            data.get().addOnSuccessListener { prodId ->
                for(document in prodId) {
                    val item = hashMapOf<String, String>((document.id to document["productName"]) as Pair<String, String>)
                    itemList.add(item)
                }
            }.addOnFailureListener { exception ->
                Log.w("MainPageActivity", "Error getting documents")
            }
        }
        return itemList
    }

    /*
    fun loadItem(dataSnapshot: DataSnapshot, adapter: MainViewAdapter) {
        val prodIterator = dataSnapshot.children.iterator()
        if (prodIterator.hasNext()) {
            adapter.items.clear()
            val product = prodIterator.next()
            val itemsIterator = product.children.iterator()
            while (itemsIterator.hasNext()) {
                val currentItem = itemsIterator.next()
                val map = currentItem.value as HashMap<String, Any>
                //val userId = map["userId"].toString()
                //val author = map["userName"].toString()
                //val category = map["category"].toString()
                val thumb = map["imageUri"].toString()
                val name = map["productName"].toString()
                //val time = Date(map["uploadTime"] as Long).toString()

                adapter.items.add(MainItem(thumb,name))
            }
        }
    }
     */

}
