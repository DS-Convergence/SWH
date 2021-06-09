package com.example.squirrelwarehouse

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.squirrelwarehouse.models.Product

class ItemAdapter(private val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var datas = mutableListOf<Item>()

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
        private val title: TextView = itemView.findViewById(R.id.titleTV)

        fun bind(item: Item) {
            title.text = item.title

            itemView.setOnClickListener {
                Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("data",item.prodId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                }.run { context.startActivity(this)}
            }
        }
    }
}