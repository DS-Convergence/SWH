package com.example.squirrelwarehouse

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.squirrelwarehouse.models.Product
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.listview_form.*

class UpdateMoreActivity : AppCompatActivity() {

    lateinit var itemAdapter : ItemAdapter
    val datas = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)

        val layoutManager = LinearLayoutManager(this)
        listView.setLayoutManager(layoutManager)

        itemAdapter = ItemAdapter(this)
        listView.adapter = itemAdapter

        var data : ArrayList<HashMap<String, String>> = intent.getSerializableExtra("updateList") as ArrayList<HashMap<String, String>>

        for(i in 0..data.size-1) {
            lateinit var item : Item
            val keys : Iterator<String> = data.get(i).keys.iterator()
            while (keys.hasNext()) {
                val tmp : String = ""
                val prodId = keys.next()
                val title : String? = data.get(i).get(prodId).toString()

                item = Item(prodId,title,"")
                datas.add(item)
            }
        }

        itemAdapter.datas = datas
        itemAdapter.notifyDataSetChanged()


    }


    /*
    inner class ViewHolder {
        /*
        fun setItem(item: Item) {
            if (item.imgbtn != "") {
                // 이미지 데이터 가져와서 넣는부분 - activity에서 uri 넣으면 여기서 item에 적용.
            } else {
                // 이미지 데이터가 비어있을 때 기본이미지로 도토리 넣기
                itemView.prevImg.setImageResource(R.drawable.acorn)
            }
            itemView.titleTV.text = item.title
            //itemView.timeTV.text = item.time
            itemView.detailTV.text = item.detail
            // https://recipes4dev.tistory.com/168

        }
         */

        fun bind(viewHolder: com.xwray.groupie.ViewHolder, position: Int) {
            lateinit var item: Item
            viewHolder.itemView.titleTV.text = item.title
            //itemView.timeTV.text = item.time
            viewHolder.itemView.detailTV.text = item.detail

            //이미지를 로드하기. load our user image into the User image icon
            val data = FirebaseFirestore.getInstance()?.collection("Product")
            lateinit var imgURI : Any
            var arr : ArrayList<String> = arrayListOf()
            if (data != null) {
                data.get().addOnSuccessListener { prodId ->
                    for(document in prodId) {
                        // 이미지 넣기
                        imgURI = document["imageURI"]!!
                        var ref = FirebaseStorage.getInstance().getReference()
                        var imgref = ref.child("/product")?.child(imgURI?.toString())
                        imgref?.downloadUrl?.addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                    .load(uri)
                                    .into(viewHolder.itemView.prevImg)
                            Log.v("IMAGE","Success")
                        }
                        arr.add(document.id)
                    }
                }.addOnFailureListener { exception ->
                    Log.w("UpdateMoreActivity", "Error getting imgURI")
                }
            }
        }
    }

     */
}