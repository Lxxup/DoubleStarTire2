package com.ds.tire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.Constant;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class FuwuxieyiAddActivity extends Activity {

	private Button fu_addnext;
	private EditText ed_card;
	private LinearLayout back;
	private String TAG = "TAG";
	private String oid = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fuwuxieyi_add);

		back = (LinearLayout) findViewById(R.id.back);
		fu_addnext = (Button) findViewById(R.id.fu_addnext);
		ed_card = (EditText) findViewById(R.id.ed_card);

		
		
		fu_addnext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				oid = ed_card.getText().toString().trim();
				Log.d(TAG, oid);
				if (oid == null || oid.equals("")) {
					
					Toast.makeText(FuwuxieyiAddActivity.this, "请输入订单号", 1)
							.show();
				} else {
					// 判断订单号是否存在
					ifcardexist(oid);
				}
				

			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});

	}
	//查询相应订单是否已签名
	private void judgewrite(String oid) {
		Jdgifnamed task = new Jdgifnamed(this, oid);
		task.execute();

	}
	class Jdgifnamed extends AsyncNetworkTask<String> {

		String oid;

		public Jdgifnamed(Context context, String oid) {
			super(context);
			// TODO Auto-generated constructor stub
			this.oid = oid;
			Log.d("TAG", "判断是否已签名" + oid);
		}

		@Override
		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.ifnamed(oid);
		}

		@Override
		public void handleResult(String result) {
			Log.d("TAG", "未签名jiehuo" + result);

			if (result != null && !result.equals("")) {
				if (result.equals("0")) {

					Intent intent = new Intent();
					intent.setClass(FuwuxieyiAddActivity.this,
							FuwuxieyiStartActivity.class);
					startActivity(intent);
					 finish();
					Log.d("TAG", "未签名" + result);

				} else if (result.equals("1")) {
					Toast.makeText(FuwuxieyiAddActivity.this,
							"该订单号已存在，请重新输入。", Toast.LENGTH_SHORT).show();
					Log.d("TAG", "订单已存在" + result);
				}

			}else {
				Toast.makeText(FuwuxieyiAddActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
			}
		}
	}
	// 查询订单号是否存在
	private void ifcardexist(String oid) {
		IfcardExist task = new IfcardExist(this, oid);
		task.execute();

	}

	class IfcardExist extends AsyncNetworkTask<String> {

		String oid;

		public IfcardExist(Context context, String oid) {
			super(context);
			// TODO Auto-generated constructor stub
			this.oid = oid;
		}

		@Override
		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.ifcardexist(oid);
		}

		@Override
		public void handleResult(String result) {
			Log.d("TAG", "订单号存在查询结果" + result);

			if (result != null && !result.equals("")) {
				if (result.equals("1")) {
					
					SpUtils.setString(FuwuxieyiAddActivity.this, Constant.OID, oid);
					
					judgewrite(oid);

					
				} else if (result.equals("0")) {
					Toast.makeText(FuwuxieyiAddActivity.this,
							"您输入的订单号不存在，请重新输入。", Toast.LENGTH_SHORT).show();
					Log.d("TAG", "订单号不存在" + result);
				}

			}else {
				Toast.makeText(FuwuxieyiAddActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
}
