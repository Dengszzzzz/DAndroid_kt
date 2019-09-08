package com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_tab_pager.*
import kotlinx.android.synthetic.main.item_tab_pager_top.view.*

/**
 * Created by dengzh on 2019/9/4
 * TabLayout,ViewPager,fragment 演示
 */
class TabPagerActivity : BaseActivity<TempContract.View,TempPresenter>(),TempContract.View{

    private var mTypeList:MutableList<EventTypeBean> = ArrayList()
    private lateinit var myPagerAdapter:MyFrPagerAdapter<SimpleFragment>
    private var fragments:MutableList<SimpleFragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_tab_pager)
        initTitle("Tab和ViewPager演示")
        initData()
        initView()
    }

    private fun initData(){
        //循环5次，依次0至4
       for(i in 0 until 5){
           val bean = EventTypeBean(i,"类型$i","")
           mTypeList.add(bean)
           fragments.add(SimpleFragment.newInstance(i.toString()))
       }
    }

    private fun initView(){
        myPagerAdapter = MyFrPagerAdapter(supportFragmentManager,fragments)
        viewPager.adapter = myPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        /**
         * TabLayout.setupWithViewPager(viewPager)，将TabLayout和Viewpager两者绑定在一起。
         * 实际上，是setupWithViewPager()方法底部调用PagerAdapter中的getPageTitle()方法实现联系。
         *
         * 注意：setupWithViewPager 会执行 removeAllTabs,然后重新new Tab，所以要在关联之后。
         *      调用TabLayout.getTabAt(i)方法来设置title。或者在PagerAdapter的getPageTitle()返回标题。
         * */
        //设置样式
        mTypeList.forEachIndexed { index, bean ->
            //tabLayout.getTabAt(index)!!.text = bean.typename
            //自定义样式
            tabLayout.getTabAt(index)!!.customView = getTabView(bean.typename,bean.picture)
        }
        viewPager.setCurrentItem(0,false)
    }

    /**
     * 构造 tabview
     * @param name
     * @param url
     * @return
     */
    private fun getTabView(name:String,url:String): View {
        val view = layoutInflater.inflate(R.layout.item_tab_pager_top,null)
        view.typeNameTv.text = name
        return view
    }
}