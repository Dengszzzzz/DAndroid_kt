package com.sz.dengzh.dandroid_kt

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.sz.dengzh.commonlib.CommonConfig
import com.sz.dengzh.dandroid_kt.utils.CrashHandler
import com.xiaojinzi.component.Component
import com.xiaojinzi.component.impl.application.ModuleManager

/**
 * Created by dengzh on 2019/9/3
 */

class App: Application(){

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var ctx:Context
    }


    override fun onCreate() {
        super.onCreate()
        ctx = this
        CommonConfig.init(this)
        CrashHandler.get().init(this)
        initComponent()
    }

    private fun initComponent(){
        // 初始化
        Component.init(this,BuildConfig.DEBUG)
        // 如果你依赖了 rx 版本,需要配置这句代码,否则删除这句
        // RxErrorIgnoreUtil.ignoreError();
        // 注册业务模块,注册的字符串是各个业务模块配置在 build.gradle 中的 HOST
        ModuleManager.getInstance().registerArr("app", "drxjava")
        if (BuildConfig.DEBUG) {
            // 框架还带有检查重复的路由和重复的拦截器等功能,在 `debug` 的时候开启它
            ModuleManager.getInstance().check()
        }
    }

}