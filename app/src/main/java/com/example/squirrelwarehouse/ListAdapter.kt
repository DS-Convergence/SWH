package com.example.squirrelwarehouse

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview.view.*
import kotlinx.android.synthetic.main.listview_form.*


class ListAdapter(var items : ArrayList<Item>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)

        /*
        holder.itemView.setOnClickListener {
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("prodId", item.prodId)
            startActivityForResult(intent,0)
        }
         */
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item : Item) {
            if(item.imgbtn!="") {
                // 이미지 데이터 가져와서 넣는부분 - activity에서 uri 넣으면 여기서 item에 적용.
            }
            else {
                // 이미지 데이터가 비어있을 때 기본이미지로 도토리 넣기
                itemView.prevImg.setImageResource(R.drawable.acorn)
            }
            itemView.titleTV.text = item.title
            //itemView.timeTV.text = item.time
            itemView.detailTV.text = item.detail
            // https://recipes4dev.tistory.com/168
        }
    }
}

//private fun ImageButton.setImageResource(overViewImg: ImageButton?) {}
