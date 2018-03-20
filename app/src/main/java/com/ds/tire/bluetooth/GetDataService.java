/**
 * 
 */
package com.ds.tire.bluetooth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baidu.a.a.c.c;
import com.baidu.navisdk.ui.routeguide.fsm.RouteGuideFSM.IFSMDestStateListener;
import com.ds.tire.MonitorActivity;
import com.ds.tire.R;
import com.ds.tire.util.SpUtils;






import android.R.string;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**??
* 类功能描述
*?@author?李小敏
*?@version?2013-12-20?下午04:02:18?
*/
public class GetDataService extends Service {

	private int m=0;
	private int count=0;
	private static String TAG = "GetDataService";
    private float y;
    private final float k=0.9667f;
    private final float b=105.17f;
	public static BluetoothDevice mDevice;
	private BluetoothSocket mmSocket;
	private ConnectThread connectThread;
	private ConnectedThread connectedThread;

	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	protected BroadcastReceiver controlReceiver;

	/******************************Service自有函数**********************************************/
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
		registerBroadcastReceiver();
		Log.d(TAG, "GetDataService 注册广播成功");
	}

	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "onStart");
		setTalk();
		
	}
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		//count=0;
		resetTalk();
		unregisterReceiver(controlReceiver);

	}
	/******************************Service自有函数**********************************************/

	/**
	 * 开启服务
	 */
	public static void start(Context context, BluetoothDevice device) {
		mDevice = device;
		context.startService(new Intent(Constants.ACTION_BLUETOOTH_SERVICE));
	}

	/**
	 * 注册广播信息
	 */
	private void registerBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_CONNECT);
		intentFilter.addAction(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_STOP);
		intentFilter.addAction(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_TALK);
		intentFilter.addAction(Constants.REFRESH);
		controlReceiver = new ControlBroadcastReceiver();
		registerReceiver(controlReceiver, intentFilter);
	}

	//发送广播
	private void mSendBroadcast(int id) {
		Log.i(TAG, "mSendBroadcast");
		Intent intent_broad = new Intent();
		intent_broad.putExtra(Constants.MSG, id);
		intent_broad.setAction(Constants.ACTION_BLUETOOTH_BROADCAST);
		getApplicationContext().sendBroadcast(intent_broad);
	}

	private void mSendBroadcast(String mID ,float yaqiang, float wendu, float jiasudu) {
		Intent intent_broad = new Intent();
		intent_broad.putExtra(Constants.JIASUDU, jiasudu);
		intent_broad.putExtra(Constants.YAQIANG, yaqiang);
		intent_broad.putExtra(Constants.WENDU, wendu);
		intent_broad.putExtra(Constants.mID, mID);
		intent_broad.setAction(Constants.ACTION_BLUETOOTH_BROADCAST);
		getApplicationContext().sendBroadcast(intent_broad);
	}

	/**
	 * 建立回话
	 */
	public void setTalk() {
		resetTalk();
		connectThread = new ConnectThread();
		connectThread.start();
		

	}

	/**
	 * 开始回话
	 */
	private void startTalk() {
		connectedThread = new ConnectedThread();
		connectedThread.start();
		Log.d(TAG, "33create ConnectedThread: " );
	}

	/**
	 * 重置回话
	 */
	private synchronized void resetTalk() {
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		if (connectedThread != null) {			
			connectedThread.cancel();
			connectedThread = null;
		}
	}
	
	

	/**
	 * 建立连接的线程
	 * @author 李小敏
	 *
	 */

	private class ConnectThread extends Thread {
		

		public ConnectThread() {
			mSendBroadcast(Constants.STATE_CONNECTING);
			BluetoothSocket tmp = null;
			try {
				tmp = mDevice.createInsecureRfcommSocketToServiceRecord(Constants.MY_UUID_INSECURE);

			} catch (IOException e) {
			}
			mmSocket = tmp;
		}

		public void run() {
		
			setName("ConnectThread" + "Insecure");
			mBluetoothAdapter.cancelDiscovery();
			try {
				mmSocket.connect();
			} catch (IOException e) {
				try {
					mmSocket.connect();
				} catch (IOException ex) {
					try {
						mmSocket.close();
					} catch (IOException e2) {
						Log.e(TAG, "unable to close() " + "Insecure" + " socket during connection failure", e2);
					}
					Log.e(TAG, "ConnectThread socket failed", e);
					mSendBroadcast(Constants.MESSAGE_CONNECT_FAILED);
					return;
				}
			}
			synchronized (GetDataService.this) {
				connectThread = null;
			}
			mSendBroadcast(Constants.STATE_CONNECTED);
		
		}
		public void cancel() {
			try {
				Log.e(TAG, "mConnectedThread cancel");
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect " + "Insecure" + " socket failed", e);
			}
		}
		
	}
	/**
	 * 保持会话的线程
	 * 
	 * @author 李小敏
	 *
	 */
	private class ConnectedThread extends Thread {

		// private final InputStream mmInStream;
		// private final OutputStream mmOutStream;
		private InputStream mmInStream; // 输入流
		private OutputStream mmOutStream; // 输出流
		private ArrayList<Integer> buffer = null; // 动态数组

		private int mDateChange(int date) {
			int finalDate;
			if (date > 0x7fff) {
				int fanma = date - 1;
				int yuanma = fanma ^ 0xffff;
				finalDate = 0 - yuanma;
			} else
				finalDate = date;

			return finalDate;
		}

		// 十进制转化为十六进 Integer.toHexString(200);
		// 十六进制转化为十进制 Integer.parseInt("8C",16);
		private byte[] getHexBytes(String message) {
			int len = message.length() / 2;
			char[] chars = message.toCharArray();
			String[] hexStr = new String[len];
			byte[] bytes = new byte[len];
			for (int i = 0, j = 0; j < len; i += 2, j++) {
				hexStr[j] = "" + chars[i] + chars[i + 1];
				bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
			}
			return bytes;
		}

		public ConnectedThread() {
			Log.d(TAG, "create ConnectedThread: " + "Insecure");

			this.buffer = new ArrayList<Integer>(100);

			// 获取蓝牙的socket的InputStream和OutputStream
			try {
				mmInStream = mmSocket.getInputStream(); // 获取套接字输入流
				mmOutStream = mmSocket.getOutputStream(); // 获取套接字输出流
			} catch (IOException e) { // IOException异常
				Log.e(TAG, "temp sockets not created", e);
			}
		}

		public void run() {

			Log.i(TAG, "read data start.....");
			int getdata=0;
			// byte [] getdata = new byte[1024];
			int corrected = 0;
			int count = 0;
			int c1= 0;
			int c2= 0;
			///////////////////////////////////////////////////////////////////////////// 小月加
			// 输出注册指令 81801A05E0 到输出流 OutputStream
//			try {
//				OutputStream outStream = mmSocket.getOutputStream();
//				outStream.write(getHexBytes("81801A05E0"));
//				Log.d("TAG", "oh");
//			} catch (IOException e) {
//				Log.e("TAG", "输出错误啦", e);
//			}

			// 不断监视InputStream，一旦发来信息，就会即刻收到
			while (true) {
				Log.i(TAG, "while true.....");
				buffer.clear();
				while(buffer.size()!=9){
				Log.i(TAG, "buffer.size()!=9.....");
				try {
					Log.i(TAG, "try 读取数据.....");
					getdata = mmInStream.read();
					Log.d("读取到的数据：", Integer.toHexString(getdata));
					
					if (getdata + " " != null) {
	                    int i = getdata;
						// 纠正错误的数据
						if (i < 0) {
							corrected = ((i + 256) % 256);
							buffer.add(corrected);
							Log.i(TAG, "存储数据！！！");
						} else {
							buffer.add(i);
							Log.i(TAG, "存储数据222！！");
						}	
						
						
					}else { 
							Log.i(TAG, "getdata的数据是空的！！！");							
					}
					
					
				} catch (IOException e) {
					Log.e("TAG", "disconnected", e);
					mSendBroadcast(Constants.MESSAGE_CONNECT_LOST);
					resetTalk();
					break;
				}

			}
			//System.out.print(buffer)  下面应该是buffer.size()==9;
				Log.i(TAG, "打印buffer大小");
				Log.i("TAG", "buffer的大小："+buffer.size());
				if(buffer.size()==9){
					//先验证checksum
					Log.i(TAG, "打印buffer大小aaaa");
					String TireNo = Integer.toHexString(buffer.get(4));
					Log.i(TAG,Integer.toHexString(buffer.get(4)));
					if(TireNo.equals("5")){
						
					//右后轮
					Log.i(TAG,"右后轮");

					}else if(TireNo == "1"){
					//右前轮
					Log.i(TAG,"右前轮");

					}else if(TireNo == "4"){
					//左后轮
					Log.i(TAG,"左后轮");

					}else if(TireNo == "0"){
					//左前轮
					Log.i(TAG,"左前轮");

					}else if(TireNo == "8"){
					//备胎
					Log.i(TAG,"备胎");

					}
					//2.压强
					float P = (float) (mDateChange(buffer.get(5)));
					//3.温度
					float T = (float) (mDateChange(buffer.get(6)));
					//4.状态
					float V = (float) (mDateChange(buffer.get(7)));
					
					mSendBroadcast(TireNo, P, T, V);
					
				}
			}

		}

		public void cancel() {
			try {
				Log.e(TAG, "mConnectedThread cancel");
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	/**
	 * 广播接收器，接受传送过来的关于蓝牙的指令，选择相应的操作
	 * @author 李小敏
	 *
	 */
	private class ControlBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_CONNECT)) {
				setTalk();
			}else if (intent.getAction().equals(Constants.ACTION_BLUETOOTH_RECEIVEDE_BROADCAST_TALK)) {
				startTalk();
			}
		}

	}	
}
