package com.sz.dengzh.dandroid_kt.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.HashMap
import kotlin.system.exitProcess

/**
 * Created by dengzh on 2019/9/3
 */
class CrashHandler:Thread.UncaughtExceptionHandler{

    private lateinit var mContext: Context
    // 系统默认的 UncaughtException 处理类
    private lateinit var mDefaultHandler: Thread.UncaughtExceptionHandler
    // 用来存储设备信息和异常信息
    private val infos = HashMap<String,String>()
    private var error = "程序错误导致奔溃，请查看日志"
    private val formatter = SimpleDateFormat(
        "yyyy-MM-dd-HH-mm-ss",
        Locale.CHINA
    )

    private val CRASH_PATH = "/DAndroid_kt/Crash"

    /**
     * 伴生对象,伴生对象在类中只能存在一个
     * 1)把Java代码的static修饰的，都移到里面去
     */
    companion object{
        val TAG = CrashHandler::class.java.simpleName
        private val regexMap = HashMap<String,String>()

        /**
         * 单例模式
         */
        private var instance: CrashHandler? = null
            get() {
            if(field == null){
                initMap()
                field = CrashHandler()
            }
            return field
        }
        //这里不用getInstance作为方法名，是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
        fun get():CrashHandler{
            return instance!!
        }

        /**
         * 初始化错误的提示语
         */
        private fun initMap(){
            regexMap[".*NullPointerException.*"] = "NullPointerException"
            regexMap[".*ClassNotFoundException.*"] = "ClassNotFoundException"
            regexMap[".*ArithmeticException.*"] = "ArithmeticException"
            regexMap[".*ArrayIndexOutOfBoundsException.*"] = "ArrayIndexOutOfBoundsException"
            regexMap[".*IllegalArgumentException.*"] = "IllegalArgumentException"
            regexMap[".*IllegalAccessException.*"] = "IllegalAccessException"
            regexMap[".*SecturityException.*"] = "SecturityException"
            regexMap[".*NumberFormatException.*"] = "NumberFormatException"
            regexMap[".*OutOfMemoryError.*"] = "OutOfMemoryError"
            regexMap[".*StackOverflowError.*"] = "StackOverflowError"
            regexMap[".*RuntimeException.*"] = "RuntimeException"
        }
    }


    /**
     * 初始化
     *
     * @param context
     */
    fun init(context: Context){
        mContext = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if(e!=null){
            handleException(e)
            mDefaultHandler.uncaughtException(t,e)
        }else{
            try {
                Thread.sleep(3000)
            }catch(e: InterruptedException) {
                Log.e(TAG, "error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(0)
        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     */
    private fun handleException(ex:Throwable){
        // 收集设备参数信息
        // collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfoFile(ex)
        //线程创建有
        object :Thread(){
            override fun run() {
                Looper.prepare()
                Toast.makeText(mContext, error, Toast.LENGTH_LONG).show()
                Looper.loop()
            }
        }.start()
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    private fun collectDeviceInfo(ctx:Context){
        try {
            val pm = ctx.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = pi.versionName
                val versionCode = pi.versionCode.toString()
                infos["versionName"] = versionName
                infos["versionCode"] = versionCode
            }
        }catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        var fields = Build::class.java.getDeclaredFields()
        fields.forEach {
            try {
                it.isAccessible = true
                infos[it.name] = it.get(null).toString()
                Log.d(TAG, it.name + " : " + it.get(null))
            }catch (e: Exception) {
                    Log.e(TAG, "an error occured when collect crash info", e)
                }
        }
    }


    /**
     * 保存错误信息到文件中 *
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfoFile(ex:Throwable){
        val sb = getTraceInfo(ex)
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause !=null){
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        sb.append(writer.toString())
        try {
            val timestamp = System.currentTimeMillis()
            val time = formatter.format(Date())
            val fileName = "crash-$time-$timestamp.log"
            if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
                val path = Environment.getExternalStorageDirectory().toString() + CRASH_PATH
                var dir = File(path)
                if(!dir.exists()){
                    dir.mkdirs()
                }
                if(dir.exists()){
                    val fos = FileOutputStream("$path/$fileName")
                    fos.write(sb.toString().toByteArray())
                    fos.close()
                }
            }
        }catch (e:Exception){
            Log.e(TAG, "an error occured while writing file...", e)
        }
    }

    /**
     * 整理异常信息
     *
     * @param e
     * @return
     * */
    private fun getTraceInfo(e:Throwable):StringBuffer{
        var sb = StringBuffer()
        //e.cause不为null，返回e.cause；为null，返回e
        val ex: Throwable? = e.cause?:e
        val stacks = ex!!.stackTrace
        for ( i in stacks.indices){
            if (i == 0){
                setError(e.toString())
            }
            sb.append("class: ").append(stacks[i].className)
                .append("; method: ").append(stacks[i].methodName)
                .append("; line: ").append(stacks[i].lineNumber)
                .append("; Exception: ").append(ex.toString() + "\n")
        }
        Log.d(TAG, sb.toString())
        return sb
    }

    /**
     * 设置错误的提示语
     *
     * @param e
     */
    private fun setError(e:String){
        var pattern:Pattern
        var matcher:Matcher

        //forEach 跳出循环不能用break。可以用 return标签
        regexMap.forEach outside@{
            Log.d(TAG,"$e key: ${it.key}; value: ${it.value}")
            pattern = Pattern.compile(it.key)
            matcher = pattern.matcher(e)
            if (matcher.matches()){
                error = it.value
                return@outside
            }
        }

    }

}