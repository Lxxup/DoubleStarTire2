package com.ds.tire;

import com.ds.tire.util.SpUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SetDataActivity extends Activity {
	private EditText setWenduTop;
	private EditText setWenduBot;
	private EditText setYaqiangTop;
	private EditText setYaqiangBot;
	private Button submit;
	private View back;
	private String wenduTop;
	private String wenduBot;
	private String yaqiangTop;
	private String yaqiangBot;
	private String gwenduTop;
	private String gwenduBot;
	private String gyaqiangTop;
	private String gyaqiangBot;
	//private int wheelsCount = 0;
	
	private TextView chexing=null;
	private RadioGroup car=null;
	private RadioButton smallCar=null;
	private RadioButton bigCar=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_data);
		this.chexing=(TextView) super.findViewById(R.id.chexing);
		this.car=(RadioGroup) super.findViewById(R.id.car);
		this.smallCar=(RadioButton) super.findViewById(R.id.smallCar);
		this.bigCar=(RadioButton) super.findViewById(R.id.bigCar);
		this.car.setOnCheckedChangeListener(new OnCheckedChangeListenerImp());
		//wheelsCount = SpUtils.getInteger(SetDataActivity.this, "wheels_count", 0);
//		if(wheelsCount==0){
//			Toast.makeText(getApplicationContext(), "请确保已经输入轮胎数量！！！", Toast.LENGTH_LONG).show();  
//		} 
		init();	
		back = findViewById(R.id.safe_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private class OnCheckedChangeListenerImp implements OnCheckedChangeListener{
		

		public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		if(SetDataActivity.this.smallCar.getId()==checkedId){
		
		setWenduTop.setText("60");
		setWenduBot.setText("-10");
		setYaqiangTop.setText("320");
		setYaqiangBot.setText("180");
		SpUtils.setString(SetDataActivity.this, "cheXing", "small");
		}
		else if(SetDataActivity.this.bigCar.getId()==checkedId){
			   setWenduTop.setText("60");
			   setWenduBot.setText("-10");
			   setYaqiangTop.setText("1200");
			   setYaqiangBot.setText("600");
		SpUtils.setString(SetDataActivity.this, "cheXing", "big");
		}
		
		}
		}

	public void init(){
		
		 setWenduTop=(EditText)findViewById(R.id.setWenduTop);
		 setWenduBot=(EditText)findViewById(R.id.setWenduBot);
		 setYaqiangTop=(EditText)findViewById(R.id.setYaqiangTop);
		 setYaqiangBot=(EditText)findViewById(R.id.setYaqiangBot);
		 SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE); 
			boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true); 
			Editor editor = sharedPreferences.edit(); 
			if (isFirstRun) 
			{ 
			Log.d("debug", "第一次运行"); 
//			if(SpUtils.getString(SetDataActivity.this, "cheXing", "").equals("small")){
				setWenduTop.setText("60");
				setWenduBot.setText("-10");
				setYaqiangTop.setText("320");
				setYaqiangBot.setText("180");
				editor.putBoolean("isFirstRun", false); 
				editor.commit(); 
//			 }else if(SpUtils.getString(SetDataActivity.this, "cheXing", "").equals("big")){
//			
//			   setWenduTop.setText("60");
//			   setWenduBot.setText("-10");
//			   setYaqiangTop.setText("1200");
//			   setYaqiangBot.setText("600");
//			   editor.putBoolean("isFirstRun", false); 
//			   editor.commit(); 
//			  }
			}else { 
			Log.d("debug", "不是第一次运行"); 
			if(SpUtils.getString(SetDataActivity.this, "cheXing", "").equals("small")){
				 if(SpUtils.getString(SetDataActivity.this, "WENDUTOP", "")!=null&&SpUtils.getString(SetDataActivity.this, "WENDUTOP", "")!=null&&SpUtils.getString(SetDataActivity.this, "YAQIANGTOP", "")!=null&&SpUtils.getString(SetDataActivity.this, "YAQIANGBOT", "")!=null){
			        	setWenduTop.setText(SpUtils.getString(SetDataActivity.this, "WENDUTOP", ""));
						setWenduBot.setText(SpUtils.getString(SetDataActivity.this, "WENDUBOT", ""));
						setYaqiangTop.setText(SpUtils.getString(SetDataActivity.this, "YAQIANGTOP", ""));
						setYaqiangBot.setText(SpUtils.getString(SetDataActivity.this, "YAQIANGBOT", ""));
			        }else{
			        	
			        }
			}else if(SpUtils.getString(SetDataActivity.this, "cheXing", "").equals("big")){
				if(SpUtils.getString(SetDataActivity.this, "GWENDUTOP", "")!=null&&SpUtils.getString(SetDataActivity.this, "GWENDUTOP", "")!=null&&SpUtils.getString(SetDataActivity.this, "GYAQIANGTOP", "")!=null&&SpUtils.getString(SetDataActivity.this, "GYAQIANGBOT", "")!=null){
		        	setWenduTop.setText(SpUtils.getString(SetDataActivity.this, "GWENDUTOP", ""));
					setWenduBot.setText(SpUtils.getString(SetDataActivity.this, "GWENDUBOT", ""));
					setYaqiangTop.setText(SpUtils.getString(SetDataActivity.this, "GYAQIANGTOP", ""));
					setYaqiangBot.setText(SpUtils.getString(SetDataActivity.this, "GYAQIANGBOT", ""));
		        }else{
		        	
		        }
			}
			
			} 
		
		 submit=(Button)findViewById(R.id.submitData);
		 if(SpUtils.getString(SetDataActivity.this, "cheXing", "").equals("small")){
		   wenduTop=setWenduTop.getText().toString().trim();
		   wenduBot=setWenduBot.getText().toString().trim();
		   yaqiangTop=setYaqiangTop.getText().toString().trim();
		   yaqiangBot=setYaqiangTop.getText().toString().trim();
		 }else if(SpUtils.getString(SetDataActivity.this, "cheXing", "").equals("big")){
			 gwenduTop=setWenduTop.getText().toString().trim();
			 gwenduBot=setWenduBot.getText().toString().trim();
			 gyaqiangTop=setYaqiangTop.getText().toString().trim();
			 gyaqiangBot=setYaqiangTop.getText().toString().trim();
		 }
		 submit.setOnClickListener(listner);
		 
	}
	public void setView(){
		 
		 
	}

	OnClickListener listner=new OnClickListener() {
		//发送广播
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(SpUtils.getString(SetDataActivity.this, "cheXing", "").equals("small")){
			 wenduTop=setWenduTop.getText().toString().trim();
			 wenduBot=setWenduBot.getText().toString().trim();
			 yaqiangTop=setYaqiangTop.getText().toString().trim();
			 yaqiangBot=setYaqiangBot.getText().toString().trim();
			 SpUtils.setString(SetDataActivity.this, "WENDUTOP", wenduTop);
			 SpUtils.setString(SetDataActivity.this, "WENDUBOT", wenduBot);
			 SpUtils.setString(SetDataActivity.this, "YAQIANGTOP", yaqiangTop);
			 SpUtils.setString(SetDataActivity.this, "YAQIANGBOT", yaqiangBot);
			}else if(SpUtils.getString(SetDataActivity.this, "cheXing", "").equals("big")){
				gwenduTop=setWenduTop.getText().toString().trim();
				 gwenduBot=setWenduBot.getText().toString().trim();
				 gyaqiangTop=setYaqiangTop.getText().toString().trim();
				 gyaqiangBot=setYaqiangBot.getText().toString().trim();
				 SpUtils.setString(SetDataActivity.this, "GWENDUTOP", gwenduTop);
				 SpUtils.setString(SetDataActivity.this, "GWENDUBOT", gwenduBot);
				 SpUtils.setString(SetDataActivity.this, "GYAQIANGTOP", gyaqiangTop);
				 SpUtils.setString(SetDataActivity.this, "GYAQIANGBOT", gyaqiangBot);
			}
				
//			 MyApplication.WENDUTOP=wenduTop;
//			 MyApplication.WENDUBOT=wenduBot;
//			 MyApplication.YAQIANGTOP=yaqiangTop;
//			 MyApplication.YAQIANGBOT=yaqiangBot;
			//Intent intent = new Intent(MyApplication.SET_WEN_YA);  
	            //intent.setAction(MyApplication.SET_WEN_YA);  
//	            intent.putExtra("wenduTop", wenduTop); 
//	            intent.putExtra("wenduBot", wenduBot);
//	            intent.putExtra("yaqiangTop", yaqiangTop);
//	            intent.putExtra("yaqiangBot", yaqiangBot);
	          //  sendBroadcast(intent);  
			    
	            //Log.i("DataRecevier", "接收到:"+"温度上限："+ MyApplication.WENDUTOP+"温度下限："+MyApplication.WENDUBOT+"压强上限："+MyApplication.YAQIANGTOP+"压强下线："+MyApplication.YAQIANGBOT);  
	            Toast.makeText(getApplicationContext(), "数据设置成功！", Toast.LENGTH_LONG).show();  
		}
	};
	

}
