package com.halove.xyp.common_adapter.commonAdapter

/**
 * Created by xyp on 2018/8/20.
 * 适配器的数据源继承自该类,这种会导致viewmodel依赖view层，所以有了下面的MVVMAdapter分层使用
 */
interface CommonData {
    //使用布局id当作布局类型
    fun getType(): Int

    //dataBinding中使用的
    fun getBR(): Int{
        return -1
    }
}