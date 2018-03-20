package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	private View bt_back = null;
	private TextView tv_order_history;
	private TextView tv_ask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		findViewById();
		bt_back.setOnClickListener(new ClickListener());
		tv_order_history.setOnClickListener(new ClickListener());
		tv_ask.setOnClickListener(new ClickListener());
	}

	private void findViewById() {
		bt_back = (View) findViewById(R.id.common_head_back);
		tv_order_history = (TextView) findViewById(R.id.tv_order_history);
		tv_ask = (TextView) findViewById(R.id.tv_ask);
	}

	private class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.common_head_back:
				finish();
				break;
			case R.id.tv_order_history:
				intent.setClass(SettingsActivity.this,
						OrderHistoryActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_ask:
				intent.setClass(SettingsActivity.this, AskActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	}
}
