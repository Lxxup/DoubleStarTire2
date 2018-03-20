/**
 * 
 */
package com.ds.tire;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 *    类功能描述:加载界面  @author 李小敏  @version 2013-12-1 上午11:34:11 
 */
public class SplashActivity extends Activity
{
    // splash界面相关
    private long            startTime;
    private Timer           timer;
    private final TimerTask task         = new TimerTask()
                                         {
                                             @Override
                                             public void run()
                                             {
                                                 if (task.scheduledExecutionTime() - startTime >= 3000)
                                                 {
                                                     Message message = new Message();
                                                     message.what = 0;
                                                     timerHandler.sendMessage(message);
                                                     timer.cancel();
                                                     this.cancel();
                                                 }
                                                 
                                             }
                                         };
    private final Handler   timerHandler = new Handler()
                                         {
                                             public void handleMessage(Message msg)
                                             {
                                                 switch (msg.what)
                                                 {
                                                     case 0:
                                                         Intent intent = new Intent(SplashActivity.this,
                                                        		 com.ds.tire.MainActivity.class);
                                                         SplashActivity.this.startActivity(intent);
                                                         SplashActivity.this.finish();
                                                         break;
                                                 }
                                                 super.handleMessage(msg);
                                             }
                                         };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        timer = new Timer(true);
        startTime = System.currentTimeMillis();
        timer.schedule(task, 0, 1);
    }
    
}
