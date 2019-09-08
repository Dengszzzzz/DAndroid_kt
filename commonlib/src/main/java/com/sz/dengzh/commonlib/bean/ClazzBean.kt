package com.sz.dengzh.commonlib.bean

import android.app.Activity

/**
 * Created by dengzh on 2019/9/4
 * 下面有3个构造函数，是方法重载，在Java中，一旦方法重载数量过多，可能出现调用错误的问题。
 * 比如如果设置
 * ClazzBean("123",null) 那么就没法遍布是调用的第二个还是第三个构造函数
 *
 * 在kotlin中，可通过函数命名参数来解决这个问题。如此一来，可以不需要做方法重载了，具体看ClazzBean2。
 */
class ClazzBean{
    var clazz:Class<out Activity>? = null
    var clazzName: String? = null
    var hostAndPath: String? = null   //路由路径

    constructor()

    constructor(clazzName:String,clazz:Class<out Activity>?){
        this.clazzName = clazzName
        this.clazz = clazz
    }

    constructor(clazzName:String,hostAndPath:String?){
        this.clazzName = clazzName
        this.hostAndPath = hostAndPath
    }
}
