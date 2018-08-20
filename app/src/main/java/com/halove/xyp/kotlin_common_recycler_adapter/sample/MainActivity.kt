package com.halove.xyp.kotlin_common_recycler_adapter.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TextView
import com.halove.xyp.kotlin_common_recycler_adapter.CommonAdapter
import com.halove.xyp.kotlin_common_recycler_adapter.CommonData
import com.halove.xyp.kotlin_common_recycler_adapter.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var datas = arrayListOf<CommonData>()
        (0..5).mapTo(datas) { Bean1("数据模板1_" + it) }

        Log.d("xuyueping", "" + datas)

        recycler_view.layoutManager = LinearLayoutManager(this)
        //单布局
        /*  recycler_view.adapter = CommonAdapter(data,
                  {position -> false},
                  {holder, commonData ->
                      val textView = holder.getView(R.id.text) as TextView
                      if(commonData is Bean1)
                         textView.text = commonData.msg
          })*/

        //多布局
        (0..4).mapTo(datas) { Bean2("数据模板2_" + it) }

        val adapter = CommonAdapter(datas = datas,
                bindData = { holder, data ->
                    when (data) {
                        is Bean1 -> {
                            val textView = holder.getView(R.id.text) as TextView
                            textView.text = data.msg
                        }

                        is Bean2 -> {
                            val textView = holder.getView(R.id.text) as TextView
                            textView.text = data.msg
                        }
                    }
                })

        adapter.onItemClick = object : CommonAdapter.OnItemClick {
            override fun onItemClick(position: Int) {
                toast("点击了$position")
            }
        }

        recycler_view.adapter = adapter
    }
}
