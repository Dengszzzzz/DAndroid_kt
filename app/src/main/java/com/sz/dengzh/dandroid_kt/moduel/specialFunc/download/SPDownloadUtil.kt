package com.sz.dengzh.dandroid_kt.moduel.specialFunc.download

import android.content.Context
import android.content.SharedPreferences
import com.sz.dengzh.commonlib.CommonConfig

/**
 * 记录下载位置的sp工具类
 */
class SPDownloadUtil private constructor() {

    companion object {
        private var mSharedPreferences: SharedPreferences? = null
        private var instance: SPDownloadUtil? = null
            get() {
            if(field == null){
                field = SPDownloadUtil()
                mSharedPreferences = CommonConfig.ctx.getSharedPreferences(
                    CommonConfig.ctx.packageName + ".downloadSp", Context.MODE_PRIVATE
                )
            }
            return field
        }

        fun get():SPDownloadUtil{
            return instance!!
        }
    }


    /**
     * 清空数据
     *
     * @return true 成功
     */
    fun clear(): Boolean {
        return mSharedPreferences!!.edit().clear().commit()
    }

    /**
     * 保存数据
     *
     * @param key   键
     * @param value 保存的value
     */
    fun save(key: String, value: Long): Boolean {
        return mSharedPreferences!!.edit().putLong(key, value).commit()
    }

    /**
     * 获取保存的数据
     *
     * @param key      键
     * @param defValue 默认返回的value
     * @return value
     */
    operator fun get(key: String, defValue: Long): Long {
        return mSharedPreferences!!.getLong(key, defValue)
    }


}
