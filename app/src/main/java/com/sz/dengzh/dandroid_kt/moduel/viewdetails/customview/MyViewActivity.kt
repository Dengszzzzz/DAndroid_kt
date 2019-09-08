package com.sz.dengzh.dandroid_kt.moduel.viewdetails.customview

import android.os.Bundle
import android.view.MotionEvent
import com.socks.library.KLog
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.commonlib.utils.ToastUtils
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_my_view.*

/**
 * Created by dengzh on 2019/9/4
 */
class MyViewActivity: BaseActivity<TempContract.View, TempPresenter>(), TempContract.View{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_my_view)
        initTitle("简单验证自定义view和viewGroup")
        
        myView.setOnTouchListener { _, event ->
            KLog.d(TAG, "onTouch")
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ToastUtils.showToast("onTouch 的 ACTION_DOWN")
                    KLog.d(TAG, "onTouch 的 ACTION_DOWN")
                }
                MotionEvent.ACTION_MOVE -> {
                    ToastUtils.showToast("onTouch 的 ACTION_MOVE  x:${event.x}  y:${event.y}")
                    KLog.d(TAG, "onTouch 的 ACTION_MOVE  x:${event.x}  y:${event.y}")
                }
                MotionEvent.ACTION_UP -> {
                    ToastUtils.showToast("onTouch 的 ACTION_UP")
                    KLog.d(TAG, "onTouch 的 ACTION_UP")
                }
            }
            return@setOnTouchListener true
        }
    }
}