package com.ds.tire.mqtt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ds.tire.util.Constant;
import com.ds.tire.util.NetworkUtils;

public class MqttPublisher extends Thread implements MqttCallback
{
    private static final String        TAG                             = MqttPublisher.class.getName();
    private static final String        BROKER_URL                      = Constant.BROKER_URL;
    private static final MqttPublisher INSTANCE                        = new MqttPublisher();
    
    private volatile MqttClient        mqttClient                      = null;
    private volatile Context           context                         = null;
    private volatile boolean           isRunning                       = false;
    private volatile boolean           networkIsConnected              = false;
    
    private final Handler              myHandler                       = new MyHandler();
    private static final int           WHAT_REGISTER_NETWORK_MONITOR   = 1;
    private static final int           WHAT_UNREGISTER_NETWORK_MONITOR = 2;
    private final NetworkMonitor       networkMonitor                  = new NetworkMonitor();
    
    class PublishMessage
    {
        public String topic;
        public String message;
    }
    
    private final LinkedBlockingQueue<PublishMessage> messages;
    
    /**
     * MQTT协议的订阅者
     */
    private MqttPublisher()
    {
        messages = new LinkedBlockingQueue<PublishMessage>();
    }
    
    public static MqttPublisher getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * 开启MQTT
     */
    public synchronized void startup(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException();
        }
        
        if (!isRunning)
        {
            this.context = context;
            // 考虑到异步情况，isRunning必须在线程开启之前设置
            isRunning = true;
            
            new Thread(this).start();
        }
    }
    
    /**
     * 关闭MQTT
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
        }
    }
    
    /**
     * 发布消息
     * 
     * @param topic
     *            主题
     * @param msg
     *            消息
     */
    public void publish(String topic, String msg)
    {
        PublishMessage m = new PublishMessage();
        m.topic = topic;
        m.message = msg;
        messages.add(m);
    }
    
    /**
     * 循环发布
     */
    private void publish()
    {
        while (isRunning)
        {
            try
            {
                PublishMessage m = null;
                while (null == (m = messages.poll(1, TimeUnit.SECONDS)) && isRunning)
                {
                    Thread.yield();
                }
                
                if (null != m)
                {
                    MqttTopic mqttTopic = mqttClient.getTopic(m.topic);
                    MqttMessage mm = new MqttMessage(m.message.getBytes("UTF-8"));
                    mm.setQos(1);
                    boolean ok = false;
                    while (!ok)
                    {
                        try
                        {
                            mqttTopic.publish(mm);
                            ok = true;
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }
            }
            catch (Exception e)
            {
            }
        }
    }
    
    @Override
    public void run()
    {
        // 注册网络监听器
        myHandler.sendEmptyMessage(WHAT_REGISTER_NETWORK_MONITOR);
        
        // 连接到MQTT代理服务器
        connect();
        
        // 循环推送
        publish();
        
        // 取消网络监听器
        myHandler.sendEmptyMessage(WHAT_UNREGISTER_NETWORK_MONITOR);
        
        Log.d(TAG, "MqttPublisher shutdown!");
    }
    
    @Override
    public void connectionLost(Throwable cause)
    {
        Log.d(TAG, "connectionLost@MqttPublisher");
        cause.printStackTrace();
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
                Log.d(TAG, "MqttPublisher startup!");
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
