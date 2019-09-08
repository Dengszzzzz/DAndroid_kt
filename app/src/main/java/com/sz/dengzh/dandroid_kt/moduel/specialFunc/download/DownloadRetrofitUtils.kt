package com.sz.dengzh.dandroid_kt.moduel.specialFunc.download

import com.socks.library.KLog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.util.concurrent.TimeUnit

/**
 * @author
 * @date 2018/8/3
 * @description 文件下载工具类
 * 独立出来设置ApiService，是因为普通的网络请求可能配置了很多的拦截器，这会大大地影响文件的下载响应数据。
 * 也就是说，发起请求，在onNext接收到数据可能要耗时很长时间，目前遇到的是接近30s
 */
class DownloadRetrofitUtils private constructor() {

    private val apiService: DownloadApiService
    private val baseUrl = "http://admin123.pplussport.com/"


    companion object {
        private var instance: DownloadRetrofitUtils? = null
            get(){
                if (field == null){
                    field = DownloadRetrofitUtils()
                }
                return field
            }

        fun get():DownloadRetrofitUtils{
            return instance!!
        }
    }


    init {
        //此处把拦截器都去掉了
        val build = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .client(build)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)  //baseUrl不能为空
            .build()
        apiService = retrofit.create(DownloadApiService::class.java)
    }


    /**
     * 下载文件
     * @param range  已下载了多少kb
     * @param url    下载链接
     * @param fileName   文件名
     * @param downloadCallback  下载回调
     */
    fun downloadFile(
        range: Long, url: String, fileName: String,
        downloadCallback: DownloadCallBack) {

        val file = File(DownloadIntentService.DOWNLOAD_DIR, fileName)
        var totalLength = ""
        if (file.exists()) {
            totalLength = file.length().toString()
        }
        val unDownloadRangStr = "bytes=$range-$totalLength"
        KLog.e("DownloadIntentService", "未下载范围：$unDownloadRangStr")

        //2.如果开始下载很慢，可能是Retrofit设置太多拦截器了
        apiService.executeDownload(unDownloadRangStr, url)
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(responseBody: ResponseBody) {
                    KLog.e("DownloadIntentService", "读取到下载数据，开始解析")
                    /**
                     * RandomAccessFile支持跳到文件任意位置读写数据，RandomAccessFile对象包含一个记录
                     * 指针，用以标识当前读写处的位置，当程序创建一个新的RandomAccessFile对象时，该对象
                     * 的文件记录
                     * 指针对于文件头（也就是0处），当读写n个字节后，文件记录指针将会向后移动n个字节。
                     */
                    var randomAccessFile: RandomAccessFile? = null
                    var inputStream: InputStream? = null
                    var total = range
                    var responseLength: Long = 0
                    try {
                        responseLength = responseBody.contentLength()  //获取服务器文件大小
                        inputStream = responseBody.byteStream()        //输入流
                        //创建目录
                        val dir = File(DownloadIntentService.DOWNLOAD_DIR)
                        if (!dir.exists()) {
                            dir.mkdirs()
                        }
                        //创建文件
                        val file = File(DownloadIntentService.DOWNLOAD_DIR, fileName)
                        //自由读写文件
                        randomAccessFile = RandomAccessFile(file, "rwd")
                        //预分配多大的文件空间
                        if (range == 0L) {
                            randomAccessFile.setLength(responseLength)
                        }
                        //设置指针位置，实现从断点处继续写入文件。
                        randomAccessFile.seek(range)
                        //byte读写
                        val buf = ByteArray(2048)
                        var len = inputStream!!.read(buf)
                        var progress = 0
                        var lastProgress = 0
                        KLog.e("DownloadIntentService", "开始写入文件")
                        while (len!= -1) {
                            randomAccessFile.write(buf, 0, len)
                            total += len.toLong()
                            lastProgress = progress
                            progress = (total * 100 / randomAccessFile.length()).toInt()
                            if (progress > 0 && progress != lastProgress) {
                                downloadCallback.onProgress(progress)
                            }
                            len = inputStream!!.read(buf)
                        }
                        downloadCallback.onCompleted()
                    } catch (e: Exception) {
                        KLog.d("DownloadIntentService", e.message)
                        downloadCallback.onError(e.message!!)
                        e.printStackTrace()
                    } finally {
                        try {
                            //保存当前下载进度
                            SPDownloadUtil.get().save(fileName, total)
                            randomAccessFile?.close()
                            inputStream?.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }

                override fun onError(e: Throwable) {
                    downloadCallback.onError(e.toString())
                }

                override fun onComplete() {}
            })
    }


}
