package com.ds.tire.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ds.tire.R;

public class SafeAdapter extends BaseAdapter
{
    private Context      context;
    private List<String> title   = new ArrayList<String>();
    private List<String> content = new ArrayList<String>();
    
    public SafeAdapter(Context context, List<String> list, List<String> list2)
    {
        super();
        this.context = context;
        this.title = list;
        this.content = list2;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return title.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return title.get(position);
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
        // TODO Auto-generated method stub
        String str1 = title.get(position);
        String str2 = content.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.safe_item, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(str1);
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText(str2);
        return view;
    }
    
}
