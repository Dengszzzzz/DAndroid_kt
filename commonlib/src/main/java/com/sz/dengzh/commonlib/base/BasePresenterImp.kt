package com.sz.dengzh.commonlib.base

/**
 * Created by dengzh on 2019/9/3
 */
open class BasePresenterImp<V:IBaseView>:IBasePresenter<V>{

    protected var mView:V? = null

    override fun attachView(view: V) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

}