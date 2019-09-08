package com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PersistableBundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import com.sz.dengzh.dandroid_kt.utils.glideUtils.GlideUtils
import kotlinx.android.synthetic.main.ac_ban_pager.*

/**
 * Created by dengzh on 2019/9/4
 * ViewPager 广告页
 * ViewPager实现轮播图效果，通过addOnPageChangeListener()，在 OnPageChangeListener 监听器中有一个页面
 * 滑动结束时的回调方法 onPageSelected(int position) ,我们只需要在这个方法中，来设置item和指示器跟随变化就行了。
 * 指示器就是在ViewPager上放一个覆盖层。
 */

class BanPagerActivity : BaseActivity<TempContract.View, TempPresenter>(), TempContract.View{

    private val mList:MutableList<String> = ArrayList()
    private var lastPosition = 0
    private val delayMillis = 2000L
    private var isAutoPlay = true
    private var type = 1


    private val mHandler = object : Handler(){

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 1 && isAutoPlay){
                if(type == 0){
                    //这种除模方式，会到第3张时，又切回第1张，但是中间划过了第二张
                    viewPager.currentItem = (viewPager.currentItem + 1) % mList.size
                }else{
                    //无限轮播时
                    viewPager.currentItem+=1
                }
                this.sendEmptyMessageDelayed(1, delayMillis)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_ban_pager)
        initTitle("banner广告页")
        initData()
        initView()
    }

    private fun initData(){
        mList.add("http://pic15.nipic.com/20110628/1369025_192645024000_2.jpg")
        mList.add("http://pic25.nipic.com/20121112/9252150_150552938000_2.jpg")
        mList.add("http://www.2cto.com/uploadfile/Collfiles/20140615/20140615094106112.jpg")
    }

    private fun initView(){
        initDotView()
        viewPager.adapter = BannerAdapter(type,this,mList)
        //object修饰匿名内部类
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{

            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            /**
             * 页面滑动结束时回调
             * @param i
             */
            override fun onPageSelected(i: Int) {
                ll_dot.getChildAt(lastPosition).setBackgroundResource(R.drawable.dot_grey)
                lastPosition = if(type == 0){
                    i
                }else{
                    i % mList.size
                }
                ll_dot.getChildAt(lastPosition).setBackgroundResource(R.drawable.dot_blue)
            }
        })
        if (type == 0){
            viewPager.currentItem = 0
        }else{
            viewPager.currentItem = (10000 * mList.size)
        }
        mHandler.sendEmptyMessage(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeMessages(1)
    }

    /**
     * 初始化指示器
     */
    private fun initDotView(){
        val params = LinearLayout.LayoutParams(20,20)
        params.setMargins(10,0,10,0)
        mList.forEach {
            var view = View(this)
            view.layoutParams = params
            view.setBackgroundResource(R.drawable.dot_grey)
            ll_dot.addView(view)
        }
    }



    class BannerAdapter(val type:Int,val mContext: Context,var mList:MutableList<String>):PagerAdapter(){

        /**
         * 是获取当前窗体界面数，也就是数据的个数。
         * @return
         */
        override fun getCount(): Int {
            return if(type == 1){
                Int.MAX_VALUE
            }else{
                mList.size
            }
        }

        /**
         * 这个方法用于判断是否由对象生成界面，官方建议直接返回 return view == object;
         * @param view
         * @param o
         * @return
         */
        override fun isViewFromObject(view: View, o: Any): Boolean {
            return view == o
        }

        /**
         * 要显示的页面或需要缓存的页面，调用这个方法进行布局的初始化。
         * @param container
         * @param position
         * @return
         */
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var view = ImageView(container.context)
            view.scaleType = ImageView.ScaleType.FIT_XY
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            if (type == 0){
                GlideUtils.loadImg(mContext, view, mList[position])
            }else{
                GlideUtils.loadImg(mContext, view, mList[position % mList.size])
            }
            container.addView(view)
            return view
        }

        /**
         * 如果页面不是当前显示的页面也不是要缓存的页面，会调用这个方法，将页面销毁。
         * @param container
         * @param position
         * @param o
         */
        override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
            container.removeView(o as View)
        }
    }
}
