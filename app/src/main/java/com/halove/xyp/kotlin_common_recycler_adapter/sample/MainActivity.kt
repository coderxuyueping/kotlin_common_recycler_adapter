package com.halove.xyp.kotlin_common_recycler_adapter.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.halove.xyp.common_adapter.MVVMAdapter.HeadFootBuilder
import com.halove.xyp.common_adapter.MVVMAdapter.IBuilder
import com.halove.xyp.common_adapter.MVVMAdapter.MCommonAdapter
import com.halove.xyp.common_adapter.MVVMAdapter.MDCommonAdapter
import com.halove.xyp.common_adapter.commonAdapter.CommonAdapter
import com.halove.xyp.common_adapter.commonAdapter.CommonData
import com.halove.xyp.kotlin_common_recycler_adapter.R
import com.halove.xyp.common_adapter.dCommonAdapter.DCommonAdapter
import com.halove.xyp.kotlin_common_recycler_adapter.BR
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var datas = arrayListOf<CommonData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        (0..2).mapTo(datas) { Bean1("数据模板1_" + it) }

        Log.d("xuyueping", "" + datas)

        recycler_view.layoutManager = LinearLayoutManager(this)

        mvvmAdapter()

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


   /*     //测试数据源自动更新功能
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
        }*/
    }


    /**
     * mvvm下使用，view与model完全分开，不再依赖model里的bean
     */
    private fun mvvmAdapter(){
        val datas = arrayListOf(MVVMBean1("徐大哈布局1",20),MVVMBean2("徐大哈布局2"),
                MVVMBean1("徐大哈布局1",20),MVVMBean1("徐大哈布局1",20),
                MVVMBean2("徐大哈布局2"),MVVMBean1("徐大哈布局1",20))
        val adapter = MCommonAdapter(object : IBuilder{
            /**
             * 提供空视图布局id
             */
            override fun getEmptyLayout(): Int {
                return R.layout.empty
            }

            /**
             * 根据数据源传入对应的type,type对应布局id
             */
            override fun getType(data: Any): Int {
                when(data){
                    is MVVMBean1 -> return R.layout.rv_item_mvvm_layout1
                    is MVVMBean2 -> return R.layout.rv_item_mvvm_layout2
                }

                return 0
            }

        },datas,bindData = {holder, data ->
            when(data){
                is MVVMBean1 -> {
                    val tvName = holder.getView(R.id.name) as TextView
                    val tvAge = holder.getView(R.id.age) as TextView
                    tvName.text = data.name
                    tvAge.text = "年龄是${data.age}"
                }
                is MVVMBean2 -> {
                    val tvName = holder.getView(R.id.name) as TextView
                    tvName.text = data.name
                }
            }
        })



        recycler_view.adapter = adapter

        btn_remove.setOnClickListener {
            datas.clear()
            adapter.notifyDataSetChanged()
        }

        btn_add.setOnClickListener {
            datas.addAll(arrayListOf(MVVMBean1("徐大哈布局1",20),MVVMBean2("徐大哈布局2"),
                    MVVMBean1("徐大哈布局1",20),MVVMBean1("徐大哈布局1",20),
                    MVVMBean2("徐大哈布局2"),MVVMBean1("徐大哈布局1",20)))
            adapter.notifyDataSetChanged()
        }

        btn_add_head1.setOnClickListener {
            adapter.addHead(object : HeadFootBuilder(HeadBean1("我是头布局1")){
                /**
                 * 根据数据源传入对应的type,type对应布局id
                 */
                override fun getType(data: Any): Int {
                    return R.layout.head1
                }

                override fun bindData(view: View, data: Any){
                    if(data is HeadBean1){
                        val tv = view.findViewById<TextView>(R.id.tv_head1)
                        tv?.text = data.head
                    }
                }

            })
        }

        btn_add_head2.setOnClickListener {
            adapter.addHead(object : HeadFootBuilder(HeadBean2("我是头布局2")){
                /**
                 * 根据数据源传入对应的type,type对应布局id
                 */
                override fun getType(data: Any): Int {
                    return R.layout.head2
                }

                override fun bindData(view: View, data: Any){
                    if(data is HeadBean2){
                        val tv = view.findViewById<TextView>(R.id.tv_head2)
                        tv?.text = data.head
                    }
                }

            })
        }

        btn_remove_head.setOnClickListener {
            adapter.removeHeadAt(1)
        }

        btn_remove_all.setOnClickListener {
            adapter.removeAllHead()
        }

        adapter.onItemClick = object : MCommonAdapter.OnItemClick{
            override fun onItemClick(position: Int) {
                toast("$position")
            }

        }


        btn_addfoot1.setOnClickListener {
            adapter.addFoot(object : HeadFootBuilder(HeadBean1("我是尾布局1")){
                /**
                 * 根据数据源传入对应的type,type对应布局id
                 */
                override fun getType(data: Any): Int {
                    return R.layout.head1
                }

                override fun bindData(view: View, data: Any){
                    if(data is HeadBean1){
                        val tv = view.findViewById<TextView>(R.id.tv_head1)
                        tv?.text = data.head
                    }
                }

            })
        }


        btn_addfoot2.setOnClickListener {
            adapter.addFoot(object : HeadFootBuilder(HeadBean2("我是尾布局2")){
                /**
                 * 根据数据源传入对应的type,type对应布局id
                 */
                override fun getType(data: Any): Int {
                    return R.layout.head2
                }

                override fun bindData(view: View, data: Any){
                    if(data is HeadBean2){
                        val tv = view.findViewById<TextView>(R.id.tv_head2)
                        tv?.text = data.head
                    }
                }

            })
        }

        btn_removefoot1.setOnClickListener {
            adapter.removeFootAt(adapter.lastPosition() - 2)
        }

        btn_removeallfoot.setOnClickListener {
            adapter.removeAllFoot()
        }
    }


    /**
     * mvvm使用databinding
     */
    private fun mvvmDAdapter(){
        val datas = arrayListOf(MVVMBean1("徐大哈布局1",20),MVVMBean2("徐大哈布局2"),
                MVVMBean1("徐大哈布局1",20),MVVMBean1("徐大哈布局1",20),
                MVVMBean2("徐大哈布局2"),MVVMBean1("徐大哈布局1",20))

        recycler_view.adapter = MDCommonAdapter(object : IBuilder{
            /**
             * 提供空视图布局id
             */
//            override fun getEmptyLayout(): Int {
//                return R.layout.empty
//            }

            /**
             * 根据数据源传入对应的type,type对应布局id
             */
            override fun getType(data: Any): Int {
                when(data){
                    is MVVMBean1 -> return R.layout.d_rv_item_mvvm_layout1
                    is MVVMBean2 -> return R.layout.d_rv_item_mvvm_layout2
                }

                return 0
            }

            override fun getBR(data: Any): Int {
                when(data){
                    is MVVMBean1 -> return BR.bean1
                    is MVVMBean2 -> return BR.bean2
                }

                return super.getBR(data)
            }

        }, datas)



        btn_remove.setOnClickListener {
            datas.clear()
            (recycler_view.adapter as MDCommonAdapter).notifyDataSetChanged()
        }

        btn_add.setOnClickListener {
            datas.addAll(arrayListOf(MVVMBean1("徐大哈布局1",20),MVVMBean2("徐大哈布局2"),
                    MVVMBean1("徐大哈布局1",20),MVVMBean1("徐大哈布局1",20),
                    MVVMBean2("徐大哈布局2"),MVVMBean1("徐大哈布局1",20)))
            (recycler_view.adapter as MDCommonAdapter).notifyDataSetChanged()
        }

        btn_add_head1.setOnClickListener {
            (recycler_view.adapter as MDCommonAdapter).addHead(object : HeadFootBuilder(HeadBean1("我是头布局1")){
                /**
                 * 根据数据源传入对应的type,type对应布局id
                 */
                override fun getType(data: Any): Int {
                    return R.layout.dhead1
                }

                override fun getBR(data: Any): Int {
                    return BR.bean1
                }

            })
        }

        btn_add_head2.setOnClickListener {
            (recycler_view.adapter as MDCommonAdapter).addHead(object : HeadFootBuilder(HeadBean2("我是头布局2")){
                /**
                 * 根据数据源传入对应的type,type对应布局id
                 */
                override fun getType(data: Any): Int {
                    return R.layout.dhead2
                }

                override fun getBR(data: Any): Int {
                    return BR.bean2
                }

            })
        }

        btn_remove_head.setOnClickListener {
            (recycler_view.adapter as MDCommonAdapter).removeHeadAt(0)
        }

        btn_remove_all.setOnClickListener {
            (recycler_view.adapter as MDCommonAdapter).removeAllHead()
        }

        (recycler_view.adapter as MDCommonAdapter).onItemClick = object : MDCommonAdapter.OnItemClick{
            override fun onItemClick(position: Int) {
                toast("$position")
            }

        }
    }
}
