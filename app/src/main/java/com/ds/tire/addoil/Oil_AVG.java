package com.ds.tire.addoil;

/**
 * 平均油耗
 * 
 * @author 李小敏
 * 
 */
public class Oil_AVG
{
    private String end_time;
    private String start_time;
    private double oil_avg;
    
    public String getEnd_time()
    {
        return end_time;
    }
    
    public String getStart_time()
    {
        return start_time;
    }
    
    public double getOil_avg()
    {
        return oil_avg;
    }
    
    public void setEnd_time(String end_time)
    {
        this.end_time = end_time;
    }
    
    public void setStart_time(String start_time)
    {
        this.start_time = start_time;
    }
    
    public void setOil_avg(double oil_avg)
    {
        this.oil_avg = oil_avg;
    }
    
}
