package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

/**
 * Created by developer on 2018/3/19.
 */

public class Myorder extends Activity {

    ListView lstWeiwancheng, lstYiwancheng;
    String[] datasource_order = {"我的已完成订单1", "我的已完成订单2", "我的已完成订单3", "我的已完成订单4", "我的已完成订单5", "我的已完成订单6", "我的已完成订单7", "我的已完成订单8", "我的已完成订单9", "我的订单10", "我的已完成订单11", "我的已完成订单12", "我的已完成订单1", "我的已完成订单2", "我的已完成订单3", "我的已完成订单4", "我的已完成订单5", "我的已完成订单6", "我的已完成订单7", "我的已完成订单8", "我的已完成订单9", "我的已完成订单10", "我的已完成订单11", "我的已完成订单12", "我的已完成订单1", "我的已完成订单2", "我的已完成订单3", "我的已完成订单4", "我的已完成订单5", "我的已完成订单6", "我的已完成订单7", "我的已完成订单8", "我的已完成订单9", "我的已完成订单10", "我的已完成订单11", "我的订单12", "我的已完成订单1", "我的已完成订单2", "我的已完成订单3", "我的已完成订单4", "我的已完成订单5", "我的已完成订单6", "我的已完成订单7", "我的已完成订单8", "我的已完成订单9", "我的已完成订单10", "我的已完成订单11", "我的已完成订单12"};
    String[] datasource_order1 = {"我的未完成订单1", "我的未完成订单2", "我的未完成订单3", "我的未完成订单4", "我的未完成订单5", "我的未完成订单6", "我的未完成订单7", "我的未完成订单8", "我的未完成订单9", "我的未完成订单10", "我的未完成订单11", "我的未完成订单12", "我的未完成订单1", "我的未完成订单2", "我的未完成订单3", "我的未完成订单4", "我的未完成订单5", "我的未完成订单6", "我的未完成订单7", "我的未完成订单8", "我的未完成订单9", "我的未完成订单10", "我的未完成订单11", "我的未完成订单12", "我的未完成订单1", "我的未完成订单2", "我的未完成订单3", "我的未完成订单4", "我的未完成订单5", "我的未完成订单6", "我的未完成订单7", "我的未完成订单8", "我的未完成订单9", "我的未完成订单10", "我的未完成订单11", "我的未完成订单12", "我的未完成订单1", "我的未完成订单2", "我的未完成订单3", "我的未完成订单4", "我的未完成订单5", "我的未完成订单6", "我的未完成订单7", "我的未完成订单8", "我的未完成订单9", "我的未完成订单10", "我的未完成订单11", "我的未完成订单12"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder);
        //courses=getResources().getStringArray(R.array.course);



        /*--------------------------------未完成订单--------------------------------*/
        lstWeiwancheng = (ListView) findViewById(R.id.lst_weiwancheng);

        ArrayAdapter<String> adapter_order = new ArrayAdapter<String>(
                this,
                R.layout.rescue_record_item,
                R.id.tv1_order,
                /*android.R.layout.simple_list_item_1,
                android.R.id.text1,*/
                datasource_order1
        );

        lstWeiwancheng.setAdapter(adapter_order);


        lstWeiwancheng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String wwcdd=datasource_order1[i]+"被吃掉了";
                Toast.makeText(Myorder.this, wwcdd, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Myorder.this,OrderDetailActivity.class);
                startActivity(intent);
            }
        });


        /*--------------------------------未完成订单结束--------------------------------*/


        /*--------------------------------已完成订单------------------------------------*/
        lstYiwancheng = (ListView) findViewById(R.id.lst_yiwancheng);

        ArrayAdapter<String> adapter_order_complete = new ArrayAdapter<String>(
                this,
                R.layout.rescue_record_item,
                R.id.tv1_order,
                datasource_order
        );

        lstYiwancheng.setAdapter(adapter_order_complete);

        lstYiwancheng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ywcdd=datasource_order[i]+"订单在睡觉";
                Toast.makeText(Myorder.this, ywcdd, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Myorder.this,OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        /*--------------------------------已完成订单结束--------------------------------*/


        /*--------------------------------tabhost选项卡--------------------------------*/
        /*--------------------------------用到tabhost，复制下面代码即可~~----------------*/

        TabHost tHost = (TabHost) findViewById(R.id.tabhost_myorder);
        //在tabhost上添加选项卡
        tHost.setup();

        TabHost.TabSpec tabSpec = tHost.newTabSpec("Test");
        //选项卡上的标签或文字
        tabSpec.setIndicator("已完成");

        //选项卡对应的内容
        tabSpec.setContent(R.id.tab1);
        tHost.addTab(tabSpec);

        tHost.addTab(tHost.newTabSpec("TEST2")
                .setIndicator("未完成")
                .setContent(R.id.tab2)
        );

       /* tHost.addTab(tHost.newTabSpec("TEST3")
                .setIndicator("", getResources().getDrawable(R.drawable.jyjl))
                .setContent(R.id.tab3)
        );

        tHost.addTab(tHost.newTabSpec("TEST4")
                .setIndicator("", getResources().getDrawable(R.drawable.wdsx))
                .setContent(R.id.tab4)
        );*/

        /*--------------------------------tabhost选项卡结束--------------------------------*/

    }
}





