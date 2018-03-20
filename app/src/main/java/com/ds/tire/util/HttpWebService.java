package com.ds.tire.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.R.string;
import android.graphics.Bitmap;
import android.util.Log;

public class HttpWebService implements WebService {
	private static final String TAG = "TAG";
	@Override
	public Bitmap getLogo(String url) {
		Bitmap bitmap = NetworkUtils.getBitmap(url);
		return bitmap;
	}
	@Override
	public String checkUpdateInfo(String version, String channel) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("checkUpdateInfo.r?type=1&version=");
		sb.append(version);
		sb.append("&channel=");
		sb.append(channel);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String login(String user, String pwd) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("login.action?phone=");
		sb.append(user);
		sb.append("&password=");
		sb.append(pwd);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String regCar(String userid, String number, String type,
			String license) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("insertTaxi.action?userid=");
		sb.append(userid);
		sb.append("&number=");
		sb.append(number);
		sb.append("&type=");
		sb.append(type);
		sb.append("&license=");
		sb.append(license);

		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String regUser(String user, String pwd) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("insertDriver.action?phone=");
		sb.append(user);
		sb.append("&password=");
		sb.append(pwd);
		sb.append("&type=");
		sb.append(1);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getWeather(String cityId) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://m.weather.com.cn/data/");
		sb.append(cityId);
		sb.append(".html");
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String publishTruck(String userId, String start, String end,
			String truckNum, int type, String weight, String length,
			String width, String height, String driverName, String phone) {
		try {
			start = URLEncoder.encode(start, "UTF-8");
			end = URLEncoder.encode(end, "UTF-8");
			truckNum = URLEncoder.encode(truckNum, "UTF-8");
			driverName = URLEncoder.encode(driverName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("czh_message.action?userId=");
		sb.append(userId);
		sb.append("&cfd=");
		sb.append(start);
		sb.append("&mdd=");
		sb.append(end);
		sb.append("&cph=");
		sb.append(truckNum);
		sb.append("&chexing=");
		sb.append(type);
		sb.append("&edzz=");
		sb.append(weight);
		sb.append("&kycd=");
		sb.append(length);
		sb.append("&kykd=");
		sb.append(width);
		sb.append("&kygd=");
		sb.append(height);
		sb.append("&driver_name=");
		sb.append(driverName);
		sb.append("&phone=");
		sb.append(phone);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String publishGood(String userId, String start, String end,
			int truckType, String goodType, String volume, String weight,
			String count, String pickTime, String sendTime, String sendName,
			String phone, String realName, String idNum) {
		try {
			start = URLEncoder.encode(start, "UTF-8");
			end = URLEncoder.encode(end, "UTF-8");
			goodType = URLEncoder.encode(goodType, "UTF-8");
			sendName = URLEncoder.encode(sendName, "UTF-8");
			realName = URLEncoder.encode(realName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("hzc_message.action?userId=");
		sb.append(userId);
		sb.append("&chufa=");
		sb.append(start);
		sb.append("&mudi=");
		sb.append(end);
		sb.append("&chexing=");
		sb.append(truckType);
		sb.append("&hwlx=");
		sb.append(goodType);
		sb.append("&ztj=");
		sb.append(volume);
		sb.append("&zzl=");
		sb.append(weight);
		sb.append("&zjs=");
		sb.append(count);
		sb.append("&thsj=");
		sb.append(pickTime);
		sb.append("&fhsj=");
		sb.append(sendTime);
		sb.append("&name=");
		sb.append(sendName);
		sb.append("&phone=");
		sb.append(phone);
		sb.append("&true_name=");
		sb.append(realName);
		sb.append("&pid=");
		sb.append(idNum);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getMyPublish(String userId, int kind) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("cx_fb.action?userId=");
		sb.append(userId);
		sb.append("&kind=");
		sb.append(kind);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}
	@Override
	public String ifnamed(String oid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("judgeCardNum.u?oid=");
		sb.append(oid);
		String path = sb.toString();
		Log.d(TAG, "path="  + path);
		
		return NetworkUtils.request(path);
	}
	@Override
	public String ifcardexist(String oid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("checkOrderNum.u?oid=");
		sb.append(oid);
		String path = sb.toString();
		Log.d(TAG, "path="  + path);
			
		return NetworkUtils.request(path);
	}@Override
	public String urlget(String cid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("DownPicture.w?");
		sb.append("cid=");
		sb.append(cid);
		String path = sb.toString();
		Log.d(TAG, "path="  + path);
		
		return NetworkUtils.request(path);
	}
	@Override
	public String getPublishTrucks(double lat, double lon) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("un_hzc.action?lat=");
		sb.append(lat);
		sb.append("&lon=");
		sb.append(lon);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getPublishGoods(double lat, double lon) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("un_czh.action?lat=");
		sb.append(lat);
		sb.append("&lon=");
		sb.append(lon);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getDetailGood(String goodId) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("detail_czh.action?id=");
		sb.append(goodId);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}
	
	
	@Override
	public String getDetailTruck(String truckId) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("detail_hzc.action?id=");
		sb.append(truckId);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String finishGood(String userId, String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("js_hzc.action?userId=");
		sb.append(userId);
		sb.append("&id=");
		sb.append(id);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String finishTruck(String userId, String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("js_czh.action?userId=");
		sb.append(userId);
		sb.append("&id=");
		sb.append(id);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getMallInfo(int id) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("cx_mall.action?sort=");
		sb.append(id);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);

	}

	@Override
	public String getMallDetail(String id) {

		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("detail_mall.action?id=");
		sb.append(id);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String contactMe(String userId, String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("commit_mall.action?uid=");
		sb.append(userId);
		sb.append("&id=");
		sb.append(id);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String uploadRoadConditon(String userId, double latitude,
			double longitude, int condition, String addr) {
		try {

			addr = URLEncoder.encode(addr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("lukuang.action?userId=");
		sb.append(userId);
		sb.append("&latitude=");
		sb.append(latitude);
		sb.append("&longitude=");
		sb.append(longitude);
		sb.append("&condition=");
		sb.append(condition);
		sb.append("&addr=");
		sb.append(addr);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getRoadCondition(double latitude, double longitude) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("cx_lk.action?latitude=");
		sb.append(latitude);
		sb.append("&longitude=");
		sb.append(longitude);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String registerTruck(String userId, String truckNum, int type,
			String weight, String length, String width, String height,
			String driverName, String phone) {
		try {
			truckNum = URLEncoder.encode(truckNum, "UTF-8");
			driverName = URLEncoder.encode(driverName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("register_car.action?userId=");
		sb.append(userId);
		sb.append("&cph=");
		sb.append(truckNum);
		sb.append("&chexing=");
		sb.append(type);
		sb.append("&edzz=");
		sb.append(weight);
		sb.append("&kycd=");
		sb.append(length);
		sb.append("&kykd=");
		sb.append(width);
		sb.append("&kygd=");
		sb.append(height);
		sb.append("&driver_name=");
		sb.append(driverName);
		sb.append("&phone=");
		sb.append(phone);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String leaveMsg(String id, String name, String phone, String ps) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
			ps = URLEncoder.encode(ps, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("tj_baoxian.action?id=");
		sb.append(id);
		sb.append("&name=");
		sb.append(name);
		sb.append("&phone=");
		sb.append(phone);
		sb.append("&ps=");
		sb.append(ps);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getPersonalInfo(String uid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("users.action?uid=");
		sb.append(uid);

		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String logout(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("loginout.action?uid=");
		sb.append(userId);

		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getTruckInfo(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("car_detail.action?uid=");
		sb.append(userId);

		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getMallCategory() {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("enter_mall.action");

		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getYunyingP() {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("partner_loc.action");

		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getYunying(String province) {
		try {
			province = URLEncoder.encode(province, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("partner.action?loc=");
		sb.append(province);

		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String vavifyCaptcha(String phone, String captcha) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("checkSMS.action?sms=");
		sb.append(captcha);
		sb.append("&mobile=");
		sb.append(phone);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getCaptcha(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("sms.action?mobile=");
		sb.append(phone);

		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getAd() {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("picture.action");
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String modifyPwd(String phone, String pwd) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("update_password.action?mobile=");
		sb.append(phone);
		sb.append("&password=");
		sb.append(pwd);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getPwdCaptcha(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("update_sms.action?mobile=");
		sb.append(phone);

		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String uploadGPS(String userId, String carId, double lat, double lon) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("upload_gps.action?uid=");
		sb.append(userId);
		sb.append("&carid=");
		sb.append(carId);
		sb.append("&lat=");
		sb.append(lat);
		sb.append("&lon=");
		sb.append(lon);
		String path = sb.toString();
		Log.d(TAG, "path=" + path);
		return NetworkUtils.request(path);
	}


	@Override
	public String login(String username, String password, String push_id) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("userLogin.u?");
		sb.append("account=" + username);
		sb.append("&password=");
		sb.append(password);
		sb.append("&push_id=");
		sb.append(push_id);

		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	public String checkUser(String account, String username, String password,
			String email) {
		try {
			username = URLEncoder.encode(username, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("register.u?");
		sb.append("account=" + account);
		sb.append("&password=" + password);
		sb.append("&username=" + username);
		sb.append("&email=");
		sb.append(email);
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getManufacture() {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("readmanufacturer.action");
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getCarBrand(String manufacturer) {
		try {
			manufacturer = URLEncoder.encode(manufacturer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("read_sub_brand.u?manufacturer=");
		sb.append(manufacturer);
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getCarYear(String manufacturer, String sub_brand) {
		try {
			manufacturer = URLEncoder.encode(manufacturer, "UTF-8");
			sub_brand = URLEncoder.encode(sub_brand, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("readcarYear.u?manufacturer=");
		sb.append(manufacturer);
		sb.append("&sub_brand=");
		sb.append(sub_brand);
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getCarType(String manufacturer, String sub_brand,
			String car_Year) {
		try {
			manufacturer = URLEncoder.encode(manufacturer, "UTF-8");
			sub_brand = URLEncoder.encode(sub_brand, "UTF-8");
			car_Year = URLEncoder.encode(car_Year, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("readcarType.u?manufacturer=");
		sb.append(manufacturer);
		sb.append("&sub_brand=");
		sb.append(sub_brand);
		sb.append("&car_Year=");
		sb.append(car_Year);
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	public String downloadFeedHisto(String id) {
		try {
			id = URLEncoder.encode(id, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("downloadFeedHisto.u?");
		sb.append("id=");
		sb.append(id);
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	public String ask(String id, String mail, String title, String content) {
		try {
			title = URLEncoder.encode(title, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("uploadFeedBack.u?");
		sb.append("id=" + id);
		sb.append("&mail=" + mail);
		sb.append("&title=" + title);
		sb.append("&content=");
		sb.append(content);
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String addoil(String user_Id, int oil_species, String amount,
			String station_name, int if_fill, String mileage, String datetime) {
		if (station_name != null && !station_name.trim().equals("")) {
			try {
				station_name = URLEncoder.encode(station_name, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("uploadAddOil.u?uid=");
		sb.append(user_Id);
		sb.append("&oil_species=");
		sb.append(oil_species + 1);
		sb.append("&amount=");
		sb.append(amount);
		sb.append("&station_name=");
		sb.append(station_name);
		sb.append("&is_fill=");
		sb.append(if_fill);
		sb.append("&mileage=");
		sb.append(mileage);
		sb.append("&datetime=");
		sb.append(datetime);

		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getOil(String user_Id) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("downOilInfor.u?uid=");
		sb.append(user_Id);
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String poll(String user_Id, String location, String temp,
			String press, String latitude, String longitude) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("uploadTireInfor.u?uid=");
		sb.append(user_Id);
		sb.append("&weizhi=");
		sb.append(location);
		sb.append("&temp=");
		sb.append(temp);
		sb.append("&press=");
		sb.append(press);
		sb.append("&latitude=");
		sb.append(latitude);
		sb.append("&longitude=");
		sb.append(longitude);

		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String aid(String user_Id, double latitude, double longitude,
			String location) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("rescueRequst.u?uid=");
		sb.append(user_Id);
		sb.append("&latitude=");
		sb.append(latitude);
		sb.append("&longitude=");
		sb.append(longitude);
		sb.append("&weizhi=");
		sb.append(location);
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String displayAll() {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("downFreeRescueInfo.action");

		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String tireNo(String user_Id, String tid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("registTirenum.u?uid=");
		sb.append(user_Id);
		sb.append("&tid=");
		sb.append(tid);

		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String registerUser(String account, String username,
			String password,String platenum, String chexing, String tiresize, 
			 String wheelNum) {
		try {
			username = URLEncoder.encode(username, "UTF-8");
			platenum = URLEncoder.encode(platenum, "UTF-8");
			chexing = URLEncoder.encode(chexing, "UTF-8");
			tiresize = URLEncoder.encode(tiresize, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("register.u?");
		sb.append("account=");
		sb.append(account);
		sb.append("&username=");
		sb.append(username);
		sb.append("&password=");
		sb.append(password);
		sb.append("&platenum=");
		sb.append(platenum);
		sb.append("&chexing=");
		sb.append(chexing);
		sb.append("&tiresize=");
		sb.append(tiresize);		
		sb.append("&wheelNum=");
		sb.append(wheelNum);
		String path = sb.toString();
		Log.d("TAG", "aaaaaaaa" + path);
		Log.v("BBB", path);
		Log.d("TAG", "bbbbbbbb" + path);
		return NetworkUtils.request(path);
	}
	@Override
	public String UpdateUserInfo(String oid, String platenum, String mileage) {
		try {
			platenum = URLEncoder.encode(platenum, "UTF-8");
			mileage = URLEncoder.encode(mileage, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("updateCardInfo.u?");
		sb.append("oid=");
		sb.append(oid);
		sb.append("&plateNum=");
		sb.append(platenum);
		sb.append("&mileage=");
		sb.append(mileage);		
		String path = sb.toString();
		Log.d("TAG", "上传" + path);
		Log.v("BBB", path);
		Log.d("TAG", "bbbbbbbb" + path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getWheelsInfo(String uid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("getWheelInfo.u?");
		sb.append("uid=");
		sb.append(uid);

		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String checkComment(String orderNum) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("checkRecCom.u?");
		sb.append("his_pid=");
		sb.append(orderNum);
		String path = sb.toString();
		Log.v("BBB", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getOrderInfo(String uid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("getOrderInfo.u?uid=");
		sb.append(uid);
		String path = sb.toString();
		Log.v("UUU", path);
		return NetworkUtils.request(path);
	}
	
	@Override
	public String cardSelect(String uid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("getCardNum.u?uid=");
		sb.append(uid);
		String path = sb.toString();
		Log.v("Ucard:", path);
		return NetworkUtils.request(path);
	}
	@Override
	public String getEndOrderInfo(String his_id) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("getEndOrderInfo.u?his_id=");
		sb.append(his_id);
		String path = sb.toString();
		Log.v("HHH", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String getOnOrderInfo(String his_id) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("getOnOrderInfo.u?his_id=");
		sb.append(his_id);
		String path = sb.toString();
		Log.v("HHH", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String feedbackRescue(String pid, int role, int feed1, int feed2,
			int feed3) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("feedBackRescue.u?");
		sb.append("pid=");
		sb.append(pid);
		sb.append("&role=");
		sb.append(role);
		sb.append("&feed1=");
		sb.append(feed1);
		sb.append("&feed2=");
		sb.append(feed2);
		sb.append("&feed3=");
		sb.append(feed3);
		String path = sb.toString();
		Log.v("FFF", path);
		return NetworkUtils.request(path);
	}

	@Override
	public String selecUserInfo(String uid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("selecUserInfo.u?");
		sb.append("uid=");
		sb.append(uid);
		String path = sb.toString();
		Log.v("TAG", path);
		return NetworkUtils.request(path);
	}
	public String selectUserInfo(String cid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("getCardInfo.u?");
		sb.append("cid=");
		sb.append(cid);
		String path = sb.toString();
		Log.v("TAG", path);
		return NetworkUtils.request(path);
	}
	public String getCardInfo(String oid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("downCardInfo.u?");
		sb.append("oid=");
		sb.append(oid);
		String path = sb.toString();
		Log.v("TAG", path);
		return NetworkUtils.request(path);
	}
	@Override
	public String downloadBitmap(String uid) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("SelectUserInfo.u?");
		sb.append("uid=");
		sb.append(uid);
		String path = sb.toString();
		Log.v("TAG", path);
		return NetworkUtils.request(path);
	}
	@Override
	public String getDealersDetailed(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("getDealersDetailed.w?id=");
		sb.append(id);
		String path = sb.toString();
		Log.v("TAG", path);
		return NetworkUtils.request(path);
	}


@Override//现在使用的网点查询
	public String getDealersInfo(String province,double longitude, double latitude,String category) 
	
	{try {
		province = URLEncoder.encode(province, "UTF-8");
		category = URLEncoder.encode(category, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
		
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("getDealersInfo.w?province=");
		sb.append(province);
		
		sb.append("&longitude=");
		sb.append(longitude);
		sb.append("&latitude=");
		sb.append(latitude);
		sb.append("&category=");
		sb.append(category);
		
		
		String path = sb.toString();
		Log.v("TAG", path);
		return NetworkUtils.request(path);
	}
	private String uploadImageFile(String url, File file) {
		String result = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpPost httpPost = new HttpPost(url);// index表示Url路径
			Log.v("TAG", "11111111上传图片路径："+url+"图片："+file);
			
			InputStreamEntity entity = new InputStreamEntity(new FileInputStream(file)
					, file.length());
			httpPost.setEntity(entity);
			Log.v("TAG", "222222上传图片路径："+url+"图片："+file);

			Log.d("HttpPost", "HttpPost=" + file.length());

			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity httpEntity = response.getEntity();

			Log.d("HttpPost", "HttpPost2=" + response.getStatusLine());

			if (httpEntity != null) {
				Log.d(TAG, "httpEntity=" + httpEntity);
				result = EntityUtils.toString(httpEntity, "UTF-8").substring(0,
						1);

				// String result1 = EntityUtils.toString(response.getEntity(),
				// "UTF-8");
				Log.d(TAG, "result=" + result);// 返回的数值
				Log.d(TAG, "response.getEntity()=" + response.getEntity());
			}

			if (httpEntity != null) {
				httpEntity.consumeContent();
			}
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return result;
	}

	@Override
	public String uploadSignImg(String uid, String oid, String imgUrl) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.WEB_SITE);
		sb.append("RegisterPicture.w?uid=");
		sb.append(uid);
		sb.append("&oid=");
		sb.append(oid);
		String path = sb.toString();
		Log.v("TAG", "上传图片路径："+path+"图片："+imgUrl);
		File imgFile = new File(imgUrl);
		return uploadImageFile(path, imgFile);
	}
	//产品介绍——类别
		public String selectProduct()
		{
			StringBuffer sb = new StringBuffer();
			sb.append(Constant.WEB_SITE);
			sb.append("selectType.u");
			
			String path = sb.toString();
			Log.v("BBB", path);
			return NetworkUtils.request(path);
		}
		//产品类别-系列
		public String selectProductSeries(String id)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(Constant.WEB_SITE);
			sb.append("selectSeries.u?");
			sb.append("typeid=");
			sb.append(id);
			
			String path = sb.toString();
			Log.v("BBB", path);
			return NetworkUtils.request(path);
		}
		//产品详细信息简介
		public String selectProductDetail(String id)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(Constant.WEB_SITE);
			sb.append("selectDetail.u?");
			sb.append("seriesid=");
			sb.append(id);
			
			String path = sb.toString();
			Log.v("BBB", path);
			return NetworkUtils.request(path);
		}
		//产品具体详细信息
//		public String selectPDetail(String id)
//		{
//			StringBuffer sb = new StringBuffer();
//			sb.append(Constant.WEB_SITE);
//			sb.append("selectDetail.u?");
//			sb.append("tireid=");
//			sb.append(id);
//			
//			String path = sb.toString();
//			Log.v("BBB", path);
//			return NetworkUtils.request(path);
//		}
		//获取产品信息的图片
		public Bitmap getBitmap(String url)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(url);
			Log.d("TAG","url:"+ url);
			return NetworkUtils.getBitmap(url);
		}

}
