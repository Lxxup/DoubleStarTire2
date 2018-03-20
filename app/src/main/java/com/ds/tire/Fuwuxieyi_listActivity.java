package com.ds.tire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ds.tire.bean.FuwuxieyiList;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.Constant;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class Fuwuxieyi_listActivity extends Activity {
	
	private LinearLayout fuwuxieyi_list_back;
	private ListView fuwuxieyi_list;
	private TextView tv_add;
	private List<FuwuxieyiList> fuwuxieyilist;
	private String uid = "uid";
	private String TAG = "TAG";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fuwuxieyi_list);
		
		fuwuxieyi_list_back = (LinearLayout) findViewById(R.id.fuwuxieyi_list_back);
		tv_add = (TextView) findViewById(R.id.tv_add);
		fuwuxieyilist = new ArrayList<FuwuxieyiList>();
		fuwuxieyi_list = (ListView) findViewById(R.id.fuwuxieyi_list);

		fuwuxieyi_list_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});

		tv_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Fuwuxieyi_listActivity.this, FuwuxieyiAddActivity.class);
				startActivity(intent);
				finish();
			}
		});		
		
		fuwuxieyi_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				SpUtils.setString(Fuwuxieyi_listActivity.this, Constant.CID, fuwuxieyilist
						.get(arg2).getCard());
				intent.setClass(Fuwuxieyi_listActivity.this,
						Fuwuxieyi_read.class);
				startActivity(intent);
//				finish();

			}

		});

		uid = SpUtils.getString(this, Constant.UID, "uid");
		Log.d(TAG, "UID" + uid);

		try {

			CardSelect cardSelect = new CardSelect(Fuwuxieyi_listActivity.this,
					uid);
			cardSelect.execute();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	class CardSelect extends AsyncNetworkTask<String> {

		private String uid;

		public CardSelect(Context context, String uid) {
			super(context);
			this.uid = uid;

		}

		@Override
		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.cardSelect(uid);// 接口
		}

		@Override
		public void handleResult(String result) {

			Log.i("TAG", "请求结果是:" + result);

			if (result == null || result.equals("")) {
				DialogUtils.alert(Fuwuxieyi_listActivity.this, R.drawable.logo,
						"警告", "服务器异常，请重试", "确定", null);
			} else if (result != null && !result.equals("")) {
				if (result.indexOf("html") != -1) {
					DialogUtils.alert(Fuwuxieyi_listActivity.this,
							R.drawable.logo, "警告", "网络异常，请检查您的网络再重试", "确定",
							null);
				} else {
					try {
						JSONArray jsonArray = new JSONArray(result);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							String card = temp.getString("cid");
							Log.i("TAG", card);
							String name = temp.getString("uName");
							Log.i("TAG", name);
							String buytime = temp.getString("buyTime");
							Log.i("TAG", buytime);
							
							fuwuxieyilist.add(new FuwuxieyiList(card, name, buytime));

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					List<HashMap<String, Object>> ddd = new ArrayList<HashMap<String, Object>>();
					for (int i = 0; i < fuwuxieyilist.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("card", fuwuxieyilist.get(i).getCard());
						map.put("name", fuwuxieyilist.get(i).getName());
						map.put("buytime", fuwuxieyilist.get(i).getBuyTime());
						ddd.add(map);
						Log.d(TAG, "获得的名字"+fuwuxieyilist.get(i).getName());
				}
					// 对解析后的数据进行装载适配器
					SimpleAdapter adapter = new SimpleAdapter(
							Fuwuxieyi_listActivity.this, ddd,
							R.layout.fuwuxieyi_list_item, new String[] { "card","name",
									"buytime" }, new int[] { R.id.fu_card ,R.id.fu_name,
									R.id.fu_buytime });
					fuwuxieyi_list.setAdapter(adapter);
				}
			}

		}
	}

}
