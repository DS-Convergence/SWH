package com.example.squirrelwarehouse

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.listview_form.*

class UpdateMoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)

        // adapter.items.add(Item("title1","16:03","세부사항",""))
        if(intent.hasExtra("updateList")) {
            listView.layoutManager = LinearLayoutManager(this)
            var data : ArrayList<HashMap<String, String>> = intent.getSerializableExtra("updateList") as ArrayList<HashMap<String, String>>
            var arr : ArrayList<Item> = arrayListOf()
            // val keys: Iterator<String> = data.keys.iterator()

            for(i in 0..data.size-1) {
                lateinit var item : Item
                val keys : Iterator<String> = data.get(i).keys.iterator()
                while (keys.hasNext()) {
                    val tmp : String = ""
                    val prodId = keys.next()
                    val title : String? = data.get(i).get(prodId).toString()
                    item = Item(prodId,title,tmp,tmp)
                    arr.add(item)
                }
            }
            val adapter = ListAdapter(arr)
            listView.adapter = adapter
        } else {
            Toast.makeText(this, "데이터 불러오기 실패", Toast.LENGTH_LONG).show()
        }
    }
}
