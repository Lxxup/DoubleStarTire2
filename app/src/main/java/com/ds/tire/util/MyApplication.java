package com.ds.tire.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class MyApplication extends Application
{

    public static final String               TAG                    = MyApplication.class.getName();
    
    // 作为键的常量
    public static final String               KEY_PUSH_ID            = "push_id";
    
    // 地图相关
    public static final String               KEY                    = "gAfSIWgOuWSHIzaF7jCo2rWOL8IUnAyd";
    public static final String               COORDINATE_TYPE        = "bd09ll";                                                      // 坐标系类型(gcj02,gps,bd09,bd09ll)
    public static final int                  SCAN_SPAN              = 30000;                                                         // 定时定位的时间间隔，单位ms
                                                                                                                                      
    // 定位参数选项相关
    public static final LocationClientOption OPTION_ALL             = new LocationClientOption();
    public static final LocationClientOption OPTION_DEFAULT         = new LocationClientOption();
    
    // 应用属性
    private static MyApplication             instance               = null;
    private boolean                          keyRight               = true;
    private boolean                          onFirstLocateMyAddress = true;
    private boolean                          isUpdatingMyAddress    = false;
    private BMapManager                      mapManager             = null;
    private LocationClient                   locationClient         = null;
    private final MyLocationListener         locationListener       = new MyLocationListener();
    private final Set<BDLocationListener>    listeners              = Collections.synchronizedSet(new HashSet<BDLocationListener>());

    //已安装数量
    public static int INSTALLED_COUNT ;
    public static List<String> idList = new ArrayList<String>();
   
    /**
     * 获取应用单例
     * 
     * @return
     */
    public static MyApplication getInstance()
    {
        return instance;
    }
    
    /**
     * 开启定位
     */
    public static void startLocation()
    {
        if (instance != null)
        {
            instance.locationClient.start();
            instance.updatingMyAddress();
        }
    }
    
    /**
     * 停止定位
     */
    public static void stopLocation()
    {
        if (instance != null)
        {
            instance.locationClient.stop();
        }
    }
    
    /**
     * 获取上一次定位的位置
     * 
     * @return
     */
    public static BDLocation getLastKnownLocation()
    {
        return instance != null ? instance.locationClient.getLastKnownLocation() : null;
    }
    
    public static BMapManager getBMapManager()
    {
        return instance != null ? instance.getMapManager() : null;
    }
    
    /**
     * 判断能否定位
     * 
     * @return
     */
    public static boolean canLocate()
    {
        return instance != null ? instance.locationClient.isStarted() : false;
    }
    
    /**
     * 判断我的位置是否可以使用
     * 
     * @return
     */
    public static boolean canUseMyAddress()
    {
        return instance != null ? !instance.onFirstLocateMyAddress : false;
    }
    
    /**
     * 更新我的地址
     */
    public static void updateMyAddress()
    {
        if (instance != null)
        {
            instance.updatingMyAddress();
        }
    }
    
    /**
     * 更新我的地址
     */
    private void updatingMyAddress()
    {
        isUpdatingMyAddress = true;
        locationClient.setLocOption(OPTION_ALL);
        locationClient.requestPoi();
    }
    
    /**
     * KEY是否合法
     * 
     * @return
     */
    public boolean isKeyRight()
    {
        return keyRight;
    }
    
    /**
     * 获取地图管理器
     * 
     * @return
     */
    public BMapManager getMapManager()
    {
        return mapManager;
    }
    
    /**
     * 获取定位客户端
     * 
     * @return
     */
    public LocationClient getLocationClient()
    {
        return locationClient;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        // 初始化地图管理器
        initMapManager(this);
        // 初始化参数选项
        initOptions();
        // 初始化定位客户端
        initLocationClient(this);
        
        // 生成推送ID
        generatePushId();
        
        INSTALLED_COUNT = SpUtils.getInteger(getInstance().getApplicationContext(),SpUtils.INSTALLED_COUNT, 0);
        for(int i = 0;i<INSTALLED_COUNT;i++){
        	String mID = SpUtils.getString(getApplicationContext(), SpUtils.WHEELS_ID + i, null);
        	idList.add(mID);
        }
        
        
    }
    
    /**
     * 初始化参数选项
     */
    private void initOptions()
    {
        // 要求返回详细地址的参数
        OPTION_ALL.setOpenGps(true); // 打开gps
        OPTION_ALL.setAddrType("all"); // 要求返回地址
        OPTION_ALL.setPriority(LocationClientOption.NetWorkFirst); // 优先使用网络
        OPTION_ALL.setCoorType(COORDINATE_TYPE); // 设置坐标类型
        OPTION_ALL.setScanSpan(SCAN_SPAN); // 设置定时定位的时间间隔，单位ms
        OPTION_ALL.setPoiNumber(1); // 搜索1个POI
        
        // 默认的参数
        OPTION_DEFAULT.setOpenGps(true); // 打开gps
        OPTION_DEFAULT.setPriority(LocationClientOption.GpsFirst); // 优先使用GPS
        OPTION_DEFAULT.setCoorType(COORDINATE_TYPE); // 设置坐标类型
        OPTION_DEFAULT.setScanSpan(SCAN_SPAN); // 设置定时定位的时间间隔，单位ms
    }
    
    @Override
    public void onTerminate()
    {
        // 在Application退出之前调用BMapManager的destroy()函数，避免重复初始化带来的时间消耗
        if (mapManager != null)
        {
            mapManager.destroy();
            mapManager = null;
        }
        
        // 关闭定位
        if (locationClient != null)
        {
            locationClient.stop();
            locationClient = null;
        }
        // 关闭MQTT
        // MqttService.stop(this);
        instance = null;
        super.onTerminate();
    }
    
    /**
     * 初始化地图管理器
     * 
     * @param context
     */
    public void initMapManager(Context context)
    {
        if (mapManager == null)
        {
            mapManager = new BMapManager(context);
            Log.d(TAG, "initMapManager");
        }
        
        if (!mapManager.init(KEY, new MyGeneralListener()))
        {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), "BMapManager 初始化错误!", Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * 初始化定位客户端
     * 
     * @param context
     */
    private void initLocationClient(Context context)
    {
        if (locationClient == null)
        {
            locationClient = new LocationClient(context);
            locationClient.registerLocationListener(locationListener);
            locationClient.setLocOption(OPTION_ALL);
        }
    }
    
    /**
     * 生成推送ID
     */
    private void generatePushId()
    {
        String pushId = "_" + PhoneUtils.getDeviceId(this);
        SpUtils.setString(this, KEY_PUSH_ID, pushId);
    }
    
    /**
     * 注册定位监听器
     * 
     * @param l
     */
    public void registerLocationListener(BDLocationListener l)
    {
        listeners.add(l);
    }
    
    /**
     * 取消定位监听器
     * 
     * @param l
     */
    public void unregisterLocationListener(BDLocationListener l)
    {
        listeners.remove(l);
    }
    
    /**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (location == null)
            {
                return;
            }
            
            if (onFirstLocateMyAddress)
            {
                updatingMyAddress();
            }
            
            synchronized (listeners)
            {
                Iterator<BDLocationListener> iterator = listeners.iterator();
                while (iterator.hasNext())
                {
                    try
                    {
                        iterator.next().onReceiveLocation(location);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        public void onReceivePoi(BDLocation location)
        {
            
        }
    }
    
    /**
     * 常用事件监听，用来处理通常的网络错误，授权验证错误等
     * 
     * @author WPH
     * 
     */
    static class MyGeneralListener implements MKGeneralListener
    {
        
        @Override
        public void onGetNetworkState(int iError)
        {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT)
            {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "您的网络出错啦！", Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA)
            {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "输入正确的检索条件！", Toast.LENGTH_LONG).show();
            }
        }
        
        @Override
        public void onGetPermissionState(int iError)
        {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED)
            {
                // 授权Key错误
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "请输入正确的授权Key！", Toast.LENGTH_LONG).show();
                MyApplication.getInstance().keyRight = false;
            }
        }
    }
    
}