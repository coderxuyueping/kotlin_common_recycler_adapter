package com.halove.xyp.kotlin_common_recycler_adapter.sample

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.halove.xyp.kotlin_common_recycler_adapter.BR
import com.halove.xyp.kotlin_common_recycler_adapter.R
import com.halove.xyp.common_adapter.commonAdapter.CommonData

/**
 * Created by xyp on 2018/8/20.
 */
data class Bean3(var id: String = "") : BaseObservable(), CommonData {

    @Bindable
    var msg = id
    set(value) {
        field = value
        notifyPropertyChanged(BR.msg)
    }

    override fun getType() = R.layout.drv_layout3

    override fun getBR(): Int {
        return BR.bean3
    }
}