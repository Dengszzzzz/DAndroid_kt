package com.sz.dengzh.dandroid_kt.moduel.viewdetails.anim

import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionInflater
import android.transition.Visibility
import android.view.Gravity
import android.view.View
import android.view.Window
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_transition.*

/**
 * Created by dengzh on 2019/9/5
 * https://www.jianshu.com/p/69d48f313dc4
 *
 * 1.Transition 是指不同 UI 状态转换时的动画。其中有两个关键概念：场景（scenes）和转换（transitions）。
 *   场景定义了一个确定的 UI 状态，而转换定义了两个场景切换时的动画。
 *
 * 2.默认三种转换，分解（Explode）、滑动（Slide）、淡入淡出（Fade），如有需要可自定义Visibility子类
 *
 * 3.下列方法，需要配合使用才有对应的效果。
 *   setExitTransition() ：    A->B, A的退出变换, 在A中设置
 *   setEnterTransition() ：   A->B, B的进入变换, 在B中设置
 *   setReturnTransition() ：  B->A, B的返回变换, 在B中设置
 *   setReenterTransition() ： B->A, A的再次进入变换, 在A中设置
 *
 * 4.//设置使用TransitionManager进行动画，不设置的话系统会使用一个默认的TransitionManager
 *   getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
 *   网上都说要设置这句，目前没有设置也可以执行动画
 */
class TransitionActivity : BaseActivity<TempContract.View, TempPresenter>(), TempContract.View,View.OnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //打开窗口内容转换开关,要在setContentView()之前
        //window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(R.layout.ac_transition)
        initTitle("转场动画")

        btn_slide_in.setOnClickListener(this)
        btn_slide_in2.setOnClickListener(this)
        btn_fade_in.setOnClickListener(this)
        btn_fade_in2.setOnClickListener(this)
        btn_explode_in.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_slide_in->{
                //1.加载xml的
                val slide = TransitionInflater.from(this).inflateTransition(R.transition.activity_slide)
                window.exitTransition = slide
                TransitionActivityB.go(this, 0)
            }
            R.id.btn_slide_in2->{
                //2.代码创建
                window.exitTransition = Slide().apply {
                    duration = 3000
                    slideEdge = Gravity.END
                    mode = Visibility.MODE_OUT
                }
                TransitionActivityB.go(this, 1)
            }
            R.id.btn_fade_in->{
                window.exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade)
                TransitionActivityB.go(this, 2)
            }
            R.id.btn_fade_in2->{
                window.exitTransition = Fade().setDuration(3000)
                TransitionActivityB.go(this, 3)
            }
            R.id.btn_explode_in->{
                window.exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.activity_explode)
                TransitionActivityB.go(this, 4)
            }
        }
    }
}