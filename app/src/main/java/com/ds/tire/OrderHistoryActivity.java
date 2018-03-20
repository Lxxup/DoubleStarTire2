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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ds.tire.dao.Order;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class OrderHistoryActivity extends Activity {

	private ListView order_list;// 定义订单列表的listView控件
	private List<Order> orders;// 定义订单集合
	private Button refresh_order;
	private LinearLayout order_history_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_history);
		orders = new ArrayList<Order>();
		findViewById();
		OnClickListener();
		refreshOrder();
		order_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				String his_id = orders.get(arg2).getHis_id().toString();
				SpUtils.setString(OrderHistoryActivity.this, "his_id", his_id);
				int step = orders.get(arg2).getStatus();
				SpUtils.setInteger(OrderHistoryActivity.this, "steps", step);
				System.out.println("您点击的订单号为："+his_id);
				if(step ==1){
					//1状态，后台还未分配救援车，提示用户等待即可
					Toast.makeText(OrderHistoryActivity.this, "正在分配救援车，请耐心等待",
							1).show();
				}else if(step ==2){
					//2 状态，进入的查看已分配好的救援车的一些信息。
					intent.setClass(OrderHistoryActivity.this,
							OrderTest.class);
					startActivity(intent);
					finish();
				}else if(step == 3){
					//3 状态，进入的是待评价的页面。此时需要需要用户进行评价
					intent.setClass(OrderHistoryActivity.this,
							OrderTest.class);
					startActivity(intent);
					finish();
				}else{
					//4、5两个状态，进入的是已完成救援订单的详情页面
					intent.setClass(OrderHistoryActivity.this,
							OrderFinishActivity.class);
					startActivity(intent);
					finish();
				}
			}

		});
	}

	/**
	 * 请求救援订单列表信息
	 */
	private void refreshOrder() {
		String uid = SpUtils
				.getString(OrderHistoryActivity.this, "account", "");
		try {
			OrderTask orderTask = new OrderTask(OrderHistoryActivity.this, uid);
			orderTask.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void OnClickListener() {
		order_history_back.setOnClickListener(new ClickListener());
		refresh_order.setOnClickListener(new ClickListener());
	}

	private void findViewById() {
		order_list = (ListView) findViewById(R.id.order_list);
		order_history_back = (LinearLayout) findViewById(R.id.order_history_back);
		refresh_order = (Button) findViewById(R.id.refresh_order);
	}

	private class ClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.order_history_back:
				finish();
				break;
			case R.id.refresh_order:
				finish();
				Intent intent = new Intent();
				intent.setClass(OrderHistoryActivity.this,
						OrderHistoryActivity.class);
				startActivity(intent);
				
				break;
			default:
				break;
			}
		}

	}

	class OrderTask extends AsyncNetworkTask<String> {

		private String uid;

		public OrderTask(Context context, String uid) {
			super(context);
			this.uid = uid;

		}

		@Override
		protected String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.getOrderInfo(uid);
		}

		@Override
		protected void handleResult(String result) {
			System.out.println("测试请求订单列表信息的结果" + result);// 此处的result就是json字符串
			// 下面对json数据进行解析
			try {
				JSONArray array = new JSONArray(new String(result));
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					String his_id = item.getString("his_id");
					int status = item.getInt("steps");
					String time = item.getString("time");
					orders.add(new Order(his_id, status, time));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			List<HashMap<String, Object>> ddd = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < orders.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String order_status = "";
				switch (orders.get(i).getStatus()) {
				case 1:
					order_status = "正在分配救援车";
					break;
				case 2:
					order_status = "救援车正在路上";
					break;
				case 3:
					order_status = "等待用户去评价";
					break;
				case 4:
					order_status = "救援任务已完成";
					break;
				case 5:
					order_status = "救援任务已完成";
					break;
				default:
					break;
				}
				map.put("time", orders.get(i).getTime());
				map.put("steps", order_status);
				ddd.add(map);
			}
			// 对解析后的数据进行装载适配器
			SimpleAdapter adapter = new SimpleAdapter(
					OrderHistoryActivity.this, ddd, R.layout.order_list_item,
					new String[] { "time", "steps" }, new int[] { R.id.tv_time,
							R.id.tv_steps });
			order_list.setAdapter(adapter);
		}

	}
}
