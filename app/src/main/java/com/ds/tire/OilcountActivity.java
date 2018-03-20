package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OilcountActivity extends Activity {

	private LinearLayout oil_back;
	private TextView oil_view;
	private TextView mile_view;
	private TextView money_view;
	private TextView hundred_view;
	private EditText oilcount_oil;
	private EditText oilcount_mile;
	private EditText oilcount_money;
	private Button submit;
	public static final int LENGTH_LONG = 1;

	public void onCreate(Bundle savedInstanceState) {    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oilcount);

		oilcount_oil = (EditText) findViewById(R.id.oilcount_oil);
		oilcount_mile = (EditText) findViewById(R.id.oilcount_mile);
		oilcount_money = (EditText) findViewById(R.id.oilcount_money);

		oil_view = (TextView) findViewById(R.id.oil_tv);
		mile_view = (TextView) findViewById(R.id.mile_tv);
		money_view = (TextView) findViewById(R.id.money_tv);
		hundred_view = (TextView) findViewById(R.id.hundred_tv);

		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new onResultClickListener());

		// 返回按钮
		oil_back = (LinearLayout) findViewById(R.id.back);
		oil_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

	}

	// 创建一个监听器
	class onResultClickListener implements OnClickListener {

		public void onClick(View v) {

			// 获取2个edit输入的数字
			String oil_str = oilcount_oil.getText().toString();
			String mile_str = oilcount_mile.getText().toString();
			String money_str = oilcount_money.getText().toString();

			// 判断输入内容
			if (oil_view == null || oil_str.equals("") || mile_view == null
					|| mile_str.equals("") || money_view == null || money_str.equals("")) {
				Toast.makeText(OilcountActivity.this, "请输入数据",
						Toast.LENGTH_LONG).show();
				return;
			}
			// java中将字符串转换成浮点数的语法
			float factor_oil = Float.parseFloat(oil_str);  
			float factor_mile = Float.parseFloat(mile_str);
			float factor_money = Float.parseFloat(money_str);
			//进行数据计算
			float oil_result = factor_oil * 100 / factor_mile;
			float mile_result = factor_mile / factor_oil;
			float money_result = factor_oil * 100 / factor_mile * factor_money;
			float hundred_result = factor_mile / factor_oil * 100 / factor_money;
			

			oil_view.setText(oil_result + "");
			mile_view.setText(mile_result + "");
			money_view.setText(money_result + "");
			hundred_view.setText(hundred_result + "");

		}
	}
	/**
	 * 菜单选项
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Log.d("peidui11111", "ejjjjjjjjjjjjjjjjjj111111");
		menu.add(0, 1, 0, "设备配对").setIcon(
				android.R.drawable.ic_menu_more);
	Log.d("peidui", "ejjjjjjjjjjjjjjjjjj");
		return super.onCreateOptionsMenu(menu);
		
	}
	/**
	 * 菜单选项的点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case 1:
			/* 给每个轮胎配对*/
			Intent intent = new Intent(this, MonitorMatchActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
