package com.tangr.customui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tangr.customui.widgets.flowlayout.FlowLayout;
import com.tangr.customui.widgets.flowlayout.OnTagViewClickListener;
import com.tangr.customui.widgets.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_view);

        final FlowLayout fl1 = (FlowLayout) findViewById(R.id.flowlayout1);
        final List<String> mDatas1 = new ArrayList<String>();
        mDatas1.add("黑");
        mDatas1.add("深空灰");
        mDatas1.add("深蓝");
        mDatas1.add("玫瑰金");
        fl1.setAdapter(new TagAdapter(mDatas1));
        fl1.setMode(FlowLayout.MODE_SINGLE);
        fl1.setOnSelectListener(new OnTagViewClickListener() {
            @Override
            public void onSelect(int position) {
            }

            @Override
            public void onSelectFull(int count) {
            }
        });

        final FlowLayout fl2 = (FlowLayout) findViewById(R.id.flowlayout2);
        final List<String> mDatas2 = new ArrayList<String>();
        mDatas2.add("S");
        mDatas2.add("M");
        mDatas2.add("L");
        mDatas2.add("XL");
        mDatas2.add("XXL");
        fl2.setAdapter(new TagAdapter(mDatas2));
        fl2.setMode(FlowLayout.MODE_SINGLE);
        fl2.setOnSelectListener(new OnTagViewClickListener() {
            @Override
            public void onSelect(int position) {
            }
            @Override
            public void onSelectFull(int count) {
            }
        });

        final FlowLayout fl3 = (FlowLayout) findViewById(R.id.flowlayout3);
        final List<String> mDatas3 = new ArrayList<String>();
        mDatas3.add("物流快");
        mDatas3.add("服务好");
        mDatas3.add("质量好");
        mDatas3.add("正品");
        mDatas3.add("便宜");
        mDatas3.add("性价比高");
        fl3.setAdapter(new TagAdapter(mDatas3));
        fl3.setMode(FlowLayout.MODE_MULTI, 3);
        fl3.setOnSelectListener(new OnTagViewClickListener() {
            @Override
            public void onSelect(int position) {
            }

            @Override
            public void onSelectFull(int count) {
                Toast.makeText(MainActivity.this, "已选满" + count + "样", Toast.LENGTH_SHORT).show();
            }
        });

        final TextView tv = (TextView) findViewById(R.id.tv);

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                sb.append("你选择了---");
                List<Integer> list = fl1.getSelected();
                if(list==null||list.get(0)==-1){
                    Toast.makeText(MainActivity.this,"请选择颜色",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    sb.append("颜色:"+mDatas1.get(list.get(0)));
                }
                sb.append("   ");
                list = fl2.getSelected();
                if(list==null||list.get(0)==-1){
                    Toast.makeText(MainActivity.this,"请选择尺寸",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    sb.append("尺寸:"+mDatas2.get(list.get(0)));
                }
                sb.append("   ");
                list = fl3.getSelected();
                if(list==null||list.size()<1){
                    Toast.makeText(MainActivity.this,"请选择评价",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    sb.append("评价:");
                    for (Integer i:list){
                        sb.append(mDatas3.get(i)+",");
                    }
                    sb.deleteCharAt(sb.length()-1);
                }
                tv.setText(sb);
            }
        });
    }
}
