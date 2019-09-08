package com.sz.dengzh.dandroid_kt.moduel

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.widget.RadioGroup
import com.sz.dengzh.commonlib.base.BaseActivity
import com.sz.dengzh.commonlib.base.TempContract
import com.sz.dengzh.commonlib.base.TempPresenter
import com.sz.dengzh.commonlib.utils.AppManager
import com.sz.dengzh.commonlib.utils.ToastUtils
import com.sz.dengzh.dandroid_kt.R
import com.sz.dengzh.dandroid_kt.fragment.ProblemsFragment
import com.sz.dengzh.dandroid_kt.fragment.SpecialFuncFragment
import com.sz.dengzh.dandroid_kt.fragment.SummaryFragment
import com.sz.dengzh.dandroid_kt.fragment.ViewDetailFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: BaseActivity<TempContract.View,TempPresenter>(),RadioGroup.OnCheckedChangeListener {

    var mViewFragment:Fragment? = null
    var mSpecialFragment:Fragment? = null
    var mProblemFragment:Fragment? = null
    var mSummaryFragment:Fragment? = null
    var firstTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    fun initView(){
        tabs_rg.setOnCheckedChangeListener(this)
        rg_view_details.isChecked = true
        iv_center.setOnClickListener { ToastUtils.showToast("O(∩_∩)O暂未开放~") }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideAllFragment(transaction)
        when(checkedId){
            R.id.rg_view_details ->{
                if(mViewFragment == null){
                    mViewFragment = ViewDetailFragment.newInstance()
                    transaction.add(R.id.fr_container,mViewFragment!!)
                }else{
                    transaction.show(mViewFragment!!)
                }
            }
            R.id.rg_special_functions ->{
                if (mSpecialFragment == null) {
                    mSpecialFragment = SpecialFuncFragment.newInstance()
                    transaction.add(R.id.fr_container, mSpecialFragment!!)
                } else {
                    transaction.show(mSpecialFragment!!)
                }
            }
            R.id.rg_problems ->{
                if (mProblemFragment == null) {
                    mProblemFragment = ProblemsFragment.newInstance()
                    transaction.add(R.id.fr_container, mProblemFragment!!)
                } else {
                    transaction.show(mProblemFragment!!)
                }
            }
            R.id.rg_summary ->{
                if (mSummaryFragment == null) {
                    mSummaryFragment = SummaryFragment.newInstance()
                    transaction.add(R.id.fr_container, mSummaryFragment!!)
                } else {
                    transaction.show(mSummaryFragment!!)
                }
            }
        }
        transaction.commit()
    }

    fun hideAllFragment(transaction:FragmentTransaction){
        mViewFragment?.let { transaction.hide(it)}
        mSpecialFragment?.let { transaction.hide(it)}
        mProblemFragment?.let { transaction.hide(it)}
        mSummaryFragment?.let { transaction.hide(it)}
    }

    override fun onBackPressed() {
        val secondTime = System.currentTimeMillis()
        if(secondTime - firstTime > 2000){
            ToastUtils.showToast("再点一次退出程序")
            firstTime = secondTime
        }else{
            AppManager.getAppManager().AppExit(this)
        }
    }

}
