package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class FuwuxieyiStartActivity extends Activity
{

	private LinearLayout xieyi_back;
	private Button xieyi_next;
//	private int count;
//	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fuwuxieyi_start);
	
		
		xieyi_back = (LinearLayout) findViewById(R.id.xieyi_back);
		xieyi_next = (Button) findViewById(R.id.xieyi_next);


		xieyi_back.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				finish();
			}
		});

		xieyi_next.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(FuwuxieyiStartActivity.this, FuwuxieyiActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}

}

	