package com.halove.xyp.common_adapter.MVVMAdapter

/**
 * Created by xyp on 2018/8/30.
 * 添加头布局的Builder
 */
abstract class HeadBuilder : IBuilder{
    /**
     * 根据数据源传入对应的type,type对应布局id
     */
    abstract override fun getType(data: Any): Int
}