package com.example.squirrelwarehouse

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.listview.view.*
import kotlinx.android.synthetic.main.product_detail.*
import java.text.SimpleDateFormat

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
        viewHolder.detailTV.text = itemList[position].productDetail
        //viewHolder.timeTV.text = itemList[position].uploadTime

        // 시간 데이터 형식 변경
        var sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        var date = sdf.parse(itemList[position]?.uploadTime)
        sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")
        var dateStr = sdf.format(date)
        viewHolder.timeTV.text = dateStr
        // 사진 불러오기
        var storageRef = storage?.reference?.child("product")?.child(itemList!![position].imageURI.toString())
        storageRef?.downloadUrl?.addOnSuccessListener { uri ->
            Glide.with(context)
                    .load(uri)
                    .into(viewHolder.thumb)
            //Log.v("IMAGE","Success")

        }
        // 거래 상태에 따른 뷰 바꾸기
        // 대여중 - 아스파라거스 그린, 대여종료 - 회색, 취소선
        if(itemList[position].status.equals("대여 종료")){
            // 글자색 흰색으로 변경
            viewHolder.statusTV.setTextColor(ContextCompat.getColor(context!!,R.color.white))
            // 글자 배경색 진회색
            viewHolder.statusTV.setBackgroundColor(ContextCompat.getColor(context!!,R.color.dark_grey))
            viewHolder.statusTV.text = "대여 종료"
            //배경색 회색으로
            viewHolder.list_background.setBackgroundColor(ContextCompat.getColor(context!!,R.color.grey))

        }
        else if(itemList[position].status.equals("대여 중")){
            // 글자색 흰색으로 변경
            viewHolder.statusTV.setTextColor(ContextCompat.getColor(context!!,R.color.white))
            // 글자 배경색 녹색
            viewHolder.statusTV.setBackgroundColor(ContextCompat.getColor(context!!,R.color.asparagus_green))
            viewHolder.statusTV.text = "대여 중"
        }

        viewHolder.setOnClickListener {

            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("data", itemList!![position].userId+"_"+itemList!![position].uploadTime)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run {context.startActivity(this)}

        }
    }



}