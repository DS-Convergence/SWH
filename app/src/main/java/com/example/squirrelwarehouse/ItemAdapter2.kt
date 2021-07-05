package com.example.squirrelwarehouse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.bumptech.glide.Glide
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
    var itemList: ArrayList<Item2> = arrayListOf()

    init {
        //Log.d("실험","uid   ${uid}")
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        firestore?.collection("Product")?.whereEqualTo("userId", uid)
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
        viewHolder.titleTV.text = itemList[position].title
        viewHolder.timeTV.text = itemList[position].time
        viewHolder.detailTV.text = itemList[position].detail
        // 사진 불러오기
        var storageRef = storage?.reference?.child("product")?.child(itemList!![position].imageURI.toString())
        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
            Glide.with(context)
                    .load(uri)
                    .into(viewHolder.thumb)
            //Log.v("IMAGE","Success")

        }
    }


}