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
import com.example.squirrelwarehouse.models.Favorite
import kotlinx.android.synthetic.main.activity_my_favorite_list.*
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
        my_fav_listView.layoutManager = layoutManager
        my_fav_listView.adapter = FavListAdapter()

    }

    inner class FavListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var itemList: ArrayList<Product> = arrayListOf()
        init {
            //Log.d("찜목록", "로그 뜨나요")
            firestore?.collection("Favorite")?.document("${uid}")?.get()
                    ?.addOnSuccessListener { doc ->
                        //Log.d("찜목록", "로그 뜨나요2")
                        var item = doc.toObject(Favorite::class.java)
                        var array = item?.products
                        //Log.d("찜목록", "로그 뜨나요3")
                        if (array != null) {
                            for (productID in array) {
                                //Log.d("찜목록____id", productID)
                                firestore?.collection("Product")?.document(productID)?.get()
                                        ?.addOnSuccessListener { doc ->
                                            var thing = doc.toObject(Product::class.java)
                                            itemList.add(thing!!)
                                            //Log.d("불러와지나요1","${itemList[0].productName}")
                                            notifyDataSetChanged()//이거 위치 중요
                                        }
                            }
                        } else {
                            Log.d("찜목록", "array null")
                        }

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
            return itemList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView
            viewHolder.titleTV.text = itemList[position].productName
            viewHolder.timeTV.text = itemList[position].uploadTime
            viewHolder.detailTV.text = itemList[position].productDetail
            // 사진 불러오기
            var storageRef = storage?.reference?.child("product")?.child(itemList!![position].imageURI.toString())
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                        .load(uri)
                        .into(viewHolder.thumb)
                Log.v("IMAGE","Success")

            }
            viewHolder.setOnClickListener {
                Intent(this@MyFavoriteListActivity, ProductDetailActivity::class.java).apply {
                    putExtra("data", itemList!![position].userId + "_" + itemList!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }

            }
        }


    }
}