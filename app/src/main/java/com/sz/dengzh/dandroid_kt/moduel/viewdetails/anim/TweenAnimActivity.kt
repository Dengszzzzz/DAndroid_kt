package com.sz.dengzh.dandroid_kt.moduel.viewdetails.anim

import android.os.Bundle
import android.view.animation.AnimationUtils
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_tween_anim.*

/**
 * Created by dengzh on 2019/9/5
 * 补间动画
 * 1)单动画：
 *  在anim包下写anim资源文件，例如scale，大多数参数都是fromXX，toXX。再调用AnimationUtils.loadAnimation()
 *  得到Animation，调用iv.startAnimation(animation)开启动画;
 * 2)混合动画：
 *  用set包裹scale,alpha等单动画。
 * 3）关键方法和参数：
 *   1.大多数参数都是fromXX，toXX。
 *   2.Interpolator控制动画的变化速率。
 *     android:shareInterpolator="@android:anim/accelerate_decelerate_interpolator"
 *   3.pivot 决定了当前动画执行的参考位置
 *      android:pivotX="50%",android:pivotY="50%"。
 *   4.循环：android:repeatCount="infinite"  或  animation.setRepeatCount(-1);
 *   5.正序：  android:repeatMode="restart"  逆序：  android:repeatMode="reverse"
 *   6.组合动画，设置某个动画多久开始。android:startOffset="3000"
 * 4)注意：
 *   组合动画循环，不能给set设置repeatCount，要给单一动画设置。
 *
*/
class TweenAnimActivity :BaseActivity<TempContract.View,TempPresenter>(),TempContract.View{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_tween_anim)
        initTitle("补间动画")

        //补间动画-单
        val anim = AnimationUtils.loadAnimation(this,R.anim.anim_single)
        //anim.repeatCount = -1  //循环
        mendingIv.startAnimation(anim)

        //补间动画-组合
        val anim2 = AnimationUtils.loadAnimation(this, R.anim.anim_combination)
        //anim2.repeatCount = -1  //如此设置无效
        compenceIv.startAnimation(anim2)
    }

}