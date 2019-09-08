package com.sz.dengzh.dandroid_kt.moduel.viewdetails.viewpager.tabpager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sz.dengzh.commonlib.base.BaseFragment
import com.sz.dengzh.dandroid_kt.R
import kotlinx.android.synthetic.main.fr_simple.*

/**
 * Created by dengzh on 2019/9/4
 */
class SimpleFragment :BaseFragment(){

    lateinit var id:String

    companion object{

        /**
         * apply 函数返回调用它的对象本身。
         * 一般情况下，如果需要创建一个对象，并在相关初始化操作后赋值给一个变量可以考虑使用 apply 函数。
         * */
        fun newInstance(id:String) =
            SimpleFragment().apply {
                arguments = Bundle().apply {
                    putString("id",id)
                }
        }

      /*  fun newInstance(id:String):SimpleFragment{
            val fragment = SimpleFragment()
            val bundle = Bundle()
            bundle.putString("id",id)
            fragment.arguments = bundle
            return fragment
        }*/


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        id = arguments!!.getString("id")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fr_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        descTv.text = "页面$id"
    }
}