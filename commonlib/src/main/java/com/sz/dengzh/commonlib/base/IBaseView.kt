package com.sz.dengzh.commonlib.base

import com.sz.dengzh.commonlib.bean.NetBean
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * Created by dengzh on 2019/9/3
 */

interface IBaseView {

    fun handleFailResponse(netBean: NetBean)
    fun showLoading()
    fun dismissLoading()

    fun <T> bindToLifecycle(): LifecycleTransformer<T>
    fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T>

}