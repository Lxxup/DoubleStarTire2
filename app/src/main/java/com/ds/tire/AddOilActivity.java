package com.ds.tire;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ds.tire.addoil.DBOperate;
import com.ds.tire.addoil.TimeUtils;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

/**
 * 添加加油信息界面
 * 
 * @author 李小敏
 * 
 */
public class AddOilActivity extends Activity implements OnClickListener
{
    private View             back, submit;
    private EditText         stationET, oilAmountET, oilTypeET, milageET,
            timeET;
    private RadioGroup       fullGroup;
    private RadioButton      yes, no;
    private Resources        resources;
    private DatePickerDialog timeDialog;
    private String           userId = "", station = "", oilAmount = "",
            oilType = "", milage = "", time = "";
    private int              type   = -1, isFull = 1;
    private Builder          toast;
    private Calendar         c;
    private final String     TAG    = "TAG";
    private DBOperate        db     = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_oil);
        db = new DBOperate(this);
        userId = SpUtils.getString(this, SpUtils.ACCOUNT, "");
        resources = getResources();
        initViews();
    }
    
    @SuppressWarnings("deprecation")
    private void initViews()
    {
        back = findViewById(R.id.back);
        stationET = (EditText) findViewById(R.id.station);
        oilAmountET = (EditText) findViewById(R.id.oil_mount);
        oilTypeET = (EditText) findViewById(R.id.oil_type);
        milageET = (EditText) findViewById(R.id.mileage);
        timeET = (EditText) findViewById(R.id.time);
        fullGroup = (RadioGroup) findViewById(R.id.radiogroup);
        yes = (RadioButton) findViewById(R.id.yes);
        no = (RadioButton) findViewById(R.id.no);
        submit = findViewById(R.id.submit);
        back.setOnClickListener(this);
        oilTypeET.setOnClickListener(this);
        timeET.setOnClickListener(this);
        submit.setOnClickListener(this);
        c = Calendar.getInstance();
        time = TimeUtils.formatTime(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        timeET.setText(time);
        fullGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.yes:
                        isFull = 1;
                        break;
                    case R.id.no:
                        isFull = 0;
                        break;
                    default:
                        break;
                }
                
            }
        });
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.oil_type:
                showOilType(resources.getStringArray(R.array.oil_type), oilTypeET);
                break;
            case R.id.time:
                showTime(timeET);
                break;
            case R.id.submit:
                submmit();
                break;
            default:
                break;
        }
        
    }
    
    private void showTime(final EditText showET)
    {
        timeDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                if (calendar.after(c))
                {
                    showToast("选择日期无效！");
                }
                else
                {
                    time = TimeUtils.formatTime(year - 1900, monthOfYear, dayOfMonth);
                    showET.setText(time);
                }
            }
        }, c.get(Calendar.YEAR), // 传入年份
        c.get(Calendar.MONTH), // 传入月份
        c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        if (timeDialog.isShowing())
        {
            timeDialog.dismiss();
        }
        else
        {
            timeDialog.show();
        }
    }
    
    private void submmit()
    {
        station = stationET.getEditableText().toString();
        oilAmount = oilAmountET.getEditableText().toString();
        oilType = oilTypeET.getEditableText().toString();
        time = timeET.getEditableText().toString();
        milage = milageET.getEditableText().toString();
        if (oilAmount == null || oilAmount.trim().equals(""))
        {
            showToast("请填写加油量！");
            return;
        }
        if (oilType == null || oilType.trim().equals(""))
        {
            showToast("请填写加油种类！");
            return;
        }
        if (time == null || time.trim().equals(""))
        {
            showToast("请填写加油时间！");
            return;
        }
        if (milage == null || milage.trim().equals(""))
        {
            showToast("请填写里程！");
            return;
        }
        if (isFull == -1)
        {
            showToast("请填写是否加满！");
            return;
        }
        try
        {
            oilAmount = String.format("%.1f", Double.parseDouble(oilAmount));
        }
        catch (Exception e)
        {
            showToast("加油量输入错误！");
            return;
        }
        try
        {
            Integer.valueOf(milage);
        }
        catch (Exception e)
        {
            showToast("里程输入错误！");
            return;
        }
        
        db.open();
        db.insertAddOilInfo(time, milage, type - 1, oilAmount, station, isFull);
        db.close();
        AddOilTask addOilTask = new AddOilTask(this);
        addOilTask.showProgressDialog("提示", "正在处理数据...");
        addOilTask.execute();
    }
    
    class AddOilTask extends AsyncNetworkTask<String>
    {
        
        public AddOilTask(Context context)
        {
            super(context);
        }
        
        @Override
        protected String doNetworkTask()
        {
            WebService ws = WebServiceFactory.getWebService();
            return ws.addoil(userId, type - 1, oilAmount, "海大", isFull, milage, time);
            
        }
        
        @Override
        protected void handleResult(String result)
        {
            if (result != null && !result.equals(""))
            {
                int flag = Integer.parseInt(result);
                switch (flag)
                {
                    case 1:
                        DialogUtils.alert(AddOilActivity.this, android.R.drawable.ic_dialog_info, R.string.alert, "信息提交成功！", R.string.ok, 
                        		new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										AddOilActivity.this.finish();
									}
						});
                        break;
                    
                    default:
                        DialogUtils.alert(AddOilActivity.this, android.R.drawable.ic_dialog_alert, R.string.alert, "信息提交失败！", R.string.ok, null);
                        break;
                }
            }
        }
    }
    
    private void showToast(String msg)
    {
        DialogUtils.alert(this, android.R.drawable.ic_dialog_info, R.string.alert, msg, R.string.ok, null);
    }
    
    private void showOilType(final String[] items, final EditText parent)
    {
        type = -1;
        oilType = "";
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("选择油种类").setIcon(android.R.drawable.ic_dialog_info).setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener()
        {
            
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                type = which;
            }
            
        }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (type != -1)
                {
                    oilType = items[type];
                }
                parent.setText(oilType);
            }
        }).create();
        if (dialog.isShowing())
        {
            dialog.dismiss();
        }
        else
        {
            
            dialog.show();
        }
        
    }
}
