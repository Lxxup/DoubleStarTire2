package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageDetailActivity extends Activity
{
    
    TextView     title, content, feedback;
    LinearLayout detail_back;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagedetail);
        Intent intent = getIntent();
        String str = intent.getStringExtra("title");
        String str1 = intent.getStringExtra("content");
        String str2 = intent.getStringExtra("feedback");
        title = (TextView) findViewById(R.id.dt_title);
        content = (TextView) findViewById(R.id.dt_content);
        feedback = (TextView) findViewById(R.id.dt_feedback);
        title.setText("标题：" + str);
        content.setText("内容：" + str1);
        feedback.setText("反馈：" + str2);
        
        detail_back = (LinearLayout) findViewById(R.id.detail_back);
        detail_back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MessageDetailActivity.this.finish();
            }
        });
    }
    
}
