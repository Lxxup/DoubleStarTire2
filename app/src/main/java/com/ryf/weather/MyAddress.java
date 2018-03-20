package com.ryf.weather;

import java.io.Serializable;
import com.ds.tire.util.MapUtils;
//import com.ouc.sei.lorry.util.MapUtils;


public class MyAddress implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String            province         = null;            // �?
    private String            city             = null;            // �?
    private String            district         = null;            // 县区
    private String            street           = null;            // 街道
    private String            streetNumber     = null;            // 门牌�?
    private double            latitude         = 0;               // 维度
    private double            longitude        = 0;               // 经度
                                                                   
    private final PoiAddress  reference        = new PoiAddress(); // 参�?�物
                                                                   
    public String getProvince()
    {
        return province;
    }
    
    public void setProvince(String province)
    {
        this.province = province;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public String getDistrict()
    {
        return district;
    }
    
    public void setDistrict(String district)
    {
        this.district = district;
    }
    
    public String getStreet()
    {
        return street;
    }
    
    public void setStreet(String street)
    {
        this.street = street;
    }
    
    public String getStreetNumber()
    {
        return streetNumber;
    }
    
    public void setStreetNumber(String streetNumber)
    {
        this.streetNumber = streetNumber;
    }
    
    public double getLatitude()
    {
        return latitude;
    }
    
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
    
    public double getLongitude()
    {
        return longitude;
    }
    
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
    
    public PoiAddress getReference()
    {
        return reference;
    }
    
    public String getName()
    {
        String name = reference.getDistrict();
        double x = reference.getLongitude();
        double y = reference.getLatitude();
        
        if (name != null && !"".equals(name) && x != 0 && y != 0)
        {
            long d = MapUtils.getDistance(latitude, longitude, y, x);
            return String.format("距离%s%d�?", name, d);
        }
        else
        {
            return "";
        }
    }
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        // �?
        if (province != null)
        {
            sb.append(province);
        }
        
        // �?
        if (city != null)
        {
            sb.append(city);
        }
        
        // 县区
        if (district != null)
        {
            sb.append(district);
        }
        
        // 街道
        if (street != null)
        {
            sb.append(street);
        }
        
        // 门牌�?
        if (streetNumber != null)
        {
            sb.append(streetNumber);
        }
        
        return sb.toString();
    }
    
}
