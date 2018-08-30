package com.halove.xyp.common_adapter.dCommonAdapter

import android.util.SparseArray
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by xyp on 2018/8/21.
 */
class DCommonViewHolder(val rootView: View, val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(rootView) {
    private val childViews: SparseArray<View> by lazy { SparseArray<View>() }

    companion object {
        fun createViewHolder(view: View, dataBinding: ViewDataBinding): DCommonViewHolder {
            return DCommonViewHolder(view, dataBinding)
        }
    }

    /**
     * 获取布局中的view
     */
    fun getView(id: Int): View {
        var view = childViews.get(id)
        if(view == null){
            view = this.rootView.findViewById(id)
            childViews.put(id, view)
        }
        return view
    }
}