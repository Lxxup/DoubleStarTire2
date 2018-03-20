package com.ds.tire;
import android.content.Context;

    /**
     * �Զ����Android�����JavaScript����֮���������
     * 
     * @author 1
     * 
     */
    public class WebAppInterface
    {
        private Context context;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context context)
        {
            this.context = context;
        }

        // ���target ���ڵ���API 17������Ҫ��������ע��
        // @JavascriptInterface
        public String getLocation()
        {
           
        	 String location = LocationHelper.GetLocation(context);
        	 return location;
        }
        
        public String test(){
        	return "test";
        }
    }