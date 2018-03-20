package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class RescueActivity extends Activity {
	
private LinearLayout home_btn_rescue;

public void onCreate(Bundle savedInstanceState)
{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.newhome_test);
	home_btn_rescue = (LinearLayout) findViewById(R.id.home_btn_rescue);
	home_btn_rescue.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
		//	intent.setClass(RescueActivity.this, MapActivity.class);
			startActivity(intent);
			// TODO Auto-generated method stub
			
		}
	});
}

}
