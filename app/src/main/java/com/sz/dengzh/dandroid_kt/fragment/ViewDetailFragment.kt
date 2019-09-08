package com.sz.dengzh.dandroid_kt.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sz.dengzh.commonlib.base.BaseFragment
import com.sz.dengzh.commonlib.base.BaseListAdapter
import com.sz.dengzh.commonlib.bean.ClazzBean
import com.sz.dengzh.dandroid_kt.R
import com.sz.dengzh.dandroid_kt.moduel.viewdetails.anim.AnimListActivity
import com.sz.dengzh.dandroid_kt.moduel.viewdetails.customview.CustomListActivity
import com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.ViewPagerListActivity
import com.xiaojinzi.component.impl.Router
import kotlinx.android.synthetic.main.fr_custom_list.*

/**
 * Created by dengzh on 2019/9/4
 */
class ViewDetailFragment : BaseFragment(){

    private var mList:MutableList<ClazzBean> = ArrayList()
    private lateinit var mAdapter:BaseListAdapter


    companion object{
        fun newInstance():ViewDetailFragment{
            return ViewDetailFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fr_custom_list, container, false)
    }

    /**
     * 在xml布局加载后，再去获取控件id
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView(){
        mList.add(ClazzBean("ViewPager", ViewPagerListActivity::class.java))
        mList.add(ClazzBean("自定义View", CustomListActivity::class.java))
        mList.add(ClazzBean("Anim动画", AnimListActivity::class.java))


        mAdapter = BaseListAdapter(mList)
        mAdapter.setOnItemClickListener { _, _, position ->
            mList[position].clazz?.let{
                startActivity(it)
            }
            mList[position].hostAndPath?.let {
                Router.with(mActivity).hostAndPath(it).navigate()
            }
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mActivity)
        recyclerView.adapter = mAdapter
    }

}