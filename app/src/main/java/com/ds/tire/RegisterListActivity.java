package com.ds.tire;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterListActivity extends Activity {
	private View register_back = null;
	private Button bt_register = null;
	private EditText register_account = null;
	private EditText register_username = null;
	private EditText register_password = null;
	private EditText register_repassword = null;
	private EditText register_carnum = null;
	private EditText register_xinghao = null;
	private EditText register_tiresize = null;
	private Spinner sp_regprovince = null;
	private Spinner sp_regzimu = null;
	private Spinner sp_tirenum = null;
	private String aaa;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_list);
		findViewById();
		Intent intent = getIntent();
		String aaa = intent.getStringExtra("phone");
		Log.i("TAG", "获取的电话号码: " + aaa);
		register_account.setText(aaa + "");


		register_back.setOnClickListener(new ClickListener());
		bt_register.setOnClickListener(new ClickListener());
	}

	private void findViewById() {
		register_back = findViewById(R.id.common_head_back);
		bt_register = (Button) findViewById(R.id.bt_register);
		register_account = (EditText) findViewById(R.id.register_account);
		register_username = (EditText) findViewById(R.id.register_username);
		register_password = (EditText) findViewById(R.id.register_password);
		register_repassword = (EditText) findViewById(R.id.register_repassword);
		register_carnum = (EditText) findViewById(R.id.register_carnum);
		register_xinghao = (EditText) findViewById(R.id.register_xinghao);
		register_tiresize = (EditText) findViewById(R.id.register_tiresize);
		sp_regprovince = (Spinner) findViewById(R.id.sp_regprovince);
		sp_regzimu = (Spinner) findViewById(R.id.sp_regzimu);
		sp_tirenum = (Spinner) findViewById(R.id.sp_tirenum);

	}

	private class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.common_head_back:
					finish();
					break;
				case R.id.bt_register:

					String username = register_username.getText().toString();
					String password = register_password.getText().toString();
					String repassword = register_repassword.getText().toString();
					String account = register_account.getText().toString();
					String chexing = register_xinghao.getText().toString();
					String tiresize = register_tiresize.getText().toString();
					String carnum = register_carnum.getText().toString();
					String zimu = sp_regzimu.getSelectedItem().toString();
					String province = sp_regprovince.getSelectedItem().toString();
					String wheelNum = sp_tirenum.getSelectedItem().toString();
					String platenum = province + zimu + carnum;
					Log.d("TAG", "测试密码是" + password);
					Log.d("TAG", "测试重复密码" + repassword);

					register(account, username, password, repassword, chexing,
							tiresize,  wheelNum, platenum);
					break;
				default:
					break;
			}
		}

		private void register(String account, String username, String password,
							  String repassword, String chexing, String tiresize,
							  String wheelNum,
							  String platenum) {
			try {
				Pattern p = Pattern.compile("1" + "\\d{10}");
				Matcher m = p.matcher(account);
				// if (!m.find()) {
				// Toast.makeText(RegisterListActivity.this, "请输入正确的手机号！",
				// Toast.LENGTH_SHORT).show();
				// return;
				// }
				// if (account == null || (account = account.trim()).length() ==
				// 0) {
				// Toast.makeText(RegisterListActivity.this, "账号不能为空！",
				// Toast.LENGTH_SHORT).show();
				// return;
				// }
				if (username.equals(""))
				{
					Toast.makeText(RegisterListActivity.this, "姓名不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (password == null
						|| (password = password.trim()).length() == 0) {
					Toast.makeText(RegisterListActivity.this, "密码不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Log.d("TAG", "密码是" + password);
				Log.d("TAG", "重复密码是" + repassword);
				if (!password.equals(repassword)) {
					Toast.makeText(RegisterListActivity.this, "密码不一致",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (wheelNum == null
						) {
					Toast.makeText(RegisterListActivity.this, "车轮数量不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				RegisterTask registerTask = new RegisterTask(
						RegisterListActivity.this, account, username, password,
						platenum, chexing, tiresize, wheelNum);
				registerTask.showProgressDialog("提示", "正在注册...");
				registerTask.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class RegisterTask extends AsyncNetworkTask<String> {

		private String account;
		private String username;
		private String password;
		private String platenum;
		private String chexing;
		private String tiresize;
		private String wheelNum;

		public RegisterTask(Context context, String account, String username,
							String password, String platenum, String chexing,
							String tiresize, String wheelNum)
				throws UnsupportedEncodingException {
			super(context);
			this.account = account;
			this.username = username;
			this.password = password;
			this.platenum = platenum;
			this.chexing = chexing;
			this.tiresize = tiresize;
			this.wheelNum = wheelNum;

		}

		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.registerUser(account, username, password, platenum,
					chexing, tiresize, wheelNum);
		}

		@Override
		public void handleResult(String result) {
			if (result != null && !result.equals("")) {
				if (result.equals("0")) {
					Toast.makeText(RegisterListActivity.this, "账号已存在",
							Toast.LENGTH_SHORT).show();
				}else if(result.equals("-1")){
					Toast.makeText(RegisterListActivity.this, "密码格式不正确，请重新输入",
							Toast.LENGTH_SHORT).show();
					Log.d("TAG", "返回值" +result );
				}
//				else if(result.equals("-2")){
//					Toast.makeText(RegisterListActivity.this, "车牌号格式不正确，请重新输入",
//							Toast.LENGTH_SHORT).show();
//					Log.d("TAG", "返回车牌值" +result );
//				}
				else {
					Toast.makeText(RegisterListActivity.this, "注册成功",
							Toast.LENGTH_SHORT).show();
					RegisterListActivity.this.finish();
				}

			} else {
				Toast.makeText(RegisterListActivity.this, "服务器异常",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}






/*
package com.ds.tire;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterListActivity extends Activity {
	private View register_back = null;
	private Button bt_register = null;
	private EditText register_account = null;
	private EditText register_username = null;
	private EditText register_password = null;
	private EditText register_repassword = null;
	private EditText register_carnum = null;
	private EditText register_xinghao = null;
	private EditText register_tiresize = null;
	private Spinner sp_regprovince = null;
	private Spinner sp_regzimu = null;
	private Spinner sp_tirenum = null;
	private String aaa;
	private TextView login_back_register;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_list);
		findViewById();
		Intent intent = getIntent();
		String aaa = intent.getStringExtra("phone");
		Log.i("TAG", "获取的电话号码: " + aaa);
		register_account.setText(aaa + "");



		login_back_register.setOnClickListener(new ClickListener());
		bt_register.setOnClickListener(new ClickListener());
	}

	private void findViewById() {
		*/
/*register_back = findViewById(R.id.common_head_back);*//*

		bt_register = (Button) findViewById(R.id.bt_register);
		register_account = (EditText) findViewById(R.id.register_account);
		register_username = (EditText) findViewById(R.id.register_username);
		register_password = (EditText) findViewById(R.id.register_password);
		register_repassword = (EditText) findViewById(R.id.register_repassword);
		register_carnum = (EditText) findViewById(R.id.register_carnum);
		register_xinghao = (EditText) findViewById(R.id.register_xinghao);
		register_tiresize = (EditText) findViewById(R.id.register_tiresize);
		sp_regprovince = (Spinner) findViewById(R.id.sp_regprovince);
		sp_regzimu = (Spinner) findViewById(R.id.sp_regzimu);
		sp_tirenum = (Spinner) findViewById(R.id.sp_tirenum);
		login_back_register=(TextView)findViewById(R.id.login_back_register);

	}

	private class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_back_register:
				finish();
				break;
			case R.id.bt_register:

				String username = register_username.getText().toString();
				String password = register_password.getText().toString();
				String repassword = register_repassword.getText().toString();
				String account = register_account.getText().toString();
				String chexing = register_xinghao.getText().toString();
				String tiresize = register_tiresize.getText().toString();
				String carnum = register_carnum.getText().toString();
				String zimu = sp_regzimu.getSelectedItem().toString();
				String province = sp_regprovince.getSelectedItem().toString();
				String wheelNum = sp_tirenum.getSelectedItem().toString();
				String platenum = province + zimu + carnum;
				Log.d("TAG", "测试密码是" + password);
				Log.d("TAG", "测试重复密码" + repassword);

				register(account, username, password, repassword, chexing,
						tiresize,  wheelNum, platenum);
				break;
			default:
				break;
			}
		}

		private void register(String account, String username, String password,
				String repassword, String chexing, String tiresize,
				 String wheelNum,
				String platenum) {
			try {
				Pattern p = Pattern.compile("1" + "\\d{10}");
				Matcher m = p.matcher(account);
				// if (!m.find()) {
				// Toast.makeText(RegisterListActivity.this, "请输入正确的手机号！",
				// Toast.LENGTH_SHORT).show();
				// return;
				// }
				// if (account == null || (account = account.trim()).length() ==
				// 0) {
				// Toast.makeText(RegisterListActivity.this, "账号不能为空！",
				// Toast.LENGTH_SHORT).show();
				// return;
				// }
				if (username.equals(""))
				{
					Toast.makeText(RegisterListActivity.this, "姓名不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (password == null
						|| (password = password.trim()).length() == 0) {
					Toast.makeText(RegisterListActivity.this, "密码不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Log.d("TAG", "密码是" + password);
				Log.d("TAG", "重复密码是" + repassword);
				if (!password.equals(repassword)) {
					Toast.makeText(RegisterListActivity.this, "密码不一致",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (wheelNum == null
						) {
					Toast.makeText(RegisterListActivity.this, "车轮数量不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				RegisterTask registerTask = new RegisterTask(
						RegisterListActivity.this, account, username, password,
						platenum, chexing, tiresize, wheelNum);
				registerTask.showProgressDialog("提示", "正在注册...");
				registerTask.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class RegisterTask extends AsyncNetworkTask<String> {

		private String account;
		private String username;
		private String password;
		private String platenum;
		private String chexing;
		private String tiresize;
		private String wheelNum;

		public RegisterTask(Context context, String account, String username,
				String password, String platenum, String chexing,
				String tiresize, String wheelNum)
				throws UnsupportedEncodingException {
			super(context);
			this.account = account;
			this.username = username;
			this.password = password;
			this.platenum = platenum;
			this.chexing = chexing;
			this.tiresize = tiresize;
			this.wheelNum = wheelNum;

		}

		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.registerUser(account, username, password, platenum,
					chexing, tiresize, wheelNum);
		}

		@Override
		public void handleResult(String result) {
			if (result != null && !result.equals("")) {
				if (result.equals("0")) {
					Toast.makeText(RegisterListActivity.this, "账号已存在",
							Toast.LENGTH_SHORT).show();
				}else if(result.equals("-1")){
					Toast.makeText(RegisterListActivity.this, "密码格式不正确，请重新输入",
							Toast.LENGTH_SHORT).show();	
					Log.d("TAG", "返回值" +result );
				} 
//				else if(result.equals("-2")){
//					Toast.makeText(RegisterListActivity.this, "车牌号格式不正确，请重新输入",
//							Toast.LENGTH_SHORT).show();	
//					Log.d("TAG", "返回车牌值" +result );
//				} 
				else {
					Toast.makeText(RegisterListActivity.this, "注册成功",
							Toast.LENGTH_SHORT).show();
					RegisterListActivity.this.finish();
				}
				
			} else {
				Toast.makeText(RegisterListActivity.this, "服务器异常",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
*/
