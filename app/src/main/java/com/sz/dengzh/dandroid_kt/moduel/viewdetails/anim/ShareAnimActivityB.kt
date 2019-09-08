package com.sz.dengzh.dandroid_kt.moduel.viewdetails.anim

import android.os.Bundle
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import com.sz.dengzh.dandroid_kt.utils.glideUtils.GlideUtils
import kotlinx.android.synthetic.main.ac_share_animb.*

/**
 * 共享元素做转场动画B
 */
class ShareAnimActivityB : BaseActivity<TempContract.View, TempPresenter>(), TempContract.View{

    private var url = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=13326166,878266866&fm=26&gp=0.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_share_animb)

        initTitle("界面切换--共享元素B")
        ivBack?.setOnClickListener { finishAfterTransition() }

        url = intent.getStringExtra("url")
        GlideUtils.loadImg(this, iv_share_top, url)

/*

        //延迟共享动画
        postponeEnterTransition()
        //开始 已延迟的共享动画
        Handler().postDelayed({
            startPostponedEnterTransition()
        },3000)

*/

    }

}
