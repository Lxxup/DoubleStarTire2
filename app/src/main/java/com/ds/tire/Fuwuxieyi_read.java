package com.ds.tire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.bean.FuwuxieyiList;
import com.ds.tire.bean.Fuwuxieyitaihao;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.Constant;
import com.ds.tire.util.NetworkUtils;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class Fuwuxieyi_read extends Activity {

	private LinearLayout xieyi_back;
	private String TAG = "TAG";
	private TextView tv_username;
	private TextView tv_account;
	private TextView tv_address;
	private TextView tv_buytime;
	private TextView tv_platenum;
	private TextView tv_mileage;
//	private TextView tv_chexing;
	private ListView ls_tirecode;
	private List<Fuwuxieyitaihao> fuwuxieyitaihao;
	private ImageView imageView;
	private String cid = "cid";
	private String url;
	private Boolean down=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fuwuxieyi_read);
			
		cid = SpUtils.getString(this, Constant.CID, "cid");
		Log.d(TAG, "CID" + cid);
		
		urlget(cid);
		
		imageView = (ImageView) findViewById(R.id.imageView);
		tv_username = (TextView) findViewById(R.id.username);
		tv_account = (TextView) findViewById(R.id.account);
		tv_address = (TextView) findViewById(R.id.address);
//		tv_chexing = (TextView) findViewById(R.id.chexing);
		tv_platenum = (TextView) findViewById(R.id.platenum);
		tv_mileage = (TextView) findViewById(R.id.mileage);
		tv_buytime = (TextView) findViewById(R.id.buytime);
		fuwuxieyitaihao = new ArrayList<Fuwuxieyitaihao>();
		ls_tirecode = (ListView) findViewById(R.id.ls_tirecode);
		xieyi_back = (LinearLayout) findViewById(R.id.xieyi_back);

		try {
			SelectUserInfo selectUserInfo = new SelectUserInfo(
					Fuwuxieyi_read.this, cid);
			Log.d("cidshiddd",  cid);
			selectUserInfo.execute();
			
			if(down){
			Log.d("TAG", "下载地址dddd" + url);
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
////		获取签名图片
//		try {
//			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		xieyi_back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

//	URL
	private void urlget(String cid){
		Urlget task = new Urlget(this,cid);
		
		task.execute();
	}
	class Urlget extends AsyncNetworkTask<String> {

		String cid;

		public Urlget(Context context, String cid) {
			super(context);
			// TODO Auto-generated constructor stub
			this.cid = cid;
		}

		@Override
		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.urlget(cid);
		}

		@Override
		public void handleResult(String result) {
			Log.d("TAG", "返回值就是签名图片的名称：" + result);

			if (result != null && !result.equals("")) {
				
				url=Constant.Sign_URL+result.trim();
					// finish();
				GetImage image=new GetImage(Fuwuxieyi_read.this,url);
				image.execute();
//				String tupianpath = SpUtils.getString(Fuwuxieyi_read.this, "tupian_name", null);
				down=true;
				Log.d("TAG", "测试是否下载了签名图片" + url+down);
				

				

			}
		}
	}
	
	class GetImage extends AsyncNetworkTask<Bitmap> {
		String url;

		public GetImage(Context context, String url) {
			super(context);
			this.url = url;
		}

		@Override
		protected Bitmap doNetworkTask() {

			return NetworkUtils.getBitmap(url);
		}

		@Override
		protected void handleResult(Bitmap result) {
			Log.d("jje", "网络异常" + result);
			if (result != null) {
				Log.d("11111111111111", "22result" + result);

				imageView.setImageBitmap(result);
			} else {
				Log.d("下载签名图片", "网络异常");
			}
		}
	}

	
	//	selectuserinfo 接口换为 getcardinfo
	class SelectUserInfo extends AsyncNetworkTask<String> {

		private String cid;

		public SelectUserInfo(Context context, String cid) {
			super(context);
			this.cid = cid;

		}

		@Override
		protected String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.selectUserInfo(cid);
		}

		@Override
		protected void handleResult(String result) {
			if (!TextUtils.isEmpty(result)) {
				Log.i("TAG", result);
				try {
					
					JSONArray jsonArray = new JSONArray(result);
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					String username = jsonObject.getString("uName");
					String account = jsonObject.getString("uTel");
					String address = jsonObject.getString("uAddress");
					String buytime = jsonObject.getString("buyTime");
					String platenum = jsonObject.getString("plateNum");
					String mileage = jsonObject.getString("mileage");
					
					tv_username.setText(username);
					tv_account.setText(account);
					tv_address.setText(address);
					tv_platenum.setText(platenum);
					tv_mileage.setText(mileage);
					tv_buytime.setText(buytime);
					
					for(int i = 1; i < jsonArray.length(); i++) {
						
						jsonObject = jsonArray.getJSONObject(i);
						String taihao = jsonObject.getString("taihao");
						String chexing = jsonObject.getString("chexing");
						
						fuwuxieyitaihao.add(new Fuwuxieyitaihao(taihao, chexing));
						
					}
										

				} catch (Exception e) {
					e.printStackTrace();
					Log.d("TAG", "error:" + e.getMessage());
				}
				List<HashMap<String, Object>> ddd = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < fuwuxieyitaihao.size(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("taihao", fuwuxieyitaihao.get(i).getTaihao());
					map.put("chexing", fuwuxieyitaihao.get(i).getChexing());
					ddd.add(map);
					Log.d(TAG, "获得的名字"+fuwuxieyitaihao.get(i).getTaihao());
			}
				// 对解析后的数据进行装载适配器
				SimpleAdapter adapter = new SimpleAdapter(
						Fuwuxieyi_read.this, ddd,
						R.layout.fuwuxieyi_tirecode_item, new String[] { "taihao","chexing"
								}, new int[] { R.id.fu_taihao ,R.id.fu_chexing});
				ls_tirecode.setAdapter(adapter);
				
				
			} else {
				Toast.makeText(Fuwuxieyi_read.this, "网络异常", Toast.LENGTH_SHORT)
						.show();
			}

		}

	}
}