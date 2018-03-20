package com.ds.tire.bean;

public class RescueCar
{
    private String rescueId  = null;
    private double longitude = 0;
    private double latitude  = 0;
    private int    status;
    private String number    = null;
    private String phone     = null;
    private String name      = null;
    

	public String getRescueId()
    {
        return rescueId;
    }
    
    public void setRescueId(String rescueId)
    {
        this.rescueId = rescueId;
    }
    
    public String getNumber()
    {
        return number;
    }
    
    public void setNumber(String number)
    {
        this.number = number;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public double getLongitude()
    {
        return longitude;
    }
    
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
    
    public double getLatitude()
    {
        return latitude;
    }
    
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
}
