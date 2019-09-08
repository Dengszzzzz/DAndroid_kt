package com.sz.dengzh.dandroid_kt.moduel.specialFunc.download

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.FileProvider
import com.sz.dengzh.commonlib.base.BaseListShowActivity
import com.sz.dengzh.commonlib.utils.ToastUtils
import com.sz.dengzh.dandroid_kt.bean.EventBean
import com.sz.dengzh.dandroid_kt.utils.Constant
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import java.io.File

/**
 * Created by dengzh on 2018/4/18.
 * 文件下载
 * 涉及存储权限，注意7.0和8.0的坑
 * 7.0的
 * 8.0之前，未知应用安装权限默认开启；8.0之后，未知应用安装权限默认关闭，且权限入口隐藏。
 */

class DownloadListActivity : BaseListShowActivity() {

    private val downloadUrl = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk"

    private val REQUEST_CODE_APP_INSTALL = 312

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun initUI() {
        initTitle("文件下载")
    }

    override fun initData() {
        addClazzBean("Retrofit2 + RxJava2 断点续传下载", null)
        mAdapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> onRxDownloadApk()
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun onRxDownloadApk() {
        val intent = Intent(this, DownloadIntentService::class.java)
        val bundle = Bundle().apply {
            putString("download_url", downloadUrl)
            putString("download_file", "DAS+_v1.0.1.apk")
        }
        intent.putExtras(bundle)
        startService(intent)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDownloadSuccess(eventBean: EventBean) {
        if (eventBean.code === Constant.EventCode.DOWNLOAD_APK_SUCCESS) {
            //8.0以上，判断未知应用安装权限是否开启，没开启引导用户去设置
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val hasInstallPermission = packageManager.canRequestPackageInstalls()
                if (!hasInstallPermission) {
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                    startActivityForResult(intent, REQUEST_CODE_APP_INSTALL)
                    return
                }
            }
            installApp()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_APP_INSTALL) {
            //回调再查一次是否开启权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val hasInstallPermission = packageManager.canRequestPackageInstalls()
                if (hasInstallPermission) {
                    installApp()
                } else {
                    ToastUtils.showToast("若要安装，请允许未知应用安装权限")
                }
            }
        }
    }

    /**
     * 判断是否在7.0以上，7.0以上要用FileProvider
     */
    private fun installApp() {
        val file = File(DownloadIntentService.downLoadPath)
        val intent = Intent(Intent.ACTION_VIEW)
        val apkUri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //参数2就是AndroidManifest.xml中provider的authorities
            apkUri = FileProvider.getUriForFile(this, "$packageName.fileProvider", file)
            //临时授权读该Uri代表的文件的权限，不然安装的时候会出现“解析软件包出现问题”。
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            apkUri = Uri.fromFile(file)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  //注意别设置成setFlags(...)了,不然前面的addFlags就清掉了。
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        startActivity(intent)
    }
}
