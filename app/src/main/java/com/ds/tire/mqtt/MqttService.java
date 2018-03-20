package com.ds.tire.mqtt;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ds.tire.util.Constant;
import com.ds.tire.util.SpUtils;

public class MqttService extends Service
{
    private static final String TAG                             = MqttService.class.getName();
    
    public static final String  ACTION_STARTUP_MQTT_PUBLISHER   = "action.startup.mqtt.publisher";
    public static final String  ACTION_SHUTDOWN_MQTT_PUBLISHER  = "action.shutdown.mqtt.publisher";
    
    public static final String  ACTION_STARTUP_MQTT_SUBSCRIBER  = "action.startup.mqtt.subscriber";
    public static final String  ACTION_SHUTDOWN_MQTT_SUBSCRIBER = "action.shutdown.mqtt.subscriber";
    
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    
    @Override
    public void onStart(Intent intent, int startId)
    {
        try
        {
            String action = intent.getAction();
            Log.d(TAG, "action=" + action);
            if (ACTION_STARTUP_MQTT_SUBSCRIBER.equals(action))
            {
                // 获取推送ID，并生成MQTT客户端ID和主题
                String pushId = SpUtils.getString(this, Constant.KEY_PUSH_ID, "");
                Log.d(TAG, "pushId=" + pushId);
                String topic = "/u/" + pushId;
                List<String> topics = new ArrayList<String>();
                topics.add(topic);
                MqttSubscriber.getInstance().startup(getApplicationContext(), topics);
                
            }
            else if (ACTION_SHUTDOWN_MQTT_SUBSCRIBER.equals(action))
            {
                MqttSubscriber.getInstance().shutdown();
            }
            else if (ACTION_STARTUP_MQTT_PUBLISHER.equals(action))
            {
                MqttPublisher.getInstance().startup(getApplicationContext());
            }
            else if (ACTION_SHUTDOWN_MQTT_PUBLISHER.equals(action))
            {
                MqttPublisher.getInstance().shutdown();
            }
            else
            {
                // do nothing
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        stopSelf();
    }
    
    /**
     * 开启MQTT订阅者
     * 
     * @param context
     *            上下文
     */
    public static void startupMqttSubscriber(Context context)
    {
        Intent ms = new Intent(context, MqttService.class);
        ms.setAction(ACTION_STARTUP_MQTT_SUBSCRIBER);
        context.startService(ms);
    }
    
    /**
     * 关闭MQTT订阅者
     * 
     * @param context
     *            上下文
     */
    public static void shutdownMqttSubscriber(Context context)
    {
        Intent ms = new Intent(context, MqttService.class);
        ms.setAction(ACTION_SHUTDOWN_MQTT_SUBSCRIBER);
        context.startService(ms);
    }
    
    /**
     * 开启MQTT发布者
     * 
     * @param context
     *            上下文
     */
    public static void startupMqttPublisher(Context context)
    {
        Intent ms = new Intent(context, MqttService.class);
        ms.setAction(ACTION_STARTUP_MQTT_PUBLISHER);
        context.startService(ms);
    }
    
    /**
     * 关闭MQTT发布者
     * 
     * @param context
     *            上下文
     */
    public static void shutdownMqttPublisher(Context context)
    {
        Intent ms = new Intent(context, MqttService.class);
        ms.setAction(ACTION_SHUTDOWN_MQTT_PUBLISHER);
        context.startService(ms);
    }
    
}
