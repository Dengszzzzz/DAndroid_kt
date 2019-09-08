package com.sz.dengzh.dandroid_kt.moduel.viewdetails.anim

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionInflater
import android.view.Gravity
import android.view.View
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_transitionb.*

/**
 * Created by dengzh on 2019/6/9
 * https://www.jianshu.com/p/69d48f313dc4
 * https://www.jianshu.com/p/10a820329959
 * https://blog.csdn.net/w630886916/article/details/78319502
 *
 * 1.通过返回键 or finishAfterTransition() 触发回退，退场效果生效，且和进场效果相反。
 * 2.调用finish()，没有退场效果。
 * 3.默认回退动画和进场动画相反。
 * 因为如果reenter 或者 return transition没有明确设置，则将用exit 和enter的transition替代。
 * 4.如果要设置B退回A，B的退出动画，要设置的事setReturnTransition，而不是setExitTransition。
 *
 *
 */
class TransitionActivityB :BaseActivity<TempContract.View, TempPresenter>(), TempContract.View,View.OnClickListener{

    private var type: Int = 0

    companion object {
        fun go(context: Activity, type: Int) {
            val intent = Intent(context, TransitionActivityB::class.java)
            intent.putExtra("type", type)
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(R.layout.ac_transitionb)
        initTitle("")
        type = intent.getIntExtra("type", 0)
        ivBack!!.setOnClickListener(this)
        btn_out1.setOnClickListener(this)
        btn_out2.setOnClickListener(this)
        btn_out3.setOnClickListener(this)
        setEnterAnim()
    }

    /**
     * 设置进场动画
     */
    private fun setEnterAnim() {
        when (type) {
            0 -> {
                tvTitle!!.text = "转场动画-Slide"
                window.enterTransition = TransitionInflater.from(this).inflateTransition(R.transition.activity_slide)
            }
            1 -> {
                tvTitle!!.text = "转场动画-Slide2"
                window.enterTransition = Slide().apply {
                    duration = 3000
                    slideEdge = Gravity.END
                }
            }
            2 -> {
                tvTitle!!.text = "转场动画-Fade"
                window.enterTransition = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade)
            }
            3 -> {
                tvTitle!!.text = "转场动画-Fade2"
                window.enterTransition = Fade().setDuration(3000)
            }
            4 -> {
                tvTitle!!.text = "转场动画-Explode"
                window.enterTransition = TransitionInflater.from(this).inflateTransition(R.transition.activity_explode)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_out1 -> finish()
            R.id.btn_out2 -> finishAfterTransition()
            R.id.btn_out3 -> {
                //当B 返回A时，使B中的View退出场景的transition 在B中设置
                window.returnTransition = Slide().apply {
                    duration = 3000
                    slideEdge = Gravity.TOP
                }
                finishAfterTransition()
            }
            R.id.ivBack -> finishAfterTransition()
        }
    }
}
