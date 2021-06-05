package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.listview_form.*

class ListViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)

        listView.layoutManager = LinearLayoutManager(this)

        val adapter = ListAdapter()

        adapter.items.add(Item("title1","16:03","세부사항",""))

        listView.adapter = adapter
    }
}