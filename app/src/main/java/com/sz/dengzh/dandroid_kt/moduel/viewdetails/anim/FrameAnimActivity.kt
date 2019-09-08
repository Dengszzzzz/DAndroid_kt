package com.sz.dengzh.dandroid_kt.moduel.viewdetails.anim

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_frame_anim.*

/**
 * Created by dengzh on 2019/9/5
 *
 * 逐帧动画
 * 1)在drawable文件夹下，写animation-list
 * 2）ImageView的src设置为该resId
 * 3）iv.getDrawable()得到AnimationDrawable，调用drawable.start()开启。
 * 4) 动画结束监听，不能用isRunning来做判断。具体可参考：https://www.jianshu.com/p/dc66b371cd3b
*/
class FrameAnimActivity:BaseActivity<TempContract.View,TempPresenter>(),TempContract.View{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_frame_anim)
        initTitle("逐帧动画")

        val anim1 = iv1.drawable as AnimationDrawable
        anim1.start()

        val anim2 = iv2.drawable as AnimationDrawable
        anim2.isOneShot = false
        anim2.start()

        iv1.setOnClickListener {
            anim1.stop()
            anim1.start()
        }
    }

}