package com.ds.tire;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.NetworkUtils;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class PDetailActivity extends Activity {

	String did=null;
	String name=null;
	String introduction=null;
	String feature=null;
	String image=null;
	TextView tname=null;
	TextView tfeature=null;
	TextView tintroduction=null;
	ImageView timage1;
	ImageView timage2;
	ImageView timage3;
	String img1=null;
	String img2=null;
	String img3=null;
	public String path1=null;
	public String path2=null;
	public String path3=null;
	private static final String TAG = "TAG";
	LinearLayout back;
	//private ListView detaillist = null;
	public NetworkUtils setNetworkUtils;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pdetail);
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
		 Bundle bundle = this.getIntent().getExtras();
	      did = bundle.getString("id");	
	      name = bundle.getString("name");
	      introduction = bundle.getString("introduction");
	      feature = bundle.getString("feature");
	      path1 = bundle.getString("path1"); 
	      path2 = bundle.getString("path2");
	      path3 = bundle.getString("path3");
		  Log.d("TAG","上一个传来的path1:"+path1);
		  Log.d("TAG","上一个传来的path2:"+path2);
		  Log.d("TAG","上一个传来的path3:"+path3);
		  findViewById();		  

		  try {                                        
			  Selectimage selectimage = new Selectimage(PDetailActivity.this,SpUtils.getString(this, SpUtils.ACCOUNT, ""));	
				Log.d("TAG", "测试！SelectPDetail");
				selectimage.execute();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		  try {                                       
			  Selectimage2 selectimage2 = new Selectimage2(PDetailActivity.this,SpUtils.getString(this, SpUtils.ACCOUNT, ""));				
				Log.d("TAG", "测试！SelectPDetail");
				selectimage2.execute();				
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		  try {                                       
			  Selectimage3 selectimage3 = new Selectimage3(PDetailActivity.this,SpUtils.getString(this, SpUtils.ACCOUNT, ""));				
				Log.d("TAG", "测试！SelectPDetail");
				selectimage3.execute();				
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		  tname.setText(name);
	      tfeature.setText(feature);
	      tintroduction.setText(introduction);
	}
	private void findViewById() {
		//��Ʒ��ϸ������ʾ�ؼ�
		
		tname = (TextView) findViewById(R.id.name);
		tfeature = (TextView) findViewById(R.id.feature);
		tintroduction = (TextView) findViewById(R.id.introduction);
		timage1 = (ImageView) findViewById(R.id.image1);
		timage2 = (ImageView) findViewById(R.id.image3);
		timage3 = (ImageView) findViewById(R.id.image4);

	}
	public class Selectimage extends AsyncNetworkTask<Bitmap> {
		private String url;
		public Selectimage(Context context,String url) {
			super(context);
			this.url=path1;
			Log.d("TAG","类中定义的url:"+url);
			// TODO Auto-generated constructor stub
		}
		@Override
		protected Bitmap doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.getBitmap(url);
		}
		
		protected void handleResult(Bitmap result) {
			timage1.setImageBitmap(result);
			
		}
 }
	public class Selectimage2 extends AsyncNetworkTask<Bitmap> {
		private String url;
		public Selectimage2(Context context,String url) {
			super(context);
			this.url=path2;
			Log.d("TAG","类中定义的url:"+url);
			// TODO Auto-generated constructor stub
		}
		@Override
		protected Bitmap doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.getBitmap(url);
		}
		
		protected void handleResult(Bitmap result) {
			timage2.setImageBitmap(result);
			
		}
    }
	public class Selectimage3 extends AsyncNetworkTask<Bitmap> {
		private String url;
		public Selectimage3(Context context,String url) {
			super(context);
			this.url=path3;
			Log.d("TAG","类中定义的url:"+url);
			// TODO Auto-generated constructor stub
		}
		@Override
		protected Bitmap doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.getBitmap(url);
		}
		
		protected void handleResult(Bitmap result) {
			timage3.setImageBitmap(result);
			
		}
    }	
}
