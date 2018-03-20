package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PianfangDetailActivity extends Activity
{
    TextView     detail_title, detail_content;
    LinearLayout back;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pianfdetail);
        
        Intent intent = getIntent();
        String str1 = intent.getStringExtra("title");
        String str2 = intent.getStringExtra("content");
        detail_title = (TextView) findViewById(R.id.detail_title);
        detail_content = (TextView) findViewById(R.id.detail_content);
        detail_title.setText(str1);
        detail_content.setText(str2);
        
        back = (LinearLayout) findViewById(R.id.detail_back);
        back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PianfangDetailActivity.this.finish();
            }
        });
    }
}
