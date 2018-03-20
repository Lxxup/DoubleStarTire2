package com.ds.tire.bean;

public class FuwuxieyiList {

	
	private String card;
	private String name;
	private String buytime;
	
	public FuwuxieyiList(String card, String name, String buytime) {
		this.card =card;
		this.name = name;
		this.buytime =buytime;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBuyTime() {
		return buytime;
	}
	public void setBuyTime(String buytime) {
		this.buytime = buytime;
	}
	
}