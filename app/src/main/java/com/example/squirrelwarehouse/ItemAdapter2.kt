package com.example.squirrelwarehouse

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.listview.view.*

class ItemAdapter2(var uid : String?, private val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    //유저게시글, 나의 게시글 용 어댑터
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    var title : String? = null
    var itemList: ArrayList<Product> = arrayListOf()

    init {
        //Log.d("실험","uid   ${uid}")
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        firestore?.collection("Product")?.whereEqualTo("userId", uid)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    itemList.clear()
                    if (querySnapshot == null) return@addSnapshotListener

                    // 데이터 받아오기
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Product::class.java)
                        itemList.add(item!!)
                    }
                    notifyDataSetChanged()
                }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
        return ViewHolder(view)
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        viewHolder.titleTV.text = itemList[position].productName
        viewHolder.timeTV.text = itemList[position].uploadTime
        viewHolder.detailTV.text = itemList[position].productDetail
        // 사진 불러오기
        var storageRef = storage?.reference?.child("product")?.child(itemList!![position].imageURI.toString())
        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
            Glide.with(context)
                    .load(uri)
                    .into(viewHolder.thumb)
            //Log.v("IMAGE","Success")

        }
        viewHolder.setOnClickListener {

            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("data", itemList!![position].userId+"_"+itemList!![position].uploadTime)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run {context.startActivity(this)}

        }
    }



}