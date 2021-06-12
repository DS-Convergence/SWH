package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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


class MainPageActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var firestore = FirebaseFirestore.getInstance()
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
        moreRcmd.setOnClickListener {
            Log.v("RcmdList", UserBasedRcmd("user_ifbnimzN2RM61ZfbfeJ48ZBdu9j2").getRcmd().toString())
        }



        // 예은 실험

        //var updateList : ArrayList<Product> = dataLoading()

        //ct_title1.text = updateList.get(0).productName.toString()
        //ct_title2.text = products.get(1).productName
        //ct_title3.text = products.get(2).productName


    }

    private fun searchItem(query: String) {}

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

    inner class UserBasedRcmd(private var userId : String) {
        var user : String? = userId
        // var userIndex = 0
        // var data = arrayOf(datas)
        lateinit var sim: ArrayList<ArrayList<Double>>    // 유사도
        lateinit var users : ArrayList<String>
        lateinit var product : ArrayList<String>
        lateinit var rcmdList : ArrayList<String>   // 물건 정보 들어갈 배열

        fun UserBased(userId: String) {
            this.user = userId
            var data : ArrayList<ArrayList<Int>> = getData()    // product와 favorite 비교해서 선호데이터 받기

            // 유사도 행렬
            for (i in 0..users.size-1) {
                lateinit var simArr : ArrayList<Double>
                for(j in 0..users.size-1) {
                    if(i==j)
                        simArr.add(0.0)
                    else
                        simArr.add(cosineSimilarity(data.get(i),data.get(j)))
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
            var max: Double = sim.get(index1).get(0)
            var index2 = 0
            for (j in 0..users.size-1) {
                if (max < sim.get(index1).get(j)) {
                    max = sim.get(index1).get(j)
                    index2 = j
                }
            }

            // 나랑 유사도가 가장 높은 사람의 물건 선호도 출력
            /*for (dd in data.get(index2)) {
                print("$dd ")
            }
             */

            // 나의 물건 선호도 출력
            /*for (dd in data.get(index1)) {
                print("$dd ")
            }
             */

            // 내가 보지 않았지만, 상대는 관심있는 물건, 인덱스 출력
            for (j in 0..data.get(index1).size-1) {
                if (data.get(index1).get(j) == 0 && data.get(index2).get(j) != 0) {
                    rcmdList.add(product.get(j))
                }
            }
        }

        fun getRcmd() : ArrayList<String> {
            rcmdList.add("hi")
            return rcmdList
        }

        private fun getData() : ArrayList<ArrayList<Int>> {
            // 파이어베이스에서 데이터 불러와야 함
            // 지금은 일단 더미 데이터로 해보기
            // favorite 데이터랑 product 데이터 가져와야 함
            // data, users
            // sim = Array(data.size) { DoubleArray(data.size) }
            lateinit var productArr : ArrayList<String>
            firestore?.collection("Product")?.orderBy("uploadTime", Query.Direction.DESCENDING)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                productArr.clear()
                if (querySnapshot == null) return@addSnapshotListener

                // 데이터 받아오기
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(Product::class.java)
                    productArr.add(item!!.userId.toString() + "_" + item!!.uploadTime.toString())
                }
            }
            product = productArr

            lateinit var userArr : ArrayList<String>
            firestore?.collection("Users")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                userArr.clear()
                if (querySnapshot == null) return@addSnapshotListener

                // 데이터 받아오기
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(UserModelFS::class.java)
                    userArr.add(item!!.uid.toString())
                }
            }
            users = userArr

            lateinit var favArr : ArrayList<ArrayList<String>>
            firestore?.collection("Favorite")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                favArr.clear()
                if (querySnapshot == null) return@addSnapshotListener

                // 데이터 받아오기
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(Favorite::class.java)
                    favArr.add(item!!.products as java.util.ArrayList<String>)
                }
            }

            lateinit var data : ArrayList<ArrayList<Int>>   // 전체 유저의 선호도 데이터
            lateinit var arr : ArrayList<Int>   // 한 사람의 선호도 데이터
            for(i in 0..favArr.size-1) {
                arr.clear()
                for(j in 0..productArr.size-1) {
                    if(favArr.get(i).contains(productArr.get(j)))
                        arr.add(1)
                    else
                        arr.add(0)
                }
                data.add(arr)
            }

            return data
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

}