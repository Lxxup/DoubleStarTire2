package com.ryf.weather;

import java.util.ArrayList;
import java.util.List;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class AutoZoomAndLocation
{
    public static class In
    {
        public final List<GeoPoint> points = new ArrayList<GeoPoint>();
        public final GeoPoint       center = new GeoPoint(0, 0);
    }
    
    public static class Out
    {
        public final GeoPoint center          = new GeoPoint(0, 0);
        public int            latitudeSpanE6  = 0;
        public int            longitudeSpanE6 = 0;
    }
}
