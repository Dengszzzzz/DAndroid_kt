package com.sz.dengzh.dandroid_kt.widget.anim

import android.animation.*
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Build
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import android.view.animation.*
import com.socks.library.KLog

/**
 * Created by dengzh on 2019/9/5
 */
class PointAnimView @JvmOverloads constructor(context:Context,attrs: AttributeSet?=null,defStyleAttr:Int=0):
    View(context,attrs,defStyleAttr){

    private val TAG = javaClass.simpleName
    private val RADIUS = 20f

    //画笔
    private var mPaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var linePaint: Paint
    //当前点
    private var currentPoint: Point
    //动画集
    private var animSet: AnimatorSet? = null
    //插值器
    private var interpolatorType: TimeInterpolator = LinearInterpolator()

    //圆初始颜色
    var color:Int = 0
    set(value) {
        field = value
        mPaint.color = value
    }
    //圆初始半径
    var radius = RADIUS

    //在构造函数里面调用这个方法，如果有属性在这里面被赋值，那么那个属性是不需要写lateinit的
    init {
        mPaint.color = Color.TRANSPARENT

        linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE  //没有设置这个，canvas.drawPath()失效
            strokeWidth = 5f
        }

        currentPoint = Point(RADIUS.toInt(), RADIUS.toInt())
    }

    /**
     * onDraw()的canvas，每次都是干净的
     * 所以此处X,Y轴每次都要画
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        KLog.e(TAG, "onDraw()")
        canvas.apply {
            //画x,y轴
            drawLine(10f,height/2.toFloat(),width.toFloat(),height/2.toFloat(),linePaint)
            drawLine(10f,height/2.toFloat()-150,10f,height/2.toFloat()+150,linePaint)
            //画点
            drawPoint(currentPoint.x.toFloat(),currentPoint.y.toFloat(),linePaint)
            //画圆
            drawCircle(currentPoint.x.toFloat(),currentPoint.y.toFloat(),radius,mPaint)
        }
    }

    /**
     * 插值器，在动画开启前设置
     * @param type
     */
    fun setInterpolatorType(type: Int) {
        interpolatorType = when (type) {
            1 -> BounceInterpolator()   //弹跳效果
            2 -> AccelerateDecelerateInterpolator()  //加减速插值器
            3 -> DecelerateInterpolator()  //减速插值器
            4 -> AccelerateInterpolator()  //加速插值器
            5 -> AnticipateInterpolator()  //开始的时候向后然后向前甩
            6 -> LinearOutSlowInInterpolator()
            7 -> OvershootInterpolator()  // 向前甩一定值后再回到原来位置
            8 -> MyInterpolator()
            else -> LinearInterpolator()  //线性插值器
        }
    }

    /**
     *  接收者：with、run、apply接收者是this，而let和also接受者是it；
     *  返回值：with、run、let返回作用域的最后一个对象，而apply和also返回调用者本身
     *
     *  结论：
     *  1）apply和also返回调用者本身，apply的坏处是里面的this会覆盖this，好处是可以完全省略。
     *  2) 由此看来，let、also、apply应该是用的比较多的。
     * */
    fun startAnimation(){
        //球位置
        val startP = Point(radius.toInt(),radius.toInt())
        val endP = Point(width - radius.toInt(), height - radius.toInt())
        //1.T.let，此时我不关心返回值
        val valueAnimator = ValueAnimator.ofObject(PointSinEvaluator(), startP, endP)
        valueAnimator.let {
            it.repeatCount = -1
            it.repeatMode = ValueAnimator.REVERSE  //设置重复模式：逆向重复
            it.addUpdateListener {
                //当前点位置，自定义估值器返回值
                currentPoint = it.animatedValue as Point
                postInvalidate()
            }
        }


        //2. T.also,此时返回值是对象本身
        val animColor = ObjectAnimator.ofObject(
            this, "color", ArgbEvaluator(), Color.GREEN,
            Color.YELLOW, Color.BLUE, Color.WHITE, Color.RED
        ).also {
            it.repeatCount = -1
            it.repeatMode = ValueAnimator.REVERSE
        }


        //3. T.apply,此时返回值是对象本身,里面用this
        val animScale = ValueAnimator.ofFloat(radius, radius * 4, 60f, 10f, 35f, 55f, 10f)
            .apply {
                this.repeatCount = -1
                this.repeatMode = ValueAnimator.REVERSE
                this.duration = 5000
                this.addUpdateListener {
                    //半径，Float的估值器返回值
                    //从20->80->60->10->35->55->10,再逆序回来
                    radius = it.animatedValue as Float
                }
            }

        //动画集合
        animSet = AnimatorSet().apply {
            playTogether(valueAnimator,animColor,animScale);
            play(valueAnimator).with(animColor).with(animScale)  //和playTogether()是一样的。
            duration = 5000
            interpolator = interpolatorType
            start()
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun pauseAnimation() {
        animSet?.pause()
    }


    fun stopAnimation() {
        animSet?.let {
            animSet!!.cancel()
            clearAnimation()
        }
    }
}