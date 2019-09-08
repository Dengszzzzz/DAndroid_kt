package com.sz.dengzh.commonlib.bean

/**
 * Created by administrator on 2018/12/5.
 * 网络返回必带参数
 */
open class NetBean {

    var respCode: String? = null
    var respMsg: String? = null
    var sessionId: String? = null

    constructor() {}

    constructor(respCode: String, respMsg: String) {
        this.respCode = respCode
        this.respMsg = respMsg
    }

    override fun toString(): String {
        return "NetBean{" +
                "respCode='" + respCode + '\''.toString() +
                ", respMsg='" + respMsg + '\''.toString() +
                ", sessionId='" + sessionId + '\''.toString() +
                '}'.toString()
    }
}
