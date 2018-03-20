package com.ds.tire.addoil;

/**
 * 加油信息界面
 * 
 * @author 李小敏
 * 
 */
public class AddOil
{
    private String station;
    private String mileage;
    private String ammount;
    private String addTime;
    private int    oilType;
    private int    isFull;
    
    public AddOil(String station, String mileage, String ammount, String addTime, int oilType, int isFull)
    {
        super();
        this.station = station;
        this.mileage = mileage;
        this.ammount = ammount;
        this.addTime = addTime;
        this.oilType = oilType;
        this.isFull = isFull;
    }
    
    public AddOil(String station, String mileage, String ammount, String addTime, int oilType)
    {
        super();
        this.station = station;
        this.mileage = mileage;
        this.ammount = ammount;
        this.addTime = addTime;
        this.oilType = oilType;
    }
    
    public String getStation()
    {
        return station;
    }
    
    public void setStation(String station)
    {
        this.station = station;
    }
    
    public String getMileage()
    {
        return mileage;
    }
    
    public void setMileage(String mileage)
    {
        this.mileage = mileage;
    }
    
    public String getAmmount()
    {
        return ammount;
    }
    
    public void setAmmount(String ammount)
    {
        this.ammount = ammount;
    }
    
    public String getAddTime()
    {
        return addTime;
    }
    
    public void setAddTime(String addTime)
    {
        this.addTime = addTime;
    }
    
    public int getOilType()
    {
        return oilType;
    }
    
    public void setOilType(int oilType)
    {
        this.oilType = oilType;
    }
    
    public int getIsFull()
    {
        return isFull;
    }
    
    public void setIsFull(int isFull)
    {
        this.isFull = isFull;
    }
    
}
