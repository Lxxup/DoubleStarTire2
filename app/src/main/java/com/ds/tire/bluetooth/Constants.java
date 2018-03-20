/**
 * 
 */
package com.ds.tire.bluetooth;

import java.util.UUID;

/**  
* 类功能描述
* @author 李小敏
* @version 2013-12-20 下午06:58:18 
*/
public class Constants {

	public static String DataStatezero = "-1";
	public static String DataStateone = "-1";
	public static String DataStatetwo = "-1";
	public static String DataStatethree = "-1";
	public static String DataStatefour = "-1";
	public static String DataStatefive = "-1";
	public static String DataStatesix = "-1";
	public static String DataStateseven = "-1";
	public static String DataStateeight = "-1";
	public static String DataStatenine = "-1";
	public static String DataStateten = "-1";
	public static String DataStateeleven = "-1";
	//public static final String DATASTATE = "110";
	public static final String WenDu = "1";
	public static final String YaQiang = "2.2";
	public static final String JiaShudu = "30";
	public static int Tag = 0;
	public static final String REFRESH = "refresh";
	public static final String REFRESHOK = "refreshok";
	public static final String SET_WEN_YA="seting_data";
	// Intent 的请求码（MainActivity）
	public static final int REQUEST_CONNECT_DEVICE_INSECURE = 1;
	public static final int REQUEST_ENABLE_BT = 2;
	
	//蓝牙建立的连接的状态
	public static boolean BLUETOOTHDEVICE_CONNECT = false;
	public static final int MESSAGE_STATE_CHANGE = 11;
	public static final int MESSAGE_WRITE = 12;
	public static final int MESSAGE_READ = 13;
	public static final int MESSAGE_CONNECT_FAILED = 14;
	public static final int MESSAGE_CONNECT_LOST = 15;
	public static final String MSG = "massage";
	public static final String JIASUDU = "jiasudu";
	public static final String YAQIANG = "yaqiang";
	public static final String WENDU = "wendu";
	public static final String mID = "mID";
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	public static String KEY_CITY_ID;
	
	// 蓝牙连接的状态变化时，用到的量
	public static final int STATE_NONE = 21; // we're doing nothing
	public static final int STATE_LISTEN = 22; // now listening for incoming connections
	public static final int STATE_CONNECTING = 23; // now initiating an outgoing connection
	public static final int STATE_CONNECTED = 24; // now connected to a remote device
	public static final String SENSOR_ID0 = "sensor0";
    public static final String SENSOR_ID1 = "sensor1";
    public static final String SENSOR_ID2 = "sensor2";
    public static final String SENSOR_ID3 = "sensor3";
    public static final String SENSOR_ID4 = "sensor4";
    public static final String SENSOR_ID5 = "sensor5";
    public static final String SENSOR_ID6 = "sensor6";
    public static final String SENSOR_ID7 = "sensor7";
    public static final String SENSOR_ID8 = "sensor8";
    public static final String SENSOR_ID9 = "sensor9";
    public static final String SENSOR_ID10 = "sensor10";
    public static final String SENSOR_ID11 = "sensor11";
   
//	 public static final String SENSOR_ID0 = "0";
//	    public static final String SENSOR_ID1 = "1";
//	    public static final String SENSOR_ID2 = "2";
//	    public static final String SENSOR_ID3 = "3";
//	    public static final String SENSOR_ID4 = "4";
//	    public static final String SENSOR_ID5 = "5";
//	    public static final String SENSOR_ID6 = "6";
	// Unique UUID for this application
	public static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	
	public static final String ACTION_BLUETOOTH_SERVICE = "com.blutooth.SERVICE_START";
	public static final String ACTION_BLUETOOTH_BROADCAST = "com.blutooth.BROADCAST_START";
	public static final String ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_TALK = "com.blutooth.BROADCAST_TALK";
	public static final String ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_STOP = "com.blutooth.BROADCAST_STOP";
	public static final String ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_CONNECT = "com.blutooth.BROADCAST_CONNECT";
}
