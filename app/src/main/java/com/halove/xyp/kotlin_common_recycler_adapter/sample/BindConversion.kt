package com.halove.xyp.kotlin_common_recycler_adapter.sample

import androidx.databinding.BindingConversion

/**
 * Created by xyp on 2018/8/30.
 */
object BindConversion {
    @BindingConversion
    @JvmStatic fun int2String(i: Int): String = i.toString()
}