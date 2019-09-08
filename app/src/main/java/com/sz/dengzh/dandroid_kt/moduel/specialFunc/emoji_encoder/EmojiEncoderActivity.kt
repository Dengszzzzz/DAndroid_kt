package com.sz.dengzh.dandroid_kt.moduel.specialFunc.emoji_encoder

import android.os.Bundle
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.commonlib.utils.ToastUtils
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_emoji_encoder.*

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by Dengzh
 * on 2019/6/29
 */
class EmojiEncoderActivity : BaseActivity<TempContract.View, TempPresenter>(), TempContract.View {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_emoji_encoder)
        initTitle("Emoji表情编解码")
        btn_code.setOnClickListener {
            handle()
        }
    }

    private fun handle(){
        val src = et_content.text.toString()
        if(src.isEmpty()){
            ToastUtils.showToast("请输入包含表情的内容")
            return
        }

        //1.codePointCount 和 length 对比
        tv_length.text = "String.length()：${src.length}"
        tv_code_point_count.text = "String.codePointCount：${src.codePointCount(0, src.length)}"

        //2.对整段编码和解码
        try {
            //编码
            val encodeStr = URLEncoder.encode(src, "UTF-8")
            //解码
            val decodeStr = URLDecoder.decode(encodeStr, "UTF-8")
            tv_encode.text = "编码：$encodeStr"
            tv_decode.text = "解码：$decodeStr"
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        //3.对表情单独编码、对整段解码（因为对整段编码，也是能解码出正确的原文内容的）
        try {
            val encodeStr = EmojiUtils.escape(src)
            tv_encode2.text = "对表情单独编码：$encodeStr"
            tv_decode2.text = "对整段解码能还原内容：" + URLDecoder.decode(encodeStr, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        tv_filter.text = "过滤表情：" + EmojiUtils.filter(src)
    }

}
