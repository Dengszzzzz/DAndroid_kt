package com.sz.dengzh.commonlib.base

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sz.dengzh.commonlib.R
import com.sz.dengzh.commonlib.bean.ClazzBean

/**
 * Created by dengzh on 2019/9/3
 */
class BaseListAdapter: BaseQuickAdapter<ClazzBean, BaseViewHolder> {

    constructor(data:List<ClazzBean>?) : super(R.layout.base_item_base_list, data)

    override fun convert(helper: BaseViewHolder?, item: ClazzBean?) {
        helper!!.setText(R.id.tvName, item!!.clazzName)
    }

}