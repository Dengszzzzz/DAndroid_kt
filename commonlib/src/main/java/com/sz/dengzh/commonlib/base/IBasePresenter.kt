package com.sz.dengzh.commonlib.base

/**
 * Created by dengzh on 2019/9/3
 */

interface IBasePresenter<V:IBaseView>{

    fun attachView(view:V)
    fun detachView()
}