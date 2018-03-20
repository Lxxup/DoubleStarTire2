package com.ds.tire;

import java.io.InputStream;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SafeDetailActivity extends Activity {

	String name1=null;
	String tid=null;
	LinearLayout back;
	private static final String TAG = "TAG";
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safedetail);
		back = (LinearLayout) findViewById(R.id.safe_back);
		TextView name = (TextView) findViewById(R.id.name);
		TextView content = (TextView) findViewById(R.id.content);
        back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                finish();
            }
        });
		 Bundle bundle = this.getIntent().getExtras();
	      name1 = bundle.getString("name");	
	      tid= bundle.getString("id");
	      Log.d(TAG, "接收的系列编号:"+tid);
	      name.setText(name1);
	      if(tid.equals("1")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.a);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("2")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.b);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("3")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.c);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("4")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.d);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("5")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.e);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("6")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.f);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("7")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.g);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("8")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.h);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("9")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.i);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("10")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.j);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("11")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.k);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("12")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.l);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("13")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.aa);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("14")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.bb);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
	      if(tid.equals("15")){
	    	  InputStream inputStream = getResources().openRawResource(R.raw.cc);
				String string = TxtReader.getString(inputStream);
				content.setText(string);
	      }
 }
}
