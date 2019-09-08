package com.sz.dengzh.dandroid_kt.moduel.viewdetails.customview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socks.library.KLog
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import com.sz.dengzh.dandroid_kt.utils.formatStringColor
import com.sz.dengzh.dandroid_kt.utils.setTvColor
import kotlinx.android.synthetic.main.ac_inflater.view.*
import kotlinx.android.synthetic.main.fr_simple.view.*

/**
 * 此处总结有错误,待定
 *
 * 此篇文章只是个人学习笔记总结，详细内容可看大神的文章 https://blog.csdn.net/qq_38228254/article/details/79593153
 *
 * LayoutInflater理解
 * 1.activity 中方法 getLayoutInflater() 等价于其他地方调用  LayoutInflater.from(context)；
 * 2.以下三种方法调用理解
 * Inflate(resId , null ) 只创建temp ,返回temp
 * Inflate(resId , parent, false )创建temp，然后执行temp.setLayoutParams(params);返回temp
 * Inflate(resId , parent, true ) 创建temp，然后执行root.addView(temp, params);最后返回root
 * 3、解释
 * Inflate(resId , null)不能正确处理宽和高是因为：layout_width,layout_height是相对了父级设置的，必须与父级的LayoutParams一致。而此temp的getLayoutParams为null。
 * Inflate(resId , parent,false) 可以正确处理，因为temp.setLayoutParams(params);这个params正是root.generateLayoutParams(attrs);得到的。
 * Inflate(resId , parent,true)不仅能够正确的处理，而且已经把resId这个view加入到了parent，并且返回的是parent，和以上两者返回值有绝对的区别。
 * 举个栗子：
 * 1）在listView中调用第三个种，会报错，此时返回的view是listView而不是itemView。
 *
 */
class InflaterActivity : BaseActivity<TempContract.View, TempPresenter>(), TempContract.View {

    private lateinit var mInflater: LayoutInflater

    private var desc = "LayoutInflate理解\n" +
            "1.activity 中方法 getLayoutInflater() 等价于其他地方调用  LayoutInflater.from(context)；\n" +
            "2.以下三种方法调用理解\n" +
            "Inflate(resId , null ) 只创建temp ,返回temp\n" +
            "Inflate(resId , parent, false )创建temp，然后执行temp.setLayoutParams(params);返回temp\n" +
            "Inflate(resId , parent, true ) 创建temp，然后执行root.addView(temp, params);最后返回root\n" +
            "3、解释\n" +
            "Inflate(resId , null)不能正确处理宽和高是因为：layout_width,layout_height是相对了父级设置的，必须与父级的LayoutParams一致。而此temp的getLayoutParams为null。\n" +
            "Inflate(resId , parent,false) 可以正确处理，因为temp.setLayoutParams(params);这个params正是root.generateLayoutParams(attrs);得到的。\n" +
            "Inflate(resId , parent,true)不仅能够正确的处理，而且已经把resId这个view加入到了parent，并且返回的是parent，和以上两者返回值有绝对的区别。\n" +
            "举个栗子：\n" +
            "1）在listView中调用第三个种，会报错，此时返回的view是listView而不是itemView。"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mInflater = LayoutInflater.from(this)

        val view1 = mInflater.inflate(R.layout.ac_inflater, null)
        val view2 = mInflater.inflate(R.layout.ac_inflater,
            findViewById<View>(android.R.id.content) as ViewGroup, false
        )
        val view3 = mInflater.inflate(
            R.layout.ac_inflater,
            findViewById<View>(android.R.id.content) as ViewGroup, true
        )

        KLog.e(TAG, "view1 = $view1 , view1.layoutParams = $view1.layoutParams")
        KLog.e(TAG, "view2 = $view2 , view2.layoutParams = $view2.layoutParams")
        KLog.e(TAG, "view3 = $view3 , view3.layoutParams = $view3.layoutParams")

        //打印得结果，view1的layoutParams并不为null，而是android.widget.TextView.layoutParams

     //   view3.tv_desc.text = desc
        //view3.tv_desc.setTvColor(R.color.colorPrimary)
        view3.tv_desc.text = desc.formatStringColor(R.color.colorPrimary,0,100)

        var ll = "a123456a789"
        desc.replace('a','A')
        desc.replace("a","A")
    }
}
