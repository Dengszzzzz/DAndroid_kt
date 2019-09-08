package com.sz.dengzh.dandroid_kt.moduel.viewdetails.customview

import com.sz.dengzh.commonlib.base.BaseListShowActivity
import com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager.TabIndicatorActivity
import com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager.TabPagerActivity

/**
 * Created by dengzh on 2019/9/4
 */
class CustomListActivity:BaseListShowActivity(){

    override fun initUI() {
        initTitle("自定义View")
    }

    override fun initData() {
        addClazzBean("简单的自定义View总结", MyViewActivity::class.java)
        addClazzBean("LayoutInflate理解", InflaterActivity::class.java)
        addClazzBean("步骤指示器", VerticalStepViewActivity::class.java)
        addClazzBean("圆角or圆形图片", NiceImageActivity::class.java)

        mAdapter.notifyDataSetChanged()
    }

}
