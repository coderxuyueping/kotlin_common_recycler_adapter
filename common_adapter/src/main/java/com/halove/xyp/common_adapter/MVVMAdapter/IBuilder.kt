package com.halove.xyp.common_adapter.MVVMAdapter

/**
 * Created by xyp on 2018/8/30.
 */
interface IBuilder {

    /**
     * 根据数据源传入对应的type,type对应布局id
     */
    fun getType(data: Any): Int

    /**
     * 提供空视图布局id
     */
    fun getEmptyLayout(): Int = -1


    /**
     *  dataBinding中使用的,根据不同数据源获取到对应布局，传入相应要绑定的BR
     */
    fun getBR(data: Any): Int = -1

}