package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TireDetailActivity extends Activity
{
    
    private View     tire_detail_back;
    private TextView tv_detail;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tire_detail);
        tire_detail_back = (View) findViewById(R.id.tire_detail_back);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tire_detail_back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        tv_detail.setText(msg);
    }
}
