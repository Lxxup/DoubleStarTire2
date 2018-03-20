package com.ds.tire;

//import com.ds.tire.util.AsyncNetworkTask.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.ds.tire.bluetooth.Constants;
import com.ds.tire.util.MyApplication;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;
//import com.ryf.weather.*;
import com.ryf.weather.AsyncNetworkTask;
import com.ryf.weather.SlidingMenu;


public class WeatherActivity extends Activity implements OnClickListener {
	final String TAG = "TAG";
	TextView tdDateTV, suggestTV, cityTV, tdWeatherTV, tdTempTV, tdWindTV,
			tdIndexTV, tdUvTV, secDateTV, secWeatherTV, secTempTV, thdDateTV,
			thdWeatherTV, thdTempTV, fthDateTV, fthWeatherTV, fthTempTV;
	ImageView tdImg, secImg, thdImg, fthImg;
	View back, select;
	int tdId = -1, secId = -1, thdId = -1, fthId = -1;
	AssetManager am = null;
	ProgressDialog locatingDialog = null;
	private BDLocationListener locationListener = null;
	String tdDate, suggest, city, tdWeather, tdTemp, tdWind, tdIndex, tdUv,
			secDate, secWeather, secTemp, thdDate, thdWeather, thdTemp,
			fthDate, fthWeather, fthTemp;
	ExpandableListView citysLV;
	View cityView = null;
	Calendar c;
	List<IdName> imgList;
	List<NameValue> provincesList;
	List<List<NameValue>> citysList;
	SlidingMenu slidingMenu = null;
	ListViewAdapter adapter = null;
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				if (tdDate != null)
					tdDateTV.setText(tdDate);
				if (suggest != null)
					suggestTV.setText(suggest);
				if (city != null)
					cityTV.setText(city);
				if (tdWeather != null)
					tdWeatherTV.setText(tdWeather);
				if (tdTemp != null)
					tdTempTV.setText("气温：" + tdTemp);
				//SpUtils.setString(WeatherActivity.this, "wendu", tdTemp);
				if (tdWind != null)
					tdWindTV.setText("风力：" + tdWind);
				if (tdIndex != null)
					tdIndexTV.setText("天气指数：" + tdIndex);
				if (tdUv != null)
					tdUvTV.setText("紫外线强度：" + tdUv);
				if (secDate != null)
					secDateTV.setText(secDate);
				if (secWeather != null)
					secWeatherTV.setText(secWeather);
				if (secTemp != null)
					secTempTV.setText(secTemp);
				if (thdDate != null)
					thdDateTV.setText(thdDate);
				if (thdWeather != null)
					thdWeatherTV.setText(thdWeather);
				if (thdTemp != null)
					thdTempTV.setText(thdTemp);
				if (fthDate != null)
					fthDateTV.setText(fthDate);
				if (fthWeather != null)
					fthWeatherTV.setText(fthWeather);
				if (fthTemp != null)
					fthTempTV.setText(fthTemp);

				if (tdId > 0) {
					tdImg.setImageResource(tdId);
				}
				if (secId > 0) {
					secImg.setImageResource(secId);

				}
				if (thdId > 0) {
					thdImg.setImageResource(thdId);

				}
				if (fthId > 0) {
					fthImg.setImageResource(fthId);

				}

			} catch (Exception e) {
				Log.d(TAG, "error:" + e.getMessage());
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_main);
		// formatCode();
		initViews();
		initCityList();
		String cityId = getIntent().getStringExtra(Constants.KEY_CITY_ID);
		if (cityId != null) {
			GetWeatherTask task = new GetWeatherTask(WeatherActivity.this,
					cityId);
			task.showProgressDialog("正在更新...");
			task.execute();
		} else {
			locatingDialog = new ProgressDialog(this);
			locatingDialog.setMessage("正在定位...");
			locatingDialog.setCancelable(true);
			locatingDialog.show();
			locationListener = new LocationListener();
			MyApplication.getInstance().registerLocationListener(
					locationListener);
			MyApplication.startLocation();
		}

		
		c = Calendar.getInstance();
		tdDate = formatDate(c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

		c.add(Calendar.DAY_OF_MONTH, 1);
		secDate = formatDate(c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));

		c.add(Calendar.DAY_OF_MONTH, 1);
		thdDate = formatDate(c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));

		c.add(Calendar.DAY_OF_MONTH, 1);
		fthDate = formatDate(c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		myHandler.obtainMessage().sendToTarget();
	}

	private String formatDate(int month, int day) {
		return c.get(Calendar.MONTH) + 1 + "月" + c.get(Calendar.DAY_OF_MONTH)
				+ "日";
	}

	class IdName {
		int id;
		String title;

		public IdName(int id, String title) {
			super();
			this.id = id;
			this.title = title;
		}

	}

	class NameValue {
		String name, code;

		public NameValue(String name, String value) {
			super();
			this.name = name;
			this.code = value;
		}

	}

	Bitmap getBitmapFromUrl(int id, int imageWidth) {
		Bitmap newBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), id, options);
		if (options.outWidth != 0
				&& options.outHeight != 0
				&& (options.outWidth > imageWidth || options.outHeight > imageWidth)) {
			int sampleSize = options.outWidth / imageWidth;
			if (sampleSize < 1) {
				options.inSampleSize = 1;
			} else {
				options.inSampleSize = sampleSize;
			}
		}
		Log.d(TAG, "imageWidth:" + imageWidth + ";bounds:"
				+ options.inSampleSize);
		options.inJustDecodeBounds = false;
		BitmapFactory.decodeResource(getResources(), id, options);
		return newBitmap;
	}

	private int getImgId(String title) {
		int id = -1;
		if (imgList == null || imgList.size() == 0) {
			imgList = new ArrayList<IdName>();
			imgList.add(new IdName(R.drawable.d_sun, "晴"));
			imgList.add(new IdName(R.drawable.snow, "小雪"));
			imgList.add(new IdName(R.drawable.snow_m, "中雪"));
			imgList.add(new IdName(R.drawable.snow, "阵雪"));
			imgList.add(new IdName(R.drawable.snow_b, "大雪"));
			imgList.add(new IdName(R.drawable.snow_bao, "暴雪"));
			imgList.add(new IdName(R.drawable.rain_s, "小雨"));
			imgList.add(new IdName(R.drawable.rain_m, "中雨"));
			imgList.add(new IdName(R.drawable.rain_m, "阵雨"));
			imgList.add(new IdName(R.drawable.rain_b, "大雨"));
			imgList.add(new IdName(R.drawable.rain_bao, "暴雨"));
			imgList.add(new IdName(R.drawable.rain_bbao, "大暴雨"));
			imgList.add(new IdName(R.drawable.rain_bbbao, "超大暴雨"));
			imgList.add(new IdName(R.drawable.rain_dong, "冻雨"));
			imgList.add(new IdName(R.drawable.rain_thunder, "雷阵雨"));
			imgList.add(new IdName(R.drawable.rain_thunder_snow, "雷阵雨夹雪"));
			imgList.add(new IdName(R.drawable.rain_snow, "雨夹雪"));
			imgList.add(new IdName(R.drawable.cloudy, "阴天"));
			imgList.add(new IdName(R.drawable.cloudy, "多云"));
			imgList.add(new IdName(R.drawable.dust, "浮尘"));
			imgList.add(new IdName(R.drawable.sand, "扬沙"));
			imgList.add(new IdName(R.drawable.sandstorm, "沙尘暴"));
			imgList.add(new IdName(R.drawable.fog, "雾"));
			imgList.add(new IdName(R.drawable.cloudy, "阴转多云"));
			imgList.add(new IdName(R.drawable.stc, "晴转多云"));
			imgList.add(new IdName(R.drawable.snow_stm, "小雪转中雪"));
			imgList.add(new IdName(R.drawable.snow_mtb, "中雪转大雪"));
			imgList.add(new IdName(R.drawable.snow_btbao, "大雪转暴雪"));
			imgList.add(new IdName(R.drawable.rain_stm, "小雨转中雨"));
			imgList.add(new IdName(R.drawable.rain_mtb, "中雨转大雨"));
			imgList.add(new IdName(R.drawable.rain_btbao, "大雨转暴雨"));
			imgList.add(new IdName(R.drawable.rain_baotbbao, "暴雨转大暴雨"));
			imgList.add(new IdName(R.drawable.rain_bbaotbbbao, "大暴雨转超大暴雨"));

		}
		String matched = "";
		int match = 0;
		for (IdName img : imgList) {
			int m = matchLength(title, img.title);
			if (m > match) {
				match = m;
				matched = img.title;
				id = img.id;
				if (match == title.length()
						&& title.length() == img.title.length())
					break;

			}
		}
		Log.d(TAG, "title+" + title + ";matched=" + matched + ";match length="
				+ match + ";title length=" + title.length());
		return id;
	}

	public int matchLength(String pattern, String str) {
		int chineseCount = 0;
		String regEx = "[" + pattern + "]";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(str);
		while (mat.find()) {
			chineseCount++;
		}
		return chineseCount;
	}

	void resetData() {
		tdDate = null;
		suggest = null;
		city = null;
		tdWeather = null;
		tdTemp = null;
		tdWind = null;
		tdIndex = null;
		tdUv = null;
		secDate = null;
		secWeather = null;
		secTemp = null;
		thdDate = null;
		thdWeather = null;
		thdTemp = null;
		fthDate = null;
		fthWeather = null;
		fthTemp = null;
		tdId = -1;
		secId = -1;
		thdId = -1;
		fthId = -1;
		tdImg.setImageBitmap(null);
		secImg.setImageBitmap(null);
		thdImg.setImageBitmap(null);
		fthImg.setImageBitmap(null);
		tdDateTV.setText("");
		suggestTV.setText("");
		cityTV.setText("");
		tdWeatherTV.setText("");
		tdTempTV.setText("");
		tdWindTV.setText("");
		tdIndexTV.setText("");
		tdUvTV.setText("");
		secDateTV.setText("");
		secWeatherTV.setText("");
		secTempTV.setText("");
		thdDateTV.setText("");
		thdWeatherTV.setText("");
		thdTempTV.setText("");
		fthDateTV.setText("");
		fthWeatherTV.setText("");
		fthTempTV.setText("");
	}

	private void initViews() {
		suggestTV = (TextView) findViewById(R.id.suggest_tv);
		cityTV = (TextView) findViewById(R.id.city_tv);
		tdIndexTV = (TextView) findViewById(R.id.today_index);
		tdUvTV = (TextView) findViewById(R.id.today_uv);
		tdWindTV = (TextView) findViewById(R.id.today_wind);

		tdDateTV = (TextView) findViewById(R.id.today_date);
		secDateTV = (TextView) findViewById(R.id.sec_date);
		thdDateTV = (TextView) findViewById(R.id.third_date);
		fthDateTV = (TextView) findViewById(R.id.forth_date);

		tdTempTV = (TextView) findViewById(R.id.today_temp);
		secTempTV = (TextView) findViewById(R.id.sec_temp);
		thdTempTV = (TextView) findViewById(R.id.third_temp);
		fthTempTV = (TextView) findViewById(R.id.forth_temp);

		tdWeatherTV = (TextView) findViewById(R.id.today_weather);
		secWeatherTV = (TextView) findViewById(R.id.sec_weather);
		thdWeatherTV = (TextView) findViewById(R.id.third_weather);
		fthWeatherTV = (TextView) findViewById(R.id.forth_weather);

		tdImg = (ImageView) findViewById(R.id.today_img);
		secImg = (ImageView) findViewById(R.id.sec_img);
		thdImg = (ImageView) findViewById(R.id.third_img);
		fthImg = (ImageView) findViewById(R.id.forth_img);
		back = findViewById(R.id.back);
		back.setOnClickListener(this);
		select = findViewById(R.id.select_city);
		select.setOnClickListener(this);
		cityView = LayoutInflater.from(this).inflate(R.layout.weather_city,
				null);
		citysLV = (ExpandableListView) cityView.findViewById(R.id.citys_elv);
		initSlidingMenu();
	}

	void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// slidingMenu.setSecondaryMenu(R.layout.activity_main);
		// slidingMenu.setMenu(R.layout.weather_city);
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		slidingMenu.setBehindOffset(screenWidth / 3);
		slidingMenu.setMenu(cityView);
	}

	private void formatCode() {
		if (am == null)
			am = getAssets();
		try {
			InputStream in = am.open("code.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, Charset.forName("UTF-8")));
			String line = null;
			File f2 = new File(Environment.getExternalStorageDirectory(),
					"compass");
			if (!f2.exists())
				f2.mkdir();
			File codeFile = new File(f2, "cityCode.txt");
			FileWriter writer = new FileWriter(codeFile, true);
			BufferedWriter bw = new BufferedWriter(writer);
			try {
				JSONArray array = new JSONArray();
				line = reader.readLine();
				while (line != null && line.contains(";")) {
					String[] text = line.split(";");
					JSONObject object = new JSONObject();
					JSONObject province = new JSONObject();
					String[] pro = text[0].split("=");
					province.put("name", pro[1]);
					province.put("code", pro[0]);
					object.put("province", province);
					line = reader.readLine();
					JSONArray citys = new JSONArray();
					while (line != null && !line.contains(";")) {
						String[] city = line.split("=");
						JSONObject cityObj = new JSONObject();
						cityObj.put("name", city[1]);
						cityObj.put("code", city[0]);
						Log.d(TAG, cityObj.toString());
						citys.put(cityObj);
						line = reader.readLine();
					}
					object.put("citys", citys);
					array.put(object);
				}
				bw.write(array.toString());
				bw.newLine();
				bw.flush();
				in.close();
				reader.close();
				writer.close();
				Log.d(TAG, "read success!");
			} catch (JSONException e) {
				Log.d(TAG, "error:" + e.getMessage());
				e.printStackTrace();
			}
			am.close();
		} catch (IOException e) {
			Log.d(TAG, "error:" + e.getMessage());
			e.printStackTrace();
		}

	}

	class GetWeatherTask extends AsyncNetworkTask<String> {
		String cityId;

		public GetWeatherTask(Context context, String cityId) {
			super(context);
			this.cityId = cityId;
		}

		@Override
		protected String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.getWeather(cityId);
		}

		@Override
		protected void handleResult(String result) {
			if (result != null && !result.equals("")) {
				Log.d(TAG, result);
				try {
					JSONObject object = new JSONObject(result)
							.getJSONObject("weatherinfo");
					suggest = object.getString("index_d");
					city = object.getString("city");
					tdWeather = object.getString("weather1");
					tdTemp = object.getString("temp1");
					tdWind = object.getString("wind1");
					tdIndex = object.getString("index");
					tdUv = object.getString("index_uv");
					secWeather = object.getString("weather2");
					secTemp = object.getString("temp2");
					thdWeather = object.getString("weather3");
					thdTemp = object.getString("temp3");
					fthWeather = object.getString("weather4");
					fthTemp = object.getString("temp4");
					if (tdWeather.equals("晴")) {
						int hour = c.get(Calendar.HOUR_OF_DAY);
						if (hour >= 6 && hour <= 18) {
							tdId = R.drawable.d_sun;
						} else {
							tdId = R.drawable.n_sun;
						}
					} else {
						tdId = getImgId(tdWeather);
					}
					secId = getImgId(secWeather);
					thdId = getImgId(thdWeather);
					fthId = getImgId(fthWeather);
					myHandler.obtainMessage().sendToTarget();

				} catch (Exception e) {
					Log.d(TAG, "error:" + e.getMessage());
					e.printStackTrace();
				}
			}else {
				Toast.makeText(WeatherActivity.this, "网络异常",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	private void initCityList() {
		provincesList = new ArrayList<WeatherActivity.NameValue>();
		citysList = new ArrayList<List<NameValue>>();
		if (am == null)
			am = getAssets();
		InputStream in;
		try {
			in = am.open("code.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, Charset.defaultCharset()));
			String line = reader.readLine();
			if (line != null) {
				JSONArray array = new JSONArray(line);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					JSONObject proObj = obj.getJSONObject("province");
					provincesList.add(new NameValue(proObj.getString("name"),
							proObj.getString("code")));
					JSONArray citysObj = obj.getJSONArray("citys");
					List<NameValue> citys = new ArrayList<WeatherActivity.NameValue>();
					for (int j = 0; j < citysObj.length(); j++) {
						JSONObject cityObj = citysObj.getJSONObject(j);
						citys.add(new NameValue(cityObj.getString("name"),
								cityObj.getString("code")));
					}
					citysList.add(citys);
					if (provincesList.size() > 0) {
						adapter = new ListViewAdapter(this);
						citysLV.setAdapter(adapter);
						citysLV.setOnChildClickListener(new OnChildClick());
					}

				}

			}
			reader.close();
		} catch (Exception e) {
			Log.d(TAG, "error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	private String getCityId(String province, String city) {
		if (province == null && city == null)
			return "";

		if (province == null || province.equals(""))
			province = city;
		if (city == null || city.equals(""))
			city = province;
		Log.d(TAG, "province=" + province + ";city=" + city);
		String id = "";
		try {
			for (int i = 0; i < provincesList.size(); i++) {
				if (province.contains(provincesList.get(i).name)) {
					id += provincesList.get(i).code;
					for (NameValue c : citysList.get(i)) {
						if (city.contains(c.name)) {
							id += c.code;
							break;
						}
					}
					break;
				}
			}
		} catch (Exception e) {
			Log.d(TAG, "error:" + e.getMessage());
			e.printStackTrace();
		}

		return id;
	}

	class OnChildClick implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			String cityId = provincesList.get(groupPosition).code;
			cityId += citysList.get(groupPosition).get(childPosition).code;
			if (slidingMenu.isMenuShowing()) {
				slidingMenu.showContent(true);
			} else {
				slidingMenu.showMenu(true);
			}
			GetWeatherTask task = new GetWeatherTask(WeatherActivity.this,
					cityId);
			task.showProgressDialog("正在更新...");
			task.execute();
			return true;
			
		}
		
	}

	class ListViewAdapter extends BaseExpandableListAdapter {
		Context mContext;

		public ListViewAdapter(Context mContext) {
			super();
			this.mContext = mContext;
		}

		@Override
		public int getGroupCount() {
			return provincesList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return citysList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return provincesList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return citysList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			NameValue group = provincesList.get(groupPosition);
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.weather_city_item, null);
			}

			TextView textView = (TextView) convertView
					.findViewById(R.id.city_tv);

			textView.setTextColor(Color.GRAY);
			textView.setTextSize(20);
			textView.setPadding(5, 8, 0, 8);
			textView.setText(group.name);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			NameValue child = citysList.get(groupPosition).get(childPosition);
			if (convertView == null)
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.weather_city_item, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.city_tv);
			textView.setTextSize(18);
			textView.setTextColor(Color.BLACK);
			textView.setPadding(20, 5, 0, 5);
			textView.setText(child.name);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	class LocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			locatingDialog.dismiss();
			//formatCode();
			try {
				String cityId = getCityId(location.getProvince(),
						location.getCity());
				if (cityId != null && !cityId.equals("")) {
					GetWeatherTask task = new GetWeatherTask(
							WeatherActivity.this, cityId);
					task.showProgressDialog("正在更新...");
					task.execute();
					if (locationListener != null) {
						MyApplication.stopLocation();
						MyApplication.getInstance().unregisterLocationListener(
								locationListener);
						locationListener = null;
					}
				}

			} catch (Exception e) {
				Log.d(TAG, "error:" + e.getMessage());
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onDestroy() {
		if (locationListener != null) {
			MyApplication.stopLocation();
			MyApplication.getInstance().unregisterLocationListener(
					locationListener);
			locationListener = null;
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.select_city:
			if (slidingMenu.isMenuShowing()) {
				slidingMenu.showContent(true);
			} else {
				slidingMenu.showMenu(true);
			}
			break;
		default:
			break;
		}

	}

}
