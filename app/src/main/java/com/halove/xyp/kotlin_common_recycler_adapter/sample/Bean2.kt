package com.halove.xyp.kotlin_common_recycler_adapter.sample

import com.halove.xyp.kotlin_common_recycler_adapter.CommonData
import com.halove.xyp.kotlin_common_recycler_adapter.R

/**
 * Created by xyp on 2018/8/20.
 */
data class Bean2(var msg: String) : CommonData() {
    override fun getType() = R.layout.rv_layout2
}