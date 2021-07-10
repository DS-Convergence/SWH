package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.util.Log
import kotlinx.android.synthetic.main.activity_my_favorite_list.back_btn
import kotlinx.android.synthetic.main.activity_my_list.*
import kotlinx.android.synthetic.main.listview.view.*

class MyFavoriteListActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite_list)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        uid = auth.currentUser?.uid

        back_btn.setOnClickListener {
            finish()
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.setReverseLayout(true)
        layoutManager.setStackFromEnd(true)
        my_listView.layoutManager = layoutManager

        my_listView.adapter = FavListAdapter()
    }
    inner class FavListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var products: ArrayList<Product> = arrayListOf()
        lateinit var favList: Array<String>
        init {
            /*
            firestore?.collection("Favorite")?.document("${uid}")?.get()?.addOnSuccessListener { doc ->
                //배열에 있는 물건 하나씩 꺼내서 리사이클러뷰에 표시
                //favList = doc?.data?.get("products")
                favList = arrayOf(doc?.data?.get("products").toString())
                for(prodId in favList){
                    Log.d("실험 !!! ", prodId)
                    firestore?.collection("Product")?.document("${prodId}")?.get()?.addOnSuccessListener { doc ->
                        products.clear()
                        var item = doc.toObject(Product::class.java)
                        products.add(item!!)
                    }
                    notifyDataSetChanged()
                }
            }
             */
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
            viewHolder.titleTV.text = products[position].productName
            viewHolder.timeTV.text = products[position].uploadTime
            viewHolder.detailTV.text = products[position].productDetail
            // 사진 불러오기
            var storageRef = storage?.reference?.child("product")?.child(products!![position].imageURI.toString())
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                        .load(uri)
                        .into(viewHolder.thumb)
                //Log.v("IMAGE","Success")

            }
            viewHolder.setOnClickListener {

                // 이거를 쓰려면 product이름을 바꿔야함. userid+uploadTime 이런식으로로
                // 지금은 오류남. 암튼 이 코드로 했을 때 다음 페이지로 넘어가는 건 확실함. 해봄.
                Intent(this@MyFavoriteListActivity, ProductDetailActivity::class.java).apply {
                    putExtra("data", products!![position].userId+"_"+products!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run {startActivity(this)}

            }
        }

    }
}