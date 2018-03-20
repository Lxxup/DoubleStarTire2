//package com.ds.tire;
//
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//
//import com.ds.tire.bean.JingXiaoShang;
//import com.ds.tire.util.AsyncNetworkTask;
//import com.ds.tire.util.DialogUtils;
//import com.ds.tire.util.SpUtils;
//import com.ds.tire.util.WebService;
//import com.ds.tire.util.WebServiceFactory;
//
//public class WebsiteListActivity extends Activity {
//	private LinearLayout wdsearch02_back;
//
//	private ListView lv_list;
//	private List<JingXiaoShang> jingxiaoshang;
//	private String province;
//
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.website_list);
//		wdsearch02_back = (LinearLayout) findViewById(R.id.wdsearch02_back);
//		jingxiaoshang = new ArrayList<JingXiaoShang>();
//		lv_list = (ListView) findViewById(R.id.lv_list);
//
//		wdsearch02_back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// Intent intent = new Intent();
//				// intent.setClass(WebsiteListActivity.this,
//				// WebsiteActivity.class);
//				// startActivity(intent);
//				finish();
//			}
//		});
//
//		lv_list.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Intent intent = new Intent();
//				SpUtils.setString(WebsiteListActivity.this, "id", jingxiaoshang
//						.get(arg2).getId());
//				intent.setClass(WebsiteListActivity.this,
//						WebsiteDetailActivity.class);
//				startActivity(intent);
//				// finish();
//
//			}
//
//		});
//
//		province = SpUtils.getString(WebsiteListActivity.this, "province", null);
//
//		try {
//
//			String provinces = URLEncoder.encode(province, "utf-8");
//			FindAddress findAddress = new FindAddress(WebsiteListActivity.this,
//					provinces);
//			findAddress.execute();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//	}
//
//	class FindAddress extends AsyncNetworkTask<String> {
//
//		private String province;
//
//		public FindAddress(Context context, String province) {
//			super(context);
//			this.province = province;
//
//		}
//
//		@Override
//		public String doNetworkTask() {
//			WebService ws = WebServiceFactory.getWebService();
//			return ws.getDealersInfo(province);// 接口
//		}
//
//		@Override
//		public void handleResult(String result) {
//
//			Log.i("TAG", "请求结果是:" + result);
//
//			if (result == null || result.equals("")) {
//				DialogUtils.alert(WebsiteListActivity.this, R.drawable.logo,
//						"警告", "服务器异常，请重试", "确定", null);
//			} else if (result != null && !result.equals("")) {
//				if (result.indexOf("html") != -1) {
//					DialogUtils.alert(WebsiteListActivity.this,
//							R.drawable.logo, "警告", "网络异常，请检查您的网络再重试", "确定",
//							null);
//				} else {
//					try {
//						JSONArray jsonArray = new JSONArray(result);
//						for (int i = 0; i < jsonArray.length(); i++) {
//							JSONObject temp = (JSONObject) jsonArray.get(i);
//							String id = temp.getString("Id");
//							String name = temp.getString("Name");
//							String phone = temp.getString("LPphone");
//							if(phone==null||phone.equals("null")){
//								phone=("暂无电话信息");
//								
//							}else{
//								phone=temp.getString("LPphone");
//							}
//
//
//							jingxiaoshang
//									.add(new JingXiaoShang(name, phone, id));
//
//						}
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					Log.i("TAG", result);
//					List<HashMap<String, Object>> ddd = new ArrayList<HashMap<String, Object>>();
//					for (int i = 0; i < jingxiaoshang.size(); i++) {
//						HashMap<String, Object> map = new HashMap<String, Object>();
//						map.put("name", jingxiaoshang.get(i).getName());
//						map.put("phone", jingxiaoshang.get(i).getPhone());
//						// map.put("id", jingxiaoshang.get(i).getId());
//						ddd.add(map);
//					}
//					// 对解析后的数据进行装载适配器
//					SimpleAdapter adapter = new SimpleAdapter(
//							WebsiteListActivity.this, ddd,
//							R.layout.website_list_item, new String[] { "name",
//									"phone" }, new int[] { R.id.tv_uname,
//									R.id.tv_uphone });
//					lv_list.setAdapter(adapter);
//				}
//			}
//
//		}
//	}
//
//}
