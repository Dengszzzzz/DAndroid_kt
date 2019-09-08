package com.sz.dengzh.dandroid_kt.moduel.viewdetails.customview

import android.os.Bundle
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R

/**
 * Created by Dengzh
 * on 2019/7/15 0015
 */
class NiceImageActivity : BaseActivity<TempContract.View, TempPresenter>(), TempContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_nice_image)
        initTitle("圆角or圆形图片")
    }
}
