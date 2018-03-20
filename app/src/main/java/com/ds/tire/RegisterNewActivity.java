package com.ds.tire;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.Http;
import com.ds.tire.util.MyYzm;
import com.ds.tire.util.SpUtils;

public class RegisterNewActivity extends Activity {

    private String TAG = "TAG";
    private Button bt_register = null;// 注册按钮
    private Button bt_getvm = null;// 获取验证码按钮
    private TextView bt_shu = null;//
    private EditText et_register_phone = null;// 手机输入框
    private EditText et_vvnumb = null;// 验证码输入框
    protected static final int CN = -1;
    private boolean isCountdown = false;// 获取短信验证码的按钮，是否可以点击
    private TimeCount time;// 倒计时
    private String phone;
    private String verify_code = "";
    private TextView login_back_matchphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registernew);
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        findViewById();
        login_back_matchphone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterNewActivity.this.finish();
                /*Intent intent_backlogin=new Intent(RegisterNewActivity.this,LoginActivity.class);
				startActivity(intent_backlogin);*/
            }
        });

        bt_getvm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                phone = et_register_phone.getText().toString().trim();
                Pattern p = Pattern.compile("1[3|5|7|8|][0-9]{9}");
                Matcher m = p.matcher(phone);

                if (et_register_phone.getText().toString().trim().length() == 11 && m.find()) {
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            phone = et_register_phone.getText().toString();// 得到电话号码

                            verify_code = MyYzm.createRandom(true, 4);// 获得随机验证码
                            String content = "尊敬的用户，您本次的短信验证码为 :" + verify_code;// 得到短信内容

                            Log.i("TAG", "要发送的短信内容为：" + content);
                            String pstContent = createSubmitXml(phone, content);//
                            Log.i("TAG", "xml下的打印输出" + pstContent);
                            String result = Http.post(pstContent);
                            Log.i("TAG", "验证码xxx   " + result);
                            time.start();
                            super.run();
                        }
                    };
                    t.start();
                } else {


                    if (phone == null || (phone = phone.trim()).length() == 0) {
                        Toast.makeText(RegisterNewActivity.this, "手机号码不能为空！",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (phone.length() != numberlength(phone)) {
                            Toast.makeText(RegisterNewActivity.this, "手机号码只能是数字！",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if ((phone = phone.trim()).length() != 11) {
                                Toast.makeText(RegisterNewActivity.this, "手机号码必须为11位！",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                if (!m.find()) {
                                    Toast.makeText(RegisterNewActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {

                                }
                            }
                        }
                    }
                }
            }
        });
        bt_register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String phone = et_register_phone.getText().toString().trim();
                String vvnumb = et_vvnumb.getText().toString().trim();
                Log.d(TAG, "验证码" + et_vvnumb.getText().toString()

                        + et_vvnumb.getText().toString().length());
                register(phone, vvnumb);

            }
        });

    }

    private void findViewById() {
        login_back_matchphone = (TextView) findViewById(R.id.login_back_matchphone);
        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        et_vvnumb = (EditText) findViewById(R.id.et_vvnumb);
        bt_getvm = (Button) findViewById(R.id.bt_getvm);
        bt_register = (Button) findViewById(R.id.bt_register);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    ;

    @Override
    protected void onStop() {
        super.onStop();

    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bt_getvm.setClickable(false);
            bt_getvm.setTextColor(getResources().getColor(R.color.white));

            bt_getvm.setText("(" + millisUntilFinished / 1000 + ")" + "重新获取");
        }

        @Override
        public void onFinish() {
            bt_getvm.setText("重新获取");
            bt_getvm.setTextColor(getResources().getColor(R.color.white));
            bt_getvm.setClickable(true);

        }
    }

    private void register(String phone, String yzm) {
        Log.i("TAG", "注册时填写的电话号码：" + phone);
        try {
            Pattern p = Pattern.compile("1[3|5|7|8|][0-9]{9}");
            Matcher m = p.matcher(phone);
            if (!m.find()) {
                Toast.makeText(RegisterNewActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone == null || (phone = phone.trim()).length() == 0) {
                Toast.makeText(RegisterNewActivity.this, "手机号码不能为空！",
                        Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (phone.length() != numberlength(phone)) {
                    Toast.makeText(RegisterNewActivity.this, "手机号码只能是数字！",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if ((phone = phone.trim()).length() != 11) {
                        Toast.makeText(RegisterNewActivity.this, "手机号码必须为11位！",
                                Toast.LENGTH_SHORT).show();

                        return;
                    } else {
                        if (yzm == null || (yzm = yzm.trim()).length() == 0) {
                            Toast.makeText(RegisterNewActivity.this, "请输入验证码！",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            // RegisterTask registerTask = new RegisterTask(
                            // RegisterNewActivity.this, phone, yzm);
                            // registerTask.execute();
                            if (yzm.trim().equals(verify_code)) {

                                Intent intent = new Intent();
                                intent.setClass(RegisterNewActivity.this, RegisterListActivity.class);
                                intent.putExtra("phone", phone);
                                startActivity(intent);

                                Log.i("TAG", "接入的电话号码: " + phone);
                                finish();
                                // DialogUtils.alert(RegisterNewActivity.this,
                                // R.drawable.logo, "温馨提示", "注册吧",
                                // "确定", null);
                                // return;//    改成挑战页面
                            } else {
                                DialogUtils.alert(RegisterNewActivity.this,
                                        R.drawable.logo, "温馨提示",
                                        "您的验证码有误，请重新输入~", "确定", null);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void yzm(String phone) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int stringlength(String chas) {
        String regEx = "[a-zA-Z0-9_]";
        String s = chas;
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(s);
        int stringCount = 0;
        while (mat.find()) {
            stringCount++;
        }
        return stringCount;
    }

    public int numberlength(String chas) {
        String regEx = "\\d";
        String s = chas;
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(s);
        int numberCount = 0;
        while (mat.find()) {
            numberCount++;
        }
        return numberCount;
    }

    class yzmTask extends AsyncNetworkTask<String> {

        private String phone;

        public yzmTask(Context context, String phone)
                throws UnsupportedEncodingException {
            super(context);
            this.phone = phone;
        }

        @Override
        public void handleResult(String result) {
            if (result != null) {
                if (result.equals("00")) {
                    Toast.makeText(RegisterNewActivity.this, "验证码已发送，请注意查收~",
                            Toast.LENGTH_SHORT).show();
                } else if (result.equals("-1")) {
                    DialogUtils.alert(RegisterNewActivity.this,
                            R.drawable.logo, "温馨提示", "您输入的手机号已经注册过啦，可以直接登录~",
                            "确定", null);
                    new OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    RegisterNewActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub

                        }
                    };

                } else {
                    DialogUtils.alert(RegisterNewActivity.this,
                            R.drawable.logo, "温馨提示", "获取验证码失败，请再试一次吧~", "确定",
                            null);
                    time.start();// 开始计时
                    // bt_getvm.setClickable(true);
                }
            } else {
                DialogUtils.alert(RegisterNewActivity.this, R.drawable.logo,
                        "温馨提示", "请检查网络设置~", "确定", null);
                time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
            }

        }

        @Override
        protected String doNetworkTask() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    // 密码采用md5加密
    public static String createSubmitXml(String phone, String content) {
        Date now = new Date();
        // 2008-6-16 20:54:53
        DateFormat dtFormat = DateFormat.getDateInstance();
        // String sendtime = dtFormat.format(now);

        StringBuffer sp = new StringBuffer();
        sp.append(String
                .format("<Group Login_Name=\"%s\" Login_Pwd=\"%s\" InterFaceID=\"0\" OpKind=\"0\">",
                        "shuangxinglt", MD5("shuangxing123")));
        // sp.append(String.format("<E_Time>%s</E_Time>", sendtime));
        sp.append(String.format("<E_Time>%s</E_Time>", ""));
        sp.append("<Item>");

        sp.append("<Task>");
        sp.append("<Recive_Phone_Number>" + phone + "</Recive_Phone_Number>");
        sp.append("<Content><![CDATA[" + content + "]]></Content>");
        sp.append("<Search_ID>1</Search_ID>");
        sp.append("</Task>");

		/*
		 * sp.append("<Task>");
		 * sp.append("<Recive_Phone_Number>18059135112</Recive_Phone_Number>");
		 * sp.append("<Content><![CDATA[测试短信1002]]></Content>");
		 * sp.append("<Search_ID>1</Search_ID>"); sp.append("</Task>");
		 */

        sp.append("</Item>");
        sp.append("</Group>");

        return sp.toString();
    }

    public static String MD5(String encryptContent) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(encryptContent.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            result = buf.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {

        }
        return result;
    }
}
