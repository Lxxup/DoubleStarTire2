package com.ds.tire;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.bluetooth.Constants;
import com.ds.tire.bluetooth.DeviceList;
import com.ds.tire.bluetooth.GetDataService;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.MyApplication;
import com.ds.tire.util.SpUtils;


public class MonitorMoreActivity extends Activity {
	private String TAG = "MonitorActivity";
	private MonitorMoreActivity activity_entity;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothDevice device;
	private MessageBroadcastReceiver receiver;
	private ProgressDialog dialog = null;
	private String wenduTop;
	private String wenduBot;
	private String yaqiangTop;
	private String yaqiangBot;
	MediaPlayer mPlayer;
	AnimationDrawable circle;

	private GridView grid;
	private int wheelsCount;
	View back;
	Button aid;
	Button refresh;
	int location = -1;

	MonitorAdapter adapter = null;
	List<String> yqList = new ArrayList<String>();
	List<String> wdList = new ArrayList<String>();

	private Handler handler = new MyHandler();

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			location = (Integer) msg.obj;
			switch (msg.what) {
			case 0:
				SpUtils.setBoolean(MonitorMoreActivity.this,
						SpUtils.AIDFLAGONE, false);
				DialogUtils.alert(false, MonitorMoreActivity.this,
						R.drawable.dialog_icon, "提示", "是否呼叫救援车辆？", "确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (mPlayer != null) {
									mPlayer.stop();
									mPlayer = null;
								}
								SpUtils.setBoolean(MonitorMoreActivity.this,
										SpUtils.AIDFLAGONE, true);
								if (SpUtils.getString(MonitorMoreActivity.this,
										SpUtils.ACCOUNT, "").equals("")) {
									Toast.makeText(MonitorMoreActivity.this,
											"请先登录", Toast.LENGTH_SHORT).show();
								} else {
									Intent intent = new Intent(
											MonitorMoreActivity.this,
											OrderDetailActivity.class);
									intent.putExtra("weizhi", location);
									startActivity(intent);
								}
							}
						}, "打电话", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (mPlayer != null) {
									mPlayer.stop();
									mPlayer = null;
								}
								SpUtils.setBoolean(MonitorMoreActivity.this,
										SpUtils.AIDFLAGONE, true);
								Intent phoneIntent = new Intent(
										"android.intent.action.CALL", Uri
												.parse("tel:" + "400-017-6666"));
								startActivity(phoneIntent);
							}
						}, "取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (mPlayer != null) {
									mPlayer.stop();
									mPlayer = null;
								}
								SpUtils.setBoolean(MonitorMoreActivity.this,
										SpUtils.AIDFLAGONE, true);
							}
						});
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitor_more);
		activity_entity = this;
		SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE); 
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true); 
		Editor editor = sharedPreferences.edit(); 
		if (isFirstRun) 
		{ 
		Log.d("debug", "第一次运行"); 
		wenduTop= "60";
		wenduBot="-10";
		yaqiangTop="320";
		yaqiangBot= "180";
		SpUtils.setString(MonitorMoreActivity.this, "WENDUTOP", wenduTop);
		SpUtils.setString(MonitorMoreActivity.this, "WENDUBOT", wenduBot);
		SpUtils.setString(MonitorMoreActivity.this, "YAQIANGTOP", yaqiangTop);
		SpUtils.setString(MonitorMoreActivity.this, "YAQIANGBOT", yaqiangBot);
		editor.putBoolean("isFirstRun", false); 
		editor.commit(); 
		} else 
		{ 
		Log.d("debug", "不是第一次运行"); 
		wenduTop= SpUtils.getString(MonitorMoreActivity.this, "WENDUTOP", "");
		wenduBot=SpUtils.getString(MonitorMoreActivity.this, "WENDUBOT", "");
		yaqiangTop=SpUtils.getString(MonitorMoreActivity.this, "YAQIANGTOP", "");
		yaqiangBot= SpUtils.getString(MonitorMoreActivity.this, "YAQIANGBOT", "");
		} 
		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}
		wheelsCount = intent.getIntExtra("wheels_count", 8);

		aid = (Button) findViewById(R.id.aid);
//		refresh = (Button) findViewById(R.id.refresh);
//		refresh.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();  //Itent就是我们要发送的内容 
//	            intent.setAction(Constants.REFRESH);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
//	            sendBroadcast(intent);   //发送广播
//			}
//		});
		aid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SpUtils.getBoolean(MonitorMoreActivity.this, 
						SpUtils.AIDFLAGONE, true)) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = -1;
					handler.sendMessage(msg);
				} else {
					// 进入救援地图页面
					Intent intent = new Intent(MonitorMoreActivity.this,
							OrderDetailActivity.class);
					intent.putExtra("weizhi", location);
					startActivity(intent);
				}
			}
		});
		back = findViewById(R.id.monitor_head_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		grid = (GridView) findViewById(R.id.monitor_grid);
		grid.setNumColumns(2);

		for (int i = 0; i < wheelsCount; i++) {
			yqList.add("无数据");
			wdList.add("无数据");
		}
		adapter = new MonitorAdapter(MonitorMoreActivity.this, yqList, wdList);
		grid.setAdapter(adapter);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "不支持蓝牙服务", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		// 蓝牙的当前状态如果是关闭，则打开设备
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
		} else {
			selectDeviceToConnect();
		}
	}

	public void onUpdateWheels(String mID, float qy, float wd, float jsd) {
		int count = 0;
    	while(count < MyApplication.INSTALLED_COUNT){
    		if(mID.equalsIgnoreCase(MyApplication.idList.get(count))){
    			onUpdateWheels(count,qy,wd,jsd);
    			break;
    		}
    	}
    	if(count == MyApplication.INSTALLED_COUNT){
    		MyApplication.INSTALLED_COUNT ++;
    		MyApplication.idList.add(mID);
    		SpUtils.setInteger(activity_entity, SpUtils.INSTALLED_COUNT,MyApplication.INSTALLED_COUNT);
    		SpUtils.setString(activity_entity, SpUtils.WHEELS_ID + count, mID);
    		onUpdateWheels(count, qy, wd, jsd);
    	}
	}

	public void onUpdateWheels(int loc, float qy, float wd, float jsd) {
		if (loc >= 0&& loc < SpUtils.getInteger(MonitorMoreActivity.this,
						SpUtils.WHEELS_COUNT, 8)) {
			// 此处设置胎压显示
			yqList.set(loc, qy + "百帕");
			wdList.set(loc, wd + "度");
			adapter = new MonitorAdapter(MonitorMoreActivity.this, yqList,
					wdList);
			grid.setAdapter(adapter);

			/*
			 * 设置警报范围
			 */
//			if (SpUtils.getBoolean(MonitorMoreActivity.this,
//					SpUtils.AIDFLAGONE, true)) {
				if (qy <  Integer.parseInt(yaqiangBot)|| qy >  Integer.parseInt(yaqiangTop) || wd > Integer.parseInt(wenduTop) || wd <  Integer.parseInt(wenduBot))  {
					SpUtils.getBoolean(MonitorMoreActivity.this,SpUtils.AIDFLAGONE, true);
					// 闪烁
					if (mPlayer == null) {
						mPlayer = MediaPlayer.create(MonitorMoreActivity.this,R.raw.voi);
						mPlayer.start();
                        mPlayer.setOnErrorListener(new OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer mp, int what, int extra) {
                                        mPlayer.reset();
                                        Log.d("出错啦！","出错啦！");
                                        return false;
                                }
                        });
                     } else {
                    	mPlayer.stop();
                        mPlayer.reset();
                        mPlayer = MediaPlayer.create(MonitorMoreActivity.this,R.raw.voi);
						mPlayer.start();
                     }
					Message msg = new Message();
					msg.what = 0;
					msg.obj = loc;
					handler.sendMessage(msg);
					Toast.makeText(MonitorMoreActivity.this, "请注意，轮胎出现问题",
							Toast.LENGTH_LONG).show();
				}else {					
					
					Log.i("TAG","->是否报警："+ SpUtils.getBoolean(MonitorMoreActivity.this,SpUtils.AIDFLAGONE, true));
				}
			}

		}
	//}

	/**
	 * 函数功能：选择需要连接的设备，进行连接
	 */
	private void selectDeviceToConnect() {
		if (GetDataService.mDevice == null) {
			Intent serverIntent = new Intent(this, DeviceList.class);
			startActivityForResult(serverIntent,
					Constants.REQUEST_CONNECT_DEVICE_INSECURE);
		}
	}

	/**
	 * Start时注册广播接收器
	 */
	@Override
	protected void onStart() {
		super.onStart();
		registerBroadcastReceiver();
		Log.d(TAG, "注册广播成功");

	}

	/**
	 * stop时注销掉广播接收器
	 */
	@Override
	protected void onStop() {
		
		super.onStop();

	}

	/**
	 * destroy时，退出程序，退出服务
	 */
	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	/**
	 * onActivityResult,处理界面的返回结果
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		// 申请打开蓝牙的返回值
		case Constants.REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				selectDeviceToConnect();
			} else {
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
			break;
		// 选择相应的设备进行连接通讯
		case Constants.REQUEST_CONNECT_DEVICE_INSECURE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						Constants.EXTRA_DEVICE_ADDRESS);
				device = mBluetoothAdapter.getRemoteDevice(address);
				GetDataService.start(activity_entity, device);
			}
			break;
		}
	}

	/**
	 * 注册广播信息
	 */
	private void registerBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.ACTION_BLUETOOTH_BROADCAST);
		receiver = new MessageBroadcastReceiver();
		registerReceiver(receiver, intentFilter);
	}

	/**
	 * 函数功能： 显示提示信息的对话框，只有一个确定按钮
	 */
	private void showToast(String msg) {
		DialogUtils.alert(this, android.R.drawable.ic_dialog_info, "提示", msg,
				"确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
	}

	/**
	 * 广播接收器
	 * 
	 * @author Administrator
	 * 
	 */
	class MessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int temp = intent.getIntExtra(Constants.MSG, -1);
			if (temp != -1) {
				switch (temp) {
				case Constants.STATE_CONNECTING:
					if (dialog == null) {
						dialog = new ProgressDialog(activity_entity);
						dialog.setCancelable(false);
						dialog.setMessage("正在建立蓝牙连接...");
					}
					dialog.show();
					break;
				case Constants.STATE_CONNECTED:
					dialog.dismiss();
					showToast("蓝牙连接建立成功");
					Intent intent_broad = new Intent();
					intent_broad
							.setAction(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_TALK);
					activity_entity.sendBroadcast(intent_broad);
					break;

				case Constants.MESSAGE_CONNECT_FAILED:
					dialog.dismiss();
					Log.d(TAG, "接受广播" + "MESSAGE_CONNECT_FAILED");
					DialogUtils.alert(activity_entity,
							android.R.drawable.ic_dialog_info, "提示",
							"建立连接失败，是否重连?", "重连",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent_broad = new Intent();
									intent_broad
											.setAction(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_CONNECT);
									activity_entity.sendBroadcast(intent_broad);

								}
							}, "取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							});
					break;
				case Constants.MESSAGE_CONNECT_LOST:
					Log.d(TAG, "接受广播" + "MESSAGE_CONNECT_LOST");
					DialogUtils.alert(activity_entity,
							android.R.drawable.ic_dialog_info, "提示",
							"连接断开，是否重连?", "重连",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent_broad = new Intent();
									intent_broad
											.setAction(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_CONNECT);
									activity_entity.sendBroadcast(intent_broad);
									dialog.dismiss();

								}
							}, "取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							});
					break;
				case 51:
					if (SpUtils.getBoolean(MonitorMoreActivity.this,
							SpUtils.AIDFLAGONE, true)) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = -1;
						handler.sendMessage(msg);
					} else {
						// 进入救援地图页面
						Intent intent1 = new Intent(MonitorMoreActivity.this,
								OrderDetailActivity.class);
						intent1.putExtra("weizhi", location);
						startActivity(intent1);
					}
					break;
				default:
					break;
				}
			} else {
				float wendu = intent.getFloatExtra(Constants.WENDU, 0);
				float yaqiang = intent.getFloatExtra(Constants.YAQIANG, 0);
				float jiasudu = intent.getFloatExtra(Constants.JIASUDU, 0);
				String mID = intent.getStringExtra(Constants.mID);
				Log.v("AAA", location + "");
				onUpdateWheels(mID, yaqiang, wendu, jiasudu);
			}

		}
	}

	class MonitorAdapter extends BaseAdapter {
		private List<String> yqList = new ArrayList<String>();
		private List<String> wdList = new ArrayList<String>();
		private Context context;

		public MonitorAdapter(Context context, List<String> yqList,
				List<String> wdList) {
			super();
			this.context = context;
			this.yqList = yqList;
			this.wdList = wdList;
		}

		@Override
		public int getCount() {
			return yqList.size();
		}

		@Override
		public Object getItem(int position) {
			return yqList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (position % 2 == 0) {
				view = LayoutInflater.from(context).inflate(
						R.layout.monitor_more_left_item, null);
			} else {
				view = LayoutInflater.from(context).inflate(
						R.layout.monitor_more_right_item, null);
			}

			String yq = yqList.get(position).toString();
			TextView yaQiang = (TextView) view.findViewById(R.id.qi_ya);
			yaQiang.setText(yq);
			String wd = wdList.get(position).toString();
			TextView wenDu = (TextView) view.findViewById(R.id.wen_du);
			wenDu.setText(wd);

			return view;
		}
	}
}
