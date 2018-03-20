package com.ds.tire.util;

import java.util.List;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MapUtils
{
    private static final double EARTH_RADIUS = 6378137;
    private static final long[] zoomLevels   = { 0, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 25000, 50000, 100000, 200000, 500000, 1000000, 2000000 };
    
    private MapUtils()
    {
    }
    
    /**
     * 通过经纬度，创建GeoPoint
     * 
     * @param latitude
     * @param longitude
     * @return
     */
    public static GeoPoint newGeoPoint(double latitude, double longitude)
    {
        return new GeoPoint((int) (latitude * 1e6), (int) (longitude * 1e6));
    }
    
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    
    /**
     * 根据两点间经纬度坐标，计算两点间距离，单位为米
     * 
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static long getDistance(double lat1, double lon1, double lat2, double lon2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return Math.round(s * 10000) / 10000;
    }
    
    public static int getZoomLevel(long distance)
    {
        boolean flag = false;
        int i = 0;
        for (i = 1; i < zoomLevels.length & !flag; i++)
        {
            if (zoomLevels[i - 1] <= distance && distance < zoomLevels[i])
            {
                flag = true;
            }
        }
        
        return 21 - i;
    }
    
    // public static PointF ge(){
    // PointF p = new PointF();
    // }
    
    public static AutoZoomAndLocation.Out autoZoomAndLocation(AutoZoomAndLocation.In in)
    {
        AutoZoomAndLocation.Out out = new AutoZoomAndLocation.Out();
        
        if (in.points.size() == 0)
        {
            out.center.setLatitudeE6(in.myLocation.getLatitudeE6());
            out.center.setLongitudeE6(in.myLocation.getLongitudeE6());
            out.zoomLevel = 0;
            out.latitudeSpanE6 = 0;
            out.longitudeSpanE6 = 0;
        }
        else
        {
            List<GeoPoint> points = in.points;
            int lat = 0;
            int lon = 0;
            int minLat = points.get(0).getLatitudeE6();
            int maxLat = points.get(0).getLatitudeE6();
            int minLon = points.get(0).getLongitudeE6();
            int maxLon = points.get(0).getLongitudeE6();
            
            for (GeoPoint p : points)
            {
                lat = p.getLatitudeE6();
                lon = p.getLongitudeE6();
                if (lat <= minLat)
                {
                    minLat = lat;
                }
                else if (lat >= maxLat)
                {
                    maxLat = lat;
                }
                
                if (lon <= minLon)
                {
                    minLon = lon;
                }
                else if (lon >= maxLon)
                {
                    maxLon = lon;
                }
            }
            
            int latSpan = maxLat - minLat;
            int lonSpan = maxLon - minLon;
            out.center.setLatitudeE6((minLat + maxLat) / 2);
            out.center.setLongitudeE6((minLon + maxLon) / 2);
            
            int latLevel = 0;
            int lonLevel = 0;
            
            if (latSpan != 0 && in.latitudeSpanE6 != 0)
            {
                if (latSpan > 2 * in.latitudeSpanE6)
                {
                    latLevel = -latSpan / in.latitudeSpanE6;
                }
                else if (in.latitudeSpanE6 < 2 * latSpan)
                {
                    latLevel = in.latitudeSpanE6 / latSpan;
                }
            }
            
            if (lonSpan != 0 && in.longitudeSpanE6 != 0)
            {
                if (lonSpan >= 2 * in.longitudeSpanE6)
                {
                    lonLevel = -lonSpan / in.longitudeSpanE6;
                }
                else if (in.longitudeSpanE6 >= 2 * lonSpan)
                {
                    lonLevel = in.longitudeSpanE6 / latSpan;
                }
            }
            
            out.zoomLevel = Math.min(latLevel, lonLevel);
            
            out.latitudeSpanE6 = latSpan;
            out.longitudeSpanE6 = lonSpan;
        }
        
        return out;
    }
}
