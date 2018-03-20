package com.ds.tire;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class MyTireActivity extends Activity
{
    EditText leftfront, leftback, rightfront, rightback;
    Button   submit;
    View     back;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytire);
        
        leftfront = (EditText) findViewById(R.id.leftfront);
        leftback = (EditText) findViewById(R.id.leftback);
        rightfront = (EditText) findViewById(R.id.rightfront);
        rightback = (EditText) findViewById(R.id.rightback);
        submit = (Button) findViewById(R.id.submit);
        back = (View) findViewById(R.id.mytire_back);
        String str = SpUtils.getString(this, SpUtils.MUMBER, "");
        String[] a = str.split(",");
        if (!str.equals("") && a.length == 4)
        {
            leftfront.setText(a[0]);
            leftback.setText(a[1]);
            rightfront.setText(a[2]);
            rightback.setText(a[3]);
            submit.setClickable(false);
            Toast.makeText(MyTireActivity.this, "轮胎序列号已经注册过", Toast.LENGTH_SHORT).show();
        }
        submit.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                StringBuffer sb = new StringBuffer();
                sb.append(leftfront.getText()).append(",").append(rightfront.getText()).append(",").append(leftback.getText()).append(",").append(rightback.getText());
                try
                {
                    TireNumTask tnTask = new TireNumTask(MyTireActivity.this, sb.toString());
                    tnTask.execute();
                }
                catch (UnsupportedEncodingException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                MyTireActivity.this.finish();
            }
        });
        
    }
    
    class TireNumTask extends AsyncNetworkTask<String>
    {
        
        private String  tireno  = null;
        private Context context = null;
        
        public TireNumTask(Context context, String tireno) throws UnsupportedEncodingException
        {
            super(context);
            this.context = context;
            this.tireno = tireno;
        }
        
        public String doNetworkTask()
        {
            WebService ws = WebServiceFactory.getWebService();
            String id = SpUtils.getString(context, SpUtils.ACCOUNT, "-1");
            String path = ws.tireNo(id, tireno);
            return path;
        }
        
        @Override
        public void handleResult(String json)
        {
            if (json != null && !json.equals(""))
            {
                if (json.equals("1"))
                {
                    Toast.makeText(MyTireActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    SpUtils.setString(MyTireActivity.this, SpUtils.MUMBER, tireno);
                }
                else
                {
                    Toast.makeText(MyTireActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(MyTireActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
