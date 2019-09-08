package com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager

import android.content.res.Resources
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TabLayout
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_tab_pager.*
import java.lang.reflect.Field

/**
 * Created by dengzh on 2019/9/4
 * TabLayout指示条长度。
 * 1.通过反射修改指示条宽度，但是宽度最小不能比内容小。
 * 2.用第三方库吧
 * https://github.com/hackware1993/MagicIndicator
 * https://github.com/H07000223/FlycoTabLayout
 * https://github.com/ogaclejapan/SmartTabLayout
 */
class TabIndicatorActivity :BaseActivity<TempContract.View, TempPresenter>(), TempContract.View{

    private var mTypeList:MutableList<EventTypeBean> = ArrayList()
    private lateinit var myPagerAdapter:MyFrPagerAdapter<SimpleFragment>
    private var fragments:MutableList<SimpleFragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_tab_pager)
        initTitle("TabLayout指示条长度")
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
        /**
         * TabLayout.setupWithViewPager(viewPager)，将TabLayout和Viewpager两者绑定在一起。
         * 实际上，是setupWithViewPager()方法底部调用PagerAdapter中的getPageTitle()方法实现联系。
         *
         * 注意：setupWithViewPager 会执行 removeAllTabs,然后重新new Tab，所以要在关联之后。
         *      调用TabLayout.getTabAt(i)方法来设置title。或者在PagerAdapter的getPageTitle()返回标题。
         * */
        tabLayout.setupWithViewPager(viewPager)
        mTypeList.forEachIndexed { index, bean ->
            tabLayout.getTabAt(index)!!.text = bean.typename
        }
        viewPager.setCurrentItem(0,false)

        setIndicatorWidth(tabLayout,30)
    }


    /**
     * 方法1：
     * 反射指示器变短,制定margin。
     * api 28的Tablayout 的源码发现，原来的mTabStrip和mTextView已经改名为slidingTabIndicator和textView。
     * 此方法会把整个tabView margin设置，点击范围变小。不好
     *
     * 此方法不是对TextView做修改，而是对整个tabView做修改
     */
    private fun setIndicator(tabs:TabLayout,leftDip:Int,rightDip:Int){
        /**
         * View.post()保证操作是在View宽高计算完毕之后
         * */
        tabs.post {
            val tabLayout = tabs.javaClass
            var tabStrip: Field? = null
            try {
                tabStrip = tabLayout.getDeclaredField("slidingTabIndicator")
                //tabStrip = tabLayout.getDeclaredField("mTabStrip");
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }

            //设置该字段对象的可访问标志,在其他类里获取该类的私有成员变量时，需要设置访问标志为true，否则会报异常
            tabStrip!!.isAccessible = true
            var llTab: LinearLayout? = null
            try {
                llTab = tabStrip.get(tabs) as LinearLayout
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            //转变为标准尺寸的一个函数
            val left = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                leftDip.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
            val right = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                rightDip.toFloat(),
                Resources.getSystem().displayMetrics).toInt()

            //直接获取子View改变宽度
            for ( i in 0 until llTab!!.childCount){
                val child = llTab.getChildAt(i)
                child.setPadding(0, 0, 0, 0)
                var params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                params.leftMargin = left
                params.rightMargin = right
                child.layoutParams = params
                child.invalidate()
            }

        }
    }

    /**
     * 方法2：
     * 只对TextView做修改
     * @param tabLayout
     * @param margin
     */
    private fun setIndicatorWidth(tabs:TabLayout,margin:Int){
        tabs.post {
            try {
                // 拿到tabLayout的slidingTabIndicator属性
                val tabStrip = tabs.javaClass.getDeclaredField("slidingTabIndicator")
                tabStrip.isAccessible = true
                var llTab = tabStrip.get(tabs) as LinearLayout
                for ( i in 0 until llTab!!.childCount){
                    val tabView = llTab.getChildAt(i)
                    //拿到tabView的mTextView属性
                    var textViewField = tabView.javaClass.getDeclaredField("textView")
                    textViewField.isAccessible = true
                    var mTextView = textViewField.get(tabView) as TextView
                    tabView.setPadding(0,0,0,0)
                    // 因为想要的效果是字多宽线就多宽，所以测量mTextView的宽度
                    var width = mTextView.width
                    if (width == 0){
                        mTextView.measure(0,0)
                        width = mTextView.measuredWidth
                    }
                    // 设置tab左右间距,注意这里不能使用Padding,因为源码中线的宽度是根据tabView的宽度来设置的
                    val params = tabView.layoutParams as LinearLayout.LayoutParams
                    params.width = width
                    params.leftMargin = margin
                    params.rightMargin = margin
                    tabView.layoutParams = params
                    tabView.invalidate()
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

}