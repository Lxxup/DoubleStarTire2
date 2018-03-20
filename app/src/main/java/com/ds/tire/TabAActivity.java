package com.ds.tire;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.bluetooth.Constants;
import com.ds.tire.bluetooth.GetDataService;
import com.ds.tire.util.CheckUpdate;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.SpUtils;

public class TabAActivity extends Activity {

    public static String TAG = "homeActivity";
    private PopupWindow pop;
    // 界面控件
    private View home_btn_product;
    private View home_btn_company;
    private View home_menu_faq;
    private View home_menu_rescue;
    private TextView tv_main_set;
    private TextView tv_main_account;
    private TextView tv_main_user_center;
    private TextView tv_main_check_version;
    private TextView tv_main_version;
    private TextView tv_main_setting;
    private View home_btn_promise;
    private View home_btn_period;
    private boolean ifFirstIn = true;

    int location = -1;
    // 监听点击事件
    private OnClickListener clickListener = new MyOnClickListener();

    class MyOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            myOnClick(v);
        }
    }

    private Handler handler = new MyHandler();

    String[] datasources = {"救援订单1", "救援订单2", "救援订单3", "救援订单4", "救援订单5", "救援订单6", "救援订单7", "救援订单8", "救援订单9", "救援订单10", "救援订单11", "救援订单12", "救援订单13", "救援订单14", "救援订单15", "救援订单16", "救援订单17", "救援订单18", "救援订单19", "救援订单20", "救援订单21", "救援订单22", "救援订单23", "救援订单24", "救援订单25", "救援订单26", "救援订单27", "救援订单28", "救援订单29", "救援订单30", "救援订单31", "救援订单32", "救援订单33", "救援订单34", "救援订单35", "救援订单36", "救援订单37", "救援订单38", "救援订单39", "救援订单40", "救援订单41", "救援订单42", "救援订单43", "救援订单44", "救援订单45", "救援订单46", "救援订单47", "救援订单48", "救援订单49", "救援订单50", "救援订单7", "救援订单1", "救援订单2", "救援订单3", "救援订单4", "救援订单5", "救援订单6", "救援订单7", "救援订单1", "救援订单2", "救援订单3", "救援订单4", "救援订单5", "救援订单6", "救援订单7"};
    String[] datasources3 = {"双星产品推广1", "双星产品推广2", "双星产品推广3", "双星产品推广4", "双星产品推广5", "双星产品推广6", "双星产品推广7", "双星产品推广8", "双星产品推广9", "双星产品推广10", "双星产品推广11", "双星产品推广12", "双星产品推广13", "双星产品推广14", "双星产品推广15", "双星产品推广16"};
    String[] datasources2 = {"", "集团简介", "团队介绍", "招聘信息"};

    ListView lst_home;
    TextView tv2_home;
    Layout lst_home_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tirehome_a);
        findViewById();
        init();

        lst_home = (ListView) findViewById(R.id.lst_home);
        tv2_home = (TextView) findViewById(R.id.tv2_home);
        //lst_home_item=(LinearLayout)findViewById(R.layout.lst_home_item);

        ArrayAdapter<String> adapter_home = new ArrayAdapter<String>(
                this,
                /*android.R.layout.simple_list_item_1,
				android.R.id.text1,*/
                R.layout.lst_home_item,
                R.id.tv2_home,
                datasources3
        );
        lst_home.setAdapter(adapter_home);

        lst_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ss = datasources3[i] + "还没有上线";
                Toast.makeText(TabAActivity.this, ss, Toast.LENGTH_SHORT).show();

                Intent intent11 = new Intent(TabAActivity.this, home_good_detail.class);
                intent11.putExtra("extra_data", datasources3[i]);
                startActivity(intent11);


            }
        });


    }

    private void findViewById() {
        // 控件初始化
        home_btn_product = (View) findViewById(R.id.home_btn_product);
        home_btn_company = (View) findViewById(R.id.home_btn_company);
        home_menu_faq = (View) findViewById(R.id.home_menu_faq);
        home_menu_rescue = (View) findViewById(R.id.home_menu_rescue);
        tv_main_set = (TextView) findViewById(R.id.tv_main_set);
        home_btn_promise = (View) findViewById(R.id.home_btn_promise);
        home_btn_period = (View) findViewById(R.id.home_btn_period);

    }

    private void init() {

        home_btn_company.setOnClickListener(clickListener);

        home_btn_product.setOnClickListener(clickListener);
        tv_main_set.setOnClickListener(clickListener);
//		home_btn_rescue.setOnClickListener(clickListener);
        home_btn_promise.setOnClickListener(clickListener);
        home_btn_period.setOnClickListener(clickListener);

    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            location = (Integer) msg.obj;
            switch (msg.what) {
                case 0:
                    SpUtils.setBoolean(TabAActivity.this, SpUtils.AIDFLAGONE,
                            false);
                    DialogUtils.alert(false, TabAActivity.this,
                            R.drawable.dialog_icon, "提示", "是否呼叫救援车辆？", "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    SpUtils.setBoolean(TabAActivity.this,
                                            SpUtils.AIDFLAGONE, true);
                                    if (SpUtils.getString(TabAActivity.this,
                                            SpUtils.ACCOUNT, "").equals("")) {
                                        Toast.makeText(TabAActivity.this,
                                                "请先登录", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(
                                                TabAActivity.this,
                                                OrderDetailActivity.class);
                                        intent.putExtra("weizhi", location);
                                        startActivity(intent);
                                    }
                                }
                            }, "打电话", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    SpUtils.setBoolean(TabAActivity.this,
                                            SpUtils.AIDFLAGONE, true);
                                    Intent phoneIntent = new Intent(
                                            "android.intent.action.CALL", Uri
                                            .parse("tel:" + "400-017-6666"));
                                    startActivity(phoneIntent);
                                }
                            }, "取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    SpUtils.setBoolean(TabAActivity.this,
                                            SpUtils.AIDFLAGONE, true);
                                }
                            });
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    }

    /*
     * 通过包名启动第三方程序
     */
    private void startAppByPackageName(String packageName) {
        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = getPackageManager().queryIntentActivities(
                resolveIntent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String packageName1 = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            ComponentName cn = new ComponentName(packageName1, className);

            intent.setComponent(cn);
            startActivity(intent);
        }

    }

    private void myOnClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.home_btn_company:// 公司介绍
                intent = new Intent(this, CompanyIntroduceActivity.class);
                startActivity(intent);
                break;
            case R.id.home_btn_product:// 产品介绍
                intent = new Intent(this, ProductSeriesActivity.class);
                startActivity(intent);
                break;
//		
            case R.id.home_btn_promise:// 质量承诺
                if (SpUtils.getString(TabAActivity.this,
                        SpUtils.ACCOUNT, "").equals("")) {
                    Toast.makeText(TabAActivity.this,
                            "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(this, Fuwuxieyi_listActivity.class);
                    startActivity(intent);
                }
                break;

//			
            case R.id.home_btn_period:// 生命周期
                Intent intent111 = new Intent(TabAActivity.this, test_class.class);
                startActivity(intent111);
                break;
            case R.id.tv_main_set:
                popSetting(tv_main_set);
                break;
            default:
                break;
        }
    }

    private void popSetting(TextView parent) {
        View v = TabAActivity.this.getLayoutInflater().inflate(
                R.layout.pop_setting, null);
        tv_main_account = (TextView) v.findViewById(R.id.tv_main_account);
        tv_main_user_center = (TextView) v
                .findViewById(R.id.tv_main_user_center);
        tv_main_check_version = (TextView) v
                .findViewById(R.id.tv_main_check_version);
        tv_main_version = (TextView) v.findViewById(R.id.tv_main_version);
        tv_main_setting = (TextView) v.findViewById(R.id.tv_main_setting);
        if (SpUtils.getString(TabAActivity.this, SpUtils.ACCOUNT,
                "").equals("")) {
            tv_main_account.setText("登录");
        } else {
            tv_main_account.setText("注销");

        }


        popClick();

        pop = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        pop.setBackgroundDrawable(new PaintDrawable(android.R.color.transparent));
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.showAsDropDown(parent, Gravity.RIGHT, 0);

    }

    private void popClick() {
        tv_main_account.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SpUtils.getString(TabAActivity.this, SpUtils.ACCOUNT,
                        "").equals("")) {
                    Intent intent = new Intent();
                    intent = new Intent(TabAActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {

                    DialogUtils.alert(true, TabAActivity.this,
                            R.drawable.dialog_icon, "提示", "已登录，是否注销", "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    SpUtils.setString(TabAActivity.this,
                                            SpUtils.ACCOUNT, "");
                                    SpUtils.setInteger(TabAActivity.this,
                                            SpUtils.WHEELS_COUNT, 4);
                                    Intent i = new Intent(
                                            TabAActivity.this,
                                            LoginActivity.class);
                                    startActivity(i);
                                }
                            }, "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            }, null, null);
                }
            }
        });
        tv_main_user_center.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SpUtils.getString(TabAActivity.this, SpUtils.ACCOUNT,
                        "").equals("")) {
                    Toast.makeText(TabAActivity.this, "请先登录",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent = new Intent(TabAActivity.this,
                            SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            tv_main_version.setText("当前版本：\n" + info.versionName);
            SpUtils.setString(TabAActivity.this, "version", info.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_main_check_version.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckUpdate check = new CheckUpdate(TabAActivity.this);
                check.checkUpdate();
            }
        });
        tv_main_setting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(TabAActivity.this, "您点击了设置", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确认退出应用？")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    stopService(new Intent(
                                            Constants.ACTION_BLUETOOTH_SERVICE));
                                    GetDataService.mDevice = null;
                                    finish();
                                }
                            }).show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
