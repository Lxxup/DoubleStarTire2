package com.ds.tire.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ds.tire.R;

public class MessageAdapter extends BaseAdapter
{
    
    private List<Information> infor;
    private Context           context;
    
    public MessageAdapter(Context context, List<Information> infor)
    {
        super();
        this.infor = infor;
        this.context = context;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return infor.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return infor.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String str = infor.get(position).getTitle();
        String str1 = infor.get(position).getContent();
        String str2 = infor.get(position).getFeedback();
        View view = LayoutInflater.from(context).inflate(R.layout.message_item, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView feedback = (TextView) view.findViewById(R.id.feedback);
        title.setText("标题：" + str);
        content.setText("内容：" + str1);
        feedback.setText("反馈：" + str2);
        return view;
    }
    
}
