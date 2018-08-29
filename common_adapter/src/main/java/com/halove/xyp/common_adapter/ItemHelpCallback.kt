package com.halove.xyp.common_adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by xyp on 2018/8/29.
 * 处理recyclerView Item拖拽和滑动
 */
class ItemHelpCallback<T>(var data: ArrayList<T>) : ItemTouchHelper.Callback() {
    private var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private var state = 0

    /**
     * 默认可拖拽不可滑动
     */
    var dragEnabled = true//是否可拖拽
    var swipeEnabled = false//是否可滑动

    private var fromPosition = 0

    /**
     * 设置对应不同的管理器拖拽和滑动的方向
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        adapter = recyclerView.adapter
        var dragFlags = 0//拖拽的状态值，0表示不可拖拽
        var swipeFlags = 0//滑动状态值
        val layoutManager = recyclerView.layoutManager

        /**
         * 只能先判断GridLayoutManager，因为它是LinearLayoutManger的子类
         */
        if(layoutManager is GridLayoutManager){
            //只能拖拽不能滑动
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }else if(layoutManager is LinearLayoutManager){
            if(layoutManager.orientation == LinearLayoutManager.HORIZONTAL){
                //可以左右拖拽，上下滑动删除
                dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                swipeFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }else{
                swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
        }

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * 拖拽中回调,处理了数据变换位置
     */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        //当前位置
        var fromPosition = viewHolder.adapterPosition
        //要交换的位置
        var toPosition = target.adapterPosition
        if(fromPosition < toPosition){
            for(i in fromPosition until toPosition){
                Collections.swap(data, i, i+1)
            }
        }else{
            for(i in fromPosition downTo  toPosition + 1){
                Collections.swap(data, i, i-1)
            }
        }

        recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    /**
     * 滑动回调,处理滑动删除
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        data.removeAt(position)
        adapter?.notifyItemRemoved(position)
        //刷新position
        adapter?.notifyItemRangeChanged(position, data.size - position)
    }

    /**
     * 手指按下后根据不同的情况区分是拖拽还是滑动
     * actionState：2为拖拽，1为滑动，0为空闲
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        fromPosition = viewHolder?.adapterPosition?:fromPosition
        if(actionState != 0)
            state = actionState
    }

    /**
     * 手指松开
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        //手指松开后局部刷新，处理拖拽后position混乱
        if(state != 2){
            state = 0
            return
        }

        //优化刷新,不再全部刷新
        if(fromPosition < viewHolder.adapterPosition)
            recyclerView.adapter?.notifyItemRangeChanged(fromPosition,viewHolder.adapterPosition+1)
        else
            recyclerView.adapter?.notifyItemRangeChanged(viewHolder.adapterPosition,fromPosition+1)

        state = 0
    }

    /**
     * 设置是否可用滑动
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return swipeEnabled
    }

    /**
     * 设置是否可用拖拽
     */
    override fun isLongPressDragEnabled(): Boolean {
        return dragEnabled
    }
}