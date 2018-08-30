package com.halove.xyp.common_adapter.MVVMAdapter

/**
 * Created by xyp on 2018/8/30.
 */
interface IAdapterHead {
    /**
     * 添加头布局
     */
    fun addHead(headBuilder: HeadBuilder)

    /**
     * 移除某一个位置的头布局
     */
    fun removeHeadAt(position: Int)

    /**
     * 移除全部头布局
     */
    fun removeAllHead()
}