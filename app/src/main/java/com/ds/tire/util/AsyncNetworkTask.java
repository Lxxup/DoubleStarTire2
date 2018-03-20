package com.ds.tire.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.ds.tire.R;

public abstract class AsyncNetworkTask<T> extends AsyncTask<Void, Void, T>
{
    protected Context        context            = null;
    private boolean        error              = false;
    private ProgressDialog progressDialog     = null;
    private boolean        showNetworkSetting = true;
    
    public AsyncNetworkTask(Context context)
    {
        this.context = context;
    }
    
    public Context getContext()
    {
        return context;
    }
    
    public boolean isError()
    {
        return error;
    }
    
    public void showProgressDialog(String title, String message)
    {
        progressDialog = ProgressDialog.show(context, title, message);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                AsyncNetworkTask.this.cancel(true);
            }
        });
    }
    
    public void showNetworkSetting(boolean enable)
    {
        showNetworkSetting = enable;
    }
    
    /**
     * 执行异步任务，注意该函数只能调用一次
     */
    public final void execute()
    {
        super.execute();
    }
    
    @Override
    protected void onPreExecute()
    {
        if (NetworkUtils.isConnected(context))
        {
            error = false;
        }
        else
        {
            if (showNetworkSetting)
            {
                int icon = R.drawable.ic_launcher;
                int title = R.string.network_dialog_title;
                int message = R.string.network_dialog_message;
                int positive = R.string.network_dialog_positive;
                int negative = R.string.network_dialog_negative;
                ClickListener l = new ClickListener();
                DialogUtils.alert(context, icon, title, message, positive, l, negative, l);
            }
            
            error = true;
        }
        
        Log.d("TAG", "error=" + error);
    }
    
    @Override
    protected T doInBackground(Void... params)
    {
        return error ? null : doNetworkTask();
    }
    
    @Override
    protected void onPostExecute(T result)
    {
        handleResult(result);
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    
    /**
     * 执行网络任务
     * 
     * @return 网络任务结果
     */
    protected abstract T doNetworkTask();
    
    /**
     * 处理网络任务结果
     * 
     * @param result
     *            网络任务结果
     */
    protected abstract void handleResult(T result);
    
    class ClickListener implements DialogInterface.OnClickListener
    {
        
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            
            switch (which)
            {
                case Dialog.BUTTON_POSITIVE:
                    setNetwork();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
            
            Log.d("WebService", "which=" + which);
        }
    }
    
    private void setNetwork()
    {
        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }
}
