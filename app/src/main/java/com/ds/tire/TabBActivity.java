package com.ds.tire;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.CompanyIntroduceActivity;
import com.ds.tire.FuwuxieyiActivity;
import com.ds.tire.LoginActivity;
import com.ds.tire.MonitorActivity;
import com.ds.tire.MonitorMoreActivity;
import com.ds.tire.OilcountActivity;
import com.ds.tire.OnlinephoneActivity;
import com.ds.tire.OrderDetailActivity;
import com.ds.tire.PianfangActivity;
import com.ds.tire.ProductSeriesActivity;
import com.ds.tire.R;
import com.ds.tire.SafeActivity;
import com.ds.tire.SettingsActivity;
import com.ds.tire.WeatherInfoActivity;
import com.ds.tire.WebsiteSelectActivity;
import com.ds.tire.WeizhangActivity;
import com.ds.tire.ZhiliangActivity;
import com.ds.tire.bluetooth.Constants;
import com.ds.tire.bluetooth.GetDataService;
import com.ds.tire.util.CheckUpdate;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.SpUtils;

public class TabBActivity extends Activity {
	public static String TAG = "homeActivity";
	private PopupWindow pop;
	// 界面控件
	private View home_btn_monitor;
	private View home_btn_product;
	// private View home_btn_verson;
	private View home_btn_company;
	private View home_btn_oil;
	// private View home_btn_personal;
	private View home_menu_faq;
	private View home_menu_rescue;
	// private View home_menu_setting;
	private View home_btn_shop;
	private View home_btn_lukuang;
	private View home_btn_wangdian;
	private View home_btn_weizhang;
	private View home_btn_weather;
	private View home_btn_pianfang;
	private View home_btn_rescue;
	private TextView tv_main_set;

	private TextView tv_main_account;
	private TextView tv_main_user_center;
	private TextView tv_main_check_version;
	private TextView tv_main_version;
	private TextView tv_main_setting;
	private View home_btn_promise;
	private View home_btn_period;
	private boolean ifFirstIn = true;

	int location = -1;
	// 监听点击事件
	private OnClickListener clickListener = new MyOnClickListener();

	class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			myOnClick(v);
		}
	}

	private Handler handler = new MyHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tirehome_b);
		findViewById();
		init();
		// SQLiteDatabase db = openOrCreateDatabase("sdata.db",
		// Context.MODE_PRIVATE, null);
		// db.execSQL("DROP TABLE IF EXISTS data");
		// db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, mId VARCHAR,qiya INTEGER,wendu INTEGER,jsd INTEGER)");

	}

	private void findViewById() {
		// 控件初始化
//		home_btn_monitor = (View) findViewById(R.id.home_btn_monitor);
//		home_btn_product = (View) findViewById(R.id.home_btn_product);
		// home_btn_verson = (View) findViewById(R.id.home_btn_verson);
//		home_btn_company = (View) findViewById(R.id.home_btn_company);
//		home_btn_oil = (View) findViewById(R.id.home_btn_oil);
		// home_btn_personal = (View) findViewById(R.id.home_btn_personal);
		home_menu_faq = (View) findViewById(R.id.home_menu_faq);
//		home_menu_rescue = (View) findViewById(R.id.home_menu_rescue);
		// home_menu_setting = (View) findViewById(R.id.home_menu_setting);
		home_btn_shop = (View) findViewById(R.id.home_btn_shop);
//		home_btn_lukuang = (View) findViewById(R.id.home_btn_lukuang);
//		home_btn_weizhang = (View) findViewById(R.id.home_btn_weizhang);
//		home_btn_wangdian = (View) findViewById(R.id.home_btn_wangdian);
		home_btn_weather = (View) findViewById(R.id.home_btn_weather);
		home_btn_pianfang = (View) findViewById(R.id.home_btn_pianfang);
		tv_main_set = (TextView) findViewById(R.id.tv_main_set);
//		home_btn_rescue = (View) findViewById(R.id.home_btn_rescue);
//		home_btn_promise = (View) findViewById(R.id.home_btn_promise);
//		home_btn_period = (View) findViewById(R.id.home_btn_period);

	}

	private void init() {
		// 添加监听器
//		home_btn_wangdian.setOnClickListener(clickListener);
//		home_btn_lukuang.setOnClickListener(clickListener);
//		home_btn_monitor.setOnClickListener(clickListener);
//		home_btn_product.setOnClickListener(clickListener);
//		// home_btn_verson.setOnClickListener(clickListener);
//		home_btn_company.setOnClickListener(clickListener);
//		home_btn_oil.setOnClickListener(clickListener);
//		// home_btn_personal.setOnClickListener(clickListener);
		home_menu_faq.setOnClickListener(clickListener);
//		home_menu_rescue.setOnClickListener(clickListener);
//		// home_menu_setting.setOnClickListener(clickListener);
		home_btn_shop.setOnClickListener(clickListener);
//		home_btn_weizhang.setOnClickListener(clickListener);
		home_btn_weather.setOnClickListener(clickListener);
		home_btn_pianfang.setOnClickListener(clickListener);
		tv_main_set.setOnClickListener(clickListener);
//		home_btn_rescue.setOnClickListener(clickListener);
//		home_btn_promise.setOnClickListener(clickListener);
//		home_btn_period.setOnClickListener(clickListener);

	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			location = (Integer) msg.obj;
			switch (msg.what) {
			case 0:
				SpUtils.setBoolean(TabBActivity.this, SpUtils.AIDFLAGONE,
						false);
				DialogUtils.alert(false, TabBActivity.this,
						R.drawable.dialog_icon, "提示", "是否呼叫救援车辆？", "确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SpUtils.setBoolean(TabBActivity.this,
										SpUtils.AIDFLAGONE, true);
								if (SpUtils.getString(TabBActivity.this,
										SpUtils.ACCOUNT, "").equals("")) {
									Toast.makeText(TabBActivity.this,
											"请先登录", Toast.LENGTH_SHORT).show();
								} else {
									Intent intent = new Intent(
											TabBActivity.this,
											OrderDetailActivity.class);
									intent.putExtra("weizhi", location);
									startActivity(intent);
								}
							}
						}, "打电话", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								SpUtils.setBoolean(TabBActivity.this,
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

								SpUtils.setBoolean(TabBActivity.this,
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

	/*
	 * 通过包名启动第三方程序
	 */
	private void startAppByPackageName(String packageName) {
		PackageInfo pi = null;
		try {
			pi = getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = getPackageManager().queryIntentActivities(
				resolveIntent, 0);

		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String packageName1 = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			ComponentName cn = new ComponentName(packageName1, className);

			intent.setComponent(cn);
			startActivity(intent);
		}

	}

	private void myOnClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.home_btn_company:// 公司介绍
			intent = new Intent(this, CompanyIntroduceActivity.class);
			startActivity(intent);
			break;
		case R.id.home_btn_product:// 产品介绍
			intent = new Intent(this, ProductSeriesActivity.class);
			startActivity(intent);
			break;
		case R.id.home_menu_faq:// 使用常识
			intent = new Intent(this, SafeActivity.class);
			startActivity(intent);
			break;
		case R.id.home_btn_monitor:// 胎压检测
			int ifnbet = SpUtils.getInteger(TabBActivity.this,
					"wheels_count", 0);

			Log.i("TAG", "11111" + ifnbet);
			if (ifnbet != 0) {
//				Intent intent1 = ifnbet <= 6 ? new Intent(
//						TabBActivity.this, MonitorActivity.class)
//						: new Intent(TabBActivity.this,
//								MonitorMoreActivity.class);
				Intent intent1=new Intent(TabBActivity.this,MonitorActivity.class);
				intent1.putExtra("wheels_count", ifnbet);
				startActivity(intent1);
			} else {
				final EditText input = new EditText(this);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				input.setHint("亲,最多为12哦");
				new AlertDialog.Builder(this)
						.setTitle("请输入您安装的检测器数量")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(input)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										String number = input.getText()
												.toString().trim();
										// Toast.makeText(TabBActivity.this,
										// "您输入的数字" + number, 1).show();
										int wheelsCount = Integer
												.parseInt(number);
//										SpUtils.setInteger(
//												TabBActivity.this,
//												"wheels_count", wheelsCount);
//										Intent intent2 = wheelsCount <= 6 ? new Intent(
//												TabBActivity.this,
//												MonitorActivity.class)
//												: new Intent(
//														TabBActivity.this,
//														MonitorMoreActivity.class);
										Intent intent2=new Intent(TabBActivity.this,MonitorActivity.class);
										intent2.putExtra("wheels_count",wheelsCount);
										startActivity(intent2);

									}
								}).setNegativeButton("取消", null).show();
			}

			break;
		case R.id.home_btn_oil:// 油耗
			intent = new Intent(this, OilcountActivity.class);
			startActivity(intent);
			break;

		case R.id.home_menu_rescue:// 在线服务
			intent = new Intent(TabBActivity.this, OnlinephoneActivity.class);
			startActivity(intent);
			
			break;
		case R.id.home_btn_rescue:// 在线救援
			if (SpUtils.getBoolean(TabBActivity.this, SpUtils.AIDFLAGONE,
					true)) {
				Message msg = new Message();
				msg.what = 0;
				msg.obj = -1;
				handler.sendMessage(msg);
				
			} else {
				// 进入救援地图页面
				intent = new Intent(TabBActivity.this,
						OrderDetailActivity.class);

				intent.putExtra("weizhi", location);
				startActivity(intent);
				// Toast.makeText(MonitorActivity.this, "请求已受理，请不要重复提交救援请求",
				// 1).show();
			}
			// intent = new Intent(this, MessageActivity.class);
			break;

		case R.id.home_btn_promise:// 质量承诺
			String uid = SpUtils.getString(TabBActivity.this,
					SpUtils.ACCOUNT, null);
			if (uid == null || uid.equals("")) {
				Toast.makeText(TabBActivity.this, "您还没有登录", 1).show();
			} else {
				if (ifFirstIn) {
					Intent intent1 = new Intent();
					intent1.setClass(TabBActivity.this,
							FuwuxieyiActivity.class);
					startActivity(intent1);
					SpUtils.setBoolean(TabBActivity.this, "ifFirstIn",
							false);
				} else {
					Intent intent1 = new Intent();
					intent1.setClass(TabBActivity.this,
							ZhiliangActivity.class);
					startActivity(intent1);
				}
			}

			break;
		case R.id.home_btn_period:// 生命周期
			Toast.makeText(TabBActivity.this, "还未开放，敬请期待", 1).show();
			break;

		case R.id.home_btn_shop:// 网上购物
			Uri uri = Uri
					.parse("http://www.birdweixin.com/p/m/8a68c6dc47a08a600147a40356460de8.shtml");
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			break;

		case R.id.home_btn_wangdian:
			intent = new Intent(this, WebsiteSelectActivity.class);
			startActivity(intent);
			break;
		// Toast.makeText(this, "您点击了网点查询", 1).show();
		// break;
		case R.id.home_btn_weizhang:
			intent = new Intent(this, WeizhangActivity.class);
			startActivity(intent);
			break;
		case R.id.home_btn_weather:
			// Toast.makeText(this, "您点击了天气预报", 1).show();
			intent = new Intent(this, WeatherInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.home_btn_lukuang:

			try {
				intent = new Intent(Intent.ACTION_MAIN);
				String bd = "com.baidu.BaiduMap";
				this.startAppByPackageName(bd);
				this.startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(this, "请安装相关插件", 1).show();
			}

			break;
		case R.id.home_btn_pianfang:
			intent = new Intent(this, PianfangActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_main_set:
			popSetting(tv_main_set);
			break;
		default:
			break;
		}
	}

	private void popSetting(TextView parent) {
		View v = TabBActivity.this.getLayoutInflater().inflate(
				R.layout.pop_setting, null);
		tv_main_account = (TextView) v.findViewById(R.id.tv_main_account);
		tv_main_user_center = (TextView) v
				.findViewById(R.id.tv_main_user_center);
		tv_main_check_version = (TextView) v
				.findViewById(R.id.tv_main_check_version);
		tv_main_version = (TextView) v.findViewById(R.id.tv_main_version);
		tv_main_setting = (TextView) v.findViewById(R.id.tv_main_setting);
		if (SpUtils.getString(TabBActivity.this, SpUtils.ACCOUNT,
				"").equals("")) {
			tv_main_account.setText("登录");
		} else {
			tv_main_account.setText("注销");
			
		}
		
		
		popClick();

		pop = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		pop.setBackgroundDrawable(new PaintDrawable(android.R.color.transparent));
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.showAsDropDown(parent, Gravity.RIGHT, 0);
	}

	private void popClick() {
		tv_main_account.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SpUtils.getString(TabBActivity.this, SpUtils.ACCOUNT,
						"").equals("")) {
					Intent intent = new Intent();
					intent = new Intent(TabBActivity.this,
							LoginActivity.class);
					startActivity(intent);
				} else {
					DialogUtils.alert(true, TabBActivity.this,
							R.drawable.dialog_icon, "提示", "已登录，是否注销", "确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									SpUtils.setString(TabBActivity.this,
											SpUtils.ACCOUNT, "");
									SpUtils.setInteger(TabBActivity.this,
											SpUtils.WHEELS_COUNT, 4);
									Intent i = new Intent(
											TabBActivity.this,
											LoginActivity.class);
									startActivity(i);
								}
							}, "取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}, null, null);
				}
			}
		});
		tv_main_user_center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SpUtils.getString(TabBActivity.this, SpUtils.ACCOUNT,
						"").equals("")) {
					Toast.makeText(TabBActivity.this, "请先登录",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent();
					intent = new Intent(TabBActivity.this,
							SettingsActivity.class);
					startActivity(intent);
				}
			}
		});
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			tv_main_version.setText(info.versionName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tv_main_check_version.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckUpdate check = new CheckUpdate(TabBActivity.this);
				check.checkUpdate();
			}
		});
		tv_main_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(TabBActivity.this, "您点击了设置", 1).show();
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("确认退出应用？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									stopService(new Intent(
											Constants.ACTION_BLUETOOTH_SERVICE));
									GetDataService.mDevice = null;
									finish();
								}
							}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
