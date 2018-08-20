package com.halove.xyp.kotlin_common_recycler_adapter.sample

import android.content.Context
import android.widget.Toast

/**
 * Created by xyp on 2018/8/20.
 */
fun Context.toast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}