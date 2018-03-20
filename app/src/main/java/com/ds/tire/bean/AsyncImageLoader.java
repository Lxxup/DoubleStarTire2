package com.ds.tire.bean;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;



import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * �첽����ͼƬ�࣬�ڲ��л��棬����ͨ����̨�̻߳�ȡ����ͼƬ����������һ��ʵ����������loadDrawableByTag��������ȡһ��Drawable����
 */
public class AsyncImageLoader {
    
    /**
     * ʹ��������SoftReference��������ϵͳ��ǡ����ʱ������׵Ļ���
     */
    private HashMap<String, SoftReference<Drawable>> imageCache;
    
    
    public AsyncImageLoader(){
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }
    
    
    
    
    /**
     * ͨ�������TagInfo����ȡһ�������ϵ�ͼƬ
     * @param tag TagInfo���󣬱�����position��url��һ������ȡ��Drawable����
     * @param callback ImageCallBack���������ڻ�ȡ��ͼƬ�󹩵��ò������һ���Ĵ���
     * @return drawable ������򻺴��еõ���Drawable���󣬿�Ϊnull�����ò����ж�
     */
    public Drawable loadDrawableByTag(final TagInfo tag, final ImageCallBack callback){
        Drawable drawable;
        
        /**
         * ���ڻ������ң����ͨ��URL��ַ�����ҵ�����ֱ�ӷ��ظö���
         */
        if(imageCache.containsKey(tag.getUrl())){
            drawable = imageCache.get(tag.getUrl()).get();
            if(null!=drawable){
                return drawable;
            }
        }
        
        
        /**
         * �����ڻ�ȡ������ͼƬ�󣬱���ͼƬ�����棬���������ò�Ĵ���
         */
        final Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {

                TagInfo info = (TagInfo)msg.obj;
                imageCache.put(info.url, new SoftReference<Drawable>(info.drawable));
                callback.obtainImage(info);
                
                super.handleMessage(msg);
            }
            
        };
        
        
        /**
         * ����ڻ�����û���ҵ�������һ���߳���������������
         */
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                
                TagInfo info = getDrawableIntoTag(tag);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = info;
                handler.sendMessage(msg);
            }
        }).start();
        
        return null;
    }
    
    
    
    /**
     * ͨ�������TagInfo����������URL���ԣ�����������ͼƬ����ȡ��ͼƬ�󱣴���TagInfo��Drawable�����У������ظ�TagInfo
     * @param info TagInfo������Ҫ���������url����
     * @return TagInfo �����TagInfo����������Drawable���Ժ󷵻�
     */
    public TagInfo getDrawableIntoTag(TagInfo info){
        URL request;
        InputStream input;
        Drawable drawable = null;
        
        try{
            request = new URL(info.getUrl());
            input = (InputStream)request.getContent();
            drawable = Drawable.createFromStream(input, "src"); // �ڶ������Կ�Ϊ�գ�ΪDEBUG��ʹ�ã����ϵ�˵��
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        info.drawable = drawable;
        return info;
    }
    
    
    
    /**
     * ��ȡͼƬ�Ļص��ӿڣ������obtainImage�����ڻ�ȡ��ͼƬ����е���
     */
    public interface ImageCallBack{
        /**
         * ��ȡ��ͼƬ���ڵ��ò�ִ�о����ϸ��
         * @param info TagInfo���󣬴����info������������Drawable���ԣ������ظ�������
         */
        public void obtainImage(TagInfo info);
    }
 // 1.�õ�ͼƬ·����ת��Ϊ�ļ���
 	// 2.����Bitmap.BitmapFactory��InputStream is����������һ��Bitmap����
 	// 3.��ʾ
 	public static Drawable loadImageFromUrl(String url) {
 		URL m;
 		InputStream i = null;
 		try {
 			m = new URL(url);
 			i = (InputStream) m.getContent();
 		} catch (MalformedURLException e1) {
 			e1.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 		Drawable d = Drawable.createFromStream(i, "src");
 		return d;
 	}
    

}

