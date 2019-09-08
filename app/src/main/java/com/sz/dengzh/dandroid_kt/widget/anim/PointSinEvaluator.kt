package com.sz.dengzh.dandroid_kt.widget.anim

import android.animation.TypeEvaluator
import android.graphics.Point
import com.socks.library.KLog
import kotlin.math.sin

/**
 * Created by dengzh on 2019/9/5
 * 估值器，决定值的具体变化数值。
 */
class PointSinEvaluator :TypeEvaluator<Point>{

    /**
     * @param fraction   表示动画完成度（根据它来计算当前动画的值）
     * @param startValue 动画的初始值
     * @param endValue   动画的结束值
     * @return
     */
    override fun evaluate(fraction: Float, startValue: Point, endValue: Point): Point {
        //根据fraction 来计算当前动画的x和y值
        val x = (startValue.x + fraction * (endValue.x - startValue.x)).toInt()
        val y = ((sin(x * Math.PI / 180) * 100).toFloat() + endValue.y / 2).toInt()

        KLog.d("Point", "x: $x    y: $y")
        //返回一个新的Point
        return Point(x,y)
    }


}