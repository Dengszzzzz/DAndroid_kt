package com.sz.dengzh.dandroid_kt.moduel.specialFunc.download

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.RemoteViews
import com.socks.library.KLog
import com.sz.dengzh.commonlib.CommonConfig
import com.sz.dengzh.dandroid_kt.App
import com.sz.dengzh.dandroid_kt.R
import com.sz.dengzh.dandroid_kt.bean.EventBean
import com.sz.dengzh.dandroid_kt.utils.Constant
import org.greenrobot.eventbus.EventBus

import java.io.File

/**
 * 下载服务
 * 继承IntentService 是因为读写文件比较耗时，所以用这个开了子线程的Service，而且停止服务的后，也会继续执行子线程
 * 1.判断文件下载程度，是否已完成下载，下载完成直接安装apk
 * 2.开启通知栏
 * 3.下载apk，通知栏显示下载进度
 *
 * 当把targetSdk设为26，也就是Android 8.0后，记得填坑
 * 比如通知栏没显示问题，吊不起安装页面问题等
 */
class DownloadIntentService : IntentService("DownloadService") {

    private val NOTIFICATION_CHANNEL_ID = CommonConfig.ctx.packageName
    private val NOTIFICATION_CHANNEL_NAME = "apk_download_channel"
    private lateinit var mNotifyManager: NotificationManager  //通知管理类
    private lateinit var mNotification: Notification
    private val downloadId = 101
    private lateinit var mDownloadFileName: String

    companion object {
        private val TAG = "DownloadIntentService"
        lateinit var downLoadPath: String
        lateinit var DOWNLOAD_DIR: String  //下载目录
    }

    init {
        DOWNLOAD_DIR =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + CommonConfig.ctx.packageName + "/download/"
    }

    override fun onHandleIntent(intent: Intent?) {
        val bundle = intent?.extras ?: return
        val downloadUrl = bundle.getString("download_url")
        mDownloadFileName = bundle.getString("download_file")!!
        downLoadPath = DOWNLOAD_DIR + mDownloadFileName

        val file = File(downLoadPath)
        var range: Long = 0
        var progress = 0
        if (file.exists()) {
            range = SPDownloadUtil.get().get(mDownloadFileName, 0)
            progress = (range * 100 / file.length()).toInt()
            if (range == file.length()) {  //已下载完毕，跳去安装apk
                EventBus.getDefault().post(EventBean(Constant.EventCode.DOWNLOAD_APK_SUCCESS, null))
                return
            }
        }
        KLog.d(TAG, "range = $range")

        //2.设置通知栏显示下载进度
        val remoteViews = RemoteViews(packageName, R.layout.notify_download)
        remoteViews.run {
            setProgressBar(R.id.pb_progress, 100, progress, false)
            setTextViewText(R.id.tv_progress, "已下载$progress%")
        }
        createNotification(remoteViews)


        //3.下载apk
        DownloadRetrofitUtils.get().downloadFile(range, downloadUrl, mDownloadFileName, object : DownloadCallBack {
                override fun onProgress(progress: Int) {
                    KLog.e("已下载$progress%")
                    remoteViews.setProgressBar(R.id.pb_progress, 100, progress, false)
                    remoteViews.setTextViewText(R.id.tv_progress, "已下载$progress%")
                    mNotifyManager!!.notify(downloadId, mNotification)
                }

                override fun onCompleted() {
                    KLog.d(TAG, "下载完成")
                    mNotifyManager!!.cancel(downloadId)
                    EventBus.getDefault().post(EventBean(Constant.EventCode.DOWNLOAD_APK_SUCCESS, null))
                }

                override fun onError(msg: String) {
                    KLog.d(TAG, "下载发生错误--$msg")
                    mNotifyManager!!.cancel(downloadId)
                }
            })
    }

    /**
     * 创建通知栏
     * targetSdk >= 26 时，系统不会默认添加Channel，反之低版本则会默认添加；
     * @param remoteViews
     */
    private fun createNotification(remoteViews: RemoteViews) {
        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //1.创建通知通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //参数：通道id、名字、优先级。
            val notifyChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notifyChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            mNotifyManager!!.createNotificationChannel(notifyChannel)
        }
        //2.创建Builder对象
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }.setContent(remoteViews)
            .setTicker("正在下载")
            .setSmallIcon(R.mipmap.ic_launcher)
        //3.将Builder对象转变成普通的notification
        mNotification = builder.build()
        mNotifyManager!!.notify(downloadId, mNotification)
    }


}