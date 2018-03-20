package com.ds.tire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
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
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.bluetooth.Constants;
import com.ds.tire.bluetooth.DeviceList;
import com.ds.tire.bluetooth.GetDataService;
import com.ds.tire.db.DBManager;
import com.ds.tire.db.DBOperate;
import com.ds.tire.db.ReData;
import com.ds.tire.util.Constant;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.MyApplication;
import com.ds.tire.util.SpUtils;

public class MonitorActivityTwo extends Activity {


//	private static int call0=0;
//	private static int call1=0;
//	private static int call2=0;
//	private static int call3=0;
    String TAG;
	private int count0=0;
    private int count1=0;
    private int count2=0;
    private int count3=0;
    private int count4=0;
    private int count5=0;
    private int count6=0;
    private int count7=0;
    private int count8=0;
    private int count9=0;
    private int count10=0;
    private int count11=0;
	private String wenduTop;
	private String wenduBot;
	private String yaqiangTop;
	private String yaqiangBot;
	private String gwenduTop;
	private String gwenduBot;
	private String gyaqiangTop;
	private String gyaqiangBot;
	private MonitorActivityTwo activity_entity;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothDevice device;
	private MessageBroadcastReceiver receiver;
	private ProgressDialog dialog = null;
	LayoutInflater inflater = null;
	View layout = null;
	Button aid;
	Button refresh;
	TextView re0;
	TextView re1;
	TextView re2;
	TextView re3;
	TextView re4;
	TextView re5;
	TextView re6;
	TextView re7;
	TextView re8;
	TextView re9;
	TextView re10;
	TextView re11;
	private Button smatch;
	View back;
	MediaPlayer mPlayer;
	//MediaPlayer m;
	AnimationDrawable circleAnimation;
	private int wheelsCount = 0;
	private View[] wheels;
	int location = -1;
	DBOperate db = null;
	int count = 0;
	int sensorcount = 0;
	ArrayList<ReData> sdatas = new ArrayList<ReData>();
	private DBManager mgr;

	private Handler handler = new MyHandler();

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			location = (Integer) msg.obj;
			switch (msg.what) {
			case 0:
				SpUtils.setBoolean(MonitorActivityTwo.this, SpUtils.AIDFLAGONE,
						false);
				DialogUtils.alert(false, MonitorActivityTwo.this,
						R.drawable.dialog_icon, "提示", "是否呼叫救援车辆？", "确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								if (mPlayer != null) {
									mPlayer.stop();
									mPlayer.release();
									mPlayer = null;
								}
								
								if(circleAnimation!=null){
								circleAnimation.stop();
								}
								SpUtils.setBoolean(MonitorActivityTwo.this,
										SpUtils.AIDFLAGONE, true);
								if (SpUtils.getString(MonitorActivityTwo.this,
										SpUtils.ACCOUNT, "").equals("")) {
									Toast.makeText(MonitorActivityTwo.this,
											"请先登录", Toast.LENGTH_SHORT).show();
								} else {
									Intent intent = new Intent(
											MonitorActivityTwo.this,
											OrderDetailActivity.class);
									intent.putExtra("weizhi", location);
									startActivity(intent);
								}
							}
						}, "打电话", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								if (mPlayer != null) {
									mPlayer.stop();
									mPlayer.release();
									mPlayer = null;
								}
							
								if(circleAnimation!=null){
									circleAnimation.stop();
									}
								SpUtils.setBoolean(MonitorActivityTwo.this,
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
									mPlayer.release();
									mPlayer = null;
								}
							
								SpUtils.setBoolean(MonitorActivityTwo.this,
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(SpUtils.getString(MonitorActivityTwo.this, "cheXing", "").equals("")){
			Toast.makeText(this, "请先设定胎压范围！！！", Toast.LENGTH_LONG).show();
			
		}
		SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE); 
		Boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true); 
		Editor editor = sharedPreferences.edit(); 
		
//		if (isFirstRun) 
//		{ 
//		Log.d("debug", "第一次运行"); 
//		wenduTop= "60";
//		wenduBot="-10";
//		yaqiangTop="320";
//		yaqiangBot= "180";
//		gwenduTop= "60";
//		gwenduBot="-10";
//		gyaqiangTop="1200";
//		gyaqiangBot= "600";
//		SpUtils.setString(MonitorActivityTwo.this, "WENDUTOP", wenduTop);
//		SpUtils.setString(MonitorActivityTwo.this, "WENDUBOT", wenduBot);
//		SpUtils.setString(MonitorActivityTwo.this, "YAQIANGTOP", yaqiangTop);
//		SpUtils.setString(MonitorActivityTwo.this, "YAQIANGBOT", yaqiangBot);
//		SpUtils.setString(MonitorActivityTwo.this, "GWENDUTOP", gwenduTop);
//		SpUtils.setString(MonitorActivityTwo.this, "GWENDUBOT", gwenduBot);
//		SpUtils.setString(MonitorActivityTwo.this, "GYAQIANGTOP", gyaqiangTop);
//		SpUtils.setString(MonitorActivityTwo.this, "GYAQIANGBOT", gyaqiangBot);
//		editor.putBoolean("isFirstRun", false); 
//		editor.commit(); 
//		} else 
//		{ 
		//Log.d("debug", "不是第一次运行"); 
		wenduTop= SpUtils.getString(MonitorActivityTwo.this, "WENDUTOP", "");
		wenduBot=SpUtils.getString(MonitorActivityTwo.this, "WENDUBOT", "");
		yaqiangTop=SpUtils.getString(MonitorActivityTwo.this, "YAQIANGTOP", "");
		yaqiangBot= SpUtils.getString(MonitorActivityTwo.this, "YAQIANGBOT", "");
		gwenduTop= SpUtils.getString(MonitorActivityTwo.this, "GWENDUTOP", "");
		gwenduBot=SpUtils.getString(MonitorActivityTwo.this, "GWENDUBOT", "");
		gyaqiangTop=SpUtils.getString(MonitorActivityTwo.this, "GYAQIANGTOP", "");
		gyaqiangBot= SpUtils.getString(MonitorActivityTwo.this, "GYAQIANGBOT", "");
		//} 
		
		Log.i("oncreate得到的数据限值：", "接收到:"+"温度上限："+wenduTop+"温度下限："+wenduBot+"压强上限："+yaqiangTop+"压强下线："+yaqiangBot);  
		
		db = new DBOperate(this);
		wheelsCount = SpUtils.getInteger(MonitorActivityTwo.this, "wheels_count", 0);
	//	wheelsCount=Integer.parseInt(intent.getExtras().getString("wheels_count"));		
		//if(wheelsCount){}
//		wheelsCount = intent.getIntExtra("wheels_count",
//				SpUtils.getInteger(MonitorActivityTwo.this, "wheels_count", 0));
		//wheelsCount = intent.getExtra("wheels_count","wheels_count");
		Log.i("TAG", "wheelsCount" + wheelsCount);

		if (wheelsCount <= 4) {
			wheels = new View[4];
			setContentView( R.layout.monitor_4);
		} else if(wheelsCount==6) {
			wheels = new View[6];
			setContentView( R.layout.monitor_6);
			Log.i("TAG","6执行了！！！");
		}else if(wheelsCount==8) {
			wheels = new View[8];
			setContentView( R.layout.monitor8);
		}else if(wheelsCount==10) {
			wheels = new View[10];
			setContentView( R.layout.monitor_10);
		}else if(wheelsCount==12) {
			wheels = new View[12];
			setContentView( R.layout.monitor_12);
		}
//		setContentView(wheelsCount <= 4 ? R.layout.monitor_4
//				: R.layout.monitor_6);
		activity_entity = this;
	    re0=(TextView) findViewById(R.id.re0);
	    re1=(TextView) findViewById(R.id.re1);
	    re2=(TextView) findViewById(R.id.re2);
	    re3=(TextView) findViewById(R.id.re3);
	    re4=(TextView) findViewById(R.id.re4);
	    re5=(TextView) findViewById(R.id.re5);
	    re6=(TextView) findViewById(R.id.re6);
	    re7=(TextView) findViewById(R.id.re7);
	    re8=(TextView) findViewById(R.id.re8);
	    re9=(TextView) findViewById(R.id.re9);
	    re10=(TextView) findViewById(R.id.re10);
	    re11=(TextView) findViewById(R.id.re11);
		aid = (Button) findViewById(R.id.aid);
		refresh = (Button) findViewById(R.id.refresh);
		smatch = (Button) findViewById(R.id.match);
		back = findViewById(R.id.monitor_head_back);
		smatch.setVisibility(View.GONE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MonitorActivityTwo.this.finish();
			}
		});
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				Intent in = new Intent(MonitorActivityTwo.this,MonitorActivityTwo.class);  
				startActivity(in); 
				finish();
               Log.i(TAG,"刷新！！！！！！！！！");	            
			}
		});
		smatch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 给每个轮胎配对 */
//				Intent intent = new Intent(MonitorActivityTwo.this, MonitorMatchActivity.class);
//				startActivity(intent);	
				Log.i(TAG,"smatch!!!!!!!!！！");
				startActivityForResult(new Intent(MonitorActivityTwo.this, MonitorMatchActivity.class), 011);
				Log.i(TAG,"smatch!!!!!!!!！！");
			}
		});
		initTirePressure();

		aid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SpUtils.getBoolean(MonitorActivityTwo.this,
						SpUtils.AIDFLAGONE, true)) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = -1;
					handler.sendMessage(msg);
				} else {
					// 进入救援地图页面
					Intent intent = new Intent(MonitorActivityTwo.this,
							OrderDetailActivity.class);
					intent.putExtra("weizhi", location);
					startActivity(intent);
				}
			}
		});
        
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
			String sens0=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID0, "");
			String sens1=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID1, "");
			String sens2=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID2, "");
			String sens3=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID3, "");
			String sens4=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID4, "");
			String sens5=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID5, "");
			String sens6=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID6, "");
			String sens7=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID7, "");
			String sens8=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID8, "");
			String sens9=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID9, "");
			String sens10=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID10, "");
			String sens11=SpUtils.getString(MonitorActivityTwo.this,  Constants.SENSOR_ID11, "");
			Log.d("TAG","测试sens0---->"+sens0);
			Log.d("TAG","测试sens1---->"+sens1);
			Log.d("TAG","测试sens2---->"+sens2);
			Log.d("TAG","测试sens3---->"+sens3);
			Log.d("TAG","测试sens4---->"+sens4);
			Log.d("TAG","测试sens5---->"+sens5);
			Log.d("TAG","测试sens6---->"+sens6);
			Log.d("TAG","测试sens7---->"+sens7);
			Log.d("TAG","测试sens8---->"+sens8);
			Log.d("TAG","测试sens9---->"+sens9);
			Log.d("TAG","测试sens10---->"+sens10);
			Log.d("TAG","测试sens11---->"+sens11);
			if (isFirstRun){ 
				if(sens0.equals("")||sens0.equals("sensor0")){
				}else{
					TextView qiYa = (TextView) wheels[0].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[0].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[0].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[0].findViewById(R.id.mId);
					qiYa.setText("2.0" + "KPa");
					wenDu.setText("10"+ "℃");
					jiasudu.setText("30");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID0, ""));
				}
				if(sens1.equals("")||sens1.equals("sensor1")){
				}else{
					TextView qiYa = (TextView) wheels[1].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[1].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[1].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[1].findViewById(R.id.mId);
					qiYa.setText("2.2" + "KPa");
					wenDu.setText("13"+ "℃");
					jiasudu.setText("32");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID1, ""));
				}
		         if((sens2.equals(""))||(sens2.equals("sensor2"))){
		         }else{
					TextView qiYa = (TextView) wheels[2].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[2].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[2].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[2].findViewById(R.id.mId);
					qiYa.setText("2.0" + "KPa");
					wenDu.setText("14"+ "℃");
					jiasudu.setText("35");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID2, ""));
				}
		         if((sens3.equals(""))||(sens3.equals("sensor3"))){
		         }else{
					TextView qiYa = (TextView) wheels[3].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[3].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[3].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[3].findViewById(R.id.mId);
					qiYa.setText("2.4" + "KPa");
					wenDu.setText("10"+ "℃");
					jiasudu.setText("30");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID3, ""));
				}
		         if((sens4.equals(""))||(sens4.equals("sensor4"))){
		         }else{
					TextView qiYa = (TextView) wheels[4].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[4].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[4].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[4].findViewById(R.id.mId);
					qiYa.setText("2.0" + "KPa");
					wenDu.setText("11"+ "℃");
					jiasudu.setText("32");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID4, ""));
				}
		         if((sens5.equals(""))||(sens5.equals("sensor5"))){
		         }else{
					TextView qiYa = (TextView) wheels[5].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[5].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[5].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[5].findViewById(R.id.mId);
					qiYa.setText("2.4" + "KPa");
					wenDu.setText("10"+ "℃");
					jiasudu.setText("30");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID5, ""));
				}
		         if((sens6.equals(""))||(sens6.equals("sensor6"))){
		         }else{
					TextView qiYa = (TextView) wheels[6].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[6].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[6].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[6].findViewById(R.id.mId);
					qiYa.setText("2.0" + "KPa");
					wenDu.setText("14"+ "℃");
					jiasudu.setText("35");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID6, ""));
				}
		         if((sens7.equals(""))||(sens7.equals("sensor7"))){
		         }else{
					TextView qiYa = (TextView) wheels[7].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[7].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[7].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[7].findViewById(R.id.mId);
					qiYa.setText("2.4" + "KPa");
					wenDu.setText("10"+ "℃");
					jiasudu.setText("30");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID7, ""));
				}
		         if((sens8.equals(""))||(sens8.equals("sensor8"))){
		         }else{
					TextView qiYa = (TextView) wheels[8].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[8].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[8].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[8].findViewById(R.id.mId);
					qiYa.setText("2.0" + "KPa");
					wenDu.setText("11"+ "℃");
					jiasudu.setText("32");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID8, ""));
				}
		         if((sens9.equals(""))||(sens9.equals("sensor9"))){
		         }else{
					TextView qiYa = (TextView) wheels[9].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[9].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[9].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[9].findViewById(R.id.mId);
					qiYa.setText("2.4" + "KPa");
					wenDu.setText("10"+ "℃");
					jiasudu.setText("30");
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID9, ""));
		         }
					if((sens10.equals(""))||(sens10.equals("sensor10"))){
			         }else{
						TextView qiYa = (TextView) wheels[10].findViewById(R.id.qi_ya);
						TextView wenDu = (TextView) wheels[10].findViewById(R.id.wen_du);
						TextView jiasudu = (TextView) wheels[10].findViewById(R.id.jiasudu);
						TextView mID = (TextView) wheels[10].findViewById(R.id.mId);
						qiYa.setText("2.0" + "KPa");
						wenDu.setText("11"+ "℃");
						jiasudu.setText("32");
						mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID10, ""));
					}
			         if((sens11.equals(""))||(sens11.equals("sensor11"))){
			         }else{
						TextView qiYa = (TextView) wheels[11].findViewById(R.id.qi_ya);
						TextView wenDu = (TextView) wheels[11].findViewById(R.id.wen_du);
						TextView jiasudu = (TextView) wheels[11].findViewById(R.id.jiasudu);
						TextView mID = (TextView) wheels[11].findViewById(R.id.mId);
						qiYa.setText("2.4" + "KPa");
						wenDu.setText("10"+ "℃");
						jiasudu.setText("30");
						mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID11, ""));
					}
				
			}else{
				if(sens0.equals("")||sens0.equals("sensor0")){
				}else{
					//String data=db.getData(sens0);
					//Log.i("","从数据库获取的sens0的数据："+data);				
//					ArrayList<ReData> sdatas = new ArrayList<ReData>();
//					mgr.query();
					if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang0", "").equals("")){
						SpUtils.setString(MonitorActivityTwo.this,"yaqiang0", "2.2");
						SpUtils.setString(MonitorActivityTwo.this,"wendu0", "9");
						SpUtils.setString(MonitorActivityTwo.this,"jiasudu0", "37");
					}
					TextView qiYa = (TextView) wheels[0].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[0].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[0].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[0].findViewById(R.id.mId);
					qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang0", "") + "KPa");
					wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu0", "")+ "℃");
					jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu0", ""));
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID0, ""));
				}
				if(sens1.equals("")||sens1.equals("sensor1")){
				}else{
					if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang1", "").equals("")){
						SpUtils.setString(MonitorActivityTwo.this,"yaqiang1", "2.0");
						SpUtils.setString(MonitorActivityTwo.this,"wendu1", "13");
						SpUtils.setString(MonitorActivityTwo.this,"jiasudu1", "30");
					}
					TextView qiYa = (TextView) wheels[1].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[1].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[1].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[1].findViewById(R.id.mId);
					qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang1", "") + "KPa");
					wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu1", "")+ "℃");
					jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu1", ""));
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID1, ""));
				}
		         if((sens2.equals(""))||(sens2.equals("sensor2"))){
		         }else{
		        	 if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang2", "").equals("")){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang2", "2.4");
							SpUtils.setString(MonitorActivityTwo.this,"wendu2", "12");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu2", "33");
						}
					TextView qiYa = (TextView) wheels[2].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[2].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[2].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[2].findViewById(R.id.mId);
					qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang2", "") + "KPa");
					wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu2", "")+ "℃");
					jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu2", ""));
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID2, ""));
				}
		         if((sens3.equals(""))||(sens3.equals("sensor3"))){
		         }else{
		        	 if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang3", "").equals("")){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang3", "2.0");
							SpUtils.setString(MonitorActivityTwo.this,"wendu3", "13");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu3", "30");
						}
					TextView qiYa = (TextView) wheels[3].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[3].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[3].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[3].findViewById(R.id.mId);
					qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang3", "") + "KPa");
					wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu3", "")+ "℃");
					jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu3", ""));
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID3, ""));
				}
		         if((sens4.equals(""))||(sens4.equals("sensor4"))){
		         }else{
		        	 if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang4", "").equals("")){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang4", "2.1");
							SpUtils.setString(MonitorActivityTwo.this,"wendu4", "15");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu4", "35");
						}
					TextView qiYa = (TextView) wheels[4].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[4].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[4].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[4].findViewById(R.id.mId);
					qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang4", "") + "KPa");
					wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu4", "")+ "℃");
					jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu4", ""));
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID4, ""));
				}
		         if((sens5.equals(""))||(sens5.equals("sensor5"))){
		         }else{
		        	 if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang5", "").equals("")){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang5", "2.2");
							SpUtils.setString(MonitorActivityTwo.this,"wendu5", "12");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu5", "30");
						}
					TextView qiYa = (TextView) wheels[5].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[5].findViewById(R.id.wen_du);
					TextView jiasudu = (TextView) wheels[5].findViewById(R.id.jiasudu);
					TextView mID = (TextView) wheels[5].findViewById(R.id.mId);
					qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang5", "") + "KPa");
					wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu5", "")+ "℃");
					jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu5", ""));
					mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID5, ""));
				}
		         if(sens6.equals("")||sens6.equals("sensor6")){
					}else{
						if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang6", "").equals("")){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang6", "2.0");
							SpUtils.setString(MonitorActivityTwo.this,"wendu6", "13");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu6", "30");
						}
						TextView qiYa = (TextView) wheels[6].findViewById(R.id.qi_ya);
						TextView wenDu = (TextView) wheels[6].findViewById(R.id.wen_du);
						TextView jiasudu = (TextView) wheels[6].findViewById(R.id.jiasudu);
						TextView mID = (TextView) wheels[6].findViewById(R.id.mId);
						qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang6", "") + "KPa");
						wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu6", "")+ "℃");
						jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu6", ""));
						mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID6, ""));
					}
			         if((sens7.equals(""))||(sens7.equals("sensor7"))){
			         }else{
			        	 if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang7", "").equals("")){
								SpUtils.setString(MonitorActivityTwo.this,"yaqiang7", "2.4");
								SpUtils.setString(MonitorActivityTwo.this,"wendu7", "12");
								SpUtils.setString(MonitorActivityTwo.this,"jiasudu7", "33");
							}
						TextView qiYa = (TextView) wheels[7].findViewById(R.id.qi_ya);
						TextView wenDu = (TextView) wheels[7].findViewById(R.id.wen_du);
						TextView jiasudu = (TextView) wheels[7].findViewById(R.id.jiasudu);
						TextView mID = (TextView) wheels[7].findViewById(R.id.mId);
						qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang7", "") + "KPa");
						wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu7", "")+ "℃");
						jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu7", ""));
						mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID7, ""));
					}
			         if((sens8.equals(""))||(sens8.equals("sensor8"))){
			         }else{
			        	 if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang8", "").equals("")){
								SpUtils.setString(MonitorActivityTwo.this,"yaqiang8", "2.0");
								SpUtils.setString(MonitorActivityTwo.this,"wendu8", "13");
								SpUtils.setString(MonitorActivityTwo.this,"jiasudu8", "30");
							}
						TextView qiYa = (TextView) wheels[8].findViewById(R.id.qi_ya);
						TextView wenDu = (TextView) wheels[8].findViewById(R.id.wen_du);
						TextView jiasudu = (TextView) wheels[8].findViewById(R.id.jiasudu);
						TextView mID = (TextView) wheels[8].findViewById(R.id.mId);
						qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang8", "") + "KPa");
						wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu8", "")+ "℃");
						jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu8", ""));
						mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID8, ""));
					}
			         if((sens9.equals(""))||(sens9.equals("sensor9"))){
			         }else{
			        	 if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang9", "").equals("")){
								SpUtils.setString(MonitorActivityTwo.this,"yaqiang9", "2.1");
								SpUtils.setString(MonitorActivityTwo.this,"wendu9", "15");
								SpUtils.setString(MonitorActivityTwo.this,"jiasudu9", "35");
							}
						TextView qiYa = (TextView) wheels[9].findViewById(R.id.qi_ya);
						TextView wenDu = (TextView) wheels[9].findViewById(R.id.wen_du);
						TextView jiasudu = (TextView) wheels[9].findViewById(R.id.jiasudu);
						TextView mID = (TextView) wheels[9].findViewById(R.id.mId);
						qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang9", "") + "KPa");
						wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu9", "")+ "℃");
						jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu9", ""));
						mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID9, ""));
					}
			         if((sens10.equals(""))||(sens10.equals("sensor10"))){
			         }else{
			        	 if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang10", "").equals("")){
								SpUtils.setString(MonitorActivityTwo.this,"yaqiang10", "2.0");
								SpUtils.setString(MonitorActivityTwo.this,"wendu10", "13");
								SpUtils.setString(MonitorActivityTwo.this,"jiasudu10", "30");
							}
						TextView qiYa = (TextView) wheels[10].findViewById(R.id.qi_ya);
						TextView wenDu = (TextView) wheels[10].findViewById(R.id.wen_du);
						TextView jiasudu = (TextView) wheels[10].findViewById(R.id.jiasudu);
						TextView mID = (TextView) wheels[10].findViewById(R.id.mId);
						qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang10", "") + "KPa");
						wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu10", "")+ "℃");
						jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu10", ""));
						mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID10, ""));
					}
			         if((sens11.equals(""))||(sens11.equals("sensor11"))){
			         }else{
			        	 if(SpUtils.getString(MonitorActivityTwo.this,"yaqiang11", "").equals("")){
								SpUtils.setString(MonitorActivityTwo.this,"yaqiang11", "2.1");
								SpUtils.setString(MonitorActivityTwo.this,"wendu11", "15");
								SpUtils.setString(MonitorActivityTwo.this,"jiasudu11", "35");
							}
						TextView qiYa = (TextView) wheels[11].findViewById(R.id.qi_ya);
						TextView wenDu = (TextView) wheels[11].findViewById(R.id.wen_du);
						TextView jiasudu = (TextView) wheels[11].findViewById(R.id.jiasudu);
						TextView mID = (TextView) wheels[11].findViewById(R.id.mId);
						qiYa.setText(SpUtils.getString(MonitorActivityTwo.this,"yaqiang11", "") + "KPa");
						wenDu.setText(SpUtils.getString(MonitorActivityTwo.this,"wendu11", "")+ "℃");
						jiasudu.setText(SpUtils.getString(MonitorActivityTwo.this,"jiasudu11", ""));
						mID.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID11, ""));
					}
			}
			
		}	
		
		mPlayer = MediaPlayer.create(MonitorActivityTwo.this, R.raw.voi);
		mgr = new DBManager(MonitorActivityTwo.this);
		
	}

	public void onUpdateData(String mID, int qy, int wd, int jsd) {
		Log.i("TAG", "安装的轮子数量：" + MyApplication.INSTALLED_COUNT + "现在的数量："
				+ count);
		while (count < MyApplication.INSTALLED_COUNT) {
			if (mID.equalsIgnoreCase(MyApplication.idList.get(count))) {
				break;
			}
		}
		if (count == MyApplication.INSTALLED_COUNT) {
			MyApplication.INSTALLED_COUNT++;
			MyApplication.idList.add(mID);
			SpUtils.setInteger(activity_entity, SpUtils.INSTALLED_COUNT,
					MyApplication.INSTALLED_COUNT);
			SpUtils.setString(activity_entity, SpUtils.WHEELS_ID + count, mID);
		}

	}

	public void fullData(int loc,String mId){
		TextView qiYa = (TextView) wheels[loc].findViewById(R.id.qi_ya);
		TextView wenDu = (TextView) wheels[loc].findViewById(R.id.wen_du);
		TextView jiasudu = (TextView) wheels[loc].findViewById(R.id.jiasudu);
		TextView mID = (TextView) wheels[loc].findViewById(R.id.mId);
		qiYa.setText(Constants.YaQiang + "KPa");
		wenDu.setText(Constants.WenDu+ "℃");
		jiasudu.setText(Constants.JiaShudu + "");
		mID.setText("ID:" + mId);	
	}
	public void onUpdateWheels(int loc, int qy, int wd, int jsd, String mId) {
       
		if (loc == 100) {
		}else{

			Log.i("TAG", "现在的位置：" + loc + "轮子数量：" + SpUtils.WHEELS_COUNT);
			TextView qiYa = (TextView) wheels[loc].findViewById(R.id.qi_ya);
			TextView wenDu = (TextView) wheels[loc].findViewById(R.id.wen_du);
			TextView jiasudu = (TextView) wheels[loc].findViewById(R.id.jiasudu);
			TextView mID = (TextView) wheels[loc].findViewById(R.id.mId);
			qiYa.setTextColor(android.graphics.Color.WHITE);
			wenDu.setTextColor(android.graphics.Color.WHITE);
			jiasudu.setTextColor(android.graphics.Color.WHITE);
			qiYa.setText(qy + "KPa");
			wenDu.setText(wd + "℃");
			jiasudu.setText(jsd+"");
			mID.setText("ID:" + mId);		
			if(loc==0){
				count0++;
				//re0.setText("该传感器信息数："+count0);
				re0.setText("数据已更新!");
			}else if(loc==1){
				count1++;
				//re1.setText("该传感器信息数："+count1);
				re1.setText("数据已更新!");
			}else if(loc==2){
				count2++;
				//re2.setText("该传感器信息数："+count2);
				re2.setText("数据已更新!");
			}else if(loc==3){
				count3++;
				//re3.setText("该传感器信息数："+count3);
				re3.setText("数据已更新!");
			}else if(loc==4){
				count4++;
				//re4.setText("该传感器信息数："+count4);
				re4.setText("数据已更新!");
			}else if(loc==5){				
				count5++;
				//re5.setText("该传感器信息数："+count5);
				re5.setText("数据已更新!");
			}else if(loc==6){
				count6++;
				//re6.setText("该传感器信息数："+count6);
				re6.setText("数据已更新!");
			}else if(loc==7){
				count7++;
		        //re7.setText("该传感器信息数："+count7);
				re7.setText("数据已更新!");
			}else if(loc==8){
				count8++;
				//re8.setText("该传感器信息数："+count8);
				re8.setText("数据已更新!");
			}else if(loc==9){
				count9++;
				//re9.setText("该传感器信息数："+count9);
				re9.setText("数据已更新!");
			}else if(loc==10){
				count10++;
				//re10.setText("该传感器信息数："+count10);
				re10.setText("数据已更新!");
			}else if(loc==11){
				count11++;
				//re11.setText("该传感器信息数："+count11);
				re11.setText("数据已更新!");
			}
		
//			if (SpUtils.getBoolean(MonitorActivityTwo.this, SpUtils.AIDFLAGONE,
//					true)) {
				Log.i("得到的数据：", "接收到:"+"温度上限："+wenduTop+"温度下限："+wenduBot+"压强上限："+yaqiangTop+"压强下线："+yaqiangBot);  
				Log.i("得到的数据：", "接收到:"+"温度上限："+gwenduTop+"温度下限："+gwenduBot+"压强上限："+gyaqiangTop+"压强下线："+gyaqiangBot);
			if(SpUtils.getString(MonitorActivityTwo.this, "cheXing", "").equals("small")){
			
				if (qy < Integer.parseInt(yaqiangBot)|| qy > Integer.parseInt(yaqiangTop) || wd > Integer.parseInt(wenduTop) || wd < Integer.parseInt(wenduBot)) {
					
				  SpUtils.setBoolean(MonitorActivityTwo.this,SpUtils.AIDFLAGONE, true);
//					// 闪烁
					//Log.e(TAG,"0位置报警");
				  qiYa.setTextColor(android.graphics.Color.RED);
				  wenDu.setTextColor(android.graphics.Color.RED);
				  jiasudu.setTextColor(android.graphics.Color.RED);
					if(loc==0){
						count0++;
						//re0.setText("该传感器信息数："+count0);
						re0.setText("请注意胎压!"+count0);
						re0.setTextColor(android.graphics.Color.RED);
					}else if(loc==1){
						count1++;
						//re1.setText("该传感器信息数："+count1);
						re1.setText("请注意胎压!"+count1);
						re1.setTextColor(android.graphics.Color.RED);
					}else if(loc==2){
						count2++;
						//re2.setText("该传感器信息数："+count2);
						re2.setText("请注意胎压!"+count2);
						re2.setTextColor(android.graphics.Color.RED);
					}else if(loc==3){
						count3++;
						//re3.setText("该传感器信息数："+count3);
						re3.setText("请注意胎压!"+count3);
						re3.setTextColor(android.graphics.Color.RED);
					}else if(loc==4){
						count4++;
						//re4.setText("该传感器信息数："+count4);
						re4.setText("请注意胎压!");
						re4.setTextColor(android.graphics.Color.RED);
					}else if(loc==5){				
						count5++;
						//re5.setText("该传感器信息数："+count5);
						re5.setText("请注意胎压!");
						re5.setTextColor(android.graphics.Color.RED);
					}else if(loc==6){
						count6++;
						//re6.setText("该传感器信息数："+count6);
						re6.setText("请注意胎压!");
						re6.setTextColor(android.graphics.Color.RED);
					}else if(loc==7){
						count7++;
				        //re7.setText("该传感器信息数："+count7);
						re7.setText("请注意胎压!");
						re7.setTextColor(android.graphics.Color.RED);
					}else if(loc==8){
						count8++;
						//re8.setText("该传感器信息数："+count8);
						re8.setText("请注意胎压!");
						re8.setTextColor(android.graphics.Color.RED);
					}else if(loc==9){
						count9++;
						//re9.setText("该传感器信息数："+count9);
						re9.setText("请注意胎压!");
						re9.setTextColor(android.graphics.Color.RED);
					}else if(loc==10){
						count10++;
						//re10.setText("该传感器信息数："+count10);
						re10.setText("请注意胎压!");
						re10.setTextColor(android.graphics.Color.RED);
					}else if(loc==11){
						count11++;
						//re11.setText("该传感器信息数："+count11);
						re11.setText("请注意胎压!");
						re11.setTextColor(android.graphics.Color.RED);
					}
//				    Resources res = getResources(); //resource handle
//					Drawable drawable = res.getDrawable(R.drawable.alert); //new Image that was added to the res folder					
//					wheels[loc].setBackgroundDrawable(drawable);
					//wheels[loc].setBackgroundResource(R.anim.circle);
//					circleAnimation= (AnimationDrawable) wheels[loc].getBackground();
//					circleAnimation.start();
					if (mPlayer == null) {
						mPlayer = MediaPlayer.create(MonitorActivityTwo.this,R.raw.voi);
						mPlayer.start();
                        mPlayer.setOnErrorListener(new OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer mp, int what, int extra) {
                                        mPlayer.reset();
                                        Log.d(TAG,"media error!!！");
                                        return false;
                                }
                        });
                     } else {
                    	mPlayer.stop();
                        mPlayer.reset();
                        mPlayer = MediaPlayer.create(MonitorActivityTwo.this,R.raw.voi);
						mPlayer.start();
                     }	
					Log.i("报警：","开始报警！");
					Message msg = new Message();
					msg.what = 0;
					msg.obj = loc;
					handler.sendMessage(msg);
				 
				}else {}
			}else if(SpUtils.getString(MonitorActivityTwo.this, "cheXing", "").equals("big")){
				
				if (qy < Integer.parseInt(gyaqiangBot)|| qy > Integer.parseInt(gyaqiangTop) || wd > Integer.parseInt(gwenduTop) || wd < Integer.parseInt(gwenduBot)) {
					
					  SpUtils.setBoolean(MonitorActivityTwo.this,SpUtils.AIDFLAGONE, true);
//						// 闪烁
						//Log.e(TAG,"0位置报警");
					  qiYa.setTextColor(android.graphics.Color.RED);
					  wenDu.setTextColor(android.graphics.Color.RED);
					  jiasudu.setTextColor(android.graphics.Color.RED);
						if(loc==0){
							count0++;
							//re0.setText("该传感器信息数："+count0);
							re0.setText("请注意胎压!");
							re0.setTextColor(android.graphics.Color.RED);
						}else if(loc==1){
							count1++;
							//re1.setText("该传感器信息数："+count1);
							re1.setText("请注意胎压!");
							re1.setTextColor(android.graphics.Color.RED);
						}else if(loc==2){
							count2++;
							//re2.setText("该传感器信息数："+count2);
							re2.setText("请注意胎压!");
							re2.setTextColor(android.graphics.Color.RED);
						}else if(loc==3){
							count3++;
							//re3.setText("该传感器信息数："+count3);
							re3.setText("请注意胎压!");
							re3.setTextColor(android.graphics.Color.RED);
						}else if(loc==4){
							count4++;
							//re4.setText("该传感器信息数："+count4);
							re4.setText("请注意胎压!");
							re4.setTextColor(android.graphics.Color.RED);
						}else if(loc==5){				
							count5++;
							//re5.setText("该传感器信息数："+count5);
							re5.setText("请注意胎压!");
							re5.setTextColor(android.graphics.Color.RED);
						}else if(loc==6){
							count6++;
							//re6.setText("该传感器信息数："+count6);
							re6.setText("请注意胎压!");
							re6.setTextColor(android.graphics.Color.RED);
						}else if(loc==7){
							count7++;
					        //re7.setText("该传感器信息数："+count7);
							re7.setText("请注意胎压!");
							re7.setTextColor(android.graphics.Color.RED);
						}else if(loc==8){
							count8++;
							//re8.setText("该传感器信息数："+count8);
							re8.setText("请注意胎压!");
							re8.setTextColor(android.graphics.Color.RED);
						}else if(loc==9){
							count9++;
							//re9.setText("该传感器信息数："+count9);
							re9.setText("请注意胎压!");
							re9.setTextColor(android.graphics.Color.RED);
						}else if(loc==10){
							count10++;
							//re10.setText("该传感器信息数："+count10);
							re10.setText("请注意胎压!");
							re10.setTextColor(android.graphics.Color.RED);
						}else if(loc==11){
							count11++;
							//re11.setText("该传感器信息数："+count11);
							re11.setText("请注意胎压!");
							re11.setTextColor(android.graphics.Color.RED);
						}

						if (mPlayer == null) {
							mPlayer = MediaPlayer.create(MonitorActivityTwo.this,R.raw.voi);
							mPlayer.start();
	                        mPlayer.setOnErrorListener(new OnErrorListener() {
	                                @Override
	                                public boolean onError(MediaPlayer mp, int what, int extra) {
	                                        mPlayer.reset();
	                                        Log.d(TAG,"media error!!！");
	                                        return false;
	                                }
	                        });
	                     } else {
	                    	mPlayer.stop();
	                        mPlayer.reset();
	                        mPlayer = MediaPlayer.create(MonitorActivityTwo.this,R.raw.voi);
							mPlayer.start();
	                     }	
						Log.i("报警：","开始报警！");
						Message msg = new Message();
						msg.what = 0;
						msg.obj = loc;
						handler.sendMessage(msg);
					 
					}else {}
			}
			
			
		  } 
		}

	private void initTirePressure() {
		// String s = SpUtils.getString(this, SpUtils.TIRE_PRESSURE, null);
		if (wheelsCount <= 4) {
			wheels[0] = findViewById(R.id.wheel_1);
			wheels[1] = findViewById(R.id.wheel_2);
			wheels[2] = findViewById(R.id.wheel_3);
			wheels[3] = findViewById(R.id.wheel_4);
		} else if(wheelsCount == 6){
			wheels[0] = findViewById(R.id.wheel_1);
			wheels[1] = findViewById(R.id.wheel_2);
			wheels[2] = findViewById(R.id.wheel_3);
			wheels[3] = findViewById(R.id.wheel_4);
			wheels[4] = findViewById(R.id.wheel_5);
			wheels[5] = findViewById(R.id.wheel_6);
		}else if(wheelsCount == 8){
			wheels[0] = findViewById(R.id.wheel_1);
			wheels[1] = findViewById(R.id.wheel_2);
			wheels[2] = findViewById(R.id.wheel_3);
			wheels[3] = findViewById(R.id.wheel_4);
			wheels[4] = findViewById(R.id.wheel_5);
			wheels[5] = findViewById(R.id.wheel_6);
			wheels[6] = findViewById(R.id.wheel_7);
			wheels[7] = findViewById(R.id.wheel_8);
		}else if(wheelsCount == 10){
			wheels[0] = findViewById(R.id.wheel_1);
			wheels[1] = findViewById(R.id.wheel_2);
			wheels[2] = findViewById(R.id.wheel_3);
			wheels[3] = findViewById(R.id.wheel_4);
			wheels[4] = findViewById(R.id.wheel_5);
			wheels[5] = findViewById(R.id.wheel_6);
			wheels[6] = findViewById(R.id.wheel_7);
			wheels[7] = findViewById(R.id.wheel_8);
			wheels[8] = findViewById(R.id.wheel_9);
			wheels[9] = findViewById(R.id.wheel_10);
		}else if(wheelsCount == 12){
			wheels[0] = findViewById(R.id.wheel_1);
			wheels[1] = findViewById(R.id.wheel_2);
			wheels[2] = findViewById(R.id.wheel_3);
			wheels[3] = findViewById(R.id.wheel_4);
			wheels[4] = findViewById(R.id.wheel_5);
			wheels[5] = findViewById(R.id.wheel_6);
			wheels[6] = findViewById(R.id.wheel_7);
			wheels[7] = findViewById(R.id.wheel_8);
			wheels[8] = findViewById(R.id.wheel_9);
			wheels[9] = findViewById(R.id.wheel_10);
			wheels[10] = findViewById(R.id.wheel_eleven);
			wheels[11] = findViewById(R.id.wheel_twlve);
		}
	}

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
		//unregisterReceiver(receiver);
		super.onStop();

	}

	/**
	 * destroy时，退出程序，退出服务
	 */
	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		 if(mPlayer != null){
			 mPlayer.release();
		 }
		unregisterReceiver(receiver);
		super.onDestroy();
		mgr.closeDB();
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
		if(requestCode==011&&resultCode==RESULT_OK){
			String result0 ="-1";
			result0 = data.getExtras().getString("result0");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result0："+ result0);
		     if(result0.equals("1")){
					TextView qiYa = (TextView) wheels[0].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[0].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[0].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[0].findViewById(R.id.mId);
					qiYa.setText("2.3" + "KPa");
					wenDu.setText("12"+ "℃");
					jiasudu0.setText("34");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID0, ""));
				}else{
					
				}
		     String result1="-1";
		     result1 = data.getExtras().getString("result1");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result1："+ result1);
		     if(result1.equals("1")){
					TextView qiYa = (TextView) wheels[1].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[1].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[1].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[1].findViewById(R.id.mId);
					qiYa.setText("2.1" + "KPa");
					wenDu.setText("8"+ "℃");
					jiasudu0.setText("29");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID1, ""));
				}else{
					
				}
		     String result2="-1";
		     result2 = data.getExtras().getString("result2");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result2："+ result2);
		     if(result2.equals("1")){
					TextView qiYa = (TextView) wheels[2].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[2].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[2].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[2].findViewById(R.id.mId);
					qiYa.setText("2.2" + "KPa");
					wenDu.setText("13"+ "℃");
					jiasudu0.setText("32");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID2, ""));
				}else{
					
				}
		     String result3="-1";
		     result3 = data.getExtras().getString("result3");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result3："+ result3);
		     if(result3.equals("1")){
					TextView qiYa = (TextView) wheels[3].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[3].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[3].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[3].findViewById(R.id.mId);
					qiYa.setText("2.1" + "KPa");
					wenDu.setText("15"+ "℃");
					jiasudu0.setText("33");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID3, ""));
				}else{
					
				}
		     String result4="-1";
		     result4 = data.getExtras().getString("result4");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result4："+ result4);
		     if(result4.equals("1")){
					TextView qiYa = (TextView) wheels[4].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[4].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[4].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[4].findViewById(R.id.mId);
					qiYa.setText("2.4" + "KPa");
					wenDu.setText("14"+ "℃");
					jiasudu0.setText("31");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID4, ""));
				}else{
					
				}
		     String result5="-1";
		     result5 = data.getExtras().getString("result5");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result5："+ result5);
		     if(result5.equals("1")){
					TextView qiYa = (TextView) wheels[5].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[5].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[5].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[5].findViewById(R.id.mId);
					qiYa.setText("2.1" + "KPa");
					wenDu.setText("13"+ "℃");
					jiasudu0.setText("34");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID5, ""));
				}else{
					
				}
		     String result6="-1";
		     result6 = data.getExtras().getString("result6");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result6："+ result6);
		     if(result6.equals("1")){
					TextView qiYa = (TextView) wheels[6].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[6].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[6].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[6].findViewById(R.id.mId);
					qiYa.setText("2.1" + "KPa");
					wenDu.setText("13"+ "℃");
					jiasudu0.setText("34");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID6, ""));
				}else{
					
				}
		     String result7="-1";
		     result7 = data.getExtras().getString("result7");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result7："+ result7);
		     if(result7.equals("1")){
					TextView qiYa = (TextView) wheels[7].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[7].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[7].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[7].findViewById(R.id.mId);
					qiYa.setText("2.1" + "KPa");
					wenDu.setText("13"+ "℃");
					jiasudu0.setText("34");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID7, ""));
				}else{
					
				}
		     String result8="-1";
		     result8 = data.getExtras().getString("result8");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result8："+ result8);
		     if(result8.equals("1")){
					TextView qiYa = (TextView) wheels[8].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[8].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[8].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[8].findViewById(R.id.mId);
					qiYa.setText("2.1" + "KPa");
					wenDu.setText("13"+ "℃");
					jiasudu0.setText("34");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID8, ""));
				}else{
					
				}
		     String result9="-1";
		     result9 = data.getExtras().getString("result9");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result9："+ result9);
		     if(result9.equals("1")){
					TextView qiYa = (TextView) wheels[9].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[9].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[9].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[9].findViewById(R.id.mId);
					qiYa.setText("2.1" + "KPa");
					wenDu.setText("13"+ "℃");
					jiasudu0.setText("34");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID9, ""));
				}else{
					
				}
		     String result10="-1";
		     result10 = data.getExtras().getString("result10");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result10："+ result10);
		     if(result10.equals("1")){
					TextView qiYa = (TextView) wheels[10].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[10].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[10].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[10].findViewById(R.id.mId);
					qiYa.setText("2.1" + "KPa");
					wenDu.setText("13"+ "℃");
					jiasudu0.setText("34");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID10, ""));
				}else{
					
				}
		     String result11="-1";
		     result11 = data.getExtras().getString("result11");//得到新Activity 关闭后返回的数据
		     Log.i(TAG,"测试result11："+ result11);
		     if(result11.equals("1")){
					TextView qiYa = (TextView) wheels[11].findViewById(R.id.qi_ya);
					TextView wenDu = (TextView) wheels[11].findViewById(R.id.wen_du);
					TextView jiasudu0 = (TextView) wheels[11].findViewById(R.id.jiasudu);
					TextView mID0 = (TextView) wheels[11].findViewById(R.id.mId);
					qiYa.setText("2.1" + "KPa");
					wenDu.setText("13"+ "℃");
					jiasudu0.setText("34");
					mID0.setText("ID:" + SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID11, ""));
				}else{
					
				}
		}
		 
	}

	/**
	 * 注册广播信息
	 */
	private void registerBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.ACTION_BLUETOOTH_BROADCAST);
		if(receiver==null){receiver = new MessageBroadcastReceiver();}
		
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
	
	private void myShowToast(String msg) {
		DialogUtils.alert(this, android.R.drawable.ic_dialog_info, "提示", msg,
				"确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent_broad = new Intent();
						intent_broad.setAction(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_TALK); 
						Log.d("发送广播啦，快接收数据把！", "发送广播啦，快接收数据把");
						activity_entity.sendBroadcast(intent_broad);
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
			String action=intent.getAction();
//			
//			if(action.equals(Constants.DATASTATE)&&Constants.DataStatezero=="0"){
//				String id = intent.getExtras().getString("mID");
//				Log.d(TAG, "已接收到数据为空的广播啦0！！！！！！！！！！！！！");
//				TextView qiYa = (TextView) wheels[0].findViewById(R.id.qi_ya);
//				TextView wenDu = (TextView) wheels[0].findViewById(R.id.wen_du);
//				TextView jiasudu = (TextView) wheels[0].findViewById(R.id.jiasudu);
//				TextView mID = (TextView) wheels[0].findViewById(R.id.mId);
//				qiYa.setText(Constants.YaQiang + "KPa");
//				wenDu.setText(Constants.WenDu+ "℃");
//				jiasudu.setText(Constants.JiaShudu + "");
//				mID.setText("ID:" + id);	
//			}
			int temp = intent.getIntExtra(Constants.MSG, -1);
			Log.i("AAA", "" + temp);
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
					myShowToast("蓝牙连接建立成功");			
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
									intent_broad.setAction(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_CONNECT);
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
					if (SpUtils.getBoolean(MonitorActivityTwo.this,
							SpUtils.AIDFLAGONE, true)) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = -1;
						handler.sendMessage(msg);
					} else {
						// 进入救援地图页面
						Intent intent1 = new Intent(MonitorActivityTwo.this,
								OrderDetailActivity.class);
						intent1.putExtra("weizhi", location);
						startActivity(intent1);
					}
					break;
				default:
					break;

				}
			} else if(action.equals(Constants.ACTION_BLUETOOTH_BROADCAST)){
				
				db.open();
				sensorcount=0;
				Log.d(TAG, "MonitorActivityTwo接受广播(测试得到的数据：)->" + "数据获取");				
				float wendu = intent.getFloatExtra(Constants.WENDU, 0);
				int wenduInt = (int) wendu;
				float yaqiang = intent.getFloatExtra(Constants.YAQIANG, 0);
				int yaqiangInt = (int) yaqiang;
				float jiasudu = intent.getFloatExtra(Constants.JIASUDU, 0);
				int jiasuduInt = (int) jiasudu;
				String mID = intent.getStringExtra(Constants.mID);
				String selected = "0";
				String reliability = "0";
				
				//allcount++;
				//Log.e(TAG, "获取数据总数为::："+allcount);
				Log.i("广播传过来的数据：", "mID:" + mID + "温度：" + wenduInt + "气压："
						+ yaqiangInt + "加速度：" + jiasuduInt);
//				 if(mID.equals("ac6d821c")){
//					   count1++;
//					   Log.e(TAG, "传感器设备号："+mID+"-->第"+count1+"次出现！");
//					}else if(mID.equals("f2b8821c")){
//						count2++;
//						Log.e(TAG, "传感器设备号："+mID+"-->第"+count2+"次出现！");
//					}else if(mID.equals("1918821c")){
//					    count3++;
//					    Log.e(TAG, "传感器设备号："+mID+"-->第"+count3+"次出现！");
//					}else if(mID.equals("4b34821c")){
//						count4++;
//						Log.e(TAG, "传感器设备号："+mID+"-->第"+count4+"次出现！");
//					}
				
//				   if(mID.equals("36bc811c")){
//					   count1++;
//					   Log.e(TAG, "传感器设备号："+mID+"-->第"+count1+"次出现！");
//					}else if(mID.equals("178c821c")){
//						count2++;
//						Log.e(TAG, "传感器设备号："+mID+"-->第"+count2+"次出现！");
//					}else if(mID.equals("a264b81c")){
//					    count3++;
//					    Log.e(TAG, "传感器设备号："+mID+"-->第"+count3+"次出现！");
//					}else if(mID.equals("ad821c")){
//						count4++;
//						Log.e(TAG, "传感器设备号："+mID+"-->第"+count4+"次出现！");
//					}else if(mID.equals("7a5eb81c")){
//						count5++;
//						Log.e(TAG, "传感器设备号："+mID+"-->第"+count5+"次出现！");
//					}
				
//				if(mID.equals("36bc811c")||mID.equals("178c821c")||mID.equals("a264b81c")||mID.equals("ad821c")||mID.equals("7a5eb81c")){
//					Log.i(TAG, "第一次mID:" + mID );
//				}else{
//					Log.i(TAG, "第2次mID:" + mID );
//				if (m == null) {
//					m = MediaPlayer.create(MonitorActivityTwo.this,R.raw.voi);
//					m.start();
//                    m.setOnErrorListener(new OnErrorListener() {
//                            @Override
//                            public boolean onError(MediaPlayer mp, int what, int extra) {
//                                    m.reset();
//                                    Log.d(TAG,"media error!!！");
//                                    return false;
//                            }
//                    });
//                 } else {
//                	m.stop();
//                    m.reset();
//                    m = MediaPlayer.create(MonitorActivityTwo.this,R.raw.voi);
//					m.start();
//                 }		
//				}									
					
				try {
				
					//if(db.getSAMEMID(mID)){
						if(Constants.Tag==1){
						smatch.setVisibility(View.GONE);
					    }else{
						smatch.setVisibility(View.VISIBLE);
					    }
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID0, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang0", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu0", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu0", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID1, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang1", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu1", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu1", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID2, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang2", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu2", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu2", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID3, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang3", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu3", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu3", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID4, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang4", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu4", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu4", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID5, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang5", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu5", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu5", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID6, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang6", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu6", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu6", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID7, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang7", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu7", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu7", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID8, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang8", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu8", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu8", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID9, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang9", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu9", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu9", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID10, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang10", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu10", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu10", jiasuduInt+"");
						}
						if(SpUtils.getString(MonitorActivityTwo.this,Constants.SENSOR_ID11, "").equals(mID)){
							SpUtils.setString(MonitorActivityTwo.this,"yaqiang11", yaqiangInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"wendu11", wenduInt+"");
							SpUtils.setString(MonitorActivityTwo.this,"jiasudu11", jiasuduInt+"");
						}

//					}else{
						
						//Long res = db.insertMID(mID, selected,reliability);		
						Long res = db.insertMID(mID, selected);
						
						//Log.d(TAG, "行数:" + res);
						if (res > 0) {
							sensorcount++;
						} else {
						}
						
						
					//}					
				} finally {

				}
				int loc = 100;
				if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID0, "id0"))) {
					Log.d("TAG",
							"mID0:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID0, "id0"));
					loc = 0;
					
					
		
				} else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID1, "id1"))) {
					Log.d("TAG",
							"mID1:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID1, "id1"));
					loc = 1;
					
				} else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID2, "id2"))) {
					Log.d("TAG",
							"mID2:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID2, "id2"));
					loc = 2;
					
				} else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID3, "id3"))) {
					Log.d("TAG",
							"mID3:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID3, "id3"));
					loc = 3;
					
				} else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID4, "id4"))) {
					Log.d("TAG",
							"mID4:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID4, "id4"));
					loc = 4;
				}else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID5, "id5"))) {
					Log.d("TAG",
							"mID5:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID5, "id5"));
					loc = 5;
				}else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID6, "id6"))) {
					Log.d("TAG",
							"mID6:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID6, "id6"));
					loc = 6;
					
				} else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID7, "id7"))) {
					Log.d("TAG",
							"mID7:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID7, "id7"));
					loc = 7;
				}else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID8, "id8"))) {
					Log.d("TAG",
							"mID8:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID8, "id8"));
					loc = 8;
				} else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID9, "id9"))) {
					Log.d("TAG",
							"mID9:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID9, "id9"));
					loc = 9;
				}else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID10, "id10"))) {
					Log.d("TAG",
							"mID10:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID10, "id10"));
					loc = 10;
				}else if (mID.equals(SpUtils.getString(MonitorActivityTwo.this,
						Constants.SENSOR_ID11, "id11"))) {
					Log.d("TAG",
							"mID11:"
									+ mID
									+ SpUtils.getString(MonitorActivityTwo.this,
											Constants.SENSOR_ID11, "id11"));
					loc = 11;
				}else {
					if (sensorcount == 0) {
					} else {
						Toast.makeText(MonitorActivityTwo.this, "已扫描到传感器~",
								Toast.LENGTH_LONG).show();
					}
					Log.d(TAG, "没有配对");
				}
				onUpdateWheels(loc, yaqiangInt, wenduInt, jiasuduInt, mID);
				
				
			}

		}
	}
	
    
}

