package com.sz.dengzh.dandroid_kt.moduel.viewdetails.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_object_anim.*

/**
 * Created by dengzh on 2019/9/5
 */
class ObjectAnimActivity:BaseActivity<TempContract.View,TempPresenter>(),TempContract.View{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_object_anim)
        initTitle("属性动画")
        onRotateAnim()
        onAlphaAnim()
        onCombinationObject()
        onPointAnim()
    }

    /**
     * 旋转 从 90° 转到 360°
     * 用T.with,不能做非空判断
     * */
    fun onRotateAnim(){
        val anim = ObjectAnimator.ofFloat(
            rotateIv,"rotation",90f,36f)
        with(anim){
            this.duration = 10000
            start()
        }
    }

    /**
     * 透明度  此处设置的值可以不按大小排序，比补间动画更灵活
     * 用.run
     */
    fun onAlphaAnim(){
        val animator = ObjectAnimator.ofFloat(
            alphaIv,"alpha",1.0f, 0.8f, 0.6f, 0.4f, 0.2f, 0.0f)
        animator.run {
            this.repeatCount = ObjectAnimator.INFINITE  //无限循环
            this.repeatMode = ObjectAnimator.REVERSE  //REVERSE：0->1->0 如此模式
            this.duration = 3000
            start()
        }
    }

    fun onCombinationObject(){
        val alphaAnim = ObjectAnimator.ofFloat(
            combinationIv, "alpha",
            1.0f, 0.5f, 0.8f, 1.0f
        )
        val scaleXAnim = ObjectAnimator.ofFloat(
            combinationIv, "scaleX",
            0.0f, 1.0f
        )
        val scaleYAnim = ObjectAnimator.ofFloat(
            combinationIv, "scaleY",
            0.0f, 2.0f
        )
        val rotateAnim = ObjectAnimator.ofFloat(
            combinationIv, "rotation",
            0f, 360f
        )
        val transXAnim = ObjectAnimator.ofFloat(
            combinationIv, "translationX",
            100f, 400f
        )
        val transYAnim = ObjectAnimator.ofFloat(
            combinationIv, "translationY",
            100f, 750f
        )

        //组合动画
        val set = AnimatorSet().apply{
            //同时执行
            playTogether(alphaAnim,scaleXAnim,scaleYAnim,rotateAnim,transXAnim,transYAnim)
            //按顺序执行
            //set.playSequentially(alphaAnim, scaleXAnim, scaleYAnim, rotateAnim, transXAnim, transYAnim)
            this.duration = 10000
            start()
        }
    }

    fun onPointAnim(){
        pointAnimView.let {
            it.setInterpolatorType(8)
            it.radius = 10f
            it.color = ContextCompat.getColor(this, R.color.c_87cfdc)
            it.post {
                it.startAnimation()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        pointAnimView?.stopAnimation()
    }



}