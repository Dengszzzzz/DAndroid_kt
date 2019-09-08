package com.sz.dengzh.commonlib.base

/**
 * Created by dengzh on 2019/9/3
 */
interface TempContract {

    interface View:IBaseView

    interface Presenter:IBasePresenter<View>

}