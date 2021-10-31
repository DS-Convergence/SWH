package com.example.squirrelwarehouse

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.UserModelFS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.listview.view.*
import kotlinx.android.synthetic.main.main_viewpager.view.*
import kotlinx.android.synthetic.main.main_viewpager.view.detailTV
import kotlinx.android.synthetic.main.main_viewpager.view.thumb
import kotlinx.android.synthetic.main.product_detail.*
import java.util.*
import kotlin.collections.ArrayList


class MainViewpagerAdapter : PagerAdapter() {
    private var mContext: Context?=null

    private var firestore = FirebaseFirestore.getInstance()
    private var storage = FirebaseStorage.getInstance()

    var arr : ArrayList<String> = arrayListOf()
    var pwrg : ArrayList<UserModelFS> = arrayListOf()

    fun MainViewpagerAdapter(context: Context){
        mContext = context
    }

    // position에 맞는 내용 생성
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.main_viewpager, container, false)
        MainViewpagerAdapter(container.context)

        arr.clear()

        /*
        arr.add("5Bf4S5mm7hRhvu3LbdPUbCI8hMh1")
        // Log.v("pwrg", "arr: " + arr.get(0) + ", arr.size: " + arr.size)
        arr.add("YDNw0730r1aJzFZW4dvvzSNtfsV2")
        // Log.v("pwrg", "arr: " + arr.get(1) + ", arr.size: " + arr.size)
        arr.add("ifbnimzN2RM61ZfbfeJ48ZBdu9j2")
        // Log.v("pwrg", "arr: " + arr.get(2) + ", arr.size: " + arr.size)
         */
        arr.add("285Ex5Php6UwXupj45bQSKezF6G3")     //choco
        arr.add("4LJA89mheCebnpW760yL4E01uZ12")     //dmsqo
        arr.add("4uhJwwJVQ5c3Lj7tflHcpn4KQu23")     //dms6024
        arr.add("HWRQxKcbWFWs2AC82MkndTM39dx1")     //apple
        arr.add("jnKtozaufmR1eIWFhvgQVHNUSAr2")     //sol
        Log.v("pwrg", "arrsize: "+arr.size)

        firestore?.collection("Users")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            pwrg.clear()

            if (querySnapshot == null) return@addSnapshotListener

            var pwrgList = arrayListOf<String>()
            while(pwrgList.size < 3) {
                var pwrgUser = arr.get(Random().nextInt(arr.size))
                if(!pwrgList.contains(pwrgUser)) {
                    pwrgList.add(pwrgUser)
                    Log.v("pwrg", "pwrgUser: "+pwrgList)
                }
            }

            for(i in 0..2) {
                var uid = pwrgList.get(i)
                for(snapshot in querySnapshot!!.documents) {
                    var user = snapshot.toObject(UserModelFS::class.java)
                    if (uid.equals(user?.uid)) {
                        pwrg.add(user!!)
                        // Log.v("pwrg", "current:" + user.nickname + ", position " + position)
                    }
                }
            }

            /*
            for(uid in arr) {
                // var uid = arr.get(position)
                for(snapshot in querySnapshot!!.documents) {
                    var user = snapshot.toObject(UserModelFS::class.java)
                    if (uid.equals(user?.uid)) {
                        pwrg.add(user!!)
                        // Log.v("pwrg", "current:" + user.nickname + ", position " + position)
                    }
                }
            }
             */

            notifyDataSetChanged()

            if(pwrg.size!=0) {
                // 배너에 데이터 연결
                view.nameTV.text = pwrg!![position].nickname
                view.detailTV.text = pwrg!![position].introduce

                if(mContext!=null) {
                    Log.v("pwrg", "mContext: not null")
                    var imgRef = storage?.reference?.child("images")?.child(pwrg!![position].userProPic.toString())
                    imgRef?.downloadUrl?.addOnSuccessListener { uri ->
                        Glide.with(mContext)
                                .load(uri)
                                .into(view.thumb)
                        // Log.v("pwrg", "img Success")
                    }
                } else {
                    view.thumb.setImageResource(R.drawable.logo)
                }
            }
            else {
                view.nameTV.text = ""
                view.detailTV.text = ""
                view.thumb.setImageResource(R.drawable.logo)
            }
        }

        container.addView(view)

        view.setOnClickListener {
            Log.v("pwrg","들어왔나요?")
            val intent = Intent(mContext, UserInfoActivity::class.java)  // 오류남
            intent.putExtra("UserId",pwrg!![position].uid.toString())
            mContext!!.startActivity(intent)
        }

        return view
        //return super.instantiateItem(container, position)
    }

    // position에 위치한 페이지 제거
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
        // super.destroyItem(container, position, `object` as View)
    }

    // 사용 가능한 뷰 개수 리턴 - user 수
    override fun getCount(): Int {
        return 3
    }

    // 페이지뷰가 특정 key object와 연관되는지
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view==`object`)
    }

}