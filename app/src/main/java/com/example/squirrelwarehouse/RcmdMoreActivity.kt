package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.listview.view.*
import kotlinx.android.synthetic.main.listview_form.*
import java.text.SimpleDateFormat

class RcmdMoreActivity : AppCompatActivity() {
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null

    private lateinit var arr : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_form)
        page_title.text = "추천물품"

        back_btn.setOnClickListener {
            finish()
        }

        var intent = intent
        arr = intent.getSerializableExtra("rcmdList") as ArrayList<String>
        Log.v("RcmdList", "추천 페이지에서 배열 크기: "+arr.size)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        listView.adapter = RcmdListAdapter()
        listView.layoutManager = LinearLayoutManager(this)
    }

    inner class RcmdListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var rcmdList: ArrayList<Product> = arrayListOf()

        init {
            if(arr!=null) {
                for (prodID in arr) {
                    rcmdList.clear()
                    firestore?.collection("Product")?.document(prodID)?.get()
                        ?.addOnSuccessListener { doc ->
                            var item = doc.toObject(Product::class.java)
                            rcmdList.add(item!!)
                            notifyDataSetChanged()
                        }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

        override fun getItemCount(): Int {
            return rcmdList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.titleTV.text = rcmdList!![position].productName
            viewHolder.timeTV.text = rcmdList!![position].uploadTime
            viewHolder.detailTV.text = rcmdList!![position].productDetail

            // 시간 데이터 포맷
            var sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
            var date = sdf.parse(rcmdList[position]?.uploadTime)
            sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")
            var dateStr = sdf.format(date)
            viewHolder.timeTV.text = dateStr

            // 사진 불러오기
            var storageRef = storage?.reference?.child("product")?.child(rcmdList!![position].imageURI.toString())
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                    .load(uri)
                    .into(viewHolder.thumb)
                //Log.v("IMAGE","Success")

            }

            // 거래상태 변경
            if(rcmdList[position].status.equals("대여 종료")){
                // 글자색 흰색으로 변경
                viewHolder.statusTV.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.white))
                // 글자 배경색 진회색
                viewHolder.statusTV.setBackgroundColor(ContextCompat.getColor(applicationContext!!,R.color.dark_grey))
                viewHolder.statusTV.text = "대여 종료"
                //배경색 회색으로
                viewHolder.list_background.setBackgroundColor(ContextCompat.getColor(applicationContext!!,R.color.grey))

            }
            else if(rcmdList[position].status.equals("대여 중")){
                // 글자색 흰색으로 변경
                viewHolder.statusTV.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.white))
                // 글자 배경색 녹색
                viewHolder.statusTV.setBackgroundColor(ContextCompat.getColor(applicationContext!!,R.color.asparagus_green))
                viewHolder.statusTV.text = "대여 중"
            }
            viewHolder.setOnClickListener {
                Intent(this@RcmdMoreActivity, ProductDetailActivity::class.java).apply {
                    putExtra("data", rcmdList!![position].userId + "_" + rcmdList!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }

            }

            viewHolder.setOnClickListener {

                // 이거를 쓰려면 product이름을 바꿔야함. userid+uploadTime 이런식으로로
                // 지금은 오류남. 암튼 이 코드로 했을 때 다음 페이지로 넘어가는 건 확실함. 해봄.
                Intent(this@RcmdMoreActivity, ProductDetailActivity::class.java).apply {
                    //putExtra("data", "아무래도 물건 넣을때 이름을 줘야지 되겠어~~~~~")
                    putExtra("data", rcmdList!![position].userId+"_"+rcmdList!![position].uploadTime)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this)}

            }

        }
    }
}