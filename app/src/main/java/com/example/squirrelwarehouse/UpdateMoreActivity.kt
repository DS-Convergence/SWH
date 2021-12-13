package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.listview.view.*
import kotlinx.android.synthetic.main.listview_form.*
import java.text.SimpleDateFormat

class UpdateMoreActivity : AppCompatActivity() {

    lateinit var viewAdapter : ResultViewRecyclerViewAdapter
    val datas = mutableListOf<Item>()

    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        page_title.text = "최근 업데이트"

        back_btn.setOnClickListener {
            finish()
        }

/*
        val layoutManager = LinearLayoutManager(this)
        listView.setLayoutManager(layoutManager)

        itemAdapter = ItemAdapter(this)
        listView.adapter = itemAdapter

        var data : ArrayList<HashMap<String, String>> = intent.getSerializableExtra("updateList") as ArrayList<HashMap<String, String>>
        lateinit var item : Item

        for(i in 0..data.size-1) {
            val keys : Iterator<String> = data.get(i).keys.iterator()
            while (keys.hasNext()) {
                // val tmp : String = ""
                val prodId = keys.next()
                val title : String? = data.get(i).get(prodId).toString()

                item = Item(prodId,title)
                datas.add(item)
            }
        }

        itemAdapter.datas = datas
        itemAdapter.notifyDataSetChanged()

 */


        listView.adapter = ResultViewRecyclerViewAdapter()
        listView.layoutManager = LinearLayoutManager(this)

    }


    /*
    inner class ViewHolder {
        /*
        fun setItem(item: Item) {
            if (item.imgbtn != "") {
                // 이미지 데이터 가져와서 넣는부분 - activity에서 uri 넣으면 여기서 item에 적용.
            } else {
                // 이미지 데이터가 비어있을 때 기본이미지로 도토리 넣기
                itemView.prevImg.setImageResource(R.drawable.acorn)
            }
            itemView.titleTV.text = item.title
            //itemView.timeTV.text = item.time
            itemView.detailTV.text = item.detail
            // https://recipes4dev.tistory.com/168

        }
         */

        fun bind(viewHolder: com.xwray.groupie.ViewHolder, position: Int) {
            lateinit var item: Item
            viewHolder.itemView.titleTV.text = item.title
            //itemView.timeTV.text = item.time
            viewHolder.itemView.detailTV.text = item.detail

            //이미지를 로드하기. load our user image into the User image icon
            val data = FirebaseFirestore.getInstance()?.collection("Product")
            lateinit var imgURI : Any
            var arr : ArrayList<String> = arrayListOf()
            if (data != null) {
                data.get().addOnSuccessListener { prodId ->
                    for(document in prodId) {
                        // 이미지 넣기
                        imgURI = document["imageURI"]!!
                        var ref = FirebaseStorage.getInstance().getReference()
                        var imgref = ref.child("/product")?.child(imgURI?.toString())
                        imgref?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(viewHolder.itemView.prevImg)
                            Log.v("IMAGE","Success")
                        }
                        arr.add(document.id)
                    }
                }.addOnFailureListener { exception ->
                    Log.w("UpdateMoreActivity", "Error getting imgURI")
                }
            }
        }
    }

     */

    inner class ResultViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var products: ArrayList<Product> = arrayListOf()

        init {
            firestore?.collection("Product")?.orderBy("uploadTime", Query.Direction.DESCENDING)
                    ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        products.clear()
                        if (querySnapshot == null) return@addSnapshotListener

                        // 데이터 받아오기
                        for (snapshot in querySnapshot!!.documents) {
                            var item = snapshot.toObject(Product::class.java)
                            if(item!!.status.equals("대여 전")) {
                                products.add(item!!)
                                // Log.v("products", "Success, size: " + products.size)
                            }
                        }
                        notifyDataSetChanged()
                    }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =
                    LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun getItemCount(): Int {
            return products.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.titleTV.text = products!![position].productName
            viewHolder.timeTV.text = products!![position].uploadTime
            viewHolder.detailTV.text = products!![position].productDetail

            // 시간 데이터 포맷
            var sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
            var date = sdf.parse(products[position]?.uploadTime)
            sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")
            var dateStr = sdf.format(date)
            viewHolder.timeTV.text = dateStr

            // 사진 불러오기
            var storageRef = storage?.reference?.child("product")?.child(products!![position].imageURI.toString())
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                        .load(uri)
                        .override(150,150)
                        .into(viewHolder.thumb)
                //Log.v("IMAGE","Success")

            }

            viewHolder.setOnClickListener {
                Intent(this@UpdateMoreActivity, ProductDetailActivity::class.java).apply {
                    putExtra("data", products!![position].userId + "_" + products!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }

            }

            viewHolder.setOnClickListener {

                // 이거를 쓰려면 product이름을 바꿔야함. userid+uploadTime 이런식으로로
                // 지금은 오류남. 암튼 이 코드로 했을 때 다음 페이지로 넘어가는 건 확실함. 해봄.
               Intent(this@UpdateMoreActivity, ProductDetailActivity::class.java).apply {
                    //putExtra("data", "아무래도 물건 넣을때 이름을 줘야지 되겠어~~~~~")
                   putExtra("data", products!![position].userId+"_"+products!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this)}

            }

        }
    }
}