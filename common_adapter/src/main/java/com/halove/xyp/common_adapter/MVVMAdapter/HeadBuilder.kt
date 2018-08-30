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
     * 绑定数据
     */
     open fun bindData(view: View, data: Any){}
}