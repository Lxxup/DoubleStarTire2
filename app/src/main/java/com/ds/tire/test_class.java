package com.ds.tire;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by developer on 2018/3/19.
 */

public class test_class extends Activity {
String [] datasoures={"dhsfuh","fd积分多少","地方都是老款","dhsfuh","fd积分多少","地方都是老款"};

    ListView lst1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lst_test_item);
         lst1=(ListView)findViewById(R.id.lst1);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                datasoures
        );

        lst1.setAdapter(adapter);


    }
}
