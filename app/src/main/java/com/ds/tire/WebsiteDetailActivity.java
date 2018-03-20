package com.ds.tire;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.SpUtils;

public class WebsiteDetailActivity extends Activity {
	private LinearLayout wdsearch03_back;
	private TextView tv_sellername;
	private TextView tv_band;
	private TextView tv_representative;
	private TextView tv_phone1;
	private TextView tv_manager;
	private TextView tv_phone2;
	private TextView tv_xmanager;
	private TextView tv_phone3;
	private TextView tv_remark;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.website_detail);
		wdsearch03_back = (LinearLayout) findViewById(R.id.wdsearch03_back);
		tv_sellername = (TextView) findViewById(R.id.tv_sellername);
		tv_band = (TextView) findViewById(R.id.tv_band);
		tv_representative = (TextView) findViewById(R.id.tv_representative);
		tv_phone1 = (TextView) findViewById(R.id.tv_phone1);
		tv_manager = (TextView) findViewById(R.id.tv_manager);
		tv_phone2 = (TextView) findViewById(R.id.tv_phone2);
		tv_xmanager = (TextView) findViewById(R.id.tv_xmanager);
		tv_phone3 = (TextView) findViewById(R.id.tv_phone3);
		tv_remark = (TextView) findViewById(R.id.tv_remark);

		wdsearch03_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(WebsiteDetailActivity.this,
//						WebsiteListActivity.class);
//				startActivity(intent);
				finish();
			}
		});
		
		String id = SpUtils.getString(WebsiteDetailActivity.this, "id", null);//得到解析的id
		Log.i("TAG", "点击了哪个id页面："+id);
		try {
			GetDealersDetailed getDealersDetailed = new GetDealersDetailed(WebsiteDetailActivity.this, id);
			getDealersDetailed.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	
	
	class GetDealersDetailed extends AsyncNetworkTask<String> {
		private String id;

		public GetDealersDetailed(Context context, String id) {
			super(context);
			this.id = id;

		}

		@Override
		protected String doNetworkTask() {
			com.ds.tire.util.WebService ws = com.ds.tire.util.WebServiceFactory
					.getWebService();
			return ws.getDealersDetailed(id);
		}

		@Override
		protected void handleResult(String result) {
			Log.i("TAG", result);
			try {
				JSONObject jsonObject = new JSONObject(result);

				String sellername = jsonObject.getString("Name");
				String band = jsonObject.getString("Brand");
				String representative = jsonObject.getString("LPerson");
				String phone1 = jsonObject.getString("LPphone");
				String manager = jsonObject.getString("TMRPerson");
				String phone2 = jsonObject.getString("TMRPphone");
				String xmanager = jsonObject.getString("BTDPerson");
				String phone3 = jsonObject.getString("BTDPphone");
				String remark = jsonObject.getString("Remarks");

				tv_sellername.setText(sellername);
				tv_band.setText(band);
				tv_representative.setText(representative);
				tv_phone1.setText(phone1);;
				tv_manager.setText(manager);
				tv_phone2.setText(phone2);
				tv_xmanager.setText(xmanager);
				tv_phone3.setText(phone3);
				tv_remark.setText(remark);
				
				if(representative==null||representative.equals("null")){
					tv_representative.setText("暂无信息");
					
				}else{
					tv_representative.setText(representative);
				}
				
				if(phone1==null||phone1.equals("null")){
					tv_phone1.setText("暂无信息");
					
				}else{
					tv_phone1.setText(phone1);
				}
				
				if(manager==null||manager.equals("null")){
					tv_manager.setText("暂无信息");
					
				}else{
					tv_manager.setText(manager);
				}
				
				if(phone2==null||phone2.equals("null")){
					tv_phone2.setText("暂无信息");
					
				}else{
					tv_phone2.setText(phone2);
				}
				
				
				if(xmanager==null||xmanager.equals("null")){
					tv_xmanager.setText("暂无信息");
					
				}else{
					tv_xmanager.setText(xmanager);
				}
				
				if(phone3==null||phone3.equals("null")){
					tv_phone3.setText("暂无信息");
					
				}else{
					tv_phone3.setText(phone3);
				}
				
				if(remark==null||remark.equals("null")){
					tv_remark.setText("暂无信息");
					
				}else{
					tv_remark.setText(remark);
				}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
