package com.sz.dengzh.dandroid_kt.widget.stepView;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.socks.library.KLog;
import com.sz.dengzh.dandroid_kt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2018/8/6.
 * 步骤视图指示器
 * 类似于快递收件流程
 * 实现需求：
 * 1.状态分为：进行中，已完成，未完成
 * 2.间隔线分为：已完成实线，未完成虚线
 * 3.间隔线高度统一，并不会随内容增多而变化
 * 4.把指示圆的中心点存入列表，TextView的坐标由此决定
 *
 * 实现逻辑：
 * 1.定义各个不同状态下的Paint
 * 2.onMeasure()，总高度要根据stepNum来决定，这里的每个item的高度都是固定的，不太好。
 * 3.在改变stepNum后，需调用requestLayout()，发起重新布局请求
 * 4.onSizeChanged(), 最终测量结果，在控件大小发生改变时调用。
 * 5.onDraw()，画线分两种状态，画圆分三种状态。
 *  （注意，在画之前调用mPath.reset()，因为Invalidate()调用onDraw()不清空画布，即上一次的path还保留，所以用Path.reset()重置）
 * 6.在stepView画出后，得到stepView各个圆心点的位置，依此确定textView的坐标，画出内容。
 *
 */
public class VerticalStepViewIndicator extends View {

    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    //定义默认的长度，其他长度取它的百分比
    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
    private float mCompletedLineWidth;  //已完成线(矩形)的宽度
    private float mCompletedLineLeftX;   //已完成线(矩形)的左上角x坐标
    private float mCompletedLineRightX;  //已完成线(矩形)的右下角x坐标
    private float mCircleRadius;  //圆的半径
    private float mLinePadding;  //两个圆之间的距离
    private float mCenterX;          //该View的X轴的中间位置
    private int mHeight;  //控件高度

    private Drawable mCompleteIcon;  //完成的默认图片
    private Drawable mAttentionIcon; //正在进行的默认图片
    private Drawable mDefaultIcon;   //未完成的默认图片

    private int mStepNum = 0;    //当前有几部流程

    private List<Float> mCircleCenterPointPositionList;//定义所有圆的圆心点位置的集合
    private Paint mCirclePaint;      //进行中的Paint，用来画大一点的白圆
    private Paint mUnCompletedPaint; //未完成Paint，用来画粗大实线
    private Paint mCompletedPaint;   //已完成Paint，用来画细小虚线
    private int mUnCompletedLineColor = ContextCompat.getColor(getContext(), R.color.uncompleted_color);//定义默认未完成线的颜色
    private int mCompletedLineColor = Color.WHITE;//定义默认完成线的颜色
    private PathEffect mEffects;

    private int mCompletingPosition;  //正在进行position
    private Path mPath;  //未完成线的路径
    private Rect mRect;  //一块矩形区域，用来放圆
    private boolean mIsReverseDraw;//是否反转画图，已完成的放到下方

    private OnDrawIndicatorListener mOnDrawListener;


    public VerticalStepViewIndicator(Context context) {
        this(context, null);
    }

    public VerticalStepViewIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        //1.已完成线Paint
        mCompletedPaint = new Paint();
        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.FILL);  //填充
        mCompletedPaint.setStrokeWidth(2);
        //2.未完成线Paint
        mUnCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE); //描边
        mUnCompletedPaint.setStrokeWidth(2);
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 0);   //虚线，8px实线，8px透明，偏移量0
        mUnCompletedPaint.setPathEffect(mEffects);
        //3.图标数据
        mCompleteIcon = ContextCompat.getDrawable(mContext, R.drawable.step_view_complted_icon);  //已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(mContext, R.drawable.step_view_attention_icon); //进行中的icon
        mDefaultIcon = ContextCompat.getDrawable(mContext, R.drawable.step_view_default_icon); //默认的icon
        //4.其他数据
        mPath = new Path();
        mCircleCenterPointPositionList = new ArrayList<>();
        mCompletedLineWidth = 0.05f * defaultStepIndicatorNum;  //完成线宽度
        mCircleRadius = 0.28f * defaultStepIndicatorNum;        //圆半径
        mLinePadding = 0.85f * defaultStepIndicatorNum;         //两圆距离

        //5.进行中，稍大一点白圆
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(2);
    }

    /**
     * 测量
     * 定义宽高,宽度由widthMeasureSpec决定，高度需要把圆和线的高度相加
     * 确定：mHeight
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        KLog.d(TAG, "onMeasure");
        int width = defaultStepIndicatorNum;
        mHeight = 0;
        if (mStepNum > 0) {
            //总高度
            mHeight = (int) (getPaddingTop() + getPaddingBottom() + mCircleRadius * 2 * mStepNum + (mStepNum - 1) * mLinePadding);
        }
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            width = Math.min(width, MeasureSpec.getSize(widthMeasureSpec));
        }
        setMeasuredDimension(width, mHeight);
    }

    /**
     * 最终测量结果
     * 在控件大小发生改变时调用
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        KLog.d(TAG, "onSizeChanged");
        //确定圆中心点，完成线的左右x轴
        mCenterX = getWidth() / 2;
        mCompletedLineLeftX = mCenterX - mCompletedLineWidth / 2;
        mCompletedLineRightX = mCenterX + mCompletedLineWidth / 2;
        updateCircleCenterPointPositionList();
        //测量结束后，要通知TextView可以设置数据了
        if (mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
    }

    /**
     * 设置中心点列表 因为反转调用invalidate()，而不执行onSizeChanged()，所以独立写出一个方法
     */
    private void updateCircleCenterPointPositionList(){
        //圆中心点加入列表,只是放入Y轴坐标
        mCircleCenterPointPositionList.clear();
        for (int i = 0; i < mStepNum; i++) {
            if (mIsReverseDraw) { //反转画图，把步骤1放最下方
                mCircleCenterPointPositionList.add(mHeight - (mCircleRadius * 2 * i + mLinePadding * i + mCircleRadius));
            } else {
                mCircleCenterPointPositionList.add(mCircleRadius * 2 * i + mLinePadding * i + mCircleRadius);
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        KLog.d(TAG, "onDraw");
        if (mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
        //1.画线,区分两种状态
        //注意：Invalidate()调用onDraw()不清空画布，即上一次的path还保留，可以用Path.reset()重置
        mPath.reset();
        for (int i = 0; i < mStepNum - 1; i++) {
            //得到当前圆和下个圆的中心点Y坐标
            float prePositionCenter = mCircleCenterPointPositionList.get(i);
            float afterPositionCenter = mCircleCenterPointPositionList.get(i + 1);
            if (mIsReverseDraw) { //如果是反转，则前后中心点位置要交换一下
                prePositionCenter = mCircleCenterPointPositionList.get(i + 1);
                afterPositionCenter = mCircleCenterPointPositionList.get(i);
            }

            if (i < mCompletingPosition) {  //已完成，画粗大实线
                canvas.drawRect(mCompletedLineLeftX, prePositionCenter + mCircleRadius - 10, mCompletedLineRightX, afterPositionCenter - mCircleRadius + 10, mCompletedPaint);
            } else { //未完成，画细小虚线
                mPath.moveTo(mCenterX, prePositionCenter + mCircleRadius);
                mPath.lineTo(mCenterX, afterPositionCenter - mCircleRadius);
                canvas.drawPath(mPath, mUnCompletedPaint);
            }
        }
        //2.画圆,区分三种状态
        for (int i = 0; i < mStepNum; i++) {
            float curPositionCenter = mCircleCenterPointPositionList.get(i);
            mRect = new Rect((int) (mCenterX - mCircleRadius), (int) (curPositionCenter - mCircleRadius), (int) (mCenterX + mCircleRadius), (int) (curPositionCenter + mCircleRadius));
            if (i < mCompletingPosition) {  //已完成
                mCompleteIcon.setBounds(mRect);
                mCompleteIcon.draw(canvas);
            } else if (i == mCompletingPosition) {  //进行中
                canvas.drawCircle(mCenterX, curPositionCenter, mCircleRadius * 1.1f, mCirclePaint);
                mAttentionIcon.setBounds(mRect);  //给图标设置边界
                mAttentionIcon.draw(canvas);      //把设定边界的图标放在canvas
            } else { //未完成
                mDefaultIcon.setBounds(mRect);
                mDefaultIcon.draw(canvas);
            }
        }
    }

    /******************************** 供外使用的方法 *****************************************/

    /**
     * get圆的半径  get circle radius
     *
     * @return
     */
    public float getCircleRadius() {
        return mCircleRadius;
    }

    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    public List<Float> getCircleCenterPointPositionList() {
        return mCircleCenterPointPositionList;
    }

    /**
     * 设置流程步数
     *
     * @param stepNum 流程步数
     */
    public void setStepNum(int stepNum) {
        mStepNum = stepNum;
        requestLayout(); //改变后，发起重新布局请求
    }

    /**
     * 设置线高度的比例系数
     *
     * @param proportion
     */
    public void setIndicatorLinePaddingProportion(float proportion) {
        mLinePadding = proportion * defaultStepIndicatorNum;
    }

    /**
     * 设置选中的位置
     *
     * @param position
     */
    public void setCompletingPosition(int position) {
        mCompletingPosition = position;
        requestLayout();
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    public void setUnCompletedLineColor(int unCompletedLineColor) {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    public void setCompletedLineColor(int completedLineColor) {
        this.mCompletedLineColor = completedLineColor;
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    public void setDefaultIcon(Drawable defaultIcon) {
        this.mDefaultIcon = defaultIcon;
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    public void setCompleteIcon(Drawable completeIcon) {
        this.mCompleteIcon = completeIcon;
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    public void setAttentionIcon(Drawable attentionIcon) {
        this.mAttentionIcon = attentionIcon;
    }

    /**
     * is reverse draw 是否倒序画
     */
    public void reverseDraw(boolean isReverseDraw) {
        this.mIsReverseDraw = isReverseDraw;
        updateCircleCenterPointPositionList();
        invalidate();
    }

    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener) {
        mOnDrawListener = onDrawListener;
    }

    public interface OnDrawIndicatorListener {
        void ondrawIndicator();
    }
}
