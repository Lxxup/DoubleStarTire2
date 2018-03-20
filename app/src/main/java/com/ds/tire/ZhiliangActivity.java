package com.ds.tire;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.Constant;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class ZhiliangActivity extends Activity {

	private Button btn1;
	private String TAG="TAG";
	private TextView tv_username;
	private TextView tv_tel;
	private TextView tv_address;
	private TextView tv_buy_time;
	private TextView tv_che_xinghao;
	private LinearLayout zheng_back;
	private TextView tv_taiHao;
	private String named = "0";
	private String uid = "uid";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zhiliang_zheng);
		uid=SpUtils.getString(this, Constant.UID, "uid");
		Log.d(TAG,"UID"+ uid);
		uploadImg(uid);
//        named=SpUtils.getString(this, Constant.NAMED,"0");
//        if(named.equals("0"))
//        {
//        	Intent intent = new Intent();
//			intent.setClass(ZhiliangActivity.this, FuwuxieyiActivity.class);
//			startActivity(intent);
//        }
		
//		Log.d(TAG,"UIDDDD"+ SpUtils.getString(this, SpUtils.ACCOUNT, ""));
		btn1 = (Button)findViewById(R.id.btn1);
		tv_username = (TextView) findViewById(R.id.username);
		tv_tel = (TextView) findViewById(R.id.tel);
		tv_address = (TextView) findViewById(R.id.address);
		zheng_back = (LinearLayout) findViewById(R.id.zheng_back);

		tv_buy_time = (TextView) findViewById(R.id.buy_time);
		tv_che_xinghao = (TextView) findViewById(R.id.che_xinghao);
		tv_taiHao = (TextView) findViewById(R.id.taiHao);
		
		try {                                         //(ZhiliangActivity.this, SpUtils.ACCOUNT);
			SelecUserInfo selecUserInfo = new SelecUserInfo(ZhiliangActivity.this, SpUtils.getString(this, SpUtils.ACCOUNT, ""));
			Log.d("uidshiddd", SpUtils.getString(this, SpUtils.ACCOUNT, ""));
			selecUserInfo.execute();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		btn1.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(ZhiliangActivity.this, ZhiliangFanActivity.class);
				startActivity(intent);
			
			}
		});
		
//		zheng_back.setOnClickListener(new OnClickListener()
//		{
//
//			public void onClick(View v) {
//				finish();
//			}
//		});
		zheng_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void uploadImg(String named)
	{
		Jdgifnamed task = new Jdgifnamed(this, named);
		task.execute();

	}
	
	class Jdgifnamed extends AsyncNetworkTask<String> {
				
		String named;

		public Jdgifnamed(Context context, String named) {
			super(context);
			// TODO Auto-generated constructor stub
			this.named = named;
		}
		
		@Override
		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.ifnamed(named);
		}
		@Override
		public void handleResult(String result) {
			Log.d("TAG", "未签名jiehuo"+result);
			
			if (result != null && !result.equals("")) {
				if (result.equals("0")) {
					
					Intent intent = new Intent();
					intent.setClass(ZhiliangActivity.this, FuwuxieyiStartActivity.class);
					startActivity(intent);
					Log.d("TAG", "未签名" +result );
										
				}else if(result.equals("1")){
					
					Log.d("TAG", "已签名" +result );
				} 
		
	       }
		}
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

		@Override
		protected void handleResult(String result) {
			if(!TextUtils.isEmpty(result)){
				Log.i("TAG", result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					
					String username = jsonObject.getString("username");
					String tel = jsonObject.getString("tel");
					String address = jsonObject.getString("address");
					String buy_time = jsonObject.getString("buytime");
					String che_xinghao = jsonObject.getString("chexing");
					String taiHao = jsonObject.getString("tirecode");

					tv_username.setText(username);
					tv_tel.setText(tel);
					tv_address.setText(address);
					tv_buy_time.setText(buy_time);
					tv_che_xinghao.setText(che_xinghao);
					tv_taiHao.setText(taiHao);

				} 
				catch (Exception e) {
					e.printStackTrace();
					Log.d("TAG", "error:"+e.getMessage());
				}
			}else {
				Toast.makeText(ZhiliangActivity.this, "网络异常", Toast.LENGTH_LONG).show();
			}
			
		}

	}
}
