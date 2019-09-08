package com.sz.dengzh.dandroid_kt.moduel.viewdetails.anim

import com.sz.dengzh.commonlib.base.BaseListShowActivity
import com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager.TabIndicatorActivity
import com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager.TabPagerActivity

/**
 * Created by administrator on 2018/8/16.
 * 逐帧,补间，属性动画对比
 * 1.逐帧动画
 * 1)在drawable文件夹下，写 animation-list,在布局文件ImageView的src设置该resId，
 * 再用iv.getDrawable()得到AnimationDrawable，再调用drawable.start()开启。
 * <p>
 *
 * 2.补间动画
 * 1)单：在anim包下写anim资源文件，例如scale，大多数参数都是fromXX，toXX。再调用AnimationUtils.loadAnimation()得到Animation，
 * 调用iv.startAnimation(animation)开启动画;
 * 2)混合：在anim包下写anim资源文件，用set包裹scale,alpha等。调用方式和上述一致
 * 3)Interpolator 主要作用是可以控制动画的变化速率; pivot 决定了当前动画执行的参考位置
 * <p>
 *
 * 3.属性动画
 * 1）ObjectAnimator对象，设置相关属性，调用start()开启动画。
 * 例如：ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(targetView,"scaleX", 0.0f, 1.0f);
 * 2)具体看PointAnimView类
 * 3）也可以用xml写属性动画，只是不推荐。
 * 4) Interpolator:插值器，控制动画速率，自定义需要重写getInterpolation(float input)。
 * 5）TypeEvaluator:估值器，自定义需要重写evaluate()方法，计算对象的属性值并将其封装成一个新对象返回。
 * <p>
 *
 * 4.传统动画和属性动画对比
 * 1)属性动画，真正移动了view的位置。传统动画没有。
 * 2)属性动画，repeatCount设置为无限循环时，记得在onStop()将动画停止，否则内存泄漏
 * 3)属性动画不如xml实现的补间动画复用率高。
 */
class AnimListActivity:BaseListShowActivity(){

    override fun initUI() {
        initTitle("anim总结")
    }

    override fun initData() {
        addClazzBean("逐帧动画", FrameAnimActivity::class.java)
        addClazzBean("补间动画", TweenAnimActivity::class.java)
        addClazzBean("属性动画", ObjectAnimActivity::class.java)
        addClazzBean("界面切换动画 - Transition", TransitionActivity::class.java)
        addClazzBean("转场动画-共享元素", ShareAnimActivity::class.java)
        mAdapter.notifyDataSetChanged()
    }

}
