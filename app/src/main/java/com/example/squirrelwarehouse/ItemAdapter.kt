package com.example.squirrelwarehouse

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.listview.view.*

class ItemAdapter(private val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var datas = mutableListOf<Item>()//<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listview,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // private val title: TextView = itemView.findViewById(R.id.titleTV)

        fun bind(item: Item) {
            // title.text = item.title

            itemView.titleTV.text = item.title
            // itemView.timeTV.text = item.time
            // itemView.detailTV.text = item.detail

            //이미지를 로드하기. load our user image into the User image icon
            val data = FirebaseFirestore.getInstance()?.collection("Product")
            var ref = FirebaseStorage.getInstance().getReference()
            lateinit var imgURI : Any
            var arr : ArrayList<String> = arrayListOf()
            if (data != null) {
                data.get().addOnSuccessListener { prodId ->
                    for(document in prodId) {
                        // 이미지 넣기
                        imgURI = document["imageURI"]!!
                        var imgref = ref.child("/product")?.child(imgURI?.toString())
                        imgref?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(context)
                                    .load(uri)
                                    .into(itemView.thumb)
                            Log.v("IMAGE","Success")
                        }
                        arr.add(document.id)
                    }
                }.addOnFailureListener { exception ->
                    Log.w("UpdateMoreActivity", "Error getting imgURI")
                }
            }

            itemView.setOnClickListener {
                Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("data", item.prodId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this)}
            }
        }
    }
}