package com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by dengzh on 2019/9/4
 * Kotlin主构造函数不能包含任何代码，如果需要在构造函数中做一些初始化的操作，可用关键字 init声明
 */
class MyFrPagerAdapter <T:Fragment> (fm:FragmentManager, fragments:MutableList<T>)
    : FragmentPagerAdapter(fm){

    private val mFrList: MutableList<T>?

    //初始化代码块
    init {
        //在这里面做一些需要在主构造函数中做的初始化操作
        mFrList = fragments
    }

    override fun getCount(): Int {
        return mFrList?.size?:0
    }

    override fun getItem(position: Int): T {
        return mFrList!![position]
    }

}