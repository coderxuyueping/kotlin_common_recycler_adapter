package com.halove.xyp.common_adapter.MVVMAdapter

/**
 * Created by xyp on 2018/8/30.
 */
interface IAdapterFoot {
    /**
     * 添加尾布局
     */
    fun addFoot(headBuilder: HeadBuilder)

    /**
     * 移除某一个位置的尾布局
     */
    fun removeFootAt(position: Int)

    /**
     * 移除全部尾布局
     */
    fun removeAllFoot()
}