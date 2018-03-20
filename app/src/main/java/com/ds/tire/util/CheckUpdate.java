package com.ds.tire.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ds.tire.R;

public class CheckUpdate
{
    private static final int DOWNLOAD_UPDATE      = 1;
    private static final int DOWNLOAD_OVER        = 2;
    private static final int DOWNLOAD_ERROR       = 3;
    String                   apkUrl;
    private Context          mContext             = null;
    // 返回的安装包url
    private Dialog           noticeDialog         = null;
    private Dialog           downloadDialog       = null;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private TextView         mHintTextView        = null;
    private ProgressBar      mProgress            = null;
    private int              progress             = 0;
    private int              total                = 0;
    private int              count                = 0;
    private boolean          interceptFlag        = false;
    private final Handler    mHandler             = new MyHandler();
    private Thread           downLoadThread       = null;
    private Runnable         mDownloadApkRunnable = null;
    /* 下载包安装路径 */
    private final File       savePath             = new File(Environment.getExternalStorageDirectory(), "download");
    private File             apkFile              = null;
    private String           fileName             = null;
    HttpURLConnection        conn                 = null;
    String                   TAG                  = "TAG";
    
    /**
     * @param 上下文
     * @param 服务器请求地址
     * 
     */
    public CheckUpdate(Context mContext)
    {
        super();
        this.mContext = mContext;
    }
    
    public void checkUpdate()
    {
        CheckUpdateTask task = new CheckUpdateTask(mContext);
        task.showProgressDialog("提示", "正在检测版本...");
        task.execute();
    }
    
    private void checkVersion(String flag)
    {
        if (flag.equals("error"))
        {
            showUpdateNoticeDialog("error");
        }
        else if (flag.equals("true"))
        {
            showUpdateNoticeDialog("true");
        }
        else
        {
            showUpdateNoticeDialog("false");
        }
    }
    
    class CheckUpdateTask extends AsyncNetworkTask<String>
    {
        
        public CheckUpdateTask(Context context)
        {
            super(context);
        }
        
        @Override
        protected String doNetworkTask()
        {
            StringBuffer sb = new StringBuffer();
            sb.append(Constant.WEB_SITE);
            sb.append("checkUpdateInfo.r?");
            sb.append("version=" + SpUtils.getString(getContext(), "version", null));
            sb.append("&type=" + Constant.TYPE);
            sb.append("&role=" + Constant.ROLE);
            String path = sb.toString();
            Log.d(TAG, "path=" + path);
            return NetworkUtils.request(path);
        }
        
        @Override
        protected void handleResult(String json)
        {
            Log.d(TAG, "json=" + json);
            if (json != null && !json.equals(""))
            {
                Log.d(TAG, json);
                try
                {
                    JSONObject object = new JSONObject(json);
                    String flag = object.getString("flag");
                    if (flag.equals("true"))
                    {
                        apkUrl = object.getString("apkUrl");
                        Log.d(TAG, apkUrl);
                        if (apkUrl != null && !apkUrl.equals("") && flag != null && !flag.equals(""))
                        {
                            fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1, apkUrl.length());
                            checkVersion(flag);
                        }
                    }
                    else
                    {
                        checkVersion(flag);
                    }
                }
                catch (JSONException e)
                {
                    showErrorDialog("数据异常！");
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    /**
     * Description：版本更新提示 Parameters ：是否更新
     */
    
    private void showUpdateNoticeDialog(String update)
    {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("版本更新");
        if (update.equals("true"))
        {
            builder.setMessage("有最新的软件包哦，亲快下载吧~");
            
            builder.setPositiveButton("下载", new OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                    {
                        showDownloadDialog();
                    }
                    else
                    {
                        showErrorDialog("未检测到SD卡！");
                    }
                }
            });
            
            builder.setNegativeButton("以后再说", new OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
        }
        else if (update.equals("error"))
        {
            builder.setMessage("未检测到版本信息，请稍后再试");
            builder.setPositiveButton("确定", null);
        }
        else
        {
            builder.setMessage("当前已是最新版本");
            builder.setPositiveButton("确定", null);
        }
        noticeDialog = builder.create();
        noticeDialog.show();
        
    }
    
    /**
     * 显示下载对话框
     */
    private void showDownloadDialog()
    {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("下载新版本");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mHintTextView = (TextView) v.findViewById(R.id.hint_text_view);
        mHintTextView.setText("正在准备下载……");
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        builder.setCancelable(false);
        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
    }
    
    /**
     * 
     * 
     * 类描述：下载线程
     */
    class DownloadApkRunnable implements Runnable
    {
        
        @Override
        public void run()
        {
            InputStream is = null;
            FileOutputStream fos = null;
            try
            {
                Log.d(TAG, "apk=" + apkUrl);
                URL url = new URL(apkUrl);
                
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.connect();
                Log.d(TAG, "connetion==" + conn.getResponseCode());
                total = conn.getContentLength();
                is = conn.getInputStream();
                
                if (!savePath.exists())
                {
                    savePath.mkdir();
                }
                fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1, apkUrl.length());
                Log.d(TAG, fileName);
                apkFile = new File(savePath, fileName);
                fos = new FileOutputStream(apkFile);
                
                count = 0;
                progress = 0;
                
                mHandler.sendEmptyMessage(DOWNLOAD_UPDATE);
                
                byte buf[] = new byte[1024];
                
                do
                {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / total) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWNLOAD_UPDATE);
                    if (numread <= 0)
                    {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWNLOAD_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                }
                while (!interceptFlag);// 点击取消就停止下载.
                
                fos.close();
                is.close();
                conn.disconnect();
                conn = null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
                if (is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                
                if (fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                
                mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            }
            
        }
    }
    
    /**
     * 下载apk
     */
    
    private void downloadApk()
    {
        total = 0;
        count = 0;
        mDownloadApkRunnable = new DownloadApkRunnable();
        downLoadThread = new Thread(mDownloadApkRunnable);
        downLoadThread.start();
    }
    
    /**
     * 安装apk
     */
    private void installApk()
    {
        if (downloadDialog != null)
        {
            downloadDialog.dismiss();
        }
        
        if (!apkFile.exists())
        {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
    
    /**
     * 消息处理器
     * 
     * @author WPH
     * 
     */
    class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case DOWNLOAD_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_OVER:
                    installApk();
                    break;
                case DOWNLOAD_ERROR:
                    showError();
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * Description：显示错误对话框 Parameters ：错误信息
     */
    
    private void showErrorDialog(String msg)
    {
        new AlertDialog.Builder(mContext).setTitle("提示").setMessage(msg).setPositiveButton("确定", null).create().show();
    }
    
    /**
     * 显示错误信息
     */
    private void showError()
    {
        conn = null;
        if (downloadDialog != null)
        {
            downloadDialog.dismiss();
        }
        showErrorDialog("下载出错了！");
    }
}
