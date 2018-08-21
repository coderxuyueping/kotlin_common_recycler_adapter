package com.halove.xyp.kotlin_common_recycler_adapter.dCommonAdapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.halove.xyp.kotlin_common_recycler_adapter.commonAdapter.CommonData

/**
 * Created by xyp on 2018/8/20.
 * 支持单布局与多布局的适配器
 */
class DCommonAdapter @JvmOverloads constructor(private val datas: List<CommonData>, private val isFullSpan: (position: Int) -> Boolean = {false}) : RecyclerView.Adapter<DCommonViewHolder>() {

    override fun getItemCount() = datas.size

    override fun getItemViewType(position: Int) = datas[position].getType()

    //外部传进来的type需要是布局id，便于这里直接使用
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DCommonViewHolder{
       val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), viewType, parent,false)
        return DCommonViewHolder.createViewHolder(dataBinding.root, dataBinding)
    }

    override fun onBindViewHolder(holder: DCommonViewHolder, position: Int) {
        holder.dataBinding.setVariable(datas[position].getBR(), datas[position])
        holder.dataBinding.executePendingBindings()//防止闪烁
        holder.rootView.setOnClickListener {
            if (onItemClick != null)
                onItemClick!!.onItemClick(position)
        }
    }

    /**
     * 为瀑布管理器设置是否独占一列
     */
    override fun onViewAttachedToWindow(holder: DCommonViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (isFullSpan(holder.layoutPosition)) {
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }

    /**
     * 为网格管理器设置是否独占一列
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val lm = recyclerView.layoutManager
        if (lm is GridLayoutManager) {
            lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = if (isFullSpan(position)) lm.spanCount else 1
            }
        }
    }


    //整体item布局的点击事件
    interface OnItemClick {
        fun onItemClick(position: Int)
    }

    //不再对外提供set方法，kotlin默认实现
    var onItemClick: OnItemClick? = null

}