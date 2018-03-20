package com.ds.tire;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ds.tire.util.Constant;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.SpUtils;

public class ServiceOnlineActivity extends Activity {

	private Button bt_rescue;
	private Button bt_zlcn;
	private Handler handler = new MyHandler();
	int location = -1;
	private boolean ifFirstIn=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_online);
		ifFirstIn = SpUtils.getBoolean(ServiceOnlineActivity.this, "ifFirstIn", true);
		bt_rescue = (Button)findViewById(R.id.bt_rescue);
		bt_zlcn =(Button)findViewById(R.id.bt_zlcn);
		bt_rescue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent;
				if (SpUtils.getBoolean(ServiceOnlineActivity.this, SpUtils.AIDFLAGONE,
						true)) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = -1;
					handler.sendMessage(msg);
				} else {
					// 进入救援地图页面
					intent = new Intent(ServiceOnlineActivity.this,
							OrderDetailActivity.class);
					
					intent.putExtra("weizhi", location);
					startActivity(intent);
					// Toast.makeText(MonitorActivity.this, "请求已受理，请不要重复提交救援请求",
					// 1).show();
				}
				// intent = new Intent(this, MessageActivity.class);
			}
		});
		bt_zlcn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uid = SpUtils.getString(ServiceOnlineActivity.this, SpUtils.ACCOUNT, null);
				if(uid==null||uid.equals("")){
					Toast.makeText(ServiceOnlineActivity.this, "您还没有登录", 1).show();
				}else{
					if(ifFirstIn){
						Intent intent = new Intent();
						intent.setClass(ServiceOnlineActivity.this,FuwuxieyiActivity.class);
						startActivity(intent);
						SpUtils.setBoolean(ServiceOnlineActivity.this, "ifFirstIn", false);
					}else{
						Intent intent = new Intent();
						intent.setClass(ServiceOnlineActivity.this,ZhiliangActivity.class);
						startActivity(intent);
					}
				}
				
			}
		});
	}
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			location = (Integer) msg.obj;
			switch (msg.what) {
			case 0:
				SpUtils.setBoolean(ServiceOnlineActivity.this, SpUtils.AIDFLAGONE,
						false);
				DialogUtils.alert(false, ServiceOnlineActivity.this,
						R.drawable.dialog_icon, "提示", "是否呼叫救援车辆？", "确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SpUtils.setBoolean(ServiceOnlineActivity.this,
										SpUtils.AIDFLAGONE, true);
								if (SpUtils.getString(ServiceOnlineActivity.this,
										SpUtils.ACCOUNT, "").equals("")) {
									Toast.makeText(ServiceOnlineActivity.this,
											"请先登录", Toast.LENGTH_SHORT).show();
								} else {
									Intent intent = new Intent(
											ServiceOnlineActivity.this,
											OrderDetailActivity.class);
									intent.putExtra("weizhi", location);
									startActivity(intent);
								}
							}
						}, "打电话", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								SpUtils.setBoolean(ServiceOnlineActivity.this,
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

								SpUtils.setBoolean(ServiceOnlineActivity.this,
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

}
