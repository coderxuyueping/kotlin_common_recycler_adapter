package com.halove.xyp.common_adapter.MVVMAdapter

import android.view.View

/**
 * Created by xyp on 2018/8/30.
 * 添加头布局的Builder
 */
abstract class HeadBuilder(var headData: Any) : IBuilder{
    /**
     * 根据数据源传入对应的type,type对应布局id
     */
    abstract override fun getType(data: Any): Int

    /**
     * 绑定数据,如果是采用dataBinding的方式不需要实现，但是需要实现getBR
     */
     open fun bindData(view: View, data: Any){}
}