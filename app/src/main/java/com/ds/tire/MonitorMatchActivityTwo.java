package com.ds.tire;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ds.tire.bluetooth.Constants;
import com.ds.tire.bluetooth.DeviceList;
import com.ds.tire.bluetooth.GetDataService;
import com.ds.tire.db.DBManager;
import com.ds.tire.db.DBOperate;
import com.ds.tire.db.ReData;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.MyApplication;
import com.ds.tire.util.SpUtils;

public class MonitorMatchActivityTwo extends Activity {

	private String TAG = "TAG";
	private MonitorMatchActivityTwo activity_entity;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothDevice device;

	private ProgressDialog dialog = null;
	LayoutInflater inflater = null;
	View layout = null;
	Button finish;
	Button clear;
	View back;
	MediaPlayer mPlayer;
	AnimationDrawable circle;
	private int wheelsCount = 0;
	private View[] wheels;
	int location = -1;
	DBOperate db = null;
	int count = 0;
	String MID = "sensor_name";

	ArrayList<ReData> sdatas = new ArrayList<ReData>();
	ArrayList<String> mid = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		db = new DBOperate(this);
		if (intent == null) {
			finish();
		}
		wheelsCount = intent.getIntExtra("wheels_count", SpUtils.getInteger(
				MonitorMatchActivityTwo.this, "wheels_count", 0));
		Log.i("TAG", "wheelsCount" + wheelsCount);

		if (wheelsCount <= 4) {
			wheels = new View[4];
			setContentView( R.layout.monitor_match4);
		} else if(wheelsCount==6) {
			wheels = new View[6];
			setContentView( R.layout.monitor_match6);
		}else if(wheelsCount==8) {
			wheels = new View[8];
			setContentView( R.layout.monitor_match8);
		}else if(wheelsCount==10) {
			wheels = new View[10];
			setContentView( R.layout.monitor_match10);
		}else if(wheelsCount==12) {
			wheels = new View[12];
			setContentView( R.layout.monitor_match12);
		}
//		if (wheelsCount <= 4) {
//			wheels = new View[4];
//		} else {
//			wheels = new View[6];
//		}
//		setContentView(wheelsCount <= 4 ? R.layout.monitor_match4
//				: R.layout.monitor_match6);
		activity_entity = this;
		finish = (Button) findViewById(R.id.finish);
		clear = (Button) findViewById(R.id.clear);
		back = findViewById(R.id.monitor_head_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//MonitorMatchActivityTwo.this.finish();
				 Intent intent = new Intent();
	                //把返回数据存入Intent
				 String result0=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatezero", "");
				 String result1=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateone", "");
				 String result2=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatetwo", "");
				 String result3=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatethree", "");
				 String result4=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatefour", "");
				 String result5=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatefive", "");
				 String result6=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatesix", "");
				 String result7=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateseven", "");
				 String result8=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateeight", "");
				 String result9=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatenine", "");
				 String result10=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateten", "");
				 String result11=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateeleven", "");
	                intent.putExtra("result0",result0);
	                intent.putExtra("result1",result1);
	                intent.putExtra("result2",result2);
	                intent.putExtra("result3",result3);
	                intent.putExtra("result4",result4);
	                intent.putExtra("result5",result5);
	                intent.putExtra("result6",result6);
	                intent.putExtra("result7",result7);
	                intent.putExtra("result8",result8);
	                intent.putExtra("result9",result9);
	                intent.putExtra("result10",result10);
	                intent.putExtra("result11",result11);
	                //设置返回数据
	                MonitorMatchActivityTwo.this.setResult(RESULT_OK, intent);
	                //关闭Activity
	                MonitorMatchActivityTwo.this.finish();
			}
		});
		finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				MonitorMatchActivityTwo.this.finish();
			}
		});
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				db.open();
				db.deletemid();
				Toast.makeText(MonitorMatchActivityTwo.this, "设备已清除~",
						Toast.LENGTH_SHORT).show();
				db.close();				
					Constants.Tag=1;
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatezero", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateone", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatetwo", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatethree", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatefour", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatefive", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatesix", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateseven", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateeight", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatenine", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateten", "-1");
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateeleven", "-1");
			}
		});
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatezero", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateone", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatetwo", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatethree", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatefour", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatefive", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatesix", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateseven", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateeight", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatenine", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateten", "-1");
		SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateeleven", "-1");
		initTirePressure();
		if (wheelsCount <= 4) {
			onUpdateWheels(0);

			onUpdateWheels(1);

			onUpdateWheels(2);

			onUpdateWheels(3);

		} else if(wheelsCount==6){
			onUpdateWheels(0);
			onUpdateWheels(1);
			onUpdateWheels(2);
			onUpdateWheels(3);
			onUpdateWheels(4);
			onUpdateWheels(5);
		}else if(wheelsCount==8){
			onUpdateWheels(0);
			onUpdateWheels(1);
			onUpdateWheels(2);
			onUpdateWheels(3);
			onUpdateWheels(4);
			onUpdateWheels(5);
			onUpdateWheels(6);
			onUpdateWheels(7);
		
		}else if(wheelsCount==10){
			onUpdateWheels(0);
			onUpdateWheels(1);
			onUpdateWheels(2);
			onUpdateWheels(3);
			onUpdateWheels(4);
			onUpdateWheels(5);
			onUpdateWheels(6);
			onUpdateWheels(7);
			onUpdateWheels(8);
			onUpdateWheels(9);
		}else if(wheelsCount==12){
			onUpdateWheels(0);
			onUpdateWheels(1);
			onUpdateWheels(2);
			onUpdateWheels(3);
			onUpdateWheels(4);
			onUpdateWheels(5);
			onUpdateWheels(6);
			onUpdateWheels(7);
			onUpdateWheels(8);
			onUpdateWheels(9);
			onUpdateWheels(10);
			onUpdateWheels(11);
		}

	}

	public void onUpdateData(String mID, int qy, int wd, int jsd) {
		Log.i("TAG", "安装的轮子数量：" + MyApplication.INSTALLED_COUNT + "现在的数量："
				+ count);
		// int count = 0;
		while (count < MyApplication.INSTALLED_COUNT) {
			if (mID.equalsIgnoreCase(MyApplication.idList.get(count))) {
				// onUpdateWheels(count, qy, wd, jsd);
				break;

			}
		}
		if (count == MyApplication.INSTALLED_COUNT) {
			MyApplication.INSTALLED_COUNT++;
			MyApplication.idList.add(mID);
			SpUtils.setInteger(activity_entity, SpUtils.INSTALLED_COUNT,
					MyApplication.INSTALLED_COUNT);
			SpUtils.setString(activity_entity, SpUtils.WHEELS_ID + count, mID);
			// onUpdateWheels(count, qy, wd, jsd);
		}

	}

	public void onUpdateWheels(int loc) {
		final int Loc = loc;
		// ReData reData = new ReData(mId, qy, wd, jsd);
		// sdatas.add(reData);
		// mgr.add(sdatas);
		Log.i("TAG", "现在的位置：" + loc + "轮子数量：" + SpUtils.WHEELS_COUNT);

		final TextView mID = (TextView) wheels[loc].findViewById(R.id.mId);
		TextView addID = (TextView) wheels[loc].findViewById(R.id.addId);
		TextView updateID = (TextView) wheels[loc].findViewById(R.id.updateId);
		TextView clearID = (TextView) wheels[loc].findViewById(R.id.clearId);
		if (loc == 0) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID0, "sensor0"));
		} else if (loc == 1) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID1, "sensor1"));
		} else if (loc == 2) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID2, "sensor2"));
		} else if (loc == 3) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID3, "sensor3"));
		} else if (loc == 4) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID4, "sensor4"));
		} else if (loc == 5) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID5, "sensor5"));
		} else if (loc == 6) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID6, "sensor6"));
		} else if (loc == 7) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID7, "sensor7"));
		} else if (loc == 8) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID8, "sensor8"));
		}else if (loc == 9) {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID9, "sensor9"));
		}else if (loc== 10) {
			Log.i("TAG", "现测试sensor0："+SpUtils.getString(MonitorMatchActivityTwo.this,Constants.SENSOR_ID10,"sensor10"));
			//String sensor10=SpUtils.getString(MonitorMatchActivityTwo.this,Constants.SENSOR_ID10,"sensor10");
			mID.setText("ID:"+ SpUtils.getString(MonitorMatchActivityTwo.this,Constants.SENSOR_ID10,"sensor10"));
			//mID.setText("ID:"+sensor10);
		}else {
			mID.setText("ID:"
					+ SpUtils.getString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID11, "sensor11"));
		}
		addID.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String res = null;
				db.open();
				res = db.getMID();

				if (res != null) {
					if (res.equals("[]")) {
						Toast.makeText(MonitorMatchActivityTwo.this, "没有扫描到传感器~",
								Toast.LENGTH_SHORT).show();
					} else {
						ArrayList<String> sensor = new ArrayList<String>();
						try {
							JSONArray array = new JSONArray(res);
							for (int i = 0; i < array.length(); i++) {
								JSONObject object = array.getJSONObject(i);
								sensor.add(object.getString("mId"));    
								System.out.print("Object对象："+object);
							}
							showPop(sensor, mID, Loc);

						} catch (JSONException e) {
							e.printStackTrace();
						} finally {
							db.close();
						}
					}
				} else {
					Toast.makeText(MonitorMatchActivityTwo.this, "没有扫描到传感器~",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		updateID.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String res = null;
				db.open();
				res = db.getMID();

				if (res != null) {
					if (res.equals("[]")) {
						Toast.makeText(MonitorMatchActivityTwo.this, "没有扫描到传感器~",
								Toast.LENGTH_SHORT).show();
					} else {
						ArrayList<String> sensor = new ArrayList<String>();
						try {
							JSONArray array = new JSONArray(res);
							for (int i = 0; i < array.length(); i++) {
								JSONObject object = array.getJSONObject(i);
								sensor.add(object.getString("mId"));
							}
							showPop(sensor, mID, Loc);
						} catch (JSONException e) {
							e.printStackTrace();
						} finally {
							
							db.close();
						}
					}
				} else {
					Toast.makeText(MonitorMatchActivityTwo.this, "没有扫描到传感器~",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		if(loc == 0){
			clearID.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mID.setText("ID:sensor0");
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID0, "sensor0");
					SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStatezero", "-1");
				}
			});
		 }else if (loc == 1) {
				clearID.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						mID.setText("ID:sensor1");
						SpUtils.setString(MonitorMatchActivityTwo.this,
								Constants.SENSOR_ID1, "sensor1");
						SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStateone", "-1");
					}
				});
			}else if (loc == 2) {
				clearID.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							mID.setText("ID:sensor2");
							SpUtils.setString(MonitorMatchActivityTwo.this,
									Constants.SENSOR_ID2, "sensor2");
							SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatetwo", "-1");
						}
					});
				} else if(loc==3){
					clearID.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							mID.setText("ID:sensor3");
							SpUtils.setString(MonitorMatchActivityTwo.this,
									Constants.SENSOR_ID3, "sensor3");
							SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStatethree", "-1");
						}
					});
					}else if(loc==4){
							clearID.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								mID.setText("ID:sensor4");
								SpUtils.setString(MonitorMatchActivityTwo.this,
										Constants.SENSOR_ID4, "sensor4");
								SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStatefour", "-1");
							}
						});
							}else if(loc==5){
									clearID.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {

											mID.setText("ID:sensor5");
											SpUtils.setString(MonitorMatchActivityTwo.this,
													Constants.SENSOR_ID5, "sensor5");
											SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStatefive", "-1");
										}
									});
									}else if(loc==6){
											clearID.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												mID.setText("ID:sensor6");
												SpUtils.setString(MonitorMatchActivityTwo.this,
														Constants.SENSOR_ID6, "sensor6");
												SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStatesix", "-1");
											}
										});
									}else if(loc==7){
											clearID.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												mID.setText("ID:sensor7");
												SpUtils.setString(MonitorMatchActivityTwo.this,
														Constants.SENSOR_ID7, "sensor7");
												SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStateseven", "-1");
											}
										});
									}else if(loc==8){
											clearID.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												mID.setText("ID:sensor8");
												SpUtils.setString(MonitorMatchActivityTwo.this,
														Constants.SENSOR_ID8, "sensor8");
												SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStateeight", "-1");
											}
										});
									}else if(loc==9){
											clearID.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												mID.setText("ID:sensor9");
												SpUtils.setString(MonitorMatchActivityTwo.this,
														Constants.SENSOR_ID9, "sensor9");
												SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStatenine", "-1");
											}
										});
									}else if(loc==10){
											clearID.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												mID.setText("ID:sensor10");
												SpUtils.setString(MonitorMatchActivityTwo.this,
														Constants.SENSOR_ID10, "sensor10");
												SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStateten", "-1");
											}
										});
									}else if(loc==11){
											clearID.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												mID.setText("ID:sensor11");
												SpUtils.setString(MonitorMatchActivityTwo.this,
														Constants.SENSOR_ID11, "sensor11");
												SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStateeleven", "-1");
											}
										});
									}
	                     }	
							    

					
//								clearID.setOnClickListener(new OnClickListener(){
//									@Override
//									public void onClick(View v) {
//
//										mID.setText("ID:sensor5");
//										SpUtils.setString(MonitorMatchActivityTwo.this,
//												Constants.SENSOR_ID5, "sensor5");
//										SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatefive", "-1");
//									}
//								});
				
				
		
		
	

	private void initTirePressure() {
		// String s = SpUtils.getString(this, SpUtils.TIRE_PRESSURE, null);
		if (wheelsCount <= 4) {
			wheels[0] = findViewById(R.id.wheel_1);
			wheels[1] = findViewById(R.id.wheel_2);
			wheels[2] = findViewById(R.id.wheel_3);
			wheels[3] = findViewById(R.id.wheel_4);
		}if(wheelsCount == 6){
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

	// void refreshmid(TextView mid) {
	// String res = null;
	// db.open();
	// res = db.getMID();
	//
	// if (res != null) {
	// if (res.equals("[]")) {
	//
	// } else {
	// ArrayList<String> clothes = new ArrayList<String>();
	// try {
	// JSONArray array = new JSONArray(res);
	// for (int i = 0; i < array.length(); i++) {
	// JSONObject object = array.getJSONObject(i);
	// clothes.add(object.getString("mId"));
	// }
	// showPop(clothes, mid);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// } finally {
	// db.close();
	// }
	// }
	// } else {
	// // Toast.makeText(this, "请同步轮胎类别！", Toast.LENGTH_SHORT)
	// // .show();
	// }
	// }
	private void showPop(final ArrayList<String> items, final TextView parent,
			final int loc) {
		// parent.setText("");
		final Dialog progressDialog = new Dialog(this, R.style.MyDialogStyle);
		View view = LayoutInflater.from(this)
				.inflate(R.layout.pop_dialog, null);
		ListView popList = (ListView) view.findViewById(R.id.pop_list);
		// popList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		popList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.pop_dialog_item, R.id.pop_dialog_item_tv, items));
		progressDialog.setContentView(view, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progressDialog.setCanceledOnTouchOutside(true);
		progressDialog.setCancelable(true);
		popList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				parent.setText(items.get(arg2));
				MID = items.get(arg2);
//				if(MID!=null&&Constants.DataState==0){
////					Intent intent_broad = new Intent();
////					intent_broad.setAction(Constants.DATASTATE); 
////					Log.d("发送广播啦", "数据状态为空！！！！！！！！！！！！！");
////					activity_entity.sendBroadcast(intent_broad);
//				}else{
//					
//				}
				Log.i("TAG", "安装：" + MID + "现在的：" + items.get(arg2));
				if (loc == 0) {
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID0, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this,"DataStatezero", "1");
					Log.i("TAG", "设置0：" + MID + "现在的：" + items.get(arg2));
				} else if (loc == 1) {
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID1, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStateone", "1");
					Log.i("TAG", "设置1：" + MID + "现在的：" + items.get(arg2));
				} else if (loc == 2) {
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID2, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStatetwo", "1");
					Log.i("TAG", "设置2：" + MID + "现在的：" + items.get(arg2));
				} else if (loc == 3){
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID3, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStatethree", "1");
					Log.i("TAG", "设置3：" + MID + "现在的：" + items.get(arg2));
				}else if (loc == 4){
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID4, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatefour", "1");
					Log.i("TAG", "设置4：" + MID + "现在的：" + items.get(arg2));
				}else if (loc == 5){
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID5, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatefive", "1");
					Log.i("TAG", "设置5：" + MID + "现在的：" + items.get(arg2));
				}else if (loc == 6){
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID6, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatesix", "1");
					Log.i("TAG", "设置6：" + MID + "现在的：" + items.get(arg2));
				}else if (loc == 7){
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID7, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateseven", "1");
					Log.i("TAG", "设置7：" + MID + "现在的：" + items.get(arg2));
				}else if (loc == 8){
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID8, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateeight", "1");
					Log.i("TAG", "设置8：" + MID + "现在的：" + items.get(arg2));
				}else if (loc == 9){
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID9, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStatenine", "1");
					Log.i("TAG", "设置9：" + MID + "现在的：" + items.get(arg2));
				}else if (loc == 10){
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID10, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this, "DataStateten", "1");
					Log.i("TAG", "设置10：" + MID + "现在的：" + items.get(arg2));
				}else {
					SpUtils.setString(MonitorMatchActivityTwo.this,
							Constants.SENSOR_ID11, MID);
					SpUtils.setString(MonitorMatchActivityTwo.this,  "DataStateeleven", "1");
				}
				switch (parent.getId()) {

				default:
					break;
				}
				Log.i("TAG", "设置00000："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStatezero", ""));
				Log.i("TAG", "设置222："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStateone", ""));
				Log.i("TAG", "设置222："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStatetwo", ""));
				Log.i("TAG", "设置333："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStatethree", ""));
				Log.i("TAG", "设置444："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStatefour", ""));
				Log.i("TAG", "设置555："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStatefive", ""));
				Log.i("TAG", "设置666："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStatesix", ""));
				Log.i("TAG", "设置777："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStateseven", ""));
				Log.i("TAG", "设置888："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStateeight", ""));
				Log.i("TAG", "设置999："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStatenine", ""));
				Log.i("TAG", "设置1010："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStateten", ""));
				Log.i("TAG", "设置1111："+SpUtils.getString(MonitorMatchActivityTwo.this,  "DataStateeleven", ""));
				
				progressDialog.dismiss();
			}
		});
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		} else {
			progressDialog.dismiss();
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
		//registerBroadcastReceiver();
		Log.d(TAG, "注册广播成功");

	}

	/**
	 * stop时注销掉广播接收器
	 */
	@Override
	protected void onStop() {
		// unregisterReceiver(receiver);
		super.onStop();

	}

	/**
	 * destroy时，退出程序，退出服务
	 */
	@Override
	protected void onDestroy() {
		Log.i(TAG, "MonitorMatch onDestroy");
		super.onDestroy();

	}

	// /**
	// * onActivityResult,处理界面的返回结果
	// */
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// switch (requestCode) {
	//
	// // 申请打开蓝牙的返回值
	// case Constant.REQUEST_ENABLE_BT:
	// if (resultCode == Activity.RESULT_OK) {
	// selectDeviceToConnect();
	// } else {
	// Toast.makeText(this, R.string.bt_not_enabled_leaving,
	// Toast.LENGTH_SHORT).show();
	// finish();
	// }
	// break;
	// // 选择相应的设备进行连接通讯
	// case Constant.REQUEST_CONNECT_DEVICE_INSECURE:
	// if (resultCode == Activity.RESULT_OK) {
	// String address = data.getExtras().getString(
	// Constant.EXTRA_DEVICE_ADDRESS);
	// device = mBluetoothAdapter.getRemoteDevice(address);
	// GetDataService.start(activity_entity, device);
	// }
	// break;
	// }
	// }

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
         
        	Intent intent = new Intent();
            //把返回数据存入Intent
		 String result0=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatezero", "");
		 String result1=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateone", "");
		 String result2=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatetwo", "");
		 String result3=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatethree", "");
		 String result4=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatefour", "");
		 String result5=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatefive", "");
		 String result6=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatesix", "");
		 String result7=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateseven", "");
		 String result8=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateeight", "");
		 String result9=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStatenine", "");
		 String result10=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateten", "");
		 String result11=SpUtils.getString(MonitorMatchActivityTwo.this,"DataStateeleven", "");
            intent.putExtra("result0",result0);
            intent.putExtra("result1",result1);
            intent.putExtra("result2",result2);
            intent.putExtra("result3",result3);
            intent.putExtra("result4",result4);
            intent.putExtra("result5",result5);
            intent.putExtra("result6",result6);
            intent.putExtra("result7",result7);
            intent.putExtra("result8",result8);
            intent.putExtra("result9",result9);
            intent.putExtra("result10",result10);
            intent.putExtra("result11",result11);
            //设置返回数据
            MonitorMatchActivityTwo.this.setResult(RESULT_OK, intent);
            //关闭Activity
            MonitorMatchActivityTwo.this.finish();
	
        }
        return true;
    }

}
