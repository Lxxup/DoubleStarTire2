package com.ds.tire;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.and.netease.utils.MoveBg;

public class MainActivity extends TabActivity {
	TabHost tabHost;
	TabHost.TabSpec tabSpec;
	RadioGroup radioGroup;
	RelativeLayout bottom_layout;
	ImageView img;
	int startLeft;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);

		bottom_layout = (RelativeLayout) findViewById(R.id.layout_bottom);

		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab_a").setIndicator("tab_a")
				.setContent(new Intent(this, TabAActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("tab_b").setIndicator("tab_b")
				.setContent(new Intent(this, MonitorActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("tab_c").setIndicator("tab_c")
				.setContent(new Intent(this,Myorder.class)));
		tabHost.addTab(tabHost.newTabSpec("tab_d").setIndicator("tab_d")
				.setContent(new Intent(this, TabDActivity.class)));

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		WindowManager wm = (WindowManager)this.getSystemService(
				Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) radioGroup.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���   
		linearParams.height = height/9;// ���ؼ��ĸ�ǿ�������Ļ1/7  
		radioGroup.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�myGrid    
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);

		img = new ImageView(this);

		bottom_layout.addView(img);
	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_news:
				tabHost.setCurrentTabByTag("tab_a");
//				moveFrontBg(img, startLeft, 0, 0, 0);
				MoveBg.moveFrontBg(img, startLeft, 0, 0, 0);
				startLeft = 0;
				break;
			case R.id.radio_topic:
				tabHost.setCurrentTabByTag("tab_b");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth(), 0, 0);
				startLeft = img.getWidth();
				break;
			case R.id.radio_pic:
				tabHost.setCurrentTabByTag("tab_c");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 2, 0, 0);
				startLeft = img.getWidth() * 2;
				break;
			case R.id.radio_follow:
				tabHost.setCurrentTabByTag("tab_d");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 3, 0, 0);
				
				
				startLeft = img.getWidth() * 3;
				break;
			

			default:
				break;
			}
		}
	};
}