package com.ds.tire.util;

public final class WebServiceFactory
{
    private WebServiceFactory()
    {
    }
    
    public static WebService getWebService()
    {
        return new HttpWebService();
    }
}
