package com.ds.tire;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ShopActivity extends Activity{

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    //setContentView(R.layout.shop);
        		 Uri uri = Uri.parse("http://www.birdweixin.com/p/m/8a68c6dc47a08a600147a40356460de8.shtml");    
        		 Intent it = new Intent(Intent.ACTION_VIEW, uri);    
        		 startActivity(it);
       
	}


}
