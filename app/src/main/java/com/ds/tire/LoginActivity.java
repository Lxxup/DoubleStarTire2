package com.ds.tire;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.Constant;
import com.ds.tire.util.PhoneUtils;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class LoginActivity extends Activity
{
    private EditText     login_account       = null;
    private EditText     login_password      = null;
    private TextView login_back          = null; // 返回
    private LinearLayout bt_login            = null; // 登录
    private TextView     login_regist_button = null; // 注册
    private String       push_id;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViewById();
        login_back.setOnClickListener(new ClickListener());
        login_regist_button.setOnClickListener(new ClickListener());
        bt_login.setOnClickListener(new ClickListener());
        
    }
    
    private void findViewById()
    {
        login_back = (TextView) findViewById(R.id.login_back);
        bt_login = (LinearLayout) findViewById(R.id.bt_login);
        login_regist_button = (TextView) findViewById(R.id.login_regist_button);
        login_account = (EditText) findViewById(R.id.login_account);
        login_password = (EditText) findViewById(R.id.login_password);
    }
    
    private class ClickListener implements View.OnClickListener
    {
        
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.login_back:
                    finish();
                    break;
                case R.id.bt_login:
                    String account = login_account.getText().toString();
                    String password = login_password.getText().toString();
                    login(account, password);
                    break;
                case R.id.login_regist_button:
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, RegisterNewActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        
        private void login(String account, String password)
        {
            try
            {
                Pattern p = Pattern.compile("1" + "\\d{10}");
                Matcher m = p.matcher(account);
                if (!m.find())
                {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (account == null || (account = account.trim()).length() == 0)
                {
                    Toast.makeText(LoginActivity.this, "账号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password == null || (password = password.trim()).length() == 0)
                {
                    Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                push_id = "_" + PhoneUtils.getDeviceId(LoginActivity.this);
                SpUtils.setString(LoginActivity.this, Constant.KEY_PUSH_ID, push_id);
                LoginTask loginTask = new LoginTask(LoginActivity.this, account, password);
                loginTask.showProgressDialog("提示", "正在登录...");
                loginTask.execute();
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    class LoginTask extends AsyncNetworkTask<String>
    {
        private String account;
        private String password;
        
        public LoginTask(Context context, String account, String password) throws UnsupportedEncodingException
        {
            super(context);
            this.account = account;
            this.password = password;
        }
        
        public String doNetworkTask()
        {
            WebService ws = WebServiceFactory.getWebService();
            return ws.login(account, password, push_id);
        }
        
        @Override
        public void handleResult(String result)
        {
//        	result = "1";
        	Log.d("denglu", "RESULT"+result);
        	try{
            if (result != null && !result.equals("")&&!result.equals(""))
            {
                int uid = Integer.valueOf(result);
                
                if (uid > 0)
                {
                    SpUtils.setString(context, SpUtils.ACCOUNT, result);
                    SpUtils.setString(context,Constant.UID, result);
                    new GetWheelsInfoTask(context, result).execute();
                }
                else
                {
                    Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(context, "服务器异常", Toast.LENGTH_LONG).show();
            }
        	} catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(context, "服务器异常", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    class GetWheelsInfoTask extends AsyncNetworkTask<String>
    {
        private String uid;
        
        public GetWheelsInfoTask(Context context, String uid)
        {
            super(context);
            this.uid = uid;
        }
        
        @Override
        protected String doNetworkTask()
        {
            WebService ws = WebServiceFactory.getWebService();
            return ws.getWheelsInfo(uid);
        }
        
        @Override
        protected void handleResult(String result)
        {
            try
            {
                JSONObject jobj = new JSONObject(result);
                
                int wheelsCount = jobj.getInt("count");
                Log.v("AAA", wheelsCount+"bbb");
                SpUtils.setInteger(context, SpUtils.WHEELS_COUNT, wheelsCount);
                
                finish();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(context, "服务器异常", Toast.LENGTH_LONG).show();
            }
        }
    }
}
