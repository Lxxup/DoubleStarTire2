package com.ds.tire;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.AutoZoomAndLocation;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.MapUtils;
import com.ds.tire.util.MyApplication;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class OrderTest extends Activity {

	private MapView mapView = null;// 地图主控件
	private MapController mapController = null;// 用MapController完成地图控制
	private MyLocationOverlay myLocationOverlay = null;// 定位图层
	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public LocationListener locationListener = new LocationListener();

	private Button history_order = null;// 历史订单按钮
	private Button callButton = null;// 拨打司机电话按钮
	private Button taxiOverButton = null;// 评论按钮
	private LinearLayout map_back = null;// 返回控件

	private double dis = 0;// 距离变量

	private TextView taxi_number_text_view;// 车牌号
	private TextView distance_text_view;// 距离
	private String his_id;// 订单号
	private String rlongitude;// 救援车的经度
	private String rlatitude;// 救援车的纬度
	private double drlongitude;// 救援车的double类型的经度
	private double drlatitude;// 救援车的double类型的纬度

	// 评价页面的控件
	private RadioGroup rg_handle;
	private RadioGroup rg_please;
	private RadioGroup rg_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_test);
		locationListener = new LocationListener();
		his_id = SpUtils.getString(this, "his_id", null);
		findViewById();
		OnClickListener();
		initMapView();
	}

	/**
	 * 初始化地图
	 */
	private void initMapView() {
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setLongClickable(true); // 允许长点击
		mapView.setBuiltInZoomControls(true); // 设置内建缩放工具
		myLocationOverlay = new MyLocationOverlay(mapView);
		myLocationOverlay.enableCompass();
		mapView.getOverlays().add(myLocationOverlay);
		// 定位我的位置
		BDLocation l = MyApplication.getLastKnownLocation();
		if (l == null) {
			Toast.makeText(OrderTest.this, "暂无法定位您的位置！", 1).show();
		}
		mapController = mapView.getController();
		mapController.setZoom(mapView.getMaxZoomLevel());
		mapController.enableClick(true); // 允许点击
		mapView.refresh();
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class LocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null)
				return;
			LocationData ld = myLocationOverlay.getMyLocation();
			if (ld == null) {
				try {
					ld = new LocationData();
					GetOnOrderTask getOnOrderrTask;
					try {
						getOnOrderrTask = new GetOnOrderTask(OrderTest.this,
								his_id);
						getOnOrderrTask.execute();

					} catch (Exception e) {
						e.printStackTrace();
					}

					ld.latitude = location.getLatitude();
					ld.longitude = location.getLongitude();
					ld.accuracy = location.getRadius();
					ld.direction = location.getDerect();
					myLocationOverlay.setData(ld);
					drlongitude = Double.parseDouble(SpUtils.getString(
							OrderTest.this, "longitude", rlongitude));
					drlatitude = Double.parseDouble(SpUtils.getString(
							OrderTest.this, "latitude", rlatitude));
					System.out.println("xxxxxxxxxxxx" + (ld.latitude) * 1E6);
					update();
				} catch (Exception e) {
				}

			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void OnClickListener() {
		map_back.setOnClickListener(new ClickListener());
		history_order.setOnClickListener(new ClickListener());
		callButton.setOnClickListener(new ClickListener());
		taxiOverButton.setOnClickListener(new ClickListener());
	}

	private void findViewById() {
		map_back = (LinearLayout) findViewById(R.id.map_back);
		history_order = (Button) findViewById(R.id.history_order);
		taxi_number_text_view = (TextView) findViewById(R.id.taxi_number_text_view);
		distance_text_view = (TextView) findViewById(R.id.distance_text_view);
		callButton = (Button) findViewById(R.id.call_button);
		taxiOverButton = (Button) findViewById(R.id.taxi_over_button);

	}

	/**
	 * 按钮监听器
	 * 
	 * @author WPH
	 * 
	 */

	private class ClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.map_back:
				finish();
				break;
			case R.id.history_order:
				intent = new Intent();
				intent.setClass(OrderTest.this, OrderHistoryActivity.class);
				startActivity(intent);
				break;
			case R.id.call_button:
				intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ SpUtils.getString(OrderTest.this, "phone", null)));
				startActivity(intent);
				break;
			case R.id.taxi_over_button:
				int step = SpUtils.getInteger(OrderTest.this, "steps", 0);
				if (step == 2) {
					Toast.makeText(OrderTest.this, "救援任务还未结束，您暂时不能评价！", 1)
							.show();
				} else {
					commentDialog();
				}
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 请求救援 异步访问接口
	 * 
	 * @author Administrator
	 * 
	 */
	class GetOnOrderTask extends AsyncNetworkTask<String> {

		private String his_id;

		public GetOnOrderTask(Context context, String his_id) {
			super(context);
			this.his_id = his_id;
		}

		@Override
		protected String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.getOnOrderInfo(his_id);
		}

		@Override
		protected void handleResult(String result) {
			System.out.println("测试请求完成订单详情的结果" + result);// 此处的result就是json字符串
			try {
				JSONObject jsonObject = new JSONObject(result);
				String phone = jsonObject.getString("phone");
				String name = jsonObject.getString("name");// 救援车车牌号
				rlongitude = jsonObject.getString("longitude");// 救援车的经度
				rlatitude = jsonObject.getString("latitude");// 救援车的纬度
				taxi_number_text_view.setText(name);
				System.out.println("车牌号是" + name);
				SpUtils.setString(OrderTest.this, "longitude", rlongitude);
				SpUtils.setString(OrderTest.this, "latitude", rlatitude);
				SpUtils.setString(OrderTest.this, "phone", phone);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		MyApplication.startLocation();
	}

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.stopLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		MyApplication.getInstance().registerLocationListener(locationListener);

	}

	@Override
	protected void onPause() {
		MyApplication.getInstance()
				.unregisterLocationListener(locationListener);
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mapView.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 覆盖物图层
	 * 
	 * @author WPH
	 * 
	 */
	class MyOverlay extends ItemizedOverlay<MyOverlayItem> {
		private List<MyOverlayItem> items = new ArrayList<MyOverlayItem>();

		public MyOverlay(Drawable marker) {
			super(marker, mapView);
			populate();
		}

		protected boolean onTap(int index) {
			super.onTap(index);
			return false;
		}

		public boolean onTap(GeoPoint pt, MapView mapView) {
			super.onTap(pt, mapView);
			return false;
		}

		@Override
		protected MyOverlayItem createItem(int i) {
			return items.get(i);
		}

		@Override
		public int size() {
			return items.size();
		}

		public void addItem(MyOverlayItem item) {
			items.add(item);
			populate();
		}

		public void removeItem(int index) {
			items.remove(index);
			populate();
		}
	}

	/**
	 * 覆盖物图层项，表示一辆空载的出租车
	 * 
	 * @author WPH
	 * 
	 */
	class MyOverlayItem extends OverlayItem {
		public MyOverlayItem(GeoPoint point, String title, String snippet) {
			super(point, title, snippet);
		}
	}

	/**
	 * 更新地图
	 */
	private void update() {
		// 清空覆盖物
		mapView.getOverlays().clear();
		mapView.refresh();
		// 覆盖物
		MyOverlay mo = new MyOverlay(null);
		GeoPoint taxiGeoPoint = null;
		// 出租车覆盖物
		System.out.println("yyyyyyyyyyyyyyyyyyyy" + drlatitude);
		if (drlatitude != 0 && drlongitude != 0) {
			taxiGeoPoint = MapUtils.newGeoPoint(drlatitude, drlongitude);
			Drawable vehicleMarker = getResources().getDrawable(
					R.drawable.vehiclemarker);
			MyOverlayItem item = new MyOverlayItem(taxiGeoPoint, "item", "item");
			item.setMarker(vehicleMarker);
			mo.addItem(item);
			mapView.getOverlays().add(mo);
		}
		LocationData ld = myLocationOverlay.getMyLocation();
		if (ld != null) {
			mapView.getOverlays().add(myLocationOverlay);
		}
		if (ld != null && drlatitude != 0 && drlongitude != 0) {
			AutoZoomAndLocation.In in = new AutoZoomAndLocation.In();
			in.points.add(taxiGeoPoint);
			in.points.add(MapUtils.newGeoPoint(ld.latitude, ld.longitude));
			AutoZoomAndLocation.Out out = MapUtils.autoZoomAndLocation(in);
			mapController.setCenter(out.center);
			mapController.zoomToSpan(out.latitudeSpanE6, out.longitudeSpanE6);
			dis = MapUtils.getDistance(drlatitude, drlongitude, ld.latitude,
					ld.longitude);
			distance_text_view.setText(dis + "米");
		} else if (ld == null) {
			mapController.setCenter(taxiGeoPoint);
		} else {
			mapController.setCenter(MapUtils.newGeoPoint(ld.latitude,
					ld.longitude));
		}

		mapView.refresh();
	}

	public void populate() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 评价对话框
	 */
	private void commentDialog() {
		Builder builder = new android.app.AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		LinearLayout comment_dialog = (LinearLayout) getLayoutInflater()
				.inflate(R.layout.uderevalute, null);
		builder.setView(comment_dialog);
		builder.setTitle("请为接线员评价");
		rg_handle = (RadioGroup) comment_dialog.findViewById(R.id.rg_handle);
		rg_please = (RadioGroup) comment_dialog.findViewById(R.id.rg_please);
		rg_money = (RadioGroup) comment_dialog.findViewById(R.id.rg_money);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int hlen = rg_handle.getChildCount();
				int plen = rg_please.getChildCount();
				int mlen = rg_money.getChildCount();
				String hmsgString = "";
				String pmsgString = "";
				String mmsgString = "";
				for (int i = 0; i < hlen; i++) {
					RadioButton radioButton = (RadioButton) rg_handle
							.getChildAt(i);
					if (radioButton.isChecked()) {
						hmsgString = radioButton.getText().toString();
						break;
					}
				}
				for (int i = 0; i < plen; i++) {
					RadioButton radioButton = (RadioButton) rg_please
							.getChildAt(i);
					if (radioButton.isChecked()) {
						pmsgString = radioButton.getText().toString();
						break;
					}
				}
				for (int i = 0; i < mlen; i++) {
					RadioButton radioButton = (RadioButton) rg_money
							.getChildAt(i);
					if (radioButton.isChecked()) {
						mmsgString = radioButton.getText().toString();
						break;
					}
				}
				System.out.println(hmsgString+pmsgString+mmsgString);
				// 此处填写用户对接线员评价的异步任务
				int role = 1;
				int feed1, feed2, feed3;
				if (hmsgString == "是") {
					feed1 = 1;
				} else {
					feed1 = 0;
				}
				if (pmsgString == "是") {
					feed2 = 1;
				} else {
					feed2 = 0;
				}
				if (mmsgString == "是") {
					feed3 = 1;
				} else {
					feed3 = 0;
				}
				try {
					CommentTask commentTask = new CommentTask(OrderTest.this,
							his_id, role, feed1, feed2, feed3);
					commentTask.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dialog.dismiss();
				commetDilog2();

			}
		}).create().show();

	}

	protected void commetDilog2() {
		Builder builder2 = new android.app.AlertDialog.Builder(this);
		builder2.setIcon(R.drawable.ic_launcher);
		builder2.setTitle("请为司机评价");
		LinearLayout comment_dialog = (LinearLayout) getLayoutInflater()
				.inflate(R.layout.uderevalute, null);
		builder2.setView(comment_dialog);
		rg_handle = (RadioGroup) comment_dialog.findViewById(R.id.rg_handle);
		rg_please = (RadioGroup) comment_dialog.findViewById(R.id.rg_please);
		rg_money = (RadioGroup) comment_dialog.findViewById(R.id.rg_money);
		builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int hlen = rg_handle.getChildCount();
				int plen = rg_please.getChildCount();
				int mlen = rg_money.getChildCount();
				String hmsgString = "";
				String pmsgString = "";
				String mmsgString = "";
				for (int i = 0; i < hlen; i++) {
					RadioButton radioButton = (RadioButton) rg_handle
							.getChildAt(i);
					if (radioButton.isChecked()) {
						hmsgString = radioButton.getText().toString();
						break;
					}
				}
				for (int i = 0; i < plen; i++) {
					RadioButton radioButton = (RadioButton) rg_please
							.getChildAt(i);
					if (radioButton.isChecked()) {
						pmsgString = radioButton.getText().toString();
						break;
					}
				}
				for (int i = 0; i < mlen; i++) {
					RadioButton radioButton = (RadioButton) rg_money
							.getChildAt(i);
					if (radioButton.isChecked()) {
						mmsgString = radioButton.getText().toString();
						break;
					}
				}
				// 此处填写用户对司机评价的异步任务
				int role = 2;
				int feed1, feed2, feed3;
				if (hmsgString == "是") {
					feed1 = 1;
				} else {
					feed1 = 0;
				}
				if (pmsgString == "是") {
					feed2 = 1;
				} else {
					feed2 = 0;
				}
				if (mmsgString == "是") {
					feed3 = 1;
				} else {
					feed3 = 0;
				}
				try {
					CommentTask commentTask = new CommentTask(OrderTest.this,
							his_id, role, feed1, feed2, feed3);
					commentTask.execute();
					commentTask.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// dialog.dismiss();

			}
		}).create().show();
	}

	/**
	 * 异步评价接口
	 * 
	 * @author Administrator
	 * 
	 */
	class CommentTask extends AsyncNetworkTask<String> {
		private String his_id;
		private int role;
		private int feed1;
		private int feed2;
		private int feed3;

		public CommentTask(Context context, String his_id, int role, int feed1,
				int feed2, int feed3) {
			super(context);
			this.his_id = his_id;
			this.role = role;
			this.feed1 = feed1;
			this.feed2 = feed2;
			this.feed3 = feed3;
		}

		@Override
		protected String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.feedbackRescue(his_id, role, feed1, feed2, feed3);
		}

		@Override
		protected void handleResult(String result) {
			System.out.println("用户的评价结果返回值：" + result);
			// 结束本界面
			if (result != null && !result.equals("")) {
				try {

					int flag = Integer.parseInt(result);
					if (flag == 1) {
						if (role == 2) {
							DialogUtils.alert(OrderTest.this,
									R.drawable.ic_launcher, R.string.alert,
									"评价成功！", R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									});
						} else {

						}

					} else {
						Toast.makeText(OrderTest.this, "评论失败！", 1).show();
					}

				} catch (Exception e) {
					Toast.makeText(OrderTest.this, "数据异常！", 1).show();
				}

			}
		}

	}
}
