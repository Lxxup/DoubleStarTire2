package com.ds.tire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.os.Build;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class ProductSeriesActivity extends Activity {
	private static final String TAG = "TAG";
	LinearLayout back;
	
	private String[] names=new String[]{"双星","一路发","路路顺","新远征","劲倍力","骏固","创新成本"};
	
	private int[] images=new int[]{R.drawable.shuangxing,R.drawable.yilufa,R.drawable.lulushun,R.drawable.xinyuanzheng,R.drawable.jinbeili,R.drawable.jungu,R.drawable.chengben};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.series);
		back = (LinearLayout) findViewById(R.id.safe_back);
        back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                finish();
            }
        });
		List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
		for(int i=0;i<names.length;i++)
		{
			Map<String,Object> listItem=new HashMap<String,Object>();
			listItem.put("header",images[i]);
			listItem.put("personName",names[i]);
			//listItem.put("desc",descs[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.items,
				new String[]{"header","personName"},
				new int[]{R.id.header,R.id.name,});
		ListView list=(ListView)findViewById(R.id.mylist);
		list.setAdapter(simpleAdapter);
		
		list.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent,View view,int position,long id)
			{
				try {
					//final ProductSeries series = pseriesAdapter.getItem(position);
					Log.d(TAG, "传递系列编号！");
					System.out.println(position);
					int cid=position+1;
					String sid = Integer.toString(cid);//整形转化为字符型
					//id=series.getId();
					Log.i("刚刚获取的seriesId是：",sid);
					Intent intent = new Intent(ProductSeriesActivity.this,ProductDetailActivity.class);
					Bundle bundle=new Bundle();
					//intent.putExtra("id", position);
					bundle.putString("id", sid);
	                intent.putExtras(bundle);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		list.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			{
				System.out.println(names[position]+"被选中了！");
			}
			public void onNothingSelected(AdapterView<?> parent){}
		});
	}
	
}

