package com.ds.tire;

import com.ds.tire.bean.PianfangAdapter;
import com.ds.tire.util.Constant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PianfangActivity extends Activity
{
    ListView plist;
    LinearLayout back;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pianfang);
        back = (LinearLayout) findViewById(R.id.safe_back);
        back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                finish();
            }
        });
        plist = (ListView) findViewById(R.id.plist);
        PianfangAdapter adapter = new PianfangAdapter(this, Constant.getPianfang(), Constant.getPcontent());
        plist.setAdapter(adapter);
        
        plist.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // TODO Auto-generated method stub
                Intent intent = new Intent(PianfangActivity.this, PianfangDetailActivity.class);
                intent.putExtra("title", Constant.getPianfang().get(position));
                intent.putExtra("content", Constant.getPcontent().get(position));
                startActivity(intent);
            }
        });
    }
}
