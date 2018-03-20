package com.ds.tire;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

/**
 * 已完成救援订单详情页面
 * 
 * @author Administrator
 * 
 */
public class OrderFinishActivity extends Activity {

	private LinearLayout order_finish_back;
	private TextView finish_order_number;
	private TextView finish_order_time;
	private String his_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_finish);
		findViewById();
		OnClickListener();
		his_id = SpUtils.getString(OrderFinishActivity.this, "his_id", null);
		Toast.makeText(OrderFinishActivity.this, his_id, 1).show();
		try {
			GetEndOrderTask getEndOrderrTask = new GetEndOrderTask(
					OrderFinishActivity.this, his_id);
			getEndOrderrTask.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void OnClickListener() {
		order_finish_back.setOnClickListener(new ClickListener());
	}

	private void findViewById() {
		order_finish_back = (LinearLayout) findViewById(R.id.order_finish_back);
		finish_order_number = (TextView) findViewById(R.id.finish_order_number);
		finish_order_time = (TextView) findViewById(R.id.finish_order_time);
	}

	private class ClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.order_finish_back:
				Intent intent = new Intent();
				intent.setClass(OrderFinishActivity.this,
						OrderHistoryActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		}

	}

	class GetEndOrderTask extends AsyncNetworkTask<String> {

		private String his_id;

		public GetEndOrderTask(Context context,String his_id) {
			super(context);
			this.his_id = his_id;
		}

		@Override
		protected String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.getEndOrderInfo(his_id);
		}

		@Override
		protected void handleResult(String result) {
			System.out.println("测试请求完成订单详情的结果" + result);// 此处的result就是json字符串
			try {
				JSONObject jsonObject = new JSONObject(result);
				String time = jsonObject.getString("time");//订单完成时间
				String name = jsonObject.getString("name");//救援车车牌号
				System.out.println("ccccccccc"+name);
				finish_order_time.setText(time);
				finish_order_number.setText(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}

	}
}
