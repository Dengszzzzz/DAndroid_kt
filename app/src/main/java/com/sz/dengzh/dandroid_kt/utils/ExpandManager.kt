package com.sz.dengzh.dandroid_kt.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.text.ParcelableSpan
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.sz.dengzh.dandroid_kt.App
import java.util.regex.Pattern


/**
 * Created by dengzh on 2019/9/4
 * 拓展函数管理类
 */
val Float.dp2px: Float                 // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

val Int.dp2px: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()

val Float.sp2px: Float                 // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )


val Int.sp2px: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()

//----------设置TextView的文字颜色---------
fun TextView.setTvColor(resId:Int){
    this.setTextColor(ContextCompat.getColor(context,resId))
}

//----------拼接不同颜色的字符串---------
fun CharSequence.formatStringColor(colorId: Int, start: Int, end: Int): SpannableString {
    return this.setSpan(ForegroundColorSpan(ContextCompat.getColor(App.ctx, colorId)), start, end)
}

private fun CharSequence.setSpan(span: ParcelableSpan, start: Int, end: Int): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    return spannableString
}

//------------获取版本名---------------
val Context.versionName:String get(){
    val manager = this.packageManager
    val info = manager.getPackageInfo(this.packageName,0)
    return info.versionName
}

//------------获取版本号---------------
val Context.versionCode:Int get(){
    return this.packageManager.getPackageInfo(this.packageName,0).versionCode
}

//-----------判断email格式是否正确----------
fun String.isEmail():Boolean{
    val str =
        "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
    val p = Pattern.compile(str)
    val m = p.matcher(this)
    return m.matches()
}

//-----------判断是否是手机号---------
fun String.isPhone():Boolean{
    val str = "^1[3|4|5|6|7|8][0-9]\\d{8}$"
    val p = Pattern.compile(str)
    val m = p.matcher(this)
    return m.matches()
}

//-----------把文本复制到粘贴板---------
fun String.clipContent(){
    val cm = App.ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val mClipData = ClipData.newPlainText("Label",this)
    cm.primaryClip = mClipData
}
