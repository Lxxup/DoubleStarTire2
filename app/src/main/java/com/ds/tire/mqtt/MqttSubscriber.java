package com.ds.tire.mqtt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ds.tire.util.Constant;
import com.ds.tire.util.NetworkUtils;

public class MqttSubscriber extends Thread implements MqttCallback
{
    private static final String         TAG                             = MqttSubscriber.class.getName();
    private static final String         BROKER_URL                      = Constant.BROKER_URL;
    private static final MqttSubscriber INSTANCE                        = new MqttSubscriber();
    
    private volatile MqttClient         mqttClient                      = null;
    private volatile Context            context                         = null;
    private volatile List<String>       topics                          = null;
    private volatile Thread             thread                          = null;
    private volatile boolean            isRunning                       = false;
    private volatile boolean            networkIsConnected              = false;
    
    private final Handler               myHandler                       = new MyHandler();
    private static final int            WHAT_MQTT                       = 0;
    private static final int            WHAT_REGISTER_NETWORK_MONITOR   = 1;
    private static final int            WHAT_UNREGISTER_NETWORK_MONITOR = 2;
    private final NetworkMonitor        networkMonitor                  = new NetworkMonitor();
    
    public static MqttSubscriber getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * MQTT订阅者
     */
    private MqttSubscriber()
    {
    }
    
    /**
     * 开启MQTT订阅者
     * 
     * @param context
     *            上下文
     * @param topics
     *            主题
     */
    public synchronized void startup(Context context, List<String> topics)
    {
        if (context == null || topics == null)
        {
            throw new NullPointerException();
        }
        
        if (!isRunning)
        {
            this.context = context;
            this.topics = topics;
            // 考虑到异步情况，isRunning必须在线程开启之前设置
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    
    /**
     * 关闭MQTT订阅者
     */
    public synchronized void shutdown()
    {
        if (isRunning)
        {
            isRunning = false;
            
            if (mqttClient != null && mqttClient.isConnected())
            {
                try
                {
                    mqttClient.disconnect(0);
                }
                catch (MqttException e)
                {
                    e.printStackTrace();
                }
            }
            // 确保线程关闭
            mqttClient = null;            
            try
            {
            	thread.join();
            }
            catch (Exception e)
            {
            	e.printStackTrace();
			}
        }
    }
    
    @Override
    public void run()
    {
        isRunning = true;
        // 注册网络监听器
        myHandler.sendEmptyMessage(WHAT_REGISTER_NETWORK_MONITOR);
        
        // 连接到MQTT代理服务器
        connect();
        
        // 线程保活
        while (isRunning)
        {
            Thread.yield();
        }
        
        // 取消网络监听器
        myHandler.sendEmptyMessage(WHAT_UNREGISTER_NETWORK_MONITOR);
        
        Log.d(TAG, "MqttSubscriber shutdown!");
    }
    
    @Override
    public void connectionLost(Throwable cause)
    {
        cause.printStackTrace();
        Log.d(TAG, "connectionLost@MqttSubscriber");
        connect();
    }
    
    /**
     * 连接到MQTT代理服务器
     */
    private synchronized void connect()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        Random random = new Random();
        // 若ImOnlineMqtt服务正在运行，并且mqttClient没有连接成功，那么继续
        while (isRunning && networkIsConnected && null == mqttClient)
        {
            try
            {
                String format = "%s%03d";
                String id = String.format(format, dateFormat.format(new Date()), (random.nextInt() & 0xFFFF) % 1000);
                mqttClient = new MqttClient(BROKER_URL, id, new MemoryPersistence());
                mqttClient.setCallback(this);
                // 连接选项
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                options.setConnectionTimeout(5000); // 连接超时5秒
                options.setKeepAliveInterval(30000); // 心跳间隔为30秒
                // 连接
                mqttClient.connect(options);
                
                // 订阅主题
                for (String topic : topics)
                {
                    mqttClient.subscribe(topic, 1);
                }
                
                Log.d(TAG, "MqttSubscriber startup!");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                // 若已经连接，则断开连接
                try
                {
                    mqttClient.disconnect(0);
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
                mqttClient = null;
                
                // 抛出当前时间片
                Thread.yield();
            }
        }
    }
    
    @Override
    public void deliveryComplete(MqttDeliveryToken token)
    {
    }
    
    @Override
    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception
    {
        String json = new String(message.getPayload());
        Log.d(TAG, json + "");
        // 创建数据
        Bundle data = new Bundle();
        data.putString(Constant.KEY_JSON, json);
        // 创建消息
        Message msg = new Message();
        msg.what = WHAT_MQTT;
        msg.setData(data);
        // 处理消息
        myHandler.sendMessage(msg);
    }
    
    /**
     * 异步处理
     * 
     * @author WPH
     * 
     */
    private class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case WHAT_MQTT:
                    handleMqtt(msg);
                    break;
                case WHAT_REGISTER_NETWORK_MONITOR:
                    registerNetworkMonitor();
                    break;
                case WHAT_UNREGISTER_NETWORK_MONITOR:
                    unregisterNetworkMonitor();
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * 处理MQTT协议
     * 
     * @param msg
     */
    
    public void handleMqtt(Message msg)
    {
        Bundle data = msg.getData();
        String json = data.getString(Constant.KEY_JSON);
        Log.d(TAG, "json=" + json);
        
        String action = null;
        
        // 解析json数据
        try
        {
            JSONObject obj = new JSONObject(json);
            
            // 根据类型返回广播的Action
            String clazz = obj.getString("class");
            action = getAction(clazz);
            
            json = obj.getJSONObject("contents").toString();
        }
        catch (JSONException e)
        {
            Log.d(TAG, "error:" + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        if (null != action)
        {
            // 发送广播
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_JSON, json);
            intent.setAction(action);
            context.sendBroadcast(intent);
        }
    }
    
    /**
     * 根据类型返回广播的Action
     * 
     * @param clazz
     *            类型
     * @return
     */
    public String getAction(String clazz)
    {
        String action = null;
        if ("1".equals(clazz))
        {
            action = Constant.ACTION_RESCUE_LOCATION;
        }
        else if ("2".equals(clazz))
        {
            action = Constant.ACTION_RESCUE_DETAILS;
        }
        else if("3".equals(clazz))
        {
        	action = Constant.ACTION_RESCUE_DETAILS;
        }
        else if("4".equals(clazz))
        {
        	action = Constant.ACTION_RESCUE_DETAILS;
        }
        else
        {
            action = null;
        }
        
        return action;
    }
    
    /**
     * 注册网络监听器
     */
    private void registerNetworkMonitor()
    {
        networkIsConnected = NetworkUtils.isConnected(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkMonitor, filter);
    }
    
    /**
     * 取消网络监听器
     */
    private void unregisterNetworkMonitor()
    {
        context.unregisterReceiver(networkMonitor);
    }
    
    /**
     * 网络监听器
     * 
     * @author WPH
     * 
     */
    class NetworkMonitor extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            try
            {
                networkIsConnected = NetworkUtils.isConnected(context);
                if (networkIsConnected)
                {
                    new Thread(new RestartRunable()).start();
                }
            }
            catch (Exception e)
            {
            }
        }
    }
    
    class RestartRunable implements Runnable
    {
        @Override
        public void run()
        {
            connect();
        }
    }
}
