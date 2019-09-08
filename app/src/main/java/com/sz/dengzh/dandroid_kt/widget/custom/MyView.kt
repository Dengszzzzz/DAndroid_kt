package com.sz.dengzh.dandroid_kt.widget.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.socks.library.KLog
import com.sz.dengzh.dandroid_kt.R

/**
 * Created by dengzh on 2019/5/31.
 * View总结：
 * 1)对于自定义view，前两个构造函数就足够了。一般要重写onMeasure(),onDraw()。
 * 2)onMeasure()：
 *      MeasureSpec.size()获取Size，MeasureSpec.mode()获取模式，
 *      模式包括unSpecified,AT_MOST,EXACTLY,
 *      其中UNSPECIFIED一般不处理；AT_MOST就是Wrap_content；EXACTLY就是确切的值或者MATCH_PARENT；
 *      最后记得调用setMeasuredDimension(width,size);设置宽高
 * 3)onSizeChanged()：会得到最终的宽高，当view的size有变化时会调用。
 * 4)onDraw():注意不要在此方法创建新对象，例如Paint不要放在里面new出来，onDraw()需要知道Paint,Canvas。
 *            Invalidate()调用onDraw()不清空画布，上一次的path还会保留，可以用Path.reset()重置。
 * 5)TypeArray获取attrs.xml定义的属性。
 */
class MyView @JvmOverloads constructor(context:Context,attrs: AttributeSet? = null): View(context,attrs){

    private val TAG = javaClass.simpleName
    private var defaultSize = 0
    private lateinit var paint:Paint

    /**
     *  1.一般在代码中创建View的时候用View(Context)。
     *  2.一般在layout文件中使用的时候会调用，关于它的所有属性(包括自定义属性)都会包含在attrs中传递进来。
     * */
    init {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView)
        defaultSize = typedArray.getDimensionPixelSize(R.styleable.MyView_defaultSize, 100)
        typedArray.recycle()

        //初始化画笔
        paint = Paint().apply {
            this.strokeWidth = 5f
            this.style = Paint.Style.STROKE
            color = Color.GREEN
        }
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var width = getSize(widthMeasureSpec)
        var height = getSize(heightMeasureSpec)
        //宽高保持一致
        if (width>height){
            width = height
        }else{
            height = width
        }
        //重新测量
        setMeasuredDimension(width,height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     * 绘画
     * 举例：中心点画圆
     *
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //坐标是相对View边界的，不是相对父容器的
        val r = measuredWidth/2.toFloat()
        canvas.drawCircle(r, r, r, paint)
    }

    private fun getSize(measureSpec:Int):Int{
        val size = MeasureSpec.getSize(measureSpec)
        return when (MeasureSpec.getMode(measureSpec)) {
            MeasureSpec.UNSPECIFIED -> //如果没有指定大小，就设置为默认大小
                defaultSize
            MeasureSpec.AT_MOST -> //如果测量模式是wrap_content,取默认值
                defaultSize
            else -> //如果是固定的大小（match_parent/确切值），那就不要去改变它
                size
        }
    }

    /*********************************** 事件分发机制 ****************************************/
    /**
     * 一般没必要重写 view的dispatchTouchEvent()
     * 此方法伪代码如下，可以看出onTouch() 和 onTouchEvent()的优先级和区别
     * if(onFilterTouchEventForSecurity(event)){
    ListenerInfo li = mListenerInfo;
    if(li != null && li.mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED
    && li.mOnTouchListener.onTouch(this, event)) {
    return true;
    }
    if(onTouchEvent(event)){
    return true;
    }
    }
    三个条件说明：
     * 1.mOnTouchListener不能为空
     * 2.当前View必须是enable状态
     * 3.onTouch()返回true
     * @param event
     * @return
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        KLog.d(TAG, "MyView 的dispatchTouchEvent-----$event.action")
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        KLog.d(TAG, "MyView 的onTouchEvent")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> KLog.d(TAG, "MyView 的 ACTION_DOWN")
            MotionEvent.ACTION_MOVE -> KLog.d(TAG, "MyView 的 ACTION_MOVE  x:${event.x}  y:${event.y}")
            MotionEvent.ACTION_UP -> KLog.d(TAG, "MyView 的 ACTION_UP")
        }
        return true  //如果不设置为true，则整个分发流程没有消费触摸事件，则ACTION_MOVE,ACTION_UP不会执行
        //return super.onTouchEvent(event);
    }


}