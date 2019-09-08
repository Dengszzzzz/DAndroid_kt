package com.sz.dengzh.commonlib.bean

import android.app.Activity

/**
 * Created by dengzh on 2019/9/4
 */
class ClazzBean2(clazzName: String? = null,clazz:Class<out Activity>? = null,hostAndPath: String? = null)

//具体使用
// val clazzBean = ClazzBean2(clazzName = "123",clazz = LoginActivity::class.java, hostAndPath = "6666")
