package com.ryf.weather;

public class Ad {
	int imageId, imageWidth, imageHeight;
	int category;
	String url;
	
	public Ad(int imageId, int imageWidth, int imageHeight, int category) {
		super();
		this.imageId = imageId;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.category = category;
	}
	
	public Ad(String url,int imageWidth, int imageHeight, int category) {
		super();
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.category = category;
		this.url = url;
	}

	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public int getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	public int getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	
}
