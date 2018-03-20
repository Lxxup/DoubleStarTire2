package com.ds.tire.util;

import android.R.string;
import android.graphics.Bitmap;

public interface WebService
{
    public Bitmap getLogo(String url);
    
    public String checkUser(String account, String username, String password, String email);
    
    public String registerUser(String account, String username,
			String password, String platenum,  String chexing,
			String tiresize, 
			String wheelNum);
    public String UpdateUserInfo(String oid, String platenum, String mileage);
    public String ifnamed(String oid);
    
    public String getManufacture();
    
    public String getCarBrand(String manufacturer);
    
    public String getCarYear(String manufacturer, String sub_brand);
    
    public String getCarType(String manufacturer, String sub_brand, String car_Year);
    
    public String downloadFeedHisto(String id);
    
    public String ask(String id, String mail, String title, String content);
    
    public String addoil(String user_Id, int oil_species, String amount, String station_name, int if_fill, String mileage, String datetime);
    
    public String getOil(String user_Id);
    
    public String poll(String user_Id, String location, String temp, String press, String longtitude, String lantitude);
    
    public String displayAll();
    
    public String login(String username, String password, String push_id);
    
    public String aid(String user_Id, double latitude, double longitude, String location);
    
    public String tireNo(String user_Id, String tid);
    
    public String getWheelsInfo(String uid);

	public String checkComment(String orderNum);

	public String getOrderInfo(String uid);

	public String getEndOrderInfo(String his_id);

	public String getOnOrderInfo(String his_id);

	public String feedbackRescue(String his_id, int role, int feed1, int feed2,
			int feed3);

	public String selecUserInfo(String uid);
	public String downloadBitmap(String uid);
	public String selectUserInfo(String uid);
	public String getCardInfo(String oid);
	
	
	//天气预报
	public String checkUpdateInfo(String version,String channel);

	public String login(String user, String pwd);

	public String regUser(String user, String pwd);

	public String regCar(String userid, String number, String type,
			String license);

	public String getWeather(String cityId);

	public String publishTruck(String userId, String start, String end,
			String truckNum, int type, String weight, String length,
			String width, String height, String driverName, String phone);
	public String publishGood(String userId, String start, String end,
			int truckType, String goodType,String volume, String weight, String count,
			String pickTime, String sendTime, String sendName, String phone,String realName,String idNum);
	public String getMyPublish(String userId,int kind);
	public String getPublishTrucks(double lat,double lon);
	public String getPublishGoods(double lat,double lon);
	public String getDetailGood(String goodId);
	public String getDetailTruck(String truckId);
	public String finishGood(String userId,String id);
	public String finishTruck(String userId,String id);
	public String getMallInfo(int id);
	public String getMallDetail(String id);
	public String contactMe(String userId,String id);
	public String uploadRoadConditon(String userId,double latitude,double longitude,int condition,String addr);
	public String getRoadCondition(double latitude,double longitude);
	public String registerTruck(String userId,String truckNum, int type, String weight, String length,
			String width, String height, String driverName, String phone);
	public String leaveMsg(String id,String  name,String  phone,String ps);
	public String getPersonalInfo(String uid);
	public String logout(String userId);
	public String getTruckInfo(String userId);
	public String getMallCategory();
	public String getYunyingP();
	public String getYunying(String province);
	public String getCaptcha(String phone);
	public String getPwdCaptcha(String phone);
	public String vavifyCaptcha(String phone,String captcha);
	public String getAd();
	public String modifyPwd(String phone,String pwd);
	public String uploadGPS(String userId,String carId,double lat,double lon);
//产品介绍
	public String selectProduct();
	public String selectProductSeries(String id);
	public String selectProductDetail(String id);
	public String urlget(String cid);
	public Bitmap getBitmap(String url);

	public String uploadSignImg(String uid,String oid, String imgUrl);
	
	public String getDealersDetailed(String id);

	public String getDealersInfo(String province,double longitude, double latitude,String category);

	public String cardSelect(String uid);

	public String ifcardexist(String oid);

	

	
}