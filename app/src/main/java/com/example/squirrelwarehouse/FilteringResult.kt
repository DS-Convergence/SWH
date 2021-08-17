package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.listview.view.*
import kotlinx.android.synthetic.main.listview_form.*

class FilteringResult : AppCompatActivity() {
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        /*
        if(intent.hasExtra("category")) {
            page_title.text = intent.getStringExtra("category")
            listView.adapter = ResultViewRecyclerViewAdapter()
            listView.layoutManager = LinearLayoutManager(this)
        }
        else {
            Toast.makeText(this,"카테고리 미선택",Toast.LENGTH_LONG).show()
        }
         */

        var valarr = intent.getSerializableExtra("valList") as Array<String>
        if(valarr.size!=0) {
            page_title.text = "검색결과"
            // Log.v("filtering result", "arr size: "+valarr.size) 로그 출력이 안 됨
        }
        else
            page_title.text = "검색조건 미선택"
    }

    /*
    inner class ResultViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var products:ArrayList<Product> = arrayListOf()

        init {
            firestore?.collection("Product")?.whereEqualTo("category", intent.getStringExtra("category"))?.orderBy("uploadTime", Query.Direction.DESCENDING)
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


            // 사진 불러오기
            /*var storageRef = storage?.reference?.child("product")?.child(products!![position].imageURI.toString())
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                        .load(uri)
                        .into(viewHolder.thumb)
            }*/

            viewHolder.setOnClickListener {
                Intent(this@FilteringResult, ProductDetailActivity::class.java).apply {
                    //putExtra("data", "아무래도 물건 넣을때 이름을 줘야지 되겠어~~~~~")
                    putExtra("data", products!![position].userId+"_"+products!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this)}
            }

        }
    }

     */
}