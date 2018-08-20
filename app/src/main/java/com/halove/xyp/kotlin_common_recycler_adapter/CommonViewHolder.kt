package com.halove.xyp.kotlin_common_recycler_adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by xyp on 2018/8/20.
 * 通用的ViewHolder
 */
class CommonViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
    private val childViews: SparseArray<View> by lazy { SparseArray<View>() }

    companion object {
        fun createViewHolder(parent: ViewGroup, layoutId: Int): CommonViewHolder{
            return CommonViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
        }
    }

    /**
     * 获取根布局
     */
    fun getRootView() = rootView

    /**
     * 获取布局中的view
     */
    fun getView(id: Int): View{
        var view = childViews.get(id)
        if(view == null){
            view = this.rootView.findViewById(id)
            childViews.put(id, view)
        }
        return view
    }
}