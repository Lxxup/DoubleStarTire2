package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TireActivity extends Activity
{
    
    private View tv_back;
    
    int[]        msg_a         = { R.string.a1, R.string.a2, R.string.a3, R.string.a4, R.string.a5, R.string.a6, R.string.a7 };
    int[]        msg_b         = { R.string.b1, R.string.b2, R.string.b3, R.string.b4, R.string.b5 };
    int[]        msg_c         = { R.string.c1, R.string.c2, R.string.c3, R.string.c4, R.string.c5, R.string.c6, R.string.c7, R.string.c8, R.string.c9, R.string.c10, R.string.c11, R.string.c12, R.string.c13, R.string.c14, R.string.c15, R.string.c16 };
    int[]        msg_d         = { R.string.d1, R.string.d2, R.string.d3, R.string.d4, R.string.d5, R.string.d6, R.string.d7, R.string.d8, R.string.d9, R.string.d10, R.string.d11, R.string.d12, R.string.d13, R.string.d14, R.string.d15, R.string.d16, R.string.d17, R.string.d18, R.string.d19, R.string.d20 };
    int[]        msg_e         = { R.string.e1, R.string.e2, R.string.e3, R.string.e4, R.string.e5, R.string.e6, R.string.e7 };
    int[]        msg_f         = { R.string.f1, R.string.f2 };
    int[]        msgIds_a      = { R.string.a1type, R.string.a2type, R.string.a3type, R.string.a4type, R.string.a5type, R.string.a6type, R.string.a7type };
    int[]        msgIds_b      = { R.string.b1type, R.string.b2type, R.string.b3type, R.string.b4type, R.string.b5type };
    int[]        msgIds_c      = { R.string.c1type, R.string.c2type, R.string.c3type, R.string.c4type, R.string.c5type, R.string.c6type, R.string.c7type, R.string.c8type, R.string.c9type, R.string.c10type, R.string.c11type, R.string.c12type, R.string.c13type, R.string.c14type, R.string.c15type, R.string.c16type };
    int[]        msgIds_d      = { R.string.d1type, R.string.d2type, R.string.d3type, R.string.d4type, R.string.d5type, R.string.d6type, R.string.d7type, R.string.d8type, R.string.d9type, R.string.d10type, R.string.d11type, R.string.d12type, R.string.d13type, R.string.d14type, R.string.d15type, R.string.d16type, R.string.d17type, R.string.d18type, R.string.d19type, R.string.d20type };
    int[]        msgIds_e      = { R.string.e1type, R.string.e2type, R.string.e3type, R.string.e4type, R.string.e5type, R.string.e6type, R.string.e7type };
    int[]        msgIds_f      = { R.string.f1type, R.string.f2type };
    int[]        drawableIds_a = { R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7 };
    int[]        drawableIds_b = { R.drawable.b1, R.drawable.b2, R.drawable.b3, R.drawable.b4, R.drawable.b5 };
    int[]        drawableIds_c = { R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5, R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9, R.drawable.c10, R.drawable.c11, R.drawable.c12, R.drawable.c13, R.drawable.c14, R.drawable.c15, R.drawable.c16 };
    int[]        drawableIds_d = { R.drawable.d1, R.drawable.d2, R.drawable.d3, R.drawable.d4, R.drawable.d5, R.drawable.d6, R.drawable.d7, R.drawable.d8, R.drawable.d9, R.drawable.d10, R.drawable.d11, R.drawable.d12, R.drawable.d13, R.drawable.d14, R.drawable.d15, R.drawable.d16, R.drawable.d17, R.drawable.d18, R.drawable.d19, R.drawable.d20 };
    int[]        drawableIds_e = { R.drawable.e1, R.drawable.e2, R.drawable.e3, R.drawable.e4, R.drawable.e5, R.drawable.e6, R.drawable.e7 };
    int[]        drawableIds_f = { R.drawable.f1, R.drawable.f2 };
    
    int[]        msg;
    int[]        drawableIds;
    int[]        msgIds;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tire_type);
        
        tv_back = (View) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        // 接收的activity
        Intent intent = getIntent();
        if (intent != null)
        {
            int typeId = -1;
            typeId = intent.getIntExtra("title", -1);
            Log.d("title", typeId + "");
            initData(typeId);
        }
        ListView tire_type = (ListView) this.findViewById(R.id.tire_type);
        BaseAdapter ba = new BaseAdapter()
        {
            @Override
            public int getCount()
            {
                return drawableIds.length;
            }
            
            @Override
            public Object getItem(int position)
            {
                return null;
            }
            
            @Override
            public long getItemId(int position)
            {
                return position;
            }
            
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                LinearLayout ll = new LinearLayout(TireActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(5, 5, 5, 5);
                
                ImageView iv = new ImageView(TireActivity.this);
                iv.setImageDrawable(getResources().getDrawable(drawableIds[position]));
                iv.setLayoutParams(new Gallery.LayoutParams(140, 140));
                ll.addView(iv);
                
                TextView tv = new TextView(TireActivity.this);
                tv.setText(getResources().getText(msgIds[position]));// zheli
                                                                     // yuejie?
                tv.setTextSize(20);
                tv.setTextColor(TireActivity.this.getResources().getColor(R.color.grey));
                tv.setPadding(5, 5, 5, 5);
                tv.setGravity(Gravity.LEFT);
                ll.addView(tv);
                return ll;
            }
        };
        tire_type.setAdapter(ba);
        tire_type.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                Intent intent = new Intent(TireActivity.this, TireDetailActivity.class);
                intent.putExtra("msg", getResources().getText(msg[arg2]));
                startActivity(intent);
            }
        });
        
    }
    
    private void initData(int typeId)
    {
        switch (typeId)
        {
            case 0:
                
                drawableIds = drawableIds_a;
                msgIds = msgIds_a;
                msg = msg_a;
                break;
            case 1:
                
                drawableIds = drawableIds_b;
                msgIds = msgIds_b;
                msg = msg_b;
                break;
            case 2:
                drawableIds = drawableIds_c;
                msgIds = msgIds_c;
                msg = msg_c;
                break;
            case 3:
                drawableIds = drawableIds_d;
                msgIds = msgIds_d;
                msg = msg_d;
                break;
            case 4:
                drawableIds = drawableIds_e;
                msgIds = msgIds_e;
                msg = msg_e;
                break;
            case 5:
                drawableIds = drawableIds_f;
                msgIds = msgIds_f;
                msg = msg_f;
                break;
            
            default:
                drawableIds = drawableIds_a;
                msgIds = msgIds_a;
                msg = msg_a;
                break;
        }
        
    }
}
