package com.ryf.weather;

public class Constant {
	// 旧服务器
	// public final static String WEB_SITE="http://118.145.0.82:80/Compass/";
	// 新服务器
//	public final static String WEB_SITE="http://115.28.242.49/Compass/";
	// 测试服务�?
	public final static String WEB_SITE = "http://211.64.154.160:8080/Compass/";
	public final static String WEB_SITE_IMAGE = WEB_SITE + "images/";

	// public static String USER_ID = null;  
	public final static String ACTION_UPDATE = "action_update";
	public final static String KEY_JSON = "key_json";
	public final static String KEY_NEW_VERSION = "key_new_version";
	public final static String KEY_USER_ID = "key_user_id";
	public final static String KEY_CAR_ID = "key_car_id";
	public final static String KEY_CHANNEL = "key_channel";
	public final static String KEY_PHONE = "key_user";
	public final static String KEY_PWD = "key_pwd";
	public final static String KEY_CAPTCHA = "key_captcha";
	public final static String KEY_START_POIADDR = "key_start_poiaddr";
	public final static String KEY_END_POIADDR = "key_end_poiaddr";
	public final static String KEY_MSG = "key_message";
	public final static String KEY_REG_TYPE = "key_reg_type";
	public final static String KEY_ACTIVITY_NAME = "key_activity_name";
	public final static String KEY_CAR_NUM = "key_car_num";
	public final static String KEY_NICK_NAME = "key_nick_name";
	public final static String KEY_REAL_NAME= "key_real_name";
	public final static String KEY_YUNYING = "key_yunying";
	public final static String KEY_IMG_PATH = "key_img_path";
	public final static String KEY_FIRST_USE = "key_first_use";
	public final static String KEY_FLAG_REG = "key_flag_reg";
	public final static String KEY_FLAG_PWD = "key_flag_pwd";
	public final static String KEY_LOGIN_REFRESH = "key_login_refresh";
	//public final static String KEY_LOGOUT = "key_logout";
	

	// public final static String KEY_PUSH_ID="key_push_id";
	public final static String KEY_ORDER_ID = "key_order_id";
	public final static String KEY_MALL_CATEGORY = "key_mall_category";
	public final static String KEY_CITY_ID = "key_city_id";

	public final static String KEY_IS_REFRESH = "key_is_refresh";

	public final static String KEY_IS_MY_ORDER = "key_is_my_order";

	public static final String DBNAME = "compass_city.db";
	public static final int DBVERSION = 1;

	public final static int REQ_NVI_INPUT = 0X01;
	public final static int REQ_REFRESH = 0x02;
	public final static int REQ_NICK_INPUT = 0x03;
	public final static int REQ_TAKEPHOTO = 0x04;
	public final static int REQ_GALLERY = 0x05;
	public final static int REQ_LOGIN = 0x06;
	public final static int REQ_LOGOUT = 0x07;

}
