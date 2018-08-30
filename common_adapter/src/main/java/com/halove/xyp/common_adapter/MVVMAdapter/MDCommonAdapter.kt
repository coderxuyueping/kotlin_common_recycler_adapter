package com.halove.xyp.common_adapter.MVVMAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.halove.xyp.common_adapter.commonAdapter.CommonViewHolder
import com.halove.xyp.common_adapter.dCommonAdapter.DCommonViewHolder

/**
 * Created by xyp on 2018/8/30.
 * 支持单布局与多布局的适配器
 */
class MDCommonAdapter @JvmOverloads constructor(private val builder: IBuilder, private val data: ArrayList<Any>,private val isFullSpan: (position: Int) -> Boolean = {false}) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = if(data.size == 0 && builder.getEmptyLayout() != -1) 1 else data.size

    override fun getItemViewType(position: Int) = if(data.size == 0 && builder.getEmptyLayout() != -1) builder.getEmptyLayout() else builder.getType(data[position])

    //外部传进来的type需要是布局id，便于这里直接使用
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //因为空视图用来展示用的，不需要绑定数据
        if(data.size == 0)
            return CommonViewHolder.createViewHolder(parent, viewType)
       val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), viewType, parent,false)
        return DCommonViewHolder.createViewHolder(dataBinding.root, dataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(data.size != 0 && holder is DCommonViewHolder){
            holder.dataBinding.setVariable(builder.getBR(data[position]), data[position])
            holder.dataBinding.executePendingBindings()//防止闪烁
            holder.rootView.setOnClickListener {
                onItemClick?.onItemClick(position)
            }
        }else if(holder is CommonViewHolder)
            holder.rootView.setOnClickListener {
                onItemClick?.onItemClick(position)
            }

    }

    /**
     * 为瀑布管理器设置是否独占一列
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
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