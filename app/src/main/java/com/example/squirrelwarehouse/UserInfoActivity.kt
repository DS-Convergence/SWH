package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.listview.view.*


class UserInfoActivity : AppCompatActivity() {
    private lateinit var adapter:ItemAdapter
    private var firestore : FirebaseFirestore? = null
    private lateinit var auth : FirebaseAuth
    var title : String? = null
    var time : String? = null
    var detail : String? = null
    var item_pic : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        report_btn.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
        back_btn.setOnClickListener {
            finish()
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.setReverseLayout(true)
        layoutManager.setStackFromEnd(true)
        user_listview.layoutManager = layoutManager

        user_listview.adapter = ItemAdapter2()
        

    }
    inner class ItemAdapter2 : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var itemList: ArrayList<Item2> = arrayListOf()

        init {
            firestore?.collection("Product")?.whereEqualTo("userId", auth.currentUser?.uid)
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
        }
    }

}