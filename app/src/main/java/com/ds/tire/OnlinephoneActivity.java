package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class OnlinephoneActivity extends Activity {
	private Button bt_phone;
	private LinearLayout phone_back;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onlinephone);
		bt_phone= (Button)findViewById(R.id.bt_phone);
		phone_back = (LinearLayout) findViewById(R.id.phone_back);
		
		phone_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		bt_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent phoneIntent = new Intent(
						"android.intent.action.CALL", Uri
								.parse("tel:" + "400-017-6666"));
				startActivity(phoneIntent);
				
				
			}
		});
	}

}
