package com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager

import com.sz.dengzh.commonlib.base.BaseListShowActivity
import com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager.TabIndicatorActivity
import com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager.TabPagerActivity

/**
 * Created by dengzh on 2019/9/4
 */
class ViewPagerListActivity:BaseListShowActivity(){

    override fun initUI() {
        initTitle("viewpager示例")
    }

    override fun initData() {
        addClazzBean("TabLayout，ViewPager，Fragment", TabPagerActivity::class.java)
        addClazzBean("TabLayout的指示器", TabIndicatorActivity::class.java)
        addClazzBean("广告条 viewpager", BanPagerActivity::class.java)
        mAdapter.notifyDataSetChanged()
    }

}
