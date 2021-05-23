package com.example.squirrelwarehouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.listview.view.*
import kotlinx.android.synthetic.main.main_itemview.view.*

class MainViewAdapter(var items:ArrayList<MainItem>) : RecyclerView.Adapter<MainViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.main_itemview,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item:MainItem) {
            if(item.thumb!="") {
                // 이미지 데이터 가져와서 넣는부분 - activity에서 uri 넣으면 여기서 item에 적용.
            }
            else {
                // 이미지 데이터가 비어있을 때 기본이미지로 도토리 넣기
                itemView.thumb.setImageResource(R.drawable.acorn)
            }
            itemView.itemName.text = item.name
        }
    }
}