package com.ds.tire;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SafeActivity extends Activity implements OnClickListener{

	LinearLayout back;
	private static final String TAG = "TAG";
	ListView safelist1;
	ListView safelist2;
	TextView title1;
	TextView title2;
	TextView item;
	private String[] name1=new String[]{"路面触点","定期检查轮胎","胎压","动平衡","四轮定位","轮胎调位","轮胎修补","冬季换胎","避免撞击障碍物","轮胎的存放及保养","避免轮胎空转","轮胎的使用建议"};
	private String[] name2=new String[]{"轮胎替换注意事项","轮胎安装注意事项","如何更换备胎 "};
	//private int[] images=new int[]{R.drawable.monkey,R.drawable.monkey,R.drawable.monkey,R.drawable.monkey};
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.i("TAG","测试！");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safe);
		findViewById();
		
		//title1.setOnClickListener(listener1);
		//title2.setOnClickListener(listener2);
		safelist1.setVisibility(View.VISIBLE);
		safelist2.setVisibility(View.VISIBLE);
        back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                finish();
            }
        });
        Log.i("TAG","测试！");
        //ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,R.layout.item,name1);	
        List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
		for(int i=0;i<name1.length;i++)
		{
			Map<String,Object> listItem=new HashMap<String,Object>();
			//listItem.put("header",images[i]);
			listItem.put("name",name1[i]);
			//listItem.put("desc",descs[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item1,
				new String[]{"name"},
				new int[]{R.id.name});
		   safelist1.setAdapter(simpleAdapter); 
		//ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,R.layout.item,name2);	
		   List<Map<String,Object>> listItems2=new ArrayList<Map<String,Object>>();
			for(int i=0;i<name2.length;i++)
			{
				Map<String,Object> listItem=new HashMap<String,Object>();
				//listItem.put("header",images[i]);
				listItem.put("name2",name2[i]);
				//listItem.put("desc",descs[i]);
				listItems2.add(listItem);
			}
			SimpleAdapter simpleAdapter2=new SimpleAdapter(this,listItems2,R.layout.item2,
					new String[]{"name2"},
					new int[]{R.id.name});
			   safelist2.setAdapter(simpleAdapter2); 
		   //safelist2.setAdapter(adapter2);   
		   safelist1.setOnItemClickListener(new OnItemClickListener()
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
						Intent intent = new Intent(SafeActivity.this,SafeDetailActivity.class);
						Bundle bundle=new Bundle();
						//intent.putExtra("id", position);
						bundle.putString("id", sid);
						bundle.putString("name", name1[cid-1]);
		                intent.putExtras(bundle);
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		   
		   safelist2.setOnItemClickListener(new OnItemClickListener()
			{
				public void onItemClick(AdapterView<?> parent,View view,int position,long id)
				{
					try {
						//final ProductSeries series = pseriesAdapter.getItem(position);
						Log.d(TAG, "传递系列编号！");
						System.out.println(position);
						int cid=position+13;
						String sid = Integer.toString(cid);//整形转化为字符型
						//id=series.getId();
						Log.i("刚刚获取的seriesId是：",sid);
						Intent intent = new Intent(SafeActivity.this,SafeDetailActivity.class);
						Bundle bundle=new Bundle();
						//intent.putExtra("id", position);
						bundle.putString("id", sid);
						bundle.putString("name", name2[cid-13]);
		                intent.putExtras(bundle);
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
 }
	private void findViewById() {
		back = (LinearLayout) findViewById(R.id.safe_back);
		title1 = (TextView) findViewById(R.id.title1);
		title2 = (TextView) findViewById(R.id.title2);
		//item = (TextView) findViewById(R.id.name1);
		safelist1= (ListView) findViewById(R.id.safelist1);
		safelist2= (ListView) findViewById(R.id.safelist2);
	}
//	OnClickListener listener1=new OnClickListener(){
//		
//		public void onClick(View v) {
//			safelist1.setVisibility(View.VISIBLE);
//			safelist2.setVisibility(View.GONE);
//			
//		}
//	};
//    OnClickListener listener2=new OnClickListener(){
//		
//		public void onClick(View v) {
//			safelist2.setVisibility(View.VISIBLE);
//			safelist1.setVisibility(View.GONE);
//			
//		}
//	};
	
	public void onClick(View v) {
		
		
	}

}
