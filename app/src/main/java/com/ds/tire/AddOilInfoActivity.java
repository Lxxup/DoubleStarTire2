package com.ds.tire;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ds.tire.addoil.AddOil;
import com.ds.tire.addoil.CalendarView;
import com.ds.tire.addoil.Cell;
import com.ds.tire.addoil.DBOperate;
import com.ds.tire.addoil.Oil_AVG;
import com.ds.tire.addoil.TimeUtils;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

/**
 * 查看油耗信息界面
 * 
 * @author 李小敏
 * 
 */
public class AddOilInfoActivity extends Activity implements OnClickListener, CalendarView.OnCellTouchListener
{
    private String       TAG   = "AddOilInfoActivity";
    /***************************************** 相关布局的控件声明 ************************************/
    private View         back, add;                    // 顶部的返回、添加加油信息按钮
    private View         up, down;                     // 日历的上一月、下一月按钮
    private TextView     dateTV;                       // 日历的显示当前日期
    private CalendarView mView = null;                 // 日历控件相关
    // 加油详情相关变量
    private LinearLayout detailLayout;
    private TextView     stationTV, ammountTV, mileageTV, isFullTV;
    // 油耗相关的控件
    private TextView     oilAvg;
    /***************************************** 相关布局的控件声明 ************************************/
    
    private DBOperate    db    = null;
    private List<AddOil> list;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_oil_info);
        initViews();
        db = new DBOperate(this);
        list = new ArrayList<AddOil>();
        db.open();
        if (db.getRecordNum() == 0)
        {
            getOil task = new getOil(this);
            task.execute();
        }
        db.close();
    }
    
    class getOil extends AsyncNetworkTask<String>
    {
        String userId, carId;
        
        public getOil(Context context)
        {
            super(context);
            userId = SpUtils.getString(context, SpUtils.ACCOUNT, "");
        }
        
        @Override
        protected String doNetworkTask()
        {
            WebService ws = WebServiceFactory.getWebService();
            return ws.getOil(userId);
        }
        
        @Override
        protected void handleResult(String result)
        {
            if (result != null && !result.equals(""))
            {
                insertRecordToDB(result);
                refreshData(0);
                getOilAvg();
            }
            
        }
        
    }
    
    void initViews()
    {
        dateTV = (TextView) findViewById(R.id.selected_date);
        up = findViewById(R.id.up);
        down = findViewById(R.id.down);
        back = findViewById(R.id.back);
        add = findViewById(R.id.add);
        
        up.setOnClickListener(this);
        down.setOnClickListener(this);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        
        mView = (CalendarView) findViewById(R.id.calendar);
        mView.setOnCellTouchListener(this);
        
        detailLayout = (LinearLayout) findViewById(R.id.detail_layout);
        stationTV = (TextView) findViewById(R.id.station);
        ammountTV = (TextView) findViewById(R.id.oil_mount);
        mileageTV = (TextView) findViewById(R.id.mileage);
        isFullTV = (TextView) findViewById(R.id.is_full);
        
        oilAvg = (TextView) findViewById(R.id.oil_avg);
        
    }
    
    /**
     * 函数功能：刷新页面的显示
     * 
     * @param dx
     *            dx=0 更新当前月的加油信息 || dx<0显示上一月加油信息 || dx>0显示下一月的加油信息
     *             @author 李小敏  @version 2013-11-21 上午11:24:37 
     */
    void refreshData(int dx)
    {
        detailLayout.setVisibility(View.GONE);
        List<Integer> days = new ArrayList<Integer>();
        String res = null;
        if (dx > 0)
        {
            mView.nextMonth();
        }
        else if (dx < 0)
        {
            mView.previousMonth();
        }
        Calendar c = Calendar.getInstance();
        c.set(mView.getYear(), mView.getMonth() - 1, 1);
        db.open();
        res = db.getAddOilInfo(TimeUtils.formatTime(mView.getYear() - 1900, mView.getMonth(), c.getActualMinimum(Calendar.DAY_OF_MONTH)), TimeUtils.formatTime(mView.getYear() - 1900, mView.getMonth(), c.getActualMaximum(Calendar.DAY_OF_MONTH)));
        db.close();
        days = getAddOilDays(res);
        mView.addHightLightDay(days);
        String date = formatTime(mView.getYear(), mView.getMonth());
        dateTV.setText(date);
        
    }
    
    /**
     * 函数功能：获取的网络数据存储至本地数据库中  @author 李小敏  @version 2013-11-21 上午11:13:00 
     */
    private void insertRecordToDB(String jsonArray)
    {
        if (jsonArray != null && !jsonArray.equals(""))
        {
            JSONArray array;
            JSONObject obj;
            String station, mileage, ammount, addTime;
            int oilType, isFull;
            Log.d("TAG", "jsonArray:" + jsonArray);
            try
            {
                db.open();
                array = new JSONArray(jsonArray);
                for (int i = 0; i < array.length(); i++)
                {
                    obj = array.getJSONObject(i);
                    oilType = obj.getInt("oil_species");
                    ammount = obj.getString("amount");
                    station = obj.getString("station_name");
                    isFull = obj.getInt("is_fill");
                    mileage = obj.getString("mileage");
                    addTime = obj.getString("datetime");
                    if (addTime.length() > 10)
                    {
                        addTime = TimeUtils.timeToDate(addTime);
                    }
                    Long res = db.insertAddOilInfo(addTime, mileage, oilType, ammount, station, isFull);
                    
                }
                
            }
            catch (JSONException e)
            {
                Log.d(TAG, "error:" + e.getMessage());
                e.printStackTrace();
            }
            finally
            {
                db.close();
            }
        }
    }
    
    /**
     * 函数功能：从数据库获取的信息(json格式)转化为list以及获取加油对应的天数  @author 李小敏
     *  @version 2013-11-21 上午11:14:21 
     */
    private List<Integer> getAddOilDays(String jsonArray)
    {
        List<Integer> addOils = new ArrayList<Integer>();
        if (jsonArray != null && !jsonArray.equals(""))
        {
            JSONArray array;
            JSONObject obj;
            String station, mileage, ammount, addTime;
            int oilType, isFull;
            list.clear();
            Log.d("TAG", "jsonArray:" + jsonArray);
            try
            {
                array = new JSONArray(jsonArray);
                for (int i = 0; i < array.length(); i++)
                {
                    obj = array.getJSONObject(i);
                    station = obj.getString("station_name");
                    mileage = obj.getString("mileage");
                    ammount = obj.getString("amount");
                    addTime = obj.getString("datetime");
                    if (addTime.length() > 10)
                    {
                        addTime = TimeUtils.timeToDate(addTime);
                    }
                    Log.d(TAG, addTime);
                    oilType = obj.getInt("oil_species");
                    isFull = obj.getInt("if_fill");
                    list.add(new AddOil(station, mileage, ammount, addTime, oilType, isFull));
                    addOils.add(TimeUtils.getDate(addTime).get(Calendar.DAY_OF_MONTH));
                }
                
            }
            catch (JSONException e)
            {
                Log.d(TAG, "error:" + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return addOils;
    }
    
    /**
     * 函数功能：将日期拼接成2013-11格式  @author 李小敏  @version 2013-11-21 上午11:17:34 
     */
    @SuppressWarnings("deprecation")
    private String formatTime(int year, int month)
    {
        // c.add(Calendar.MONTH, dx);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(year - 1900, month, 1);
        return format.format(date);
    }
    
    /**
     * 函数功能：计算平均油耗  @author 李小敏  @version 2013-11-21 下午02:44:06 
     */
    private void getOilAvg()
    {
        Oil_AVG avg = null;
        db.open();
        avg = db.getOilAvg();
        db.close();
        if (avg == null)
        {
            oilAvg.setText("加油信息太少，暂时无法计算平均油耗");
        }
        else
        {
            oilAvg.setText(String.format("%.1f", avg.getOil_avg() * 100) + "升每百公里");
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                Intent intent = new Intent(this, AddOilActivity.class);
                startActivity(intent);
                break;
            case R.id.up:
                refreshData(-1);
                break;
            case R.id.down:
                refreshData(1);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onTouch(Cell cell)
    {
        if (cell.mPaint.getColor() == Color.GRAY)
        {
            refreshData(-1);
        }
        else if (cell.mPaint.getColor() == Color.LTGRAY)
        {
            refreshData(1);
        }
        else
        {
            Rect ecBounds = cell.getBound();
            String slectDay = TimeUtils.formatTime(mView.getYear() - 1900, mView.getMonth(), cell.getDayOfMonth());
            Log.d(TAG, "selected date:" + slectDay);
            if (cell.isHighlighted())
            {
                detailLayout.setVisibility(View.VISIBLE);
                for (AddOil oil : list)
                {
                    if (oil.getAddTime().equals(slectDay))
                    {
                        stationTV.setText(oil.getStation());
                        ammountTV.setText(oil.getAmmount());
                        mileageTV.setText(oil.getMileage());
                        if (oil.getIsFull() == 0)
                        {
                            isFullTV.setText("否");
                        }
                        else
                        {
                            isFullTV.setText("是");
                        }
                        break;
                    }
                }
            }
            else
            {
                detailLayout.setVisibility(View.GONE);
            }
            slectDay = null;
            mView.mDecoraClick.setBounds(ecBounds);
            mView.invalidate();
            
        }
    }
    
    @Override
    protected void onResume()
    {
        refreshData(0);
        getOilAvg();
        super.onResume();
    }
    
}
