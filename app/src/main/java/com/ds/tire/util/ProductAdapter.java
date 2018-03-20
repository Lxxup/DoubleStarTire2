package com.ds.tire.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ds.tire.R;

public class ProductAdapter extends BaseAdapter
{
    private Context        context;
    private List<String>   title = new ArrayList<String>();
    private List<Drawable> image = new ArrayList<Drawable>();
    
    public ProductAdapter(Context context, List<String> title, List<Drawable> image)
    {
        super();
        this.context = context;
        this.title = title;
        this.image = image;
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
        Drawable str2 = image.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, null);
        TextView title = (TextView) view.findViewById(R.id.producttitle);
        title.setText(str1);
        ImageView image = (ImageView) view.findViewById(R.id.productimage);
        image.setBackgroundDrawable(str2);
        return view;
    }
    
}
