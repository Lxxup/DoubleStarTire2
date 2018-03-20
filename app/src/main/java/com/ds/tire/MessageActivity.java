package com.ds.tire;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.Information;
import com.ds.tire.util.MessageAdapter;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class MessageActivity extends Activity
{
    public static final String TAG      = null;
    Button                     ask;
    LinearLayout               back;
    ListView                   message_list;
    List<Information>          messages = new ArrayList<Information>();
    
    private Handler            handler  = new MyHandler();
    
    class MyHandler extends Handler
    {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    messages = (List<Information>) msg.obj;
                    MessageAdapter adapter = new MessageAdapter(MessageActivity.this, messages);
                    message_list.setAdapter(adapter);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
        
    }
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        findViewById();
        
        back.setOnClickListener(new ClickListener());
        ask.setOnClickListener(new ClickListener());
        
        message_list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(MessageActivity.this, MessageDetailActivity.class);
                intent.putExtra("title", messages.get(position).getTitle());
                intent.putExtra("content", messages.get(position).getContent());
                intent.putExtra("feedback", messages.get(position).getFeedback());
                startActivity(intent);
            }
        });
    }
    
    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        if (SpUtils.getString(MessageActivity.this, SpUtils.ACCOUNT, "").equals(""))
        {
            Toast.makeText(MessageActivity.this, "您还未登录，无法获取留言列表", Toast.LENGTH_SHORT).show();
        }
        else
        {
            try
            {
                MessageTask messageTask = new MessageTask(MessageActivity.this);
                messageTask.showProgressDialog("提示", "正在加载留言列表...");
                messageTask.execute();
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private void findViewById()
    {
        ask = (Button) findViewById(R.id.bt_message);
        back = (LinearLayout) findViewById(R.id.message_back);
        message_list = (ListView) findViewById(R.id.message_list);
    }
    
    private class ClickListener implements View.OnClickListener
    {
        
        @Override
        public void onClick(View v)
        {
            
            switch (v.getId())
            {
                case R.id.message_back:
                    MessageActivity.this.finish();
                    break;
                case R.id.bt_message:
                    Intent intent = new Intent(MessageActivity.this, AskActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
    
    class MessageTask extends AsyncNetworkTask<String>
    {
        
        String          title    = null;
        String          content  = null;
        String          feedback = null;
        String          tag      = null;
        private Context context  = null;
        
        public MessageTask(Context context) throws UnsupportedEncodingException
        {
            super(context);
            this.context = context;
        }
        
        public String doNetworkTask()
        {
            WebService ws = WebServiceFactory.getWebService();
            String id = SpUtils.getString(context, SpUtils.ACCOUNT, "-1");
            String path = ws.downloadFeedHisto(id);
            return path;
        }
        
        @Override
        public void handleResult(String json)
        {
            if (json != null && !json.equals(""))
            {
                List<Information> messages = new ArrayList<Information>();
                try
                {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        Information infor = new Information();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        title = jsonObject.getString("title");
                        infor.setTitle(title);
                        content = jsonObject.getString("content");
                        infor.setContent(content);
                        feedback = jsonObject.getString("feedback");
                        if (feedback.equals("null"))
                        {
                            feedback = "未处理";
                        }
                        infor.setFeedback(feedback);
                        messages.add(infor);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                
                Message msg = new Message();
                msg.what = 0;
                msg.obj = messages;
                handler.sendMessage(msg);
            }
        }
    }
    
}
