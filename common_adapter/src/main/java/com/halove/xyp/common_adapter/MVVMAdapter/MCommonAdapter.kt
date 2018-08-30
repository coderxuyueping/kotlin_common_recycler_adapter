package com.halove.xyp.common_adapter.MVVMAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.halove.xyp.common_adapter.commonAdapter.CommonViewHolder

/**
 * Created by xyp on 2018/8/30.
 * 支持单布局与多布局的适配器,可显示空视图，添加头尾布局
 */
class MCommonAdapter @JvmOverloads constructor(private val builder: IBuilder, private val data: ArrayList<Any>,private val isFullSpan: (position: Int) -> Boolean = {false}, private val bindData: (holder: CommonViewHolder, data: Any) -> Unit) : RecyclerView.Adapter<CommonViewHolder>(), IAdapterHead {

    //存放头布局的builder
    private val headBuilders: ArrayList<HeadBuilder> by lazy { ArrayList<HeadBuilder>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return CommonViewHolder.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.rootView.setOnClickListener {
            //这个position包含了头
            onItemClick?.onItemClick(position)
        }

        if(headBuilders.size > position) {
            headBuilders.forEach{
                it.bindData(holder.rootView, it.headData)
            }

            return
        }

        if(data.size != 0)
            bindData(holder, data[position - headBuilders.size])

    }

    override fun getItemCount(): Int {
        //如果数据源为空并且外部设置了显示的空视图布局,则显示一个空视图,注意：需要算上头布局
        if(data.size == 0 && builder.getEmptyLayout() != -1)
            return 1 + headBuilders.size

        return data.size + headBuilders.size
    }

    override fun getItemViewType(position: Int): Int {
        if (headBuilders.size > position)
            return headBuilders[position].getType(headBuilders[position].headData)

        return if(data.size == 0 && builder.getEmptyLayout() != -1) builder.getEmptyLayout()
        else builder.getType(data[position - headBuilders.size])
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



    override fun addHead(headBuilder: HeadBuilder){
        headBuilders.add(headBuilder)
        notifyItemInserted(headBuilders.size-1)
        //刷新position
        notifyItemRangeChanged(headBuilders.size - 1, data.size+1)
    }


    override fun removeHeadAt(position: Int){
        if(position >= headBuilders.size) return
        headBuilders.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size + headBuilders.size - position - 1)
    }


    override fun removeAllHead(){
        if(headBuilders.size != 0){
            headBuilders.clear()
            notifyDataSetChanged()
        }
    }


    //整体item布局的点击事件
    interface OnItemClick {
        fun onItemClick(position: Int)
    }

    //不再对外提供set方法，kotlin默认实现
    var onItemClick: OnItemClick? = null



    /**
     * 头布局的数量
     */
    fun headSize() = headBuilders.size

    /**
     * 空视图是否在显示
     */
    fun emptyVisible() = data.size == 0
}