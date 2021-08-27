package com.example.squirrelwarehouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Favorite
import com.example.squirrelwarehouse.models.Product
import com.example.squirrelwarehouse.models.UserModelFS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.main_itemview.view.*
import kotlinx.android.synthetic.main.main_page.*
import kotlinx.android.synthetic.main.product_detail.*


class MainPageActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var firestore = FirebaseFirestore.getInstance()
    private var storage = FirebaseStorage.getInstance()
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
        // TODO: 지도 시작
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
        searchBar.queryHint = "검색어를 입력하세요"
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchItem(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        // 파워람쥐 뷰페이저 구현
        val vpAdapter = MainViewpagerAdapter()
        VPpoweruser.adapter = vpAdapter



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
        // var updateList : ArrayList<HashMap<String, String>> = dataInArray()

        // 추천테스트: var updateList = UserBasedRcmd("user_ifbnimzN2RM61ZfbfeJ48ZBdu9j2").getRcmd()

        // updateAdapter.items.clear()
        // updateView.adapter = updateAdapter

        // 업데이트 - 더보기
        var moreUpdate = moreUpdate
        moreUpdate.setOnClickListener {
            val intent = Intent(this, UpdateMoreActivity::class.java)
            //intent.putExtra("updateList",updateList)
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
        var ubr = UserBasedRcmd("user_" + FirebaseAuth.getInstance().currentUser!!.uid.toString())  // 현재 유저 아이디 필요
        moreRcmd.setOnClickListener {
            //Log.v("RcmdList", UserBasedRcmd("user_ifbnimzN2RM61ZfbfeJ48ZBdu9j2").getRcmd().toString())
            //var ubr = UserBasedRcmd("user_l0kyyYR3SNfT1zJsdrAvHYy6M3J2")  // 현재 유저 아이디 필요
            ubr.getRcmd()
        }



        // 예은 실험

        //var updateList : ArrayList<Product> = dataLoading()

        //ct_title1.text = updateList.get(0).productName.toString()
        //ct_title2.text = products.get(1).productName
        //ct_title3.text = products.get(2).productName

        // 일단 물건 id로 하나씩 가져오는 건 성공
        /*
        firestore?.collection("Product")?.document("5Bf4S5mm7hRhvu3LbdPUbCI8hMh1_20210612_144047")?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
            task ->
            if(task.isSuccessful) {
                var product = task.result.toObject(Product::class.java)
                up_title1.text = product?.productName

                var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                            .load(uri)
                            .into(up_thbm1)
                    Log.v("IMAGE", "Success")
                }?.addOnFailureListener { //이미지 로드 실패시
                    Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                    Log.v("IMAGE","failed")
                }
            }

        }
         */


        // 성공,,,,,,
        // 최신 업데이트
        var products = ArrayList<Product>()
        firestore?.collection("Product")?.orderBy("uploadTime", Query.Direction.DESCENDING)?.limit(3)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    //products.clear()
                    if (querySnapshot == null) return@addSnapshotListener

                    // 데이터 받아오기
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Product::class.java)
                        if (item != null) {
                            products.add(item)
                            Log.v("PRODUCTS",products.size.toString())
                            Log.v("PRODUCTS",item.productName.toString())
                        }

                    }

                    // 일단 다 invisible
                    up_title1.visibility = View.INVISIBLE
                    up_title2.visibility = View.INVISIBLE
                    up_title3.visibility = View.INVISIBLE

                    // 데이터가 3개보다 적을 수 있기 때문에 if문을 이렇게 작성함
                    // 이것보다 더 좋은 방법이 있다면 그거 사용해도 무방.
                    if(products.size >= 1) {
                        up_title1.visibility = View.VISIBLE

                        up_title1.text = products.get(0).productName.toString()

                        var storageRef = storage?.reference?.child("product")?.child(products.get(0).imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(up_thbm1)
                            Log.v("IMAGE","Success")
                        }
                    }
                    if(products.size >= 2) {
                        up_title2.visibility = View.VISIBLE

                        up_title2.text = products.get(1).productName.toString()

                        var storageRef = storage?.reference?.child("product")?.child(products.get(1).imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(up_thbm2)
                            Log.v("IMAGE","Success")
                        }
                    }
                    if(products.size >= 3) {
                        up_title3.visibility = View.VISIBLE

                        up_title3.text = products.get(2).productName.toString()

                        var storageRef = storage?.reference?.child("product")?.child(products.get(2).imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(up_thbm3)
                            Log.v("IMAGE","Success")
                        }
                    }
                }


        update1.setOnClickListener {
            var intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("data",products!![0].userId+"_"+products!![0].uploadTime)
            startActivityForResult(intent, 0)
        }

        update2.setOnClickListener {
            var intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("data",products!![1].userId+"_"+products!![1].uploadTime)
            startActivityForResult(intent, 0)
        }

        update3.setOnClickListener {
            var intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("data",products!![2].userId+"_"+products!![2].uploadTime)
            startActivityForResult(intent, 0)
        }




        // 특정 카테고리 물품만
        var cateProds = ArrayList<Product>()
        var category = "디지털/가전"
        cateTextView.text = category
        firestore?.collection("Product")?.whereEqualTo("category", category)?.orderBy("uploadTime", Query.Direction.DESCENDING)?.limit(3)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    //cateProds.clear()
                    if (querySnapshot == null) return@addSnapshotListener

                    // 데이터 받아오기
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Product::class.java)
                        if (item != null) {
                            cateProds.add(item)
                            Log.v("PRODUCTS", cateProds.size.toString())
                            Log.v("PRODUCTS", item.productName.toString())
                        }
                    }

                    ct_title1.visibility = View.INVISIBLE
                    ct_title2.visibility = View.INVISIBLE
                    ct_title3.visibility = View.INVISIBLE

                    if(cateProds.size >= 1) {
                        ct_title1.visibility = View.VISIBLE

                        ct_title1.text = cateProds.get(0).productName.toString()

                        var storageRef = storage?.reference?.child("product")?.child(cateProds.get(0).imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(ct_thbm1)
                            Log.v("IMAGE","Success")
                        }
                    }
                    if(cateProds.size >= 2) {
                        ct_title2.visibility = View.VISIBLE

                        ct_title2.text = cateProds.get(1).productName.toString()

                        var storageRef = storage?.reference?.child("product")?.child(cateProds.get(1).imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(ct_thbm2)
                            Log.v("IMAGE","Success")
                        }
                    }
                    if(cateProds.size >= 3) {
                        ct_title3.visibility = View.VISIBLE

                        ct_title3.text = cateProds.get(2).productName.toString()

                        var storageRef = storage?.reference?.child("product")?.child(cateProds.get(2).imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(ct_thbm3)
                            Log.v("IMAGE","Success")
                        }
                    }



                }

        // 여기서 오류남. ArrayList가 또 0이라고함. 위에꺼는 안그런데,,
        // https://stackoverflow.com/questions/50123649/does-tasks-whenallsuccess-guarantee-the-order-in-which-i-pass-tasks-to-it 이거 해보기
        // 아무래도 생명주기와 관련이 있지 않을가,,,,, 아닌듯,,,
        cate1.setOnClickListener {
            var intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("data",cateProds!![0].userId+"_"+cateProds!![0].uploadTime)
            startActivityForResult(intent, 0)
        }
        cate2.setOnClickListener {
            var intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("data",cateProds!![1].userId+"_"+cateProds!![1].uploadTime)
            startActivityForResult(intent, 0)
        }
        cate3.setOnClickListener {
            var intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("data", cateProds!![2].userId + "_" + cateProds!![2].uploadTime)
            startActivityForResult(intent, 0)
        }

/*
        // 추천목록 메인에서만 일단 보여주기
        firestore?.collection("Product")?.document("YDNw0730r1aJzFZW4dvvzSNtfsV2_20210612_143229")?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
            task ->
            if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                var product = task.result.toObject(Product::class.java)
                rc_title1.text = product?.productName
                // 사진 불러오기
                var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                            .load(uri)
                            .into(rc_thbm1)
                    Log.v("IMAGE", "Success")
                }?.addOnFailureListener { //이미지 로드 실패시
                    Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                    Log.v("IMAGE", "failed")
                }
            }
        }

        firestore?.collection("Product")?.document("YDNw0730r1aJzFZW4dvvzSNtfsV2_20210612_143256")?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
            task ->
            if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                var product = task.result.toObject(Product::class.java)
                rc_title2.text = product?.productName
                // 사진 불러오기
                var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                            .load(uri)
                            .into(rc_thbm2)
                    Log.v("IMAGE", "Success")
                }
            }
        }

        firestore?.collection("Product")?.document("YDNw0730r1aJzFZW4dvvzSNtfsV2_20210612_143229")?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
            task ->
            if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                var product = task.result.toObject(Product::class.java)
                rc_title3.text = product?.productName
                // 사진 불러오기
                var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                            .load(uri)
                            .into(rc_thbm3)
                    Log.v("IMAGE", "Success")
                }
            }
        }

        firestore?.collection("Product")?.document("nqOPrU4ZcTfI1xoKlapXjjvFOXE2_20210612_142809")?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
            task ->
            if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                var product = task.result.toObject(Product::class.java)
                rc_title1.text = product?.productName
                // 사진 불러오기
                var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    Glide.with(applicationContext)
                            .load(uri)
                            .into(rc_thbm1)
                    Log.v("IMAGE", "Success")
                }
            }
        }
*/
        /*firestore?.collection("Product")?.orderBy("uploadTime", Query.Direction.DESCENDING)?.limit(3)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    //products.clear()
                    if (querySnapshot == null) return@addSnapshotListener

                    // 데이터 받아오기
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Product::class.java)
                        if (item != null) {
                            products.add(item)
                            Log.v("PRODUCTS",products.size.toString())
                            Log.v("PRODUCTS",item.productName.toString())
                        }

                    }

                    // 일단 다 invisible
                    up_title1.visibility = View.INVISIBLE
                    up_title2.visibility = View.INVISIBLE
                    up_title3.visibility = View.INVISIBLE

                    // 데이터가 3개보다 적을 수 있기 때문에 if문을 이렇게 작성함
                    // 이것보다 더 좋은 방법이 있다면 그거 사용해도 무방.
                    if(products.size >= 1) {
                        up_title1.visibility = View.VISIBLE

                        up_title1.text = products.get(0).productName.toString()

                        var storageRef = storage?.reference?.child("product")?.child(products.get(0).imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(up_thbm1)
                            Log.v("IMAGE","Success")
                        }
                    }
                    if(products.size >= 2) {
                        up_title2.visibility = View.VISIBLE

                        up_title2.text = products.get(1).productName.toString()

                        var storageRef = storage?.reference?.child("product")?.child(products.get(1).imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(up_thbm2)
                            Log.v("IMAGE","Success")
                        }
                    }
                    if(products.size >= 3) {
                        up_title3.visibility = View.VISIBLE

                        up_title3.text = products.get(2).productName.toString()

                        var storageRef = storage?.reference?.child("product")?.child(products.get(2).imageURI.toString())
                        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(up_thbm3)
                            Log.v("IMAGE","Success")
                        }
                    }
                }
         */
    }



    private fun searchItem(query: String) {
        var intent = Intent(this, SearchResult::class.java)
        startActivityForResult(intent, 0)
    }

    fun dataInArray(): ArrayList<HashMap<String, String>> {

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

    fun dataLoading() : ArrayList<Product> {
        var products: ArrayList<Product> = arrayListOf()
        firestore?.collection("Product")?.orderBy("uploadTime", Query.Direction.DESCENDING)?.limit(3)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    products.clear()
                    if (querySnapshot == null) return@addSnapshotListener

                    // 데이터 받아오기
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Product::class.java)
                        if (item != null) {
                            products.add(item)
                            Log.v("PRODUCTS",products.size.toString())
                            Log.v("PRODUCTS",item.productName.toString())
                        }

                    }
                }

        return products
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

    inner class ViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var products: ArrayList<Product> = arrayListOf()

        init {
            firestore?.collection("Product")?.orderBy("uploadTime", Query.Direction.DESCENDING)?.limit(3)
                    ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        products.clear()
                        if (querySnapshot == null) return@addSnapshotListener

                        // 데이터 받아오기
                        for (snapshot in querySnapshot!!.documents) {
                            var item = snapshot.toObject(Product::class.java)
                            products.add(item!!)
                        }
                        notifyDataSetChanged()
                    }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =
                    LayoutInflater.from(parent.context).inflate(R.layout.main_itemview, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun getItemCount(): Int {
            return products.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.itemName.text = products!![position].productName



            // 사진 불러오기
            /*
            var storageRef = storage?.reference?.child("product")?.child(products!![position].imageURI.toString())
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                        .load(uri)
                        .into(viewHolder.thumb)
                //Log.v("IMAGE","Success")

            }

             */
            viewHolder.setOnClickListener {

                // 이거를 쓰려면 product이름을 바꿔야함. userid+uploadTime 이런식으로로
                // 지금은 오류남. 암튼 이 코드로 했을 때 다음 페이지로 넘어가는 건 확실함. 해봄.
                Intent(this@MainPageActivity, ProductDetailActivity::class.java).apply {
                    //putExtra("data", "아무래도 물건 넣을때 이름을 줘야지 되겠어~~~~~")
                    putExtra("data", "2rTo5Hxw65DQ7JbvGKpH")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this)}

            }

        }
    }

    inner class UserBasedRcmd {
        lateinit var user : String
        // var userIndex = 0
        // var data = arrayOf(datas)
        var sim = ArrayList<ArrayList<Double>>()    // 유사도
        var users = ArrayList<String>()
        var product = ArrayList<String>()
        var fav = ArrayList<ArrayList<String>>()
        var rcmdList = ArrayList<String>()   // 물건 정보 들어갈 배열

        var dataArr = ArrayList<ArrayList<Int>>()   // 전체 유저의 선호도 데이터


        constructor(userId : String) {

            this.user = userId
            //var data : ArrayList<ArrayList<Int>> = getData()    // product와 favorite 비교해서 선호데이터 받기

            firestore?.collection("Users")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                //userArr.clear()
                if (querySnapshot == null) return@addSnapshotListener

                // 데이터 받아오기
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(UserModelFS::class.java)
                    users.add(item!!.uid.toString())
                    Log.v("RcmdList", "user: " + item!!.uid.toString())
                }


                firestore?.collection("Product")?.orderBy("uploadTime", Query.Direction.DESCENDING)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    //productArr.clear()
                    if (querySnapshot == null) return@addSnapshotListener

                    // 데이터 받아오기
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Product::class.java)
                        product.add(item!!.userId.toString() + "_" + item!!.uploadTime.toString())
                        //Log.v("RcmdList", item!!.productName.toString())
                    }

                    firestore?.collection("Favorite")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        //favArr.clear()
                        if (querySnapshot == null) return@addSnapshotListener

                        // 데이터 받아오기
                        for (snapshot in querySnapshot!!.documents) {
                            var item = snapshot.toObject(Favorite::class.java)
                            fav.add(item!!.products as java.util.ArrayList<String>)
                        }

                        //Log.v("RcmdList", "유저개수: " + users.size)
                        //Log.v("RcmdList", "물건개수: " + product.size)
                        //Log.v("RcmdList", "좋아요개수: " + fav.size)


                        // 물건 있는지 없는지 0 1 행렬
                        for(i in 0..fav.size-1) {
                            var arr = ArrayList<Int>()   // 한 사람의 선호도 데이터
                            for(j in 0..product.size-1) {
                                // Log.v("RcmdList", "물건개수: " + j)
                                if(fav.get(i).contains(product.get(j)))
                                    arr.add(1)
                                else
                                    arr.add(0)

                                //Log.v("RcmdList", "dataArr: " + arr.get(j))
                            }
                            dataArr.add(arr)

                            //Log.v("RcmdList", "dataArr개수: " + dataArr.size)
                            //Log.v("RcmdList", "dataArr개수: " + dataArr.get(i).size)
                        }


                        // 유사도 행렬
                        for (i in 0..users.size-1) {
                            var simArr = ArrayList<Double>()
                            for(j in 0..users.size-1) {
                                if(i==j)
                                    simArr.add(0.0)
                                else
                                    simArr.add(cosineSimilarity(dataArr.get(i),dataArr.get(j)))

                                Log.v("RcmdList", "sim : " + simArr.get(j).toString())
                            }
                            sim.add(simArr)

                            Log.v("RcmdList", "sim개수: " + sim.size)
                        }


                        // 현재 유저와 다른 유저와의 유사도만 뽑아내기
                        var index1 = 0  // 현재 유저의 순서?인덱스? 뽑아내기
                        while (index1 < users.size) {
                            if (users.get(index1).equals(user.substring(5))) //userIndex = i;
                                break
                            index1++
                        }
                        Log.v("RcmdList", "user: " + users.get(index1))

                        // 유사도 제일 높은 사람의 정보 출력하기
                        var max = sim.get(index1).get(0)
                        var index2 = 0
                        for (j in 0..users.size-1) {
                            if (max < sim.get(index1).get(j)) {
                                max = sim.get(index1).get(j)
                                index2 = j
                            }
                        }
                        Log.v("RcmdList", "user: " + users.get(index2))

                        // 내가 보지 않았지만, 상대는 관심있는 물건, 인덱스 출력
                        for (j in 0..dataArr.get(index1).size-1) {
                            if (dataArr.get(index1).get(j) == 0 && dataArr.get(index2).get(j) != 0) {
                                if(!product.get(j).contains(user.substring(5))) {
                                    rcmdList.add(product.get(j))
                                    Log.v("RcmdList", "추천물품 : "+product.get(j))

                                    /*
                                    firestore?.collection("Product")?.document(product.get(j))?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                                        task ->
                                        if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                                            var product = task.result.toObject(Product::class.java)
                                            rc_title1.text = product?.productName
                                            // 사진 불러오기
                                            var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                                            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                                                Glide.with(applicationContext)
                                                        .load(uri)
                                                        .into(rc_thbm1)
                                                Log.v("IMAGE", "Success")
                                            }?.addOnFailureListener { //이미지 로드 실패시
                                                Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                                                Log.v("IMAGE", "failed")
                                            }
                                        }
                                    }*/
                                }
                            }
                        }

                        // 일단 다 invisible
                        rc_title1.visibility = View.INVISIBLE
                        rc_title2.visibility = View.INVISIBLE
                        rc_title3.visibility = View.INVISIBLE

                        Log.v("RcmdList", "추천물품 개수 : "+rcmdList.size)

                        // 데이터가 3개보다 적을 수 있기 때문에 if문을 이렇게 작성함
                        // 이것보다 더 좋은 방법이 있다면 그거 사용해도 무방.
                        if(rcmdList.size >= 1) {
                            firestore?.collection("Product")?.document(rcmdList.get(0))?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                                task ->
                                if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                                    var product = task.result.toObject(Product::class.java)
                                    rc_title1.text = product?.productName
                                    rc_title1.visibility = View.VISIBLE
                                    // 사진 불러오기
                                    var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                                        Glide.with(applicationContext)
                                                .load(uri)
                                                .into(rc_thbm1)
                                        Log.v("IMAGE", "Success")
                                    }?.addOnFailureListener { //이미지 로드 실패시
                                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                                        Log.v("IMAGE", "failed")
                                    }
                                }
                            }
                        }
                        if(rcmdList.size >= 2) {
                            firestore?.collection("Product")?.document(rcmdList.get(1))?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                                task ->
                                if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                                    var product = task.result.toObject(Product::class.java)
                                    rc_title2.text = product?.productName
                                    rc_title2.visibility = View.VISIBLE
                                    // 사진 불러오기
                                    var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                                        Glide.with(applicationContext)
                                                .load(uri)
                                                .into(rc_thbm2)
                                        Log.v("IMAGE", "Success")
                                    }?.addOnFailureListener { //이미지 로드 실패시
                                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                                        Log.v("IMAGE", "failed")
                                    }
                                }
                            }
                        }
                        if(rcmdList.size >= 3) {
                            firestore?.collection("Product")?.document(rcmdList.get(2))?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                                task ->
                                if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                                    var product = task.result.toObject(Product::class.java)
                                    rc_title3.text = product?.productName
                                    rc_title3.visibility = View.VISIBLE
                                    // 사진 불러오기
                                    var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                                        Glide.with(applicationContext)
                                                .load(uri)
                                                .into(rc_thbm3)
                                        Log.v("IMAGE", "Success")
                                    }?.addOnFailureListener { //이미지 로드 실패시
                                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                                        Log.v("IMAGE", "failed")
                                    }
                                }
                            }
                            Log.v("RcmdList", "배열 크기: "+rcmdList.size)
                        }

                        // 일단 다 invisible
                        rc_title1.visibility = View.INVISIBLE
                        rc_title2.visibility = View.INVISIBLE
                        rc_title3.visibility = View.INVISIBLE

                        Log.v("RcmdList", "추천물품 개수 : "+rcmdList.size)

                        // 데이터가 3개보다 적을 수 있기 때문에 if문을 이렇게 작성함
                        // 이것보다 더 좋은 방법이 있다면 그거 사용해도 무방.
                        if(rcmdList.size >= 1) {
                            firestore?.collection("Product")?.document(rcmdList.get(0))?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                                task ->
                                if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                                    var product = task.result.toObject(Product::class.java)
                                    rc_title1.text = product?.productName
                                    rc_title1.visibility = View.VISIBLE

                                    rcmd1.setOnClickListener {
                                        var intent = Intent(this@MainPageActivity, ProductDetailActivity::class.java)
                                        intent.putExtra("data",product?.userId+"_"+product?.uploadTime)
                                        startActivityForResult(intent, 0)
                                    }

                                    // 사진 불러오기
                                    var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                                        Glide.with(applicationContext)
                                                .load(uri)
                                                .into(rc_thbm1)
                                        Log.v("IMAGE", "Success")
                                    }?.addOnFailureListener { //이미지 로드 실패시
                                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                                        Log.v("IMAGE", "failed")
                                    }
                                }
                            }
                        }
                        if(rcmdList.size >= 2) {
                            firestore?.collection("Product")?.document(rcmdList.get(1))?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                                task ->
                                if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                                    var product = task.result.toObject(Product::class.java)
                                    rc_title2.text = product?.productName
                                    rc_title2.visibility = View.VISIBLE

                                    rcmd2.setOnClickListener {
                                        var intent = Intent(this@MainPageActivity, ProductDetailActivity::class.java)
                                        intent.putExtra("data",product?.userId+"_"+product?.uploadTime)
                                        startActivityForResult(intent, 0)
                                    }

                                    // 사진 불러오기
                                    var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                                        Glide.with(applicationContext)
                                                .load(uri)
                                                .into(rc_thbm2)
                                        Log.v("IMAGE", "Success")
                                    }?.addOnFailureListener { //이미지 로드 실패시
                                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                                        Log.v("IMAGE", "failed")
                                    }
                                }
                            }
                        }
                        if(rcmdList.size >= 3) {
                            firestore?.collection("Product")?.document(rcmdList.get(2))?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                                task ->
                                if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                                    var product = task.result.toObject(Product::class.java)
                                    rc_title3.text = product?.productName
                                    rc_title3.visibility = View.VISIBLE

                                    rcmd3.setOnClickListener {
                                        var intent = Intent(this@MainPageActivity, ProductDetailActivity::class.java)
                                        intent.putExtra("data",product?.userId+"_"+product?.uploadTime)
                                        startActivityForResult(intent, 0)
                                    }

                                    // 사진 불러오기
                                    var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                                        Glide.with(applicationContext)
                                                .load(uri)
                                                .into(rc_thbm3)
                                        Log.v("IMAGE", "Success")
                                    }?.addOnFailureListener { //이미지 로드 실패시
                                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                                        Log.v("IMAGE", "failed")
                                    }
                                }
                            }
                        }
                    }
                }
            }

/*
            // 유사도 행렬
            for (i in 0..users.size-1) {
                var simArr = ArrayList<Double>()
                for(j in 0..users.size-1) {
                    if(i==j)
                        simArr.add(0.0)
                    else
                        simArr.add(cosineSimilarity(data.get(i),data.get(j)))

                    Log.v("RcmdList", simArr.get(j).toString())
                }
                sim.add(simArr)
            }

            // 현재 유저와 다른 유저와의 유사도만 뽑아내기
            var index1 = 0
            while (index1 < users.size) {
                if (users.get(index1) == user) //userIndex = i;
                    break
                index1++
            }

            // 유사도 제일 높은 사람의 정보 출력하기
            var max = sim.get(index1).get(0)
            var index2 = 0
            for (j in 0..users.size-1) {
                if (max < sim.get(index1).get(j)) {
                    max = sim.get(index1).get(j)
                    index2 = j
                }
            }

            // 내가 보지 않았지만, 상대는 관심있는 물건, 인덱스 출력
            for (j in 0..data.get(index1).size-1) {
                if (data.get(index1).get(j) == 0 && data.get(index2).get(j) != 0) {
                    rcmdList.add(product.get(j))
                    Log.v("RcmdList", "추천물품 : "+product.get(j))
                }
            }*/
        }

        fun getRcmd() {
            val intent = Intent(this@MainPageActivity, RcmdMore::class.java)
            intent.putExtra("rcmdList",rcmdList)
            startActivityForResult(intent, 0)
        }

        private fun getData() : ArrayList<ArrayList<Int>> {
            // 파이어베이스에서 데이터 불러와야 함
            // 지금은 일단 더미 데이터로 해보기
            // favorite 데이터랑 product 데이터 가져와야 함
            // data, users
            // sim = Array(data.size) { DoubleArray(data.size) }

            Log.v("RcmdList", "getData()호출")

            var productArr = ArrayList<String>()
            firestore?.collection("Product")?.orderBy("uploadTime", Query.Direction.DESCENDING)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                //productArr.clear()
                if (querySnapshot == null) return@addSnapshotListener

                // 데이터 받아오기
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(Product::class.java)
                    productArr.add(item!!.userId.toString() + "_" + item!!.uploadTime.toString())
                    Log.v("RcmdList", item!!.productName.toString())
                }
            }
            product = productArr

            //var userArr = ArrayList<String>()
            firestore?.collection("Users")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                //userArr.clear()
                if (querySnapshot == null) return@addSnapshotListener

                // 데이터 받아오기
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(UserModelFS::class.java)
                    users.add(item!!.uid.toString())
                    Log.v("RcmdList", "user: " + item!!.uid.toString())
                }

                Log.v("RcmdList", "유저개수: " + users.size)
            }
            //users = userArr
            Log.v("RcmdList", "유저개2수: " + users.size)

            var favArr = ArrayList<ArrayList<String>>()
            firestore?.collection("Favorite")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                //favArr.clear()
                if (querySnapshot == null) return@addSnapshotListener

                // 데이터 받아오기
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(Favorite::class.java)
                    favArr.add(item!!.products as java.util.ArrayList<String>)
                }
            }

            var data1 = ArrayList<ArrayList<Int>>()   // 전체 유저의 선호도 데이터
            var arr = ArrayList<Int>()   // 한 사람의 선호도 데이터
            for(i in 0..favArr.size-1) {
                //arr.clear()
                for(j in 0..productArr.size-1) {
                    if(favArr.get(i).contains(productArr.get(j)))
                        arr.add(1)
                    else
                        arr.add(0)
                }
                data1.add(arr)
            }

            return data1
        }

        private fun cosineSimilarity(user1: ArrayList<Int>, user2: ArrayList<Int>): Double {
            // 코사인 유사도 계산
            var sum = 0.0
            var sum1 = 0.0
            var sum2 = 0.0
            for (i in 0..user1.size-1) {
                sum += (user1[i] * user2[i]).toDouble()
                sum1 += Math.pow(user1[i].toDouble(), 2.0)
                sum2 += Math.pow(user2[i].toDouble(), 2.0)
            }
            return sum / (Math.sqrt(sum1) * Math.sqrt(sum2))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //finish()
        // 변경된 데이터를 불러오기 위해 자신의 액티비티를 다시 호출
        var intent = Intent(this, MainPageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()


    }

}