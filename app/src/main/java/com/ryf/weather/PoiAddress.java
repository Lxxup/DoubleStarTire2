package com.ryf.weather;

import java.io.Serializable;
import com.ds.tire.util.MapUtils;


import com.baidu.platform.comapi.basestruct.GeoPoint;
//import com.ouc.sei.lorry.MyApplication;
//import com.ouc.sei.lorry.util.MapUtils;

public class PoiAddress implements Serializable {
	private static final long serialVersionUID = 1L;
	private String city = null;
	private String address = null; // 地址
	private String name = null; // 地址
	private String district = null; // 地名
	private double latitude = 0; // 维度
	private double longitude = 0; // 经度

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GeoPoint getPoint() {
		if (latitude > 0 && longitude > 0)
			return MapUtils.newGeoPoint(latitude, longitude);
		return null;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String name) {
		this.district = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setPoint(GeoPoint p) {
		this.latitude = ((double) p.getLatitudeE6()) / 1e6;
		this.longitude = ((double) p.getLongitudeE6()) / 1e6;
	}

	public boolean canUseAddress() {
		return latitude > 0 && longitude > 0;
	}

	/**
	 * 格式化为字符�?
	 * 
	 * @return
	 */
//	public String format() {
//		String addr = "";
//
//		if (address != null) {
//			addr += address;
//		}
//
//		if (district != null) {
//
//			addr += district;
//
//		}
//
//		try {
//			MyAddress myAddress = MyApplication.getMyAddress();
//
//			if (addr.startsWith(myAddress.getProvince())) {
//				addr = addr.substring(myAddress.getProvince().length());
//			}
//
//			if (addr.startsWith(myAddress.getCity())) {
//				addr = addr.substring(myAddress.getCity().length());
//			}
//
//			int start = addr.indexOf(myAddress.getDistrict());
//			addr = addr.substring(start);
//		} catch (Exception e) {
//		}
//
//		return addr;
//
//	}
}
