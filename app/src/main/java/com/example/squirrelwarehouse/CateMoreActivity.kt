package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.listview.view.*
import kotlinx.android.synthetic.main.listview_form.*
import java.text.SimpleDateFormat

class CateMoreActivity : AppCompatActivity() {
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    
    private lateinit var title : String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)

        var intent = intent
        title = intent.getStringExtra("cateName").toString()
        
        page_title.text = title

        back_btn.setOnClickListener {
            finish()
        }

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        listView.adapter = CateListAdapter()
        listView.layoutManager = LinearLayoutManager(this)
    }

    inner class CateListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var cateList : ArrayList<Product> = arrayListOf()

        init {
            // TODO:카테고리 지정해서 필터링 할 것, 정보 떴다가 사라짐
            firestore?.collection("Product")?.whereEqualTo("category", title)?.orderBy("uploadTime", Query.Direction.DESCENDING)
                    ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        // cateList.clear()
                        if (querySnapshot == null) return@addSnapshotListener

                        // 데이터 받아오기
                        for (snapshot in querySnapshot!!.documents) {
                            var item = snapshot.toObject(Product::class.java)
                            if(item!!.status.equals("대여 전")) {
                                cateList.add(item!!)
                                // Log.v("CateList", "Success, size: " + cateList.size)
                            }
                        }
                        notifyDataSetChanged()
                    }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

        override fun getItemCount(): Int {
            return cateList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.titleTV.text = cateList!![position].productName
            viewHolder.timeTV.text = cateList!![position].uploadTime
            viewHolder.detailTV.text = cateList!![position].productDetail

            // 시간 데이터 포맷
            var sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
            var date = sdf.parse(cateList[position]?.uploadTime)
            sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")
            var dateStr = sdf.format(date)
            viewHolder.timeTV.text = dateStr

            // 사진 불러오기
            var storageRef = storage?.reference?.child("product")?.child(cateList!![position].imageURI.toString())
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                    .load(uri)
                    .into(viewHolder.thumb)
                //Log.v("IMAGE","Success")

            }

            viewHolder.setOnClickListener {
                Intent(this@CateMoreActivity, ProductDetailActivity::class.java).apply {
                    putExtra("data", cateList!![position].userId + "_" + cateList!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }

            }

            viewHolder.setOnClickListener {

                // 이거를 쓰려면 product이름을 바꿔야함. userid+uploadTime 이런식으로로
                // 지금은 오류남. 암튼 이 코드로 했을 때 다음 페이지로 넘어가는 건 확실함. 해봄.
                Intent(this@CateMoreActivity, ProductDetailActivity::class.java).apply {
                    //putExtra("data", "아무래도 물건 넣을때 이름을 줘야지 되겠어~~~~~")
                    putExtra("data", cateList!![position].userId+"_"+cateList!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this)}

            }

        }
    }
}