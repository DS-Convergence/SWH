package com.example.squirrelwarehouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.listview.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    val items = ArrayList<List>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val overViewImg = itemView?.findViewById<ImageButton>(R.id.overView)
        fun setItem(item : List) {
            //itemView.overView.setImageResource()
            itemView.title.text = item.title
            itemView.time.text = item.time
            itemView.detail.text = item.detail
        }
    }
}

//private fun ImageButton.setImageResource(overViewImg: ImageButton?) {}
