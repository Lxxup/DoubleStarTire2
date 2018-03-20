package com.ds.tire.bean;
//��̥ϵ�е���
import java.io.Serializable;
public class ProductSeries implements Serializable{

	private String id=null;
	private String xname=null;
	private String image=null;

	public void setId(String id)
	{
		this.id=id;
	}
	public void setXname(String xname)
	{
		this.xname=xname;
	}
	public String getId()
	{
		return id;
	}
	public String getXname()
	{
		return xname;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
}
