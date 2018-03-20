package com.ds.tire;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ds.tire.bean.RescueCar;
//import com.ds.tire.mqtt.MqttService;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.AutoZoomAndLocation;
import com.ds.tire.util.Constant;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.MapUtils;
import com.ds.tire.util.MyApplication;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class OrderDetailActivity extends Activity implements
		View.OnClickListener {
	private static final String TAG = "TAG";
	private Toast toast = null;
	private MyLocationOverlay myLocationOverlay = null;
	String uid;
	double myLatitude;
	double myLongitude;
	// 地图相关
	private MapView mapView = null;
	private MapController mapController = null;
	// 车辆信息
	private RescueCar rescueCar = null;
	private TextView carNumTV = null;
	private TextView disTV = null;
	private Button history_order = null;// 历史订单按钮
	private Button callButton = null;// 拨打司机电话按钮
	// 打车完成按钮
	private Button taxiOverButton = null;
	private Dialog commentDialog = null;
	// 评论信息
	private int comment = 1;
	ProgressDialog locatingDialog = null;
	private View map_back = null;
	private double dis = 0;
	private BDLocationListener locationListener = null;
	// 广播接收器
	private final MyReceiver myReceiver = new MyReceiver();

	int weizhi = -1;
	ProgressDialog progressDialog = null;
	// 处理消息
	Handler handler = new MyHandler();

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				disTV.setText(dis + "公里");
				mapController.animateTo(MapUtils.newGeoPoint(
						rescueCar.getLatitude(), rescueCar.getLongitude()));
				break;
			case 1:
				progressDialog = new ProgressDialog(OrderDetailActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setTitle("提示");
				progressDialog.setMessage("正在安排救援，请耐心等待");
				progressDialog.show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						progressDialog.cancel();
					}
				}, 3000);
				break;
			case 2:
				progressDialog.cancel();
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		Intent intent = getIntent();
		weizhi = intent.getIntExtra("weizhi", -1);
		toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
		locatingDialog = new ProgressDialog(this);
		locatingDialog.setMessage("正在定位...");
		locatingDialog.setCancelable(true);
		uid = SpUtils.getString(this, SpUtils.ACCOUNT, "");
		rescueCar = new RescueCar();
		locationListener = new LocationListener();
		findViewById();
		OnClickListener();
		if (SpUtils.getInteger(OrderDetailActivity.this, "steps", 0) != 3) {
			callButton.setEnabled(false);
			taxiOverButton.setEnabled(false);
		}
		initMapView();
//		MqttService.startupMqttSubscriber(this);
	}

	private void OnClickListener() {
		map_back.setOnClickListener(this);
		callButton.setOnClickListener(this);
		history_order.setOnClickListener(this);
		taxiOverButton.setOnClickListener(this);
	}

	private void findViewById() {
		history_order = (Button) findViewById(R.id.history_order);
		map_back = findViewById(R.id.map_back);
		carNumTV = (TextView) findViewById(R.id.taxi_number_text_view);
		disTV = (TextView) findViewById(R.id.distance_text_view);
		callButton = (Button) findViewById(R.id.call_button);
		taxiOverButton = (Button) findViewById(R.id.taxi_over_button);
	}

	/**
	 * 请求救援 异步访问接口
	 * 
	 * @author Administrator
	 * 
	 */
	class AidTask extends AsyncNetworkTask<String> {
		private double latitude;
		private double longitude;
		private String location;

		public AidTask(Context context, double latitude, double longitude,
				String location) throws UnsupportedEncodingException {
			super(context);
			this.latitude = latitude;
			this.longitude = longitude;
			this.location = location;
		}

		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.aid(uid, latitude, longitude, location);
		}

		@Override
		public void handleResult(String result) {
			if ("1".equals(result)) {
				showToast("请求救援成功！");
				handler.sendEmptyMessage(1);
			} else if ("2".equals(result)) {
				showToast("请求救援正在进行中，请不要多次重复发送救援！");
				handler.sendEmptyMessage(1);
			}else if ("-1".equals(result)) {
				showToast("接线员忙，请稍后重试...");
			}
			else {
				showToast("服务器异常，请求救援失败！");
			}

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		MqttService.shutdownMqttSubscriber(this);
		if (rescueCar.getLatitude() == 0 && rescueCar.getLongitude() == 0) {
			SpUtils.setBoolean(this, Constant.KEY_FINISH_RESCUE, true);

		} else {
			SpUtils.setBoolean(this, Constant.KEY_FINISH_RESCUE, false);
		}
		SpUtils.setString(this, Constant.KEY_RESCUE_LAT,
				rescueCar.getLatitude() + "");
		SpUtils.setString(this, Constant.KEY_RESCUE_LON,
				rescueCar.getLongitude() + "");
		SpUtils.setString(this, Constant.KEY_RESCUE_NUM, rescueCar.getNumber());
		SpUtils.setString(this, Constant.KEY_RESCUE_PHONE, rescueCar.getPhone());
		SpUtils.setString(this, Constant.KEY_RESCUE_ID, rescueCar.getRescueId());
	}

	/**
	 * 监测是否可以评价
	 */
	class CheckCommentTask extends AsyncNetworkTask<String> {
		public CheckCommentTask(Context context) {
			super(context);
		}

		@Override
		protected String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.checkComment(rescueCar.getRescueId());// rescueId
		}

		@Override
		protected void handleResult(String result) {
			if (result != null && result.equals("1")) {
				commentDialog.show();
			} else if (result != null && result.equals("0")) {
				Toast.makeText(OrderDetailActivity.this, "请等待网点评价完成",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(OrderDetailActivity.this, "服务器异常",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	/**
	 * 呼叫
	 */
	private void call() {
		if (rescueCar.getPhone() != null && !rescueCar.getPhone().equals("")) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ rescueCar.getPhone()));
			startActivity(intent);
		}

	}

	/**
	 * 按钮监听器
	 * 
	 * @author WPH
	 * 
	 */

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.map_back:
			finish();
			break;
		case R.id.call_button:
			call();
			break;
		case R.id.taxi_over_button:
			Toast.makeText(OrderDetailActivity.this, "请在历史订单里面待评价的订单进行评价", 1)
					.show();
			// commentDialog();
			break;
		case R.id.history_order:
			System.out.println("历史救援按钮。。。。。");
			Intent intent = new Intent();
			intent.setClass(OrderDetailActivity.this,
					OrderHistoryActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 评价对话框
	 */
	private void commentDialog() {
		Builder builder = new android.app.AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("请为接线员评价");
		builder.setSingleChoiceItems(R.array.comments, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						int feed = which;
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 此处填写用户对接线员评价的异步任务
				String pid = rescueCar.getRescueId();
				int role = 1;
				int feed = which;
				try {
					// CommentTask commentTask = new CommentTask(
					// OrderDetailActivity.this, pid, role, feed);
					// commentTask.execute();
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
		builder2.setSingleChoiceItems(R.array.comments, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						int feed = which;
					}
				});
		builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 此处填写用户对司机评价的异步任务
				String pid = rescueCar.getRescueId();
				int role = 2;
				int feed = which;
				try {
					// CommentTask commentTask = new CommentTask(
					// OrderDetailActivity.this, pid, role, feed);
					// commentTask.execute();
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
		private String pid;
		private int role;
		private int feed;

		public CommentTask(Context context, String pid, int role, int feed) {
			super(context);
			this.pid = pid;
			this.role = role;
			this.feed = feed;
		}

		// @Override
		// protected String doNetworkTask() {
		// WebService ws = WebServiceFactory.getWebService();
		// //return ws.feedbackRescue(pid, role, feed);
		// }

		@Override
		protected void handleResult(String result) {
			System.out.println("用户的评价结果返回值：" + result);
			// 结束本界面
			if (result != null && !result.equals("")) {
				try {

					int flag = Integer.parseInt(result);
					if (flag == 1) {
						if (role == 2) {
							rescueCar.setLatitude(0);
							rescueCar.setLongitude(0);
							rescueCar.setName("");
							rescueCar.setNumber("");
							rescueCar.setPhone("");
							rescueCar.setRescueId("");
							// SpUtils.setBoolean(OrderDetailActivity.this,
							// SpUtils.AIDFLAGONE, true);
							DialogUtils.alert(OrderDetailActivity.this,
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
						showToast("评论失败！");
					}

				} catch (Exception e) {
					Log.d(TAG, "error:" + e.getMessage());
					showToast("数据异常");
				}

			}
		}

		@Override
		protected String doNetworkTask() {
			// TODO Auto-generated method stub
			return null;
		}

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
			showToast("暂无法定位您的位置！");
		}
		mapController = mapView.getController();
		mapController.setZoom(mapView.getMaxZoomLevel());
		mapController.enableClick(true); // 允许点击
		mapView.refresh();

	}

	void showToast(String text) {
		if (Build.VERSION.SDK_INT < 14) {
			toast.cancel();
		}

		toast.setText(text);
		toast.show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		locatingDialog.show();
		MyApplication.startLocation();
		registerMyReceiver();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (locatingDialog != null && locatingDialog.isShowing())
			locatingDialog.dismiss();
		if (myReceiver != null)
			unregisterReceiver(myReceiver);
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

	private void update() {
		// 清空覆盖物
		mapView.getOverlays().clear();
		mapView.refresh();
		// 覆盖物
		MyOverlay mo = new MyOverlay(null);
		GeoPoint taxiGeoPoint = null;
		// 出租车覆盖物

		if (rescueCar.getLatitude() != 0 && rescueCar.getLongitude() != 0) {
			taxiGeoPoint = MapUtils.newGeoPoint(rescueCar.getLatitude(),
					rescueCar.getLongitude());
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
		if (ld != null && rescueCar.getLatitude() != 0
				&& rescueCar.getLongitude() != 0) {
			callButton.setEnabled(true);
			taxiOverButton.setEnabled(true);
			AutoZoomAndLocation.In in = new AutoZoomAndLocation.In();
			in.points.add(taxiGeoPoint);
			in.points.add(MapUtils.newGeoPoint(ld.latitude, ld.longitude));
			AutoZoomAndLocation.Out out = MapUtils.autoZoomAndLocation(in);
			mapController.setCenter(out.center);
			mapController.zoomToSpan(out.latitudeSpanE6, out.longitudeSpanE6);
			dis = MapUtils.getDistance(rescueCar.getLatitude(),
					rescueCar.getLongitude(), ld.latitude, ld.longitude);
			disTV.setText(dis + "米");
		} else if (ld == null) {
			showToast("暂无法定位您的位置");
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
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class LocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			LocationData ld = myLocationOverlay.getMyLocation();
			locatingDialog.dismiss();
			if (ld == null) {

				try {
					ld = new LocationData();
					if (SpUtils.getBoolean(OrderDetailActivity.this,
							Constant.KEY_FINISH_RESCUE, true)) {
						AidTask task;// 请求救援任务
						try {
							task = new AidTask(OrderDetailActivity.this,
									location.getLatitude(),
									location.getLongitude(), weizhi + "");
							task.showProgressDialog("提示", "正在发送救援信息...");
							task.execute();
						} catch (UnsupportedEncodingException e) {
							showToast("数据异常");
							Log.d(TAG, "error:" + e.getMessage());
							e.printStackTrace();
						}
					} else {
						rescueCar.setLatitude(Double.parseDouble(SpUtils
								.getString(OrderDetailActivity.this,
										Constant.KEY_RESCUE_LAT, "0")));
						rescueCar.setLongitude(Double.parseDouble(SpUtils
								.getString(OrderDetailActivity.this,
										Constant.KEY_RESCUE_LON, "0")));
						Log.d(TAG, "rescuing:" + rescueCar.getLatitude());
						rescueCar.setNumber(SpUtils.getString(
								OrderDetailActivity.this,
								Constant.KEY_RESCUE_NUM, ""));
						carNumTV.setText(rescueCar.getNumber());
						rescueCar.setPhone(SpUtils.getString(
								OrderDetailActivity.this,
								Constant.KEY_RESCUE_PHONE, ""));
						rescueCar.setRescueId(SpUtils.getString(
								OrderDetailActivity.this,
								Constant.KEY_RESCUE_ID, ""));
					}
					ld.latitude = location.getLatitude();
					ld.longitude = location.getLongitude();
					ld.accuracy = location.getRadius();
					ld.direction = location.getDerect();
					myLocationOverlay.setData(ld);
					update();
				} catch (Exception e) {
					Log.d(TAG, "error:" + e.getMessage());
				}

			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void registerMyReceiver() {
		// 注册广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_RESCUE_LOCATION);
		filter.addAction(Constant.ACTION_RESCUE_DETAILS);
		registerReceiver(myReceiver, filter);
	}

	/**
	 * 推送消息接收器
	 * 
	 * @author WPH
	 * 
	 */
	class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String action = intent.getAction();
				Log.d(TAG, "action=" + action);
				String json = intent.getStringExtra(Constant.KEY_JSON);
				Log.d(TAG, "json=" + json);
				if (Constant.ACTION_RESCUE_LOCATION.equals(action)) {
					onUpdateRescueLocation(json);
				} else if (Constant.ACTION_RESCUE_DETAILS.equals(action)) {
					onUpdateResuceDetails(json);
				} else if (Constant.ACTION_RESCUE_NOCAR.equals(action)) {
					Toast.makeText(OrderDetailActivity.this, "暂无救援车，请稍后...",
							Toast.LENGTH_SHORT).show();
					OrderDetailActivity.this.finish();
				} else {
					// do nothing
				}
			} catch (Exception e) {
				Log.d(TAG, "error:" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void onUpdateRescueLocation(String json) {
		try {
			Log.d(TAG, "onUpdateRescueLocation()");
			JSONObject obj = new JSONObject(json);
			rescueCar.setLatitude(obj.getDouble("latitude"));
			rescueCar.setLongitude(obj.getDouble("longitude"));
			update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onUpdateResuceDetails(String json) {
		try {
			handler.sendEmptyMessage(2);
			JSONObject object = new JSONObject(json);
			rescueCar.setNumber(object.getString("name"));
			rescueCar.setPhone(object.getString("phone"));
			rescueCar.setLatitude(object.getDouble("latitude"));
			rescueCar.setLongitude(object.getDouble("longitude"));
			rescueCar.setRescueId(object.getString("pid"));
			carNumTV.setText(rescueCar.getNumber());
			update();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
