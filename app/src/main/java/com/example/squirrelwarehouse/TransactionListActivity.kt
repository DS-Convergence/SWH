package com.example.squirrelwarehouse

import android.content.Intent
import android.graphics.Paint
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
import com.example.squirrelwarehouse.models.Rental
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_transaction_list.*
import kotlinx.android.synthetic.main.listview.*
import kotlinx.android.synthetic.main.listview.view.*

class TransactionListActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        uid = auth.currentUser?.uid

        back_btn.setOnClickListener {
            finish()
        }
        // 거래중(returnTime이 null), 거래 종료를 모두 보여줌
        // <!--대여중 - 아스파라거스 그린--> , 대여종료 - 회색, 취소선
        //  user1 이 물건 주인
        // 거래내역에는 내가 대여중, 대여완료한 물품이 뜨고 내물건 중에 대여중, 대여 완료는 내 게시글에 띄우기
        val layoutManager = LinearLayoutManager(this)
        transaction_listView.layoutManager = layoutManager
        transaction_listView.adapter = TransactionAdapter()
    }
    inner class TransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var itemList: ArrayList<Product> = arrayListOf()
        init {
            firestore?.collection("Rental")?.whereEqualTo("userId2",uid)
                    ?.addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                        //itemList.clear()
                        if (querySnapshot == null) return@addSnapshotListener

                        // 데이터 받아오기
                        for (snapshot in querySnapshot!!.documents) {
                            var item = snapshot.toObject(Rental::class.java)
                            var productId = item?.productId
                            Log.d("거래내역", productId!!)
                            if (productId != null) {
                                firestore?.collection("Product")?.document(productId)?.get()
                                        ?.addOnSuccessListener { doc ->
                                            var thing = doc.toObject(Product::class.java)
                                            itemList.add(thing!!)
                                            Log.d("불러와지나요1","${itemList[0].productName}")
                                            notifyDataSetChanged()
                                        }
                            }
                        }
                    }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =
                    LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
            return CustomViewHolder2(view)
        }
        inner class CustomViewHolder2(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder2).itemView
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
            // 거래 상태에 따른 뷰 바꾸기
            if(itemList[position].status.equals("대여 종료")){
                //색 회색으로 변경
                viewHolder.titleTV.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.grey))
                // 최소선 긋기
                viewHolder.titleTV.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)

            }
            viewHolder.setOnClickListener {
                Intent(this@TransactionListActivity, ProductDetailActivity::class.java).apply {
                    putExtra("data", itemList!![position].userId + "_" + itemList!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }

            }
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

    }

}