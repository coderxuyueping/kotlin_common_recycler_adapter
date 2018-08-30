package com.halove.xyp.kotlin_common_recycler_adapter.sample;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.halove.xyp.common_adapter.commonAdapter.CommonViewHolder;
import com.halove.xyp.kotlin_common_recycler_adapter.R;
import com.halove.xyp.common_adapter.commonAdapter.CommonAdapter;
import com.halove.xyp.common_adapter.commonAdapter.CommonData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by xyp on 2018/8/20.
 */

public class JavaActivity extends AppCompatActivity {
    private List<CommonData> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommonAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data.add(new Bean1("数据1"));
        data.add(new Bean2("数据2"));
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //java调用kotlin的函数参数方法,判断是否独占一列的使用函数的默认实现
        adapter = new CommonAdapter(data, new Function2<CommonViewHolder, CommonData, Unit>() {
            @Override
            public Unit invoke(CommonViewHolder holder, CommonData data) {
                if(data instanceof Bean1){
                    TextView tv = (TextView) holder.getView(R.id.text);
                    tv.setText(((Bean1) data).getMsg());
                }else if(data instanceof Bean2){
                    TextView tv = (TextView) holder.getView(R.id.text);
                    tv.setText(((Bean2) data).getMsg());
                }
                return null;
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClick(new CommonAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(JavaActivity.this, "点击了"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
