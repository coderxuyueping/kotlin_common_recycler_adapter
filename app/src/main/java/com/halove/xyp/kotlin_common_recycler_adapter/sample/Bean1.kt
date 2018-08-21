package com.halove.xyp.kotlin_common_recycler_adapter.sample

import com.halove.xyp.kotlin_common_recycler_adapter.BR
import com.halove.xyp.kotlin_common_recycler_adapter.commonAdapter.CommonData
import com.halove.xyp.kotlin_common_recycler_adapter.R

/**
 * Created by xyp on 2018/8/20.
 */
data class Bean1(var msg: String) : CommonData {
    override fun getType() = R.layout.drv_layout1

    override fun getBR(): Int {
        return BR.bean1
    }
}