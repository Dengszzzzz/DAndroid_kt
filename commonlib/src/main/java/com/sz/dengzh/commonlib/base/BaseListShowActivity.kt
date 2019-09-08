package com.sz.dengzh.commonlib.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import com.sz.dengzh.commonlib.R
import com.sz.dengzh.commonlib.bean.ClazzBean
import kotlinx.android.synthetic.main.base_ac_custom_list.*

import java.util.ArrayList

/**
 * Created by dengzh on 2018/4/18.
 * 列表显示界面基类
 */

abstract class BaseListShowActivity : BaseActivity<TempContract.View,TempPresenter>(),TempContract.View{

    //List不可写了，支持可写的是MutableList。
    //这里是 List<ClazzBean> mList = new ArrayList<>();转换过来的
    protected var mList: MutableList<ClazzBean> = ArrayList()
    protected lateinit var mAdapter: BaseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_ac_custom_list)
        initView()
        initUI()
        initData()
    }

    private fun initView() {

        mAdapter = BaseListAdapter(mList)
        mAdapter.setOnItemClickListener { _, _, position ->
            //? 可用于做空判断，不为空就执行let里面的
            mList[position].clazz?.let{
                startActivity(it)
            }
        }
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
    }


    protected abstract fun initUI()
    protected abstract fun initData()

    protected fun addClazzBean(name: String, clazz: Class<out Activity>?=null) {
        mList.add(ClazzBean(name, clazz))
    }


}
