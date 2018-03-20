package com.ds.tire;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WeizhangActivity extends Activity {

	/** Called when the activity is first created. */
	private WebView webView;
	private static String URL = " ";

	@SuppressLint("JavascriptInterface")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.companyweb);

		 URL ="http://mobile.auto.sohu.com/wzcx/index.at";

		webView = (WebView) this.findViewById(R.id.webView);
		
		//���Ʋ��������������
		webView.setWebViewClient(new UserWebViewClient());
		
		//���������֧�ֽű�
		WebSettings setting = webView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setJavaScriptCanOpenWindowsAutomatically(true);

		//���õ���android�����Ľӿ�
		webView.addJavascriptInterface(new WebAppInterface(this.getApplicationContext()), "Android");
		
		Log.i("Test", "System start now!");
		//������վ
		webView.loadUrl(URL);
	}

	public class UserWebViewClient extends WebViewClient {
		
		//ʵ�ֱ�����������ʹ����������´��ڴ���ַ
		public boolean shouldOverrideUrlLoading(WebView view, String url){
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}
		//���ش�������
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			view.stopLoading();
			view.clearView();
			Message msg = handler.obtainMessage();// ����֪ͨ�������߳�
			msg.what = 1;// ֪ͨ�����Զ���404ҳ��
			handler.sendMessage(msg);// ֪ͨ���ͣ�
		}
		//���̿��ƣ�����δͨ��,��Ҫ����
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			Log.i("Test","keyCode:"+keyCode +"cangoback"+webView.canGoBack());
			if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			     webView.goBack();
			     return true;
			  }
			//return super.onKeyDown(keyCode, event);
			return false;
		}
		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			Log.i("Test","keyCode:"+event.getKeyCode() +"cangoback"+webView.canGoBack() );
			return super.shouldOverrideKeyEvent(view, event);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}
	}

	/**
	 * handler������Ϣ����
	 */
	protected Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:
				break;
			case 1:
				webView.loadUrl("file:///android_asset/404.html");
				break;
			case 2:
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}



}