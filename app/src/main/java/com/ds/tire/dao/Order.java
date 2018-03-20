package com.ds.tire.dao;

import android.provider.ContactsContract.Data;

public class Order {

	private String time;
	private int status;
	private String his_id;
	
	public Order(String his_id, int status, String time) {
		super();
		this.time = time;
		this.status = status;
		this.his_id = his_id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getHis_id() {
		return his_id;
	}
	public void setHis_id(String his_id) {
		this.his_id = his_id;
	}
	
	
	
	

	
	
}
