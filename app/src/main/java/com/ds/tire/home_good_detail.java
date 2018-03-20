package com.ds.tire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by developer on 2018/3/19.
 */

public class home_good_detail extends Activity {
    TextView tv1_detail;
    TextView tv2_detail;
    TextView tv3_detail;
    Button btn_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_good_detail);
        findViewById();

        Intent intent33 = getIntent();
        String data3 = intent33.getStringExtra("extra_data");
        String sss1 = data3 + "的属性1";
        String sss2 = data3 + "的属性2";
        String sss3 = data3 + "的属性3";

        tv1_detail.setText(sss1);
        tv2_detail.setText(sss2);
        tv3_detail.setText(sss3);


        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_good_detail.this.finish();
               /* Intent intent44=new Intent(home_good_detail.this,TabAActivity.class);
                startActivity(intent44);*/
            }
        });


    }

    private void findViewById() {
        tv1_detail = (TextView) findViewById(R.id.tv1_detail);
        tv2_detail = (TextView) findViewById(R.id.tv2_detail);
        tv3_detail = (TextView) findViewById(R.id.tv3_detail);
        btn_detail = (Button) findViewById(R.id.btn_detail);
    }


}
