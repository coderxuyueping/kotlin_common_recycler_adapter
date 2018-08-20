package com.halove.xyp.kotlin_common_recycler_adapter

/**
 * Created by xyp on 2018/8/20.
 * 适配器的数据源继承自该类
 */
open abstract class CommonData {
    //使用布局id当作布局类型
    abstract fun getType(): Int
}