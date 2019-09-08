package com.sz.dengzh.dandroid_kt.moduel.specialFunc.emoji_encoder

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * UTF-8编码有可能是两个、三个、四个字节。Emoji表情或者某些特殊字符是4个字节，而Mysql的utf8编码最多3个字节，
 * 所以数据插不进去。遇到这种问题可以交给后台处理，如果后台不处理，则ios和Andriod要统一对emoji表情编码。
 * 在解决这个问题之前，需要了解以下知识
 * 1.位、字节、字符的概念和区别。
 * 位（bit）：数据存储的最小单位，每个二进制数字0或者1就是1个位，比如 11010101是一个八位二进制数。
 * 字节（byte）：8个位构成一个字节；即：1 byte (字节)= 8 bit(位)；1 KB = 1024 B(字节)；
 * 字符：是指计算机中使用的字母、数字、字和符号，给用户看的。
 * 字符集：各种各个字符的集合，也就是某些汉字、字母（A、b、c）和符号（空格、引号..）会被收入标准中；例如ASCii字符
 * 集，gb2312字符集，Unicode字符集等。
 * 编码：规定每个“字符”分别用一个字节还是多个字节存储，用哪些字节来存储，这个规定就叫做“编码”。通俗来讲，编码就是
 * 按照规则对字符进行翻译成对应的二进制数，在计算器中运行存储，用户看的时候，再解码显示成用户能看得懂的。
 *
 * 2.UTF-8编码中，字节和字符的对应关系。
 * 1个英文字符 和 英文标点 = 1个字节
 * 1个中文（含繁体） 和 中文标点 = 3个字节
 * Emoji表情 和 某些特殊字符 = 4个字节
 *
 * 3.String类的length 和 codePointCount 的区别
 * length():返回的是使用的UTF-16编码的字符代码单元数量，不一定是我们认为的字符个数。（比如1个Emoji表情，我们当
 * 做是一个字符，但是length是2）
 * codePointCount()：返回的是代码点数量，是实际的字符个数。
 * 原因：
 * 常用的UniCode字符使用一个代码单元就可以表示（如英文、数字、中文），但有些辅助字符需要一对代码单元表示
 * （如Emoji表情）。length()计算的是代码单元的数量，codePointCount()计算的是代码点数。
 *
 * 参考
 * https://blog.csdn.net/u014166319/article/details/71308112
 * https://www.cnblogs.com/mojxtang/p/10154907.html
 */
object EmojiUtils {

    /**
     * 对emoji表情单独编码
     * @param src
     * @return
     */
    fun escape(src: String): String {
        //1.得到代码点数量，也即是实际字符数，注意和length()的区别
        //举例：
        //一个emoji表情是一个字符，codePointCount()是1，length()是2。
        //但是遇到过一个emoji表情居然带着空格符号，结果它的codePointCount()是2，length()也是2，也就是说单独拎emoji来说，它的length()就是1了。
        //推测这就是它们是否属于增补字符范围内，length=2的是增补字符，length=1的不是。
        val cpCount = src.codePointCount(0, src.length)

        //2.得到字符串的第一个代码点index，和最后一个代码点index
        //举例：比如3个emoji表情，那么它的cpCount=3；firCodeIndex=0；lstCodeIndex=4
        //因为每个emoji表情length()是2，所以第一个是0-1，第二个是2-3，第三个是4-5
        val firCodeIndex = src.offsetByCodePoints(0, 0)
        val lstCodeIndex = src.offsetByCodePoints(0, cpCount - 1)

        val sb = StringBuilder()
        var index = firCodeIndex
        while (index <= lstCodeIndex) {
            //3.获得代码点，判断是否是emoji表情
            //注意，codePointAt(int) 这个int对应的是codeIndex
            //举例:3个emoji表情，取第3个emoji表情，index应该是4
            val codepoint = src.codePointAt(index)
            if (!isEmojiCharacter(codepoint)) {
                sb.append(codepoint.toChar())
            } else {
                try {
                    //4.对emoji表情UTF-8编码，判断是否是增补字符范围内
                    val length = if (Character.isSupplementaryCodePoint(codepoint)) 2 else 1
                    val encoderStr = URLEncoder.encode(src.substring(index, index + length), "UTF-8")
                    sb.append(encoderStr)
                } catch (e: UnsupportedEncodingException) {

                }
            }
            //4.确定指定字符（Unicode代码点）是否在增补字符范围内。
            //因为除了表情，还有些特殊字符也是在增补字符方位内的。
            index += if (Character.isSupplementaryCodePoint(codepoint)) 2 else 1
        }
        return sb.toString()
    }


    /**
     * 过滤掉emoji表情
     * @param src
     * @return
     */
    fun filter(src: String): String {
        //得到codePointCount
        val cpCount = src.codePointCount(0, src.length)
        val firCodeIndex = src.offsetByCodePoints(0, 0)
        val lstCodeIndex = src.offsetByCodePoints(0, cpCount - 1)
        val sb = StringBuilder(src.length)
        var index = firCodeIndex
        while (index <= lstCodeIndex) {
            //遍历每个codePoint
            val codepoint = src.codePointAt(index)
            if (!isEmojiCharacter(codepoint)) {
                System.err.println("codepoint:" + Integer.toHexString(codepoint))
                sb.append(codepoint.toChar())
            }
            index += if (Character.isSupplementaryCodePoint(codepoint)) 2 else 1

        }
        return sb.toString()
    }

    /**
     * 是否是Emoji表情
     * @param codePoint
     * @return
     */
    private fun isEmojiCharacter(codePoint: Int): Boolean {
        return (codePoint in 0x2600..0x27BF // 杂项符号与符号字体
                || codePoint == 0x303D
                || codePoint == 0x2049
                || codePoint == 0x203C
                || codePoint in 0x2000..0x200F
                || codePoint in 0x2028..0x202F
                || codePoint == 0x205F
                || codePoint in 0x2065..0x206F
                /* 标点符号占用区域 */
                || codePoint in 0x2100..0x214F// 字母符号
                || codePoint in 0x2300..0x23FF// 各种技术符号
                || codePoint in 0x2B00..0x2BFF// 箭头A
                || codePoint in 0x2900..0x297F// 箭头B
                || codePoint in 0x3200..0x32FF// 中文符号
                || codePoint in 0xD800..0xDFFF// 高低位替代符保留区域
                || codePoint in 0xE000..0xF8FF// 私有保留区域
                || codePoint in 0xFE00..0xFE0F// 变异选择器
                || codePoint >= 0x10000) // Plane在第二平面以上的，char都不可以存，全部都转
    }

}