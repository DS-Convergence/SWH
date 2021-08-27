package com.example.squirrelwarehouse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.main_viewpager.view.*

class MainViewpagerAdapter : PagerAdapter() {
    private var mContext: Context?=null

    fun ViewPagerAdapter(context: Context){
        mContext=context;
    }

    // position에 맞는 내용 생성
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view= LayoutInflater.from(container.context).inflate(R.layout.main_viewpager,container,false)
        view.nameTV.text = "유저 "+position
        view.detailTV.text = "유저"+position+"의 한줄설명"
        view.thumb.setImageResource(R.drawable.logo)
        container.addView(view)
        return view
        //return super.instantiateItem(container, position)
    }

    // position에 위치한 페이지 제거
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
        //super.destroyItem(container, position, `object`)
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