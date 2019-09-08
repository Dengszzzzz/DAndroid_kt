package com.sz.dengzh.dandroid_kt.moduel.specialFunc.download

interface DownloadCallBack {

    fun onProgress(progress: Int)

    fun onCompleted()

    fun onError(msg: String)

}
