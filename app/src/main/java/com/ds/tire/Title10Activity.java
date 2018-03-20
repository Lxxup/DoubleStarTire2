package com.ds.tire;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Title10Activity extends Activity {

	String name1=null;
	String tid=null;
	LinearLayout back;
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
	      name1 = bundle.getString("str");	   
	      name.setText(name1);
	      InputStream inputStream = getResources().openRawResource(R.raw.j);
			String string = TxtReader.getString(inputStream);
			content.setText(string);
 }
}
