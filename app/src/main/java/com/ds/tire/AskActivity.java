package com.ds.tire;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class AskActivity extends Activity
{
    private Button       submit     = null;
    private LinearLayout back       = null;
    private EditText     et_mail    = null;
    private EditText     et_title   = null;
    private EditText     et_content = null;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask);
        findViewById();
        submit.setOnClickListener(new ClickListener());
        back.setOnClickListener(new ClickListener());
    }
    
    private void findViewById()
    {
        back = (LinearLayout) findViewById(R.id.ask_back);
        submit = (Button) findViewById(R.id.bt_ask);
        et_mail = (EditText) findViewById(R.id.et_mail);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
    }
    
    private class ClickListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.bt_ask:
                    String id = SpUtils.getString(AskActivity.this, SpUtils.ACCOUNT, "putong");
                    String mail = et_mail.getText().toString();
                    String title = et_title.getText().toString();
                    String content = et_content.getText().toString();
                    ask(id, mail, title, content);
                    break;
                case R.id.ask_back:
                    AskActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
        
        private void ask(String id, String mail, String title, String content)
        {
            try
            {
                if (mail == null || (mail = mail.trim()).length() == 0)
                {
                    Toast.makeText(AskActivity.this, "邮箱不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title == null || (title = title.trim()).length() == 0)
                {
                    Toast.makeText(AskActivity.this, "标题不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (content == null || (content = content.trim()).length() == 0)
                {
                    Toast.makeText(AskActivity.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                AskTask askTask = new AskTask(AskActivity.this, id, mail, title, content);
                askTask.showProgressDialog("提示", "正在提交...");
                askTask.execute();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    class AskTask extends AsyncNetworkTask<String>
    {
        private String id;
        private String mail;
        private String title;
        private String content;
        
        public AskTask(Context context, String id, String mail, String title, String content) throws UnsupportedEncodingException
        {
            super(context);
            this.id = id;
            this.mail = mail;
            this.title = title;
            this.content = content;
        }
        
        @Override
        public String doNetworkTask()
        {
            WebService ws = WebServiceFactory.getWebService();
            return ws.ask(id, mail, title, content);
        }
        
        @Override
        public void handleResult(String result)
        {
            if (result != null && !result.equals(""))
            {
                if (result.equals("1"))
                {
                    Toast.makeText(AskActivity.this, "提问提交成功", Toast.LENGTH_SHORT).show();
                    AskActivity.this.finish();
                }
                else
                {
                    Toast.makeText(AskActivity.this, "提问提交失败", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(AskActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
