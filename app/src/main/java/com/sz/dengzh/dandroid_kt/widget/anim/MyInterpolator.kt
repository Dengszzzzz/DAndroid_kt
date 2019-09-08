package com.sz.dengzh.dandroid_kt.widget.anim

import android.view.animation.BaseInterpolator

/**
 * Created by dengzh on 2019/9/5
 * 自定义先减速后加速插值器
 * 控制动画的变化速率
 */
class MyInterpolator :BaseInterpolator(){

    /**
     * input参数是系统根据设置的动画持续时间计算出来的
     * @param input  [0,1],从0匀速增加到1
     * @return
     */
    override fun getInterpolation(input: Float): Float {
        return (4 * input - 2) * (4 * input - 2) * (4 * input - 2) / 16f + 0.5f
    }

}