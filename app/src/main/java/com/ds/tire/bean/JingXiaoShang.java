
package com.ds.tire.bean;

public class JingXiaoShang {

	
	private String name;
//	private String province;
//	private String fcity;
//	private String scity;
	private String address;
//	private String id;
	public JingXiaoShang(String name, String address) {
		this.name =name;
		this.address = address;
		//this.id =id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
//	public String getId() {
//		return id;
//	}
//	public void setId(String id) {
//		this.id = id;
//	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
