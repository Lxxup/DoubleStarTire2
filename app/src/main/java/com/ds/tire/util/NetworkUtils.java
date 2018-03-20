package com.ds.tire.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.text.TextUtils;
import android.util.Log;

//import com.ds.tire.util.Constant;

public class NetworkUtils {

	public static final int    TIME_OUT          = 5000;
    public static final String WAP_CT            = "ctwap";
    public static final String WAP_CM            = "cmwap";
    public static final String WAP_3G            = "3gwap";
    public static final String WAP_UNI           = "uniwap";
    public static final Uri    PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    
    public static boolean isConnected(Context context)
    {
        // 获得网络连接服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取WIFI网络连接状态
        State wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        // 获取移动网络连接状态
        State mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        
        if (wifi == State.CONNECTED || mobile == State.CONNECTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isWap(Context context)
    {
        try
        {
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null || !ni.isAvailable())
            {
                
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                Log.i("", "=====================>无网络");
                return false;
            }
            else
            {
                // NetworkInfo不为null开始判断是网络类型
                int netType = ni.getType();
                if (netType == ConnectivityManager.TYPE_WIFI)
                {
                    // wifi net处理
                    Log.i("", "=====================>wifi网络");
                    return false;
                }
                else if (netType == ConnectivityManager.TYPE_MOBILE)
                {
                    // 注意二：
                    // 判断是否电信wap:
                    // 不要通过getExtraInfo获取接入点名称来判断类型，
                    // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
                    // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
                    // 所以可以通过这个进行判断！
                    final Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
                    if (c != null)
                    {
                        c.moveToFirst();
                        final String user = c.getString(c.getColumnIndex("user"));
                        if (!TextUtils.isEmpty(user))
                        {
                            Log.i("", "=====================>代理：" + c.getString(c.getColumnIndex("proxy")));
                            if (user != null && user.startsWith(WAP_CT))
                            {
                                Log.i("", "=====================>电信wap网络");
                                return true;
                            }
                        }
                    }
                    c.close();
                    
                    // 注意三：
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断
                    String netMode = ni.getExtraInfo();
                    Log.i("", "netMode ================== " + netMode);
                    if (netMode != null)
                    {
                        // 通过apn名称判断是否是联通和移动wap
                        if (WAP_CM.equals(netMode) || WAP_3G.equals(netMode) || WAP_UNI.equals(netMode))
                        {
                            Log.i("", "=====================>移动联通wap网络");
                            return true;
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return false;
    }
    
    /**
     * 请求-响应
     * 
     * @param path
     *            URL路径
     * @return
     */
    public static String request(String path)
    {
        try
        {
            Log.d("request", path);
            URL url = new URL(path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(TIME_OUT);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null)
            {
                sb.append("\n");
                sb.append(line);
            }
            
            String text = sb.toString();
            
            if (text != null && !text.equals(""))
            {
                text = text.substring(1);
            }
            
            br.close();
            http.disconnect();
            Log.d("request", text);
            return text;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取位图
     * 
     * @param path
     *            URL路径
     * @return
     */
    public static Bitmap getBitmap(String path)
    {
        URL url;
        Bitmap bm;
        URLConnection conn;
        try
        {
            url = new URL(path);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return null;
        }
        try
        {
            conn = url.openConnection();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        try
        {
            bm = BitmapFactory.decodeStream(conn.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return bm;
        
    }
    
    /**
     * 获取输入流
     * 
     * @param path
     *            URL路径
     * @return
     */
    public static InputStream getInputStream(String path)
    {
        InputStream instream = null;
        URL url = null;
        try
        {
            url = new URL(path);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try
        {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        int i = -1;
        try
        {
            i = conn.getResponseCode();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (i == 200)
        {
            try
            {
                instream = conn.getInputStream();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return instream;
        }
        
        return instream;
    }
    
    public static String uploadInfo(String interfaceName, String info)
    {
        String result = "";
        StringBuffer sb = new StringBuffer();
        sb.append(Constant.WEB_SITE);
        sb.append(interfaceName);
        String path = sb.toString();
        Log.d("TAG", path);
        // 获取HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        // 连接超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        // 请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        
        // 新建HttpPost对象
        HttpPost httpPost = new HttpPost(path);// index表示Url路径
        
        try
        {
            // 设置字符集
            HttpEntity entity = new StringEntity(info, HTTP.UTF_8);
            // 设置参数实体
            httpPost.setEntity(entity);
            
            // 获取HttpResponse实例
            HttpResponse httpResp = httpClient.execute(httpPost);
            // 判断是够请求成功
            if (httpResp.getStatusLine().getStatusCode() == 200)
            {
                // 获取返回的数据
                result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
            }
            else
            {
                Log.i("HttpPost", "HttpPost方式请求失败");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static String postInfo(String interfaceName, String info)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(Constant.WEB_SITE);
        sb.append(interfaceName);
        String path = sb.toString();
        Log.d("TAG", path);
        
        byte[] data = info.toString().getBytes();
        HttpURLConnection connection = null;
        try
        {
            connection = (HttpURLConnection) new URL(path).openConnection();
            connection.setConnectTimeout(5000);
            // 允许对外连接数据
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", data.length + "");
            connection.connect();
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null)
            {
                sb.append("\n");
                sb.append(line);
            }
            
            String text = sb.toString();
            
            if (text != null && !text.equals(""))
            {
                text = text.substring(1);
            }
            
            br.close();
            connection.disconnect();
            return text;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        /*
         * try { return (String) connection.getContent(); } catch (IOException
         * e) { e.printStackTrace(); }
         */
        return null;
    }
}
