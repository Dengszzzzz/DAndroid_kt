package com.sz.dengzh.commonlib.base

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import com.kaopiz.kprogresshud.KProgressHUD
import com.socks.library.KLog
import com.sz.dengzh.commonlib.R
import com.sz.dengzh.commonlib.bean.NetBean
import com.sz.dengzh.commonlib.utils.AppManager
import com.trello.rxlifecycle2.components.support.RxFragmentActivity
import kotlinx.android.synthetic.main.top_view.*
import java.lang.reflect.ParameterizedType

/**
 * Created by dengzh on 2019/9/3
 */
abstract class BaseActivity<V: IBaseView,P: BasePresenterImp<V>>:RxFragmentActivity(),IBaseView{

    protected val TAG = javaClass.simpleName

    var mPresenter:P? = null
    lateinit var kProgressHUD:KProgressHUD
    var tvTitle: TextView? = null
    var ivBack: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppManager.getAppManager().addActivity(this)
        printRunningActivity(this, true)

        kProgressHUD = KProgressHUD(this)
        mPresenter = getInstance(this,1)
        mPresenter?.attachView(this as V)
    }

    fun initTitle(title:String){
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        tvTitle?.text = title
        ivBack?.setOnClickListener { finish() }
    }


    override fun onDestroy() {
        super.onDestroy()
        printRunningActivity(this, false)
        AppManager.getAppManager().finishActivity(this)
        if(mPresenter!=null){
            mPresenter!!.detachView()
            mPresenter = null
        }
        kProgressHUD.dismiss()
    }

    //反射获取当前Presenter对象
    private fun <M> getInstance(o: Any,i:Int): M?{
        try {
            return ((o.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[i] as Class<M>).newInstance()
        }catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return null
    }

    override fun showLoading() {
        runOnUiThread {
            if (kProgressHUD.isShowing)
                return@runOnUiThread
            kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setCancellable(true)
                .show()
        }
    }

    override fun dismissLoading() {
        if (kProgressHUD.isShowing) {
            runOnUiThread { kProgressHUD.dismiss() }
        }
    }

    /**
     * 跳转 减少重复代码
     * @param tarActivity 目标activity
     */
    fun startActivity(tarActivity: Class<out Activity>) {
        val intent = Intent(this, tarActivity)
        startActivity(intent)
        // overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }


    /*override fun finish(){
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }*/

    private fun printRunningActivity(ac:Activity,isRunning:Boolean){
        val contextString = ac.toString()
        val s = contextString.substring(contextString.lastIndexOf(".")+1, contextString.indexOf("@"))
        if (isRunning){
            KLog.e("Activity", "app:当前正在加入的界面是:$s")
        }else{
            KLog.e("Activity", "app:当前销毁的界面是:$s")
        }
    }

    override fun handleFailResponse(netBean: NetBean) {

    }
}