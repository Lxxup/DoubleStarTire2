package com.ds.tire;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationHelper {
	
	private static Location location;//λ�ö���
	private static LocationManager locationManager;//λ�ù�����
	private static String provider;//λ���ṩ�� 
    private final static LocationListener locationListener;// ��Ϣ������ 
    
    static{
    		locationListener = new LocationListener() {  
            // λ�÷����ı�����  
            public void onLocationChanged(Location location) {  
                updateWithNewLocation(location);  
            }  
            // provider ���û��رպ����  
            public void onProviderDisabled(String provider) {  
                updateWithNewLocation(null);  
            }  
            // provider ���û����������  
            public void onProviderEnabled(String provider) {          
                  
            }  
            // provider ״̬�仯ʱ���� 
        	@Override
            public void onStatusChanged(String provider, int status,Bundle extras) {  
            }
        };
    }
	
	
	public static String GetLocation(Context context){
	    String contextString = Context.LOCATION_SERVICE;
	    locationManager = (LocationManager) context.getSystemService(contextString);
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    //criteria.setCostAllowed(true);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
	    //ȡ��Ч����õ�criteria 
	    provider = locationManager.getBestProvider(criteria, true); 
	    if (provider == null){
	    	Log.i("Test","provider:"+provider);
	        return null;
	    }
	    locationManager.requestLocationUpdates(provider, 2000, (float)0.1, locationListener);
	    //�õ�������ص���Ϣ 
	    Location location = locationManager.getLastKnownLocation(provider);
	    if (location == null) { 
	        return null; 
	    }
	    Log.i("Test","location:"+location);
	    return "{\"latitude\":"+location.getLatitude()+",\"longitude\":"+location.getLongitude()+"}";
	}
    //����λ����Ϣ
    private  static void updateWithNewLocation(Location loc) {  
       location = loc;
    }  

}
