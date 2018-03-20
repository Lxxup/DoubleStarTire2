package com.ds.tire;

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
import android.text.TextUtils;
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
import cn.com.weather.api.Weather;
import cn.com.weather.api.WeatherAsyncTask;
import cn.com.weather.api.WeatherParseUtil;
import cn.com.weather.constants.Constants.Language;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.ds.tire.bluetooth.Constants;
import com.ds.tire.util.MyApplication;
import com.ryf.weather.SlidingMenu;
import com.ryf.weather.TimeUtils;
import com.umeng.analytics.MobclickAgent;
//import com.ouc.sei.lorry.Constant;
//import com.ouc.sei.lorry.MyApplication;
//import com.ouc.sei.lorry.R;
//import com.ouc.sei.lorry.SlidingMenu;


public class WeatherInfoActivity extends Activity implements OnClickListener {
	final String TAG = "TAG";
	TextView tdDateTV, suggestTV, cityTV, tdWeatherTV, tdTempTV, tdWindTV,
			tdIndexTV, tdAirTV, secDateTV, secWeatherTV, secTempTV, thdDateTV,
			thdWeatherTV, thdTempTV, fthDateTV, fthWeatherTV, fthTempTV;
	ImageView tdImg, secImg, thdImg, fthImg;
	View back, select;
	int[] imgIds = new int[] { -1, -1, -1, -1 };
	AssetManager am = null;
	ProgressDialog locatingDialog = null;
	private BDLocationListener locationListener = null;
	String tdDate, suggest, city, tdWind, tdIndex, airQuality, secDate,
			thdDate, fthDate;
	String[] weathers, temps;
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
				if (weathers[0] != null)
					tdWeatherTV.setText(weathers[0]);
				if (temps[0] != null)
					tdTempTV.setText("气温：" + temps[0]);
				if (tdWind != null && !tdWind.equals(""))
					tdWindTV.setText("风力：" + tdWind);
				else {
					tdWindTV.setVisibility(View.GONE);
				}
				if (tdIndex != null)
					tdIndexTV.setText(tdIndex);
				if (airQuality != null)
					tdAirTV.setText("空气质量：" + airQuality);

				if (secDate != null)
					secDateTV.setText(secDate);
				if (weathers[1] != null)
					secWeatherTV.setText(weathers[1]);
				if (temps[1] != null)
					secTempTV.setText(temps[1]);

				if (thdDate != null)
					thdDateTV.setText(thdDate);
				if (weathers[2] != null)
					thdWeatherTV.setText(weathers[2]);
				if (temps[2] != null)
					thdTempTV.setText(temps[2]);

				if (fthDate != null)
					fthDateTV.setText(fthDate);
				if (weathers[3] != null)
					fthWeatherTV.setText(weathers[3]);
				if (temps[3] != null)
					fthTempTV.setText(temps[3]);

				if (imgIds[0] > 0) {
					tdImg.setImageResource(imgIds[0]);
				}
				if (imgIds[1] > 0) {
					secImg.setImageResource(imgIds[1]);

				}
				if (imgIds[2] > 0) {
					thdImg.setImageResource(imgIds[2]);

				}
				if (imgIds[3] > 0) {
					fthImg.setImageResource(imgIds[3]);

				}

			} catch (Exception e) {
				Log.d(TAG, "error:" + e.getMessage());
			}

		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_main);
		// formatCode();
		initViews();
		initCityList();
		String cityId = getIntent().getStringExtra(Constants.KEY_CITY_ID);
		if (cityId != null && !cityId.equals("")) {
			// GetWeatherTask task = new
			// GetWeatherTask(WeatherInfoActivity.this,
			// cityId);
			// task.showProgressDialog("正在更新...");
			// task.execute();
			Log.d(TAG, "locatedid" + cityId);
			if (locatingDialog == null) {
				locatingDialog = new ProgressDialog(this);
				locatingDialog.setCancelable(true);
			}
			locatingDialog.setMessage("正在获取天气...");
			locatingDialog.show();
			task.execute(cityId, Language.ZH_CN);
		} else {
			if (locatingDialog == null) {
				locatingDialog = new ProgressDialog(this);
				locatingDialog.setCancelable(true);
			}
			locatingDialog.setMessage("正在定位...");
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

	WeatherAsyncTask task = new WeatherAsyncTask(this) {

		@Override
		protected void onPostExecute(Weather arg0) {
			try {
				if (locatingDialog != null && locatingDialog.isShowing()) {
					locatingDialog.dismiss();
				}
				if (arg0 != null) {
					Log.d(TAG, "cityInfo" + arg0.getCityInfo().toString());
					Log.d(TAG, "weatherText" + arg0.getWeatherText().toString());
					suggest = arg0.getWeatherText().getString("r1");
					city = arg0.getCityInfo().getString("c3");
					weathers = new String[7];
					temps = new String[7];
					//int hour = c.get(Calendar.HOUR_OF_DAY);
					String factTime=arg0.getFactTime();
					JSONArray weatherArray = arg0.getWeatherForecastInfo(0);
					for (int i = 0; i < weatherArray.length(); i++) {
						JSONObject weatherObj = weatherArray.getJSONObject(i);
						Log.d(TAG, "weather:" + weatherObj.toString());

						weathers[i] = WeatherParseUtil.parseWeather(
								weatherObj.getString("fa"), Language.ZH_CN);

						if (TimeUtils.isTimeNowAfter(factTime) && TextUtils.isEmpty(weathers[i])) {
							weathers[i] = WeatherParseUtil.parseWeather(
									weatherObj.getString("fb"), Language.ZH_CN);
						}
						String low = weatherObj.getString("fc");
						String heigh = weatherObj.getString("fd");
						if (!TextUtils.isEmpty(low) && !TextUtils.isEmpty(heigh)) {
							temps[i] = low + "~" + heigh + "度";
						} else if (!TextUtils.isEmpty(low)) {
							temps[i] += low;
							temps[i] += "度";
						} else if (!TextUtils.isEmpty(heigh)) {
							temps[i] = heigh;
							temps[i] += "度";
						}

						if (i == 0) {
							tdWind = WeatherParseUtil.parseWindDirection(
									weatherObj.getString("fe"), Language.ZH_CN)
									+ WeatherParseUtil.parseWindForce(
											weatherObj.getString("fg"),
											Language.ZH_CN);
						}

					}

					if (arg0.getAirQualityInfo() != null) {
						Log.d(TAG, "airQuality:"
								+ arg0.getAirQualityInfo().toString());
						String airQua = arg0.getAirQualityInfo()
								.getString("k3");
						airQuality = WeatherParseUtil
								.getAQILevelInf(
										WeatherInfoActivity.this,
										airQua.substring(airQua
												.lastIndexOf("|") + 1))
								.getJSONObject(0).getString("level_in");
					}

					JSONArray indexArray = arg0.getIndexInfo(
							WeatherInfoActivity.this, new String[] { "ct",
									"pk", "ag", "yh", "bp" }, Language.ZH_CN);
					for (int j = 0; j < indexArray.length();) {
						tdIndex = indexArray.getJSONObject(j).getString("i2")
								+ "："
								+ indexArray.getJSONObject(j).getString("i5");
						Log.d(TAG, indexArray.getJSONObject(j).toString());
						break;
					}

					for (int i = 0; i < 4; i++) {
						imgIds[i] = getImgId(weathers[i]);
					}
					myHandler.obtainMessage().sendToTarget();
				} else {
					Toast.makeText(WeatherInfoActivity.this, "网络异常",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(WeatherInfoActivity.this, "数据异常",
						Toast.LENGTH_LONG).show();
				Log.d(TAG, "error:" + e.getMessage());
			}

		}
	};

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
			imgList.add(new IdName(R.drawable.rain_bbbao, "特大暴雨"));
			imgList.add(new IdName(R.drawable.rain_dong, "冻雨"));
			imgList.add(new IdName(R.drawable.rain_thunder, "雷阵雨"));
			imgList.add(new IdName(R.drawable.rain_thunder_snow, "雷阵雨夹雪"));
			imgList.add(new IdName(R.drawable.rain_snow, "雨夹雪"));
			imgList.add(new IdName(R.drawable.cloudy, "阴"));
			imgList.add(new IdName(R.drawable.cloudy, "多云"));
			imgList.add(new IdName(R.drawable.dust, "浮尘"));
			imgList.add(new IdName(R.drawable.sand, "扬沙"));
			imgList.add(new IdName(R.drawable.sandstorm, "沙尘暴"));
			imgList.add(new IdName(R.drawable.big_sandstorm, "强沙尘暴"));
			imgList.add(new IdName(R.drawable.fog, "雾"));
			imgList.add(new IdName(R.drawable.fog, "霾"));
			imgList.add(new IdName(R.drawable.cloudy, "阴转多云"));
			imgList.add(new IdName(R.drawable.stc, "晴转多云"));
			imgList.add(new IdName(R.drawable.snow_stm, "小到中雪"));
			imgList.add(new IdName(R.drawable.snow_mtb, "中到大雪"));
			imgList.add(new IdName(R.drawable.snow_btbao, "大到暴雪"));
			imgList.add(new IdName(R.drawable.rain_stm, "小到中雨"));
			imgList.add(new IdName(R.drawable.rain_mtb, "中到大雨"));
			imgList.add(new IdName(R.drawable.rain_btbao, "大到暴雨"));
			imgList.add(new IdName(R.drawable.rain_baotbbao, "暴雨转大暴雨"));
			imgList.add(new IdName(R.drawable.rain_bbaotbbbao, "大暴雨转特大暴雨"));

		}
		String matched = "";
		if (title != null && !title.equals("")) {
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
			Log.d(TAG,
					"title+" + title + ";matched=" + matched + ";match length="
							+ match + ";title length=" + title.length());
		}
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
		// tdDate = null;
		suggest = null;
		city = null;
		weathers = null;
		temps = null;
		tdWind = null;
		tdIndex = null;
		airQuality = null;
		// secDate = null;
		// thdDate = null;
		// fthDate = null;
		imgIds = new int[] { -1, -1, -1, -1 };
		tdImg.setImageBitmap(null);
		secImg.setImageBitmap(null);
		thdImg.setImageBitmap(null);
		fthImg.setImageBitmap(null);
		// tdDateTV.setText("");
		suggestTV.setText("");
		cityTV.setText("");
		tdWeatherTV.setText("");
		tdTempTV.setText("");
		tdWindTV.setText("");
		tdIndexTV.setText("");
		tdAirTV.setText("");
		// secDateTV.setText("");
		secWeatherTV.setText("");
		secTempTV.setText("");
		// thdDateTV.setText("");
		thdWeatherTV.setText("");
		thdTempTV.setText("");
		// fthDateTV.setText("");
		fthWeatherTV.setText("");
		fthTempTV.setText("");
	}

	private void initViews() {
		suggestTV = (TextView) findViewById(R.id.suggest_tv);
		cityTV = (TextView) findViewById(R.id.city_tv);
		tdIndexTV = (TextView) findViewById(R.id.today_index);
		tdAirTV = (TextView) findViewById(R.id.today_uv);
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
			InputStream in = am.open("areaid_list_.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, Charset.forName("GB2312")));
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

	private void initCityList() {
		provincesList = new ArrayList<WeatherInfoActivity.NameValue>();
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
					List<NameValue> citys = new ArrayList<WeatherInfoActivity.NameValue>();
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
			resetData();
			Log.d(TAG, "locatedid" + cityId);
			if (locatingDialog == null) {
				locatingDialog = new ProgressDialog(WeatherInfoActivity.this);
				locatingDialog.setCancelable(true);
			}
			locatingDialog.setMessage("正在获取天气...");
			locatingDialog.show();
			task.execute(cityId, Language.ZH_CN);

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

			try {
				String cityId = getCityId(location.getProvince(),
						location.getCity());
				if (cityId != null && !cityId.equals("")) {
					task.execute(cityId, Language.ZH_CN);
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
