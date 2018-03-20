package com.ds.tire;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class ZhiliangFanActivity extends Activity {
	private Button bt_back;
	private Button btn2;
	private LinearLayout fan_back;
	private TextView tv_card;
	private TextView tv_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zhiliang_fan);
		
		btn2 = (Button)findViewById(R.id.btn2);
		fan_back = (LinearLayout) findViewById(R.id.fan_back);
		tv_card = (TextView) findViewById(R.id.tv_card);
		tv_name = (TextView) findViewById(R.id.tv_name);		

		try {
			SelecUserInfo selecUserInfo = new SelecUserInfo(ZhiliangFanActivity.this, SpUtils.getString(this, SpUtils.ACCOUNT, ""));
			selecUserInfo.execute();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		fan_back.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	class SelecUserInfo extends AsyncNetworkTask<String> {
		private String uid;

		public 	SelecUserInfo(Context context, String uid) {
			super(context);
			this.uid = uid;

		}

		@Override
		protected String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.selecUserInfo(uid);
			
		}

		protected void handleResult(String result) {
			if(!TextUtils.isEmpty(result)){
				Log.i("TAG", result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					
					String card = jsonObject.getString("card_num");
					String name = jsonObject.getString("username");

					tv_card.setText(card);
					tv_name.setText(name);

				} 
				catch (Exception e) {
					e.printStackTrace();
					Log.d("TAG", "error:"+e.getMessage());
				}
			}else {
				Toast.makeText(ZhiliangFanActivity.this, "网络异常", Toast.LENGTH_LONG).show();
			}
			btn2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
			
		}		
		
	}
}
