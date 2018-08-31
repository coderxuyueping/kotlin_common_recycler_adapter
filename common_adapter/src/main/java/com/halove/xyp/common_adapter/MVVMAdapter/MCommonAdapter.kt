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
class MCommonAdapter @JvmOverloads constructor(private val builder: IBuilder, private val data: ArrayList<Any>,private val isFullSpan: (position: Int) -> Boolean = {false}, private val bindData: (holder: CommonViewHolder, data: Any) -> Unit) : RecyclerView.Adapter<CommonViewHolder>(), IAdapterHead, IAdapterFoot{

    //存放头布局的builder
    private val headBuilders: ArrayList<HeadFootBuilder> by lazy { ArrayList<HeadFootBuilder>() }

    //存放尾布局的builder
    private val footBuilders: ArrayList<HeadFootBuilder> by lazy { ArrayList<HeadFootBuilder>() }

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

        if(data.size != 0 && position < data.size + headBuilders.size) {
            bindData(holder, data[position - headBuilders.size])
            return
        }

        footBuilders.forEach{
            it.bindData(holder.rootView, it.headData)
        }

    }

    /**
     * 没有数据源的情况下不显示尾布局
     */
    override fun getItemCount(): Int {
        //如果数据源为空并且外部设置了显示的空视图布局,则显示一个空视图,注意：需要算上头布局
        return if(emptyVisible()) 1 + headBuilders.size
        else data.size + headBuilders.size + if (data.size == 0) 0 else footBuilders.size
    }

    override fun getItemViewType(position: Int): Int {
        if (headBuilders.size > position)
            return headBuilders[position].getType(headBuilders[position].headData)

        //需要算上空视图
        val dataSize = if(data.size == 0) 1 else data.size

        if(position > headBuilders.size + dataSize - 1)
            return footBuilders[position - headBuilders.size - dataSize].getType(footBuilders[position - headBuilders.size - dataSize].headData)

        return if(emptyVisible()) builder.getEmptyLayout()
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



    override fun addHead(headFootBuilder: HeadFootBuilder){
        val dataSize = if(data.size == 0) 1 else data.size
        headBuilders.add(headFootBuilder)
        notifyItemInserted(headBuilders.size - 1)
        //刷新position
        notifyItemRangeChanged(headBuilders.size - 1, dataSize + 1 + footBuilders.size)
    }


    override fun removeHeadAt(position: Int){
        if(position >= headBuilders.size) return
        headBuilders.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, lastPosition() - position + 1)
    }


    override fun removeAllHead(){
        if(headBuilders.size != 0){
            headBuilders.clear()
            notifyDataSetChanged()
        }
    }

    /**
     * 添加尾布局
     */
    override fun addFoot(headFootBuilder: HeadFootBuilder) {
        //没有数据则不能添加尾布局
        if(data.size == 0) return
        val dataSize = if(data.size == 0) 1 else data.size
        footBuilders.add(headFootBuilder)
        notifyItemInserted(dataSize + headBuilders.size + footBuilders.size - 1)
        //刷新position
        notifyItemRangeChanged(dataSize + headBuilders.size + footBuilders.size - 1, 1)
    }

    /**
     * 移除某一个位置的尾布局
     */
    override fun removeFootAt(position: Int) {
        val dataSize = if(data.size == 0) 1 else data.size
        if((position > dataSize + headBuilders.size + footBuilders.size - 1) or (position < dataSize + headBuilders.size)) return
        footBuilders.removeAt(footBuilders.size - 1 - (lastPosition() - position))
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, lastPosition() - position + 1 )
    }

    /**
     * 移除全部尾布局
     */
    override fun removeAllFoot() {
        if(footBuilders.size != 0){
            footBuilders.clear()
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
     * 尾布局的数量
     */
    fun footSize() = footBuilders.size

    /**
     * 数据源长度
     */
    fun dataSize() = data.size

    /**
     * 空视图是否在显示,注意空视图也占一个position
     */
    fun emptyVisible() = data.size == 0 && builder.getEmptyLayout() != -1

    /**
     * 获取最后一个position
     */
    fun lastPosition() = if(emptyVisible()) headBuilders.size + footBuilders.size else headBuilders.size + data.size + footBuilders.size - 1
}