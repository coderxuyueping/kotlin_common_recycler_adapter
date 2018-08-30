package com.halove.xyp.common_adapter.MVVMAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.halove.xyp.common_adapter.commonAdapter.CommonViewHolder

/**
 * Created by xyp on 2018/8/30.
 */
class MCommonAdapter @JvmOverloads constructor(private val builder: IBuilder, private val data: ArrayList<Any>,private val isFullSpan: (position: Int) -> Boolean = {false}, private val bindData: (holder: CommonViewHolder, data: Any) -> Unit) : RecyclerView.Adapter<CommonViewHolder>() {

    private val headBuilder: ArrayList<HeadBuilder> by lazy { ArrayList<HeadBuilder>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return CommonViewHolder.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        if(data.size != 0)
            bindData(holder, data[position])

        holder.rootView.setOnClickListener {
            onItemClick?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        //如果数据源为空并且外部设置了显示的空视图布局,则显示一个空视图,注意：需要算上头布局
        if(data.size == 0 && builder.getEmptyLayout() != -1)
            return 1+headBuilder.size

        return data.size+headBuilder.size
    }

    override fun getItemViewType(position: Int): Int {
//        if (headBuilder.size > position)
//            return headBuilder[position].getType(null)
        return if(data.size == 0 && builder.getEmptyLayout() != -1) builder.getEmptyLayout() else builder.getType(data[position])
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


    /**
     * 对外提供添加头布局的方法，每次添加都是往第一个位置插入
     */
    fun addHead(headBuilder: HeadBuilder){

    }

    //整体item布局的点击事件
    interface OnItemClick {
        fun onItemClick(position: Int)
    }

    //不再对外提供set方法，kotlin默认实现
    var onItemClick: OnItemClick? = null
}