package com.sz.dengzh.dandroid_kt.moduel.viewdetails.anim

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import com.sz.dengzh.dandroid_kt.utils.glideUtils.GlideUtils
import kotlinx.android.synthetic.main.ac_share_anim1.*

/**
 * 共享元素做转场动画A
 * https://www.jianshu.com/p/37e94f8b6f59
 *
 * 步骤：
 * 1.将两个Activity中需要过渡的View加上android:transitionName属性
 * 2.调用startActivity(intent, bundle);
 * 1）单元素：
 * ActivityOptionsCompat.makeSceneTransitionAnimation（Activity，View，String）
 * 第二个参数共享元素View，第三个参数是共享元素名。
 * 2）多元素：
 * Pair<>(View, String);   //每个Pair包括一个共享元素的View和name
 * ActivityOptionsCompat.makeSceneTransitionAnimation(Activity activity, Pair<View></View>, String>... sharedElements)
 *
 *
 */
class ShareAnimActivity :BaseActivity<TempContract.View, TempPresenter>(), TempContract.View, View.OnClickListener {

    private val url = GlideUtils.url

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_share_anim1)
        initTitle("界面切换--共享元素")
        GlideUtils.loadImg(this, iv_share_top, url)

        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, ShareAnimActivityB::class.java)
        intent.putExtra("url", url)
        when (v?.id) {
            R.id.btn1 -> startActivity(intent)
            R.id.btn2 -> {
                //单元素 共享
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, iv_share_top!!, getString(R.string.share_iv_top_str)
                ).toBundle()
                startActivity(intent, bundle)
            }
            R.id.btn3 -> {
                //多元素 共享
                val first = Pair<View,String>(iv_share_top, ViewCompat.getTransitionName(iv_share_top!!))
                val second = Pair<View,String>(tv_share_name, ViewCompat.getTransitionName(tv_share_name!!))
                val third = Pair<View,String>(tv_share_date, ViewCompat.getTransitionName(tv_share_date!!))
                val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, first, second, third)
                startActivity(intent, compat.toBundle())
            }
        }
    }
}
