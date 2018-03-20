//package com.ds.tire;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.ds.tire.util.MyApplication;
//import com.ds.tire.util.SpUtils;
//
//public class WebsiteActivity extends Activity {
//	private LinearLayout wdsearch_back;
//	private Button bt_get;
//	private Button bt_next;
//	private EditText et_province;
//	private String province;
//	// private LocationData myLocationData = null;// 用户位置信息
//	BDLocationListener locationListener;
//	LocationClient client;
//	// 用户点相关
//	private String userId = null;
//
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.website);
//		MyApplication.startLocation();
//		wdsearch_back = (LinearLayout) findViewById(R.id.wdsearch_back);
//		bt_get = (Button) findViewById(R.id.bt_get);
//		bt_next = (Button) findViewById(R.id.bt_next);
//		et_province = (EditText) findViewById(R.id.et_province);
//		//initMapView();
//		wdsearch_back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//
//		bt_get.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				BDLocation location = MyApplication.getLastKnownLocation();
//				if (location == null || location.getLatitude() < 1
//						|| location.getLongitude() < 1) {
//					if(locationListener==null){
//						Log.d("TAG", "registListener.....");
//						locationListener = new MyLocationListener();
//						MyApplication.getInstance().registerLocationListener(
//								locationListener);
//					}
//					
//					return;
//				}
//
//				et_province.setText(location.getProvince());
//				
//			}
//		});
//
//		et_province.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				province = et_province.getText().toString().trim();
//				if (province == null || province.equals("")) {
//					Toast.makeText(WebsiteActivity.this, "请输入省份", 1).show();
//					bt_next.setEnabled(false);
//				} else {
//					bt_next.setEnabled(true);
//					SpUtils.setString(WebsiteActivity.this, "province", province);
//				}
//			}
//		});
//
//		bt_next.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				Intent intent = new Intent();
//				intent.setClass(WebsiteActivity.this,WebsiteListActivity.class);
//				startActivity(intent);
////				finish();
//			}
//		});
//
//	}
//
//	/**
//	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
//	 */
//	public class MyLocationListener implements BDLocationListener {
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			if (location == null)
//				return;
//			Log.d("TAG", location.getAddrStr());
//			et_province.setText(location.getProvince());
//			MyApplication.getInstance().unregisterLocationListener(
//					locationListener);
//			locationListener = null;
//
//		}
//
//		public void onReceivePoi(BDLocation location) {
//
//		}
//	}
///*
//	private void initMapView() {
//		// myLocationData = new LocationData();/* 我的位置 */
//
//		
//
//		
////		 * BDLocation location = MyApplication.getLastKnownLocation(); if
////		 * (location == null) { Log.i("TAG", "空的地址"); } else {
////		 * myLocationData.latitude = location.getLatitude();
////		 * myLocationData.longitude = location.getLongitude();
////		 * myLocationData.accuracy = location.getRadius();
////		 * myLocationData.direction = location.getDerect(); Log.i("TAG",
////		 * location.getAddrStr());
////		 * 
////		 * }
//		 
//	
//
//	@Override
//	protected void onDestroy() {
//		if (locationListener != null){
//			MyApplication.getInstance().unregisterLocationListener(
//					locationListener);
//			locationListener=null;
//		}
//			
//		MyApplication.stopLocation();
//		super.onDestroy();
//	}
//
//}
