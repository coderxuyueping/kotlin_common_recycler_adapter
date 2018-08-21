package com.halove.xyp.kotlin_common_recycler_adapter.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TextView
import com.halove.xyp.kotlin_common_recycler_adapter.commonAdapter.CommonAdapter
import com.halove.xyp.kotlin_common_recycler_adapter.commonAdapter.CommonData
import com.halove.xyp.kotlin_common_recycler_adapter.R
import com.halove.xyp.kotlin_common_recycler_adapter.dCommonAdapter.DCommonAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var datas = arrayListOf<CommonData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (0..2).mapTo(datas) { Bean1("数据模板1_" + it) }

        Log.d("xuyueping", "" + datas)

        recycler_view.layoutManager = LinearLayoutManager(this)

        dCommonAdapter()

    }


    /**
     * 未采用dataBinding的形式，使用方法：
     * 1.数据源需要实现CommonData接口，type返回的是对应布局的id
     * 2.传入数据源，如果有两种不同的数据源格式，那么就对应多布局模式，根据情况实现isFullSpan是否在某一处独占一列
     * val adapter = CommonAdapter(datas,{position ->  position == 3},{holder,data->})
     * 3.实现数据源跟布局的绑定bindData
     */
    private fun commonAdapter(){
        //单布局
        /*  recycler_view.adapter = CommonAdapter(data,
                  {position -> false},
                  {holder, commonData ->
                      val textView = holder.getView(R.id.text) as TextView
                      if(commonData is Bean1)
                         textView.text = commonData.msg
          })*/

        //多布局
        (0..3).mapTo(datas) { Bean2("数据模板2_" + it) }

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

    /**
     * 使用了dataBinding，只需要传入数据源，数据的绑定交给dataBinding管理
     * 数据源除了需要实现接口CommonData，还需要继承BaseObservable监听数据改变
     */
    private fun dCommonAdapter(){
        //单布局
//        recycler_view.adapter = DCommonAdapter(datas)

        //多布局
        (0..3).mapTo(datas) { Bean2("数据模板2_" + it) }
        datas.add(Bean3("数据模板3"))
        datas.add(Bean3("数据模板3"))
        val adapter = DCommonAdapter(datas)
        recycler_view.adapter = adapter


        //测试数据源自动更新功能
        btn.setOnClickListener {
            //添加新的数据到数据源中
//            datas.add(Bean2("数据模板2_"))
//            adapter.notifyDataSetChanged()
            //删除数据
//            datas.removeAt(0)
//            adapter.notifyDataSetChanged()


            //修改集合中已经绑定上了的数据不需要adapter通知刷新
            val data = datas[datas.size-1]
            if(data is Bean3){
                data.msg = "修改的數據"
            }
        }
    }
}
