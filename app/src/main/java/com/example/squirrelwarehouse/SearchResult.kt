package com.example.squirrelwarehouse

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.listview.*
import kotlinx.android.synthetic.main.listview.view.*
import kotlinx.android.synthetic.main.listview_form.*
import java.text.SimpleDateFormat

class SearchResult : AppCompatActivity() {
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null

    var querystr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)

        page_title.text = "검색 결과"

        querystr = intent.getStringExtra("query").toString()
        Log.v("Srch","query: "+querystr)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        listView.adapter = ResultViewRecyclerViewAdapter()
        listView.layoutManager = LinearLayoutManager(this)

        back_btn.setOnClickListener {
            finish()
        }
    }

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

                            // 필터링
                            if(item!!.productName?.contains(querystr)!!) {
                                // Log.v("Srch", "prodName contains query")
                                if(item!!.status.equals("대여 전")) {
                                    products.add(item!!)
                                    // Log.v("Srch", "itemAdded "+item!!.productName)
                                }
                            }
                        }

                        notifyDataSetChanged()

                    }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
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
                        .into(viewHolder.thumb)
                //Log.v("IMAGE","Success")

            }

            viewHolder.setOnClickListener {
                Intent(this@SearchResult, ProductDetailActivity::class.java).apply {
                    putExtra("data", products!![position].userId + "_" + products!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }

            }

            viewHolder.setOnClickListener {
                Intent(this@SearchResult, ProductDetailActivity::class.java).apply {
                    putExtra("data", products!![position].userId+"_"+products!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this)}

            }

        }

    }
}