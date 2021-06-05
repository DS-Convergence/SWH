package com.example.squirrelwarehouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview.view.*


class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    val items = ArrayList<Item>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
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
            itemView.timeTV.text = item.time
            itemView.detailTV.text = item.detail
        }
    }
}

//private fun ImageButton.setImageResource(overViewImg: ImageButton?) {}
