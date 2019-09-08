package com.sz.dengzh.dandroid_kt.moduel.viewdetails.customview

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import butterknife.ButterKnife
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.ac_vertical_step_view.*

import java.util.ArrayList

/**
 * Created by administrator on 2018/8/6.
 */
class VerticalStepViewActivity : BaseActivity<TempContract.View, TempPresenter>(), TempContract.View,View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_vertical_step_view)
        ButterKnife.bind(this)
        initTitle("步骤指示器")

        initData()
        positiveBt.setOnClickListener(this)
        reverseBt.setOnClickListener(this)
    }

    private fun initData(){
        val list0 = ArrayList<String>()
        list0.add("您已提交定单，等待系统确认")
        list0.add("您的商品需要从外地调拨，我们会尽快处理，请耐心等待")
        list0.add("您的订单已经进入亚洲第一仓储中心1号库准备出库")
        list0.add("您的订单预计6月23日送达您的手中，618期间促销火爆，可能影响送货时间，请您谅解，我们会第一时间送到您的手中")
        list0.add("您的订单已打印完毕")
        list0.add("您的订单已拣货完成")
        list0.add("扫描员已经扫描")
        list0.add("打包成功")
        list0.add("您的订单在京东【华东外单分拣中心】发货完成，准备送往京东【北京通州分拣中心】")
        list0.add("您的订单在京东【北京通州分拣中心】分拣完成")
        list0.add("您的订单在京东【北京通州分拣中心】发货完成，准备送往京东【北京中关村大厦站】")
        list0.add("您的订单在京东【北京中关村大厦站】验货完成，正在分配配送员")
        list0.add("配送员【包牙齿】已出发，联系电话【130-0000-0000】，感谢您的耐心等待，参加评价还能赢取好多礼物哦")
        list0.add("感谢你在京东购物，欢迎你下次光临！")
        verticalStepView.setStepsViewIndicatorComplectingPosition(list0.size - 5)//设置完成的步数
            .setTextSize(14)
            .setStepViewTexts(list0)//总步骤
            .setStepsViewIndicatorCompletedLineColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.white
                )
            )//设置StepsViewIndicator完成线的颜色
            .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
            .setStepViewComplectedTextColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsView text完成线的颜色
            .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.uncompleted_text_color)
            )//设置StepsView text未完成线的颜色
            .setStepsViewIndicatorCompleteIcon(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.step_view_complted_icon
                )
            )//设置StepsViewIndicator CompleteIcon
            .setStepsViewIndicatorDefaultIcon(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.step_view_default_icon
                )
            )//设置StepsViewIndicator DefaultIcon
            .setStepsViewIndicatorAttentionIcon(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.step_view_attention_icon
                )
            )//设置StepsViewIndicator AttentionIcon
            .reverseDraw(true)
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.positiveBt->{//正序
               verticalStepView.reverseDraw(false)
           }
           R.id.reverseBt->{ //反转
               verticalStepView.reverseDraw(true)
           }
       }
    }



}
