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
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    var title : String? = null
    var time : String? = null
    var detail : String? = null
    var item_pic : String? = null
    var itemList: ArrayList<Product> = arrayListOf()

    init {
        //Log.d("실험","uid   ${uid}")
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        /*firestore?.collection("Product")?.whereEqualTo("userId", uid)
            ?.get()?.addOnSuccessListener { documents ->
                itemList.clear()
                for (doc in documents) {
                    title = doc?.data?.get("productName").toString()
                    time = doc?.data?.get("uploadTime").toString()
                    detail = doc?.data?.get("productDetail").toString()
                    item_pic = doc?.data?.get("imageURI").toString()
                    var item = Item2(title, time, detail, item_pic)
                    itemList.add(item!!)
                }
                notifyDataSetChanged()//새로고침
            }*/
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

            // 이거를 쓰려면 product이름을 바꿔야함. userid+uploadTime 이런식으로로
            // 지금은 오류남. 암튼 이 코드로 했을 때 다음 페이지로 넘어가는 건 확실함. 해봄.
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("data", itemList!![position].userId+"_"+itemList!![position].uploadTime)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run {context.startActivity(this)}

        }
    }
    //성공하면 Item2 클래스 지우기


}