package com.sz.dengzh.dandroid_kt.moduel.specialFunc.download


import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author
 * @date 2018/8/3
 * @description
 */
interface DownloadApiService {

    /**
     * @Streaming 注解是为了避免将整个文件读进内存，这是在下载大文件时需要注意的地方。
     * 在请求头添加Range就可以实现服务器文件的下载内容范围了。
     */
    @Streaming
    @GET
    //range下载参数，传下载区间使用
    //url 下载链接
    fun executeDownload(@Header("Range") range: String, @Url url: String): Observable<ResponseBody>
}
