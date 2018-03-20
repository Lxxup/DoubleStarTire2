package com.ds.tire;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
//import com.ds.tire.CopyOfWebsiteSelectActivity.RegisterTask;
import com.ds.tire.bean.JingXiaoShang;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.MyApplication;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class WebsiteSelectActivity extends Activity {
	private LinearLayout common_head_back;
	private Button bt_search;
	private Spinner sp_servicetype;
	private String province;
	private double longitude;
	private double latitude;
	private String servicetype;
	BDLocationListener locationListener;
	private String TAG = "TAG";

	private ListView lv_list;
	private List<JingXiaoShang> jingxiaoshang;
	private SimpleAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.websiteselect);
		MyApplication.startLocation();
		common_head_back = (LinearLayout) findViewById(R.id.common_head_back);
		bt_search = (Button) findViewById(R.id.bt_search);
		sp_servicetype = (Spinner) findViewById(R.id.sp_servicetype);
		jingxiaoshang = new ArrayList<JingXiaoShang>();
		lv_list = (ListView) findViewById(R.id.lv_list);

		Log.d(TAG, "chnhgg111");

		common_head_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		bt_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				jingxiaoshang.clear();
				BDLocation location = MyApplication.getLastKnownLocation();
				
				if (location == null || location.getLatitude() < 1
						|| location.getLongitude() < 1) {
					if (locationListener == null) {
						Log.d("TAG", "registListener.....");
						locationListener = new MyLocationListener();
						MyApplication.getInstance().registerLocationListener(
								locationListener);
					}

					return;
				}

				servicetype = sp_servicetype.getSelectedItem().toString();

				Log.d("TAG", "获得的类型是" + servicetype);

				longitude = location.getLongitude();
				latitude = location.getLatitude();
				province = location.getProvince();
				Log.d("TAG", "获得的经度1122是" + longitude);
				Log.d("TAG", "获得的纬度1122是：" + latitude);
				Log.d("TAG", "获得的省份1122是：" + location.getProvince());
				uplocation(servicetype, province, longitude, latitude);

			}

		});

	}

	private void uplocation(String servicetype, String province,
			double longitude, double latitude) {
		RegisterTask registerTask;
		try {
			registerTask = new RegisterTask(WebsiteSelectActivity.this,
					province, longitude, latitude, servicetype);
			registerTask.showProgressDialog("提示", "正在搜索...");
			registerTask.execute();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			Log.d("TAG", location.getAddrStr());
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			province = location.getProvince();
			MyApplication.getInstance().unregisterLocationListener(
					locationListener);
			locationListener = null;

		}

		public void onReceivePoi(BDLocation location) {

		}
	}

	class RegisterTask extends AsyncNetworkTask<String> {

		private String servicetype;
		private String province;
		private double longitude;
		private double latitude;

		public RegisterTask(Context context, String province, double longitude,
				double latitude, String servicetype)
				throws UnsupportedEncodingException {
			super(context);
			this.province = province;
			this.longitude = longitude;
			this.latitude = latitude;
			this.servicetype = servicetype;

		}

		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws
					.getDealersInfo(province, longitude, latitude, servicetype);
		}

		@Override
		public void handleResult(String result) {
			Log.i("TAG", "请求结果是:" + result);

			if (result == null || result.equals("") ) {
				Toast.makeText(WebsiteSelectActivity.this, "服务器异常，请重试~",
						Toast.LENGTH_SHORT).show();
			} 
			else if (result.equals("[]")) {
				Toast.makeText(WebsiteSelectActivity.this, "您周边暂无相关网点",
						Toast.LENGTH_SHORT).show();
			}
			else if (result != null && !result.equals("")) {
				if (result.indexOf("html") != -1) {
					Toast.makeText(WebsiteSelectActivity.this,
							"网络异常，请检查您的网络再重试~", Toast.LENGTH_SHORT).show();

				} else {
					try {
						JSONArray jsonArray = new JSONArray(result);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = (JSONObject) jsonArray.get(i);
							// String id = temp.getString("Id");
							String name = temp.getString("Name");
							String province = temp.getString("province");
							String fcity = temp.getString("FCity");
							String scity = temp.getString("SCity");
							String address = province + fcity + scity;
							Log.i("TAG", "网点地址:" + address);
							// String phone = temp.getString("LPphone");
							if (name == null || name.equals("null")) {
								name = ("暂无网点信息");

							} else {
								name = temp.getString("Name");
							}

							jingxiaoshang.add(new JingXiaoShang(name, address));

						}
						Log.i("TAG", "shi" + result);
						List<HashMap<String, Object>> ddd = new ArrayList<HashMap<String, Object>>();
						for (int i = 0; i < jingxiaoshang.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("name", jingxiaoshang.get(i).getName());
							map.put("address", jingxiaoshang.get(i)
									.getAddress());
							// map.put("id", jingxiaoshang.get(i).getId());
							Log.i("TAG", "sh22i"
									+ jingxiaoshang.get(i).getName()
									+ jingxiaoshang.get(i).getAddress());
							ddd.add(map);

						}
						Log.i("TAG", "sh2ddd" + ddd.size() + ddd.toString()
								+ ddd.get(0));
						// 对解析后的数据进行装载适配器
						adapter = new SimpleAdapter(WebsiteSelectActivity.this,
								ddd, R.layout.website_list_item, new String[] {
										"name", "address" }, new int[] {
										R.id.tv_uname, R.id.tv_address });
						lv_list.setAdapter(adapter);
						setListViewHeightBasedOnChildren(lv_list);
					
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		}
	}
	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ 1;
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
	@Override
	protected void onDestroy() {
		if (locationListener != null) {
			MyApplication.getInstance().unregisterLocationListener(
					locationListener);
			locationListener = null;
		}

		MyApplication.stopLocation();
		super.onDestroy();
	}

}
