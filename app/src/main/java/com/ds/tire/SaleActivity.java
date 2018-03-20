package com.ds.tire;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SaleActivity extends Activity
{
    private ImageView sale_back;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale);
        sale_back = (ImageView) findViewById(R.id.sale_back);
        sale_back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
            
        });
    }
}
