package com.halove.xyp.common_adapter.commonAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by xyp on 2018/8/20.
 * 支持单布局与多布局的适配器
 */
class CommonAdapter @JvmOverloads constructor(private val datas: List<CommonData>, private val isFullSpan: (position: Int) -> Boolean = {false}, private val bindData: (holder: CommonViewHolder, data: CommonData) -> Unit) : RecyclerView.Adapter<CommonViewHolder>() {

    override fun getItemCount() = datas.size

    override fun getItemViewType(position: Int) = datas[position].getType()

    //外部传进来的type需要是布局id，便于这里直接使用
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommonViewHolder.createViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        bindData(holder, datas[position])

        holder.rootView.setOnClickListener {
                onItemClick?.onItemClick(position)
        }
    }

    /**
     * 为瀑布管理器设置是否独占一列
     */
    override fun onViewAttachedToWindow(holder: CommonViewHolder) {
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