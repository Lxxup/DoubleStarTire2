/**
 * 
 */
package com.ds.tire.bluetooth;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ds.tire.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**  
* 类功能描述：蓝牙界面
* @author 李小敏
* @version 2013-12-27 下午02:31:54 
*/
public class DeviceList extends Activity {
	private static final String TAG = "DeviceList";
	private String flag = "1";

	private Button scanButton;
	private ArrayAdapter<String> mDevicesArrayAdapter;
	private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<BluetoothDevice>();
	private BluetoothAdapter mBtAdapter;
	private BluetoothDevice bluetoothDevice;
	private DeviceFindReceiver receiver;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devicelist);
       
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		scanButton = (Button) findViewById(R.id.button_scan);
		init();

	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("开始：","diaoyong");
		registerBroadcastReceiver();
		//doDiscovery();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(receiver);
		super.onStop();
	}

	/**
	 * 控件初始化
	 */
	private void init() {
         
		
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (flag=="1") {
					doDiscovery();
				} else if(flag=="0"){
					cancelDiscovery();
				}
			}
		});

		ListView pairedListView = (ListView) findViewById(R.id.new_devices);
		pairedListView.setAdapter(mDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
				cancelDiscovery();
				bluetoothDevice = bluetoothDeviceList.get(arg2);
				Toast.makeText(DeviceList.this, bluetoothDevice.getName() + " selected", Toast.LENGTH_LONG).show();
				Log.i("TAG", "0000000000000");
				//如果已经配对，则直接跳回界面
				if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
					back(bluetoothDevice);
					Log.i("TAG", "1111111111111111111");
				//否则，先进行配对操作，再跳回信息接收界面
				} else if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
					try {
						Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
						createBondMethod.invoke(bluetoothDevice);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

	/**
	 * 函数功能：返回信息接收界面
	 */
	private void back(BluetoothDevice bluetoothDevice) {
		Intent intent = new Intent();
		intent.putExtra(Constants.EXTRA_DEVICE_ADDRESS, bluetoothDevice.getAddress());
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	/**
	 * 开始扫面周边设备
	 */
	private void doDiscovery() {
		bluetoothDeviceList.clear();
		mDevicesArrayAdapter.clear();
		scanButton.setText("结束扫描");
		setProgressBarIndeterminateVisibility(true);
		setTitle("扫描中");
		
//		if (mBtAdapter != null) {
//			mBtAdapter.cancelDiscovery();
//		}
		mBtAdapter.startDiscovery();
		flag = "0";
		Log.d("测试：","flag=0");
	}

	/**
	 * 结束扫描
	 */
	private void cancelDiscovery() {
		flag = "1";
		Log.d("测试：","flag=1");
		scanButton.setText("重新扫描");
		setProgressBarIndeterminateVisibility(false);
		setTitle("选择设备进行连接");
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
	}

	/**
	 * 注册广播信息
	 */
	private void registerBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		receiver = new DeviceFindReceiver();
		registerReceiver(receiver, intentFilter);
	}

	private class DeviceFindReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device != null && !bluetoothDeviceList.contains(device)) {
					bluetoothDeviceList.add(device);
					mDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				cancelDiscovery();
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				cancelDiscovery();
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.equals(bluetoothDevice)&&device.getBondState() == BluetoothDevice.BOND_BONDED) {
					Log.d(TAG, device.getName() + " bonded successfully");
					back(bluetoothDevice);
				}
			}
		}
	};
}
