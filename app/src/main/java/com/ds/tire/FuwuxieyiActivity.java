package com.ds.tire;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.handwriting.DialogListener;
import cn.handwriting.WritePadDialog;

import com.ds.tire.Fuwuxieyi_read.SelectUserInfo;
import com.ds.tire.bean.Fuwuxieyitaihao;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.Constant;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

public class FuwuxieyiActivity extends Activity
{
	private String TAG="TAG";
	private Button bt_qianzi;
	private LinearLayout xieyi_back;
	private Button xieyi_next;
	private ImageView signIV;
	private TextView tv_account;
	private TextView tv_username;
	private TextView tv_address = null;
	private EditText et_platenum = null;
	private EditText et_mileage = null;
	private TextView tv_buytime = null;
//	private TextView tv_tirecode = null;
//	private TextView tv_chexing = null;
	private ListView ls_taihaochexing;
	private List<Fuwuxieyitaihao> fuwuxieyitaihao;
	private String signPath;
	private String uid = "uid";
//	private String cid = "cid";
	private String oid = "oid";

//	private int flag;
//	private int mYear;
//	private int mMonth;
//	private int mDay;
	Bitmap mSignBitmap;
	 private final int DATE_DIALOG = 1;
//	private int count;
//	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fuwuxieyi);
		uid=SpUtils.getString(this, Constant.UID, "uid");
		Log.d(TAG,"UID"+ uid);
//		cid=SpUtils.getString(this, Constant.CID, "cid");
//		Log.d(TAG,"CID"+ cid);
		oid=SpUtils.getString(this, Constant.OID, "oid");
		Log.d(TAG,"OID"+ oid);

		
//		final Calendar c = Calendar.getInstance();
//		mYear = c.get(Calendar.YEAR); //获取当前年份
//		mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
//		mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码 
		
		bt_qianzi = (Button) findViewById(R.id.bt_qianzi);
		xieyi_back = (LinearLayout) findViewById(R.id.xieyi_back);
		xieyi_next = (Button) findViewById(R.id.xieyi_next);
		tv_username = (TextView) findViewById(R.id.username);
		tv_account = (TextView) findViewById(R.id.account);
		tv_address = (TextView) findViewById(R.id.tv_address);
//		tv_chexing = (TextView) findViewById(R.id.tv_chexing);
		et_platenum = (EditText) findViewById(R.id.et_platenum);
		et_mileage = (EditText) findViewById(R.id.et_mileage); 
		tv_buytime = (TextView) findViewById(R.id.tv_buytime);
//		tv_tirecode = (TextView) findViewById(R.id.tv_tirecode);
		signIV = (ImageView) findViewById(R.id.qianzi_view);
		fuwuxieyitaihao = new ArrayList<Fuwuxieyitaihao>();
		ls_taihaochexing = (ListView) findViewById(R.id.ls_taihaochexing);
		
		
		
		try {                                         //获取注册数据
			getCardInfo getcardinfo = new getCardInfo(
					FuwuxieyiActivity.this, oid);
			getcardinfo.execute();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		bt_qianzi.setOnClickListener(signListener);
		xieyi_back.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				finish();
			}
		});

		xieyi_next.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				
				String uid = SpUtils.getString(FuwuxieyiActivity.this, SpUtils.ACCOUNT, null);
				uploadImg(uid,oid);
				
				
			}
		});
		
	}

////	获取日期时间
//	protected Dialog onCreateDialog(int id) { 
//        //用来获取日期和时间的 
//        Calendar calendar = Calendar.getInstance();  
//        
//        Dialog dialog = null; 
//        switch(id) { 
//            case DATE_DIALOG: 
//                DatePickerDialog.OnDateSetListener dateListener =  
//                    new DatePickerDialog.OnDateSetListener() { 
//                        @Override 
//                        
//                        public void onDateSet(DatePicker datePicker,  
//                                int year, int month, int dayOfMonth) { 
//                        	
//                        	EditText editText =  
//                                    (EditText) findViewById(R.id.et_buytime);
////                        	日期判断
//                        	if(year<mYear){ //设置年小于当前年，直接设置，不用判断下面的 
//                        		  //Calendar月份是从0开始,所以month要加1 
//                                editText.setText(year + "年" +  
//                                        (month+1) + "月" + dayOfMonth + "日");  
//                                flag = 1; 
//                            }else if(year == mYear){   //设置年等于当前年，则向下开始判断月 
//                                if((month+1) < mMonth){ //设置月小于当前月，直接设置，不用判断下面的 
//                                    flag = 1; 
//                                    editText.setText(year + "年" +  
//                          (month+1) + "月" + dayOfMonth + "日"); 
//                                }else if((month+1) == mMonth){     //设置月等于当前月，则向下开始判断日 
//                                    if(dayOfMonth < mDay){          //设置日小于当前日，直接设置，不用判断下面的 
//                                        flag = 1; 
//                                        editText.setText(year + "年" +  
//                          (month+1) + "月" + dayOfMonth + "日"); 
//                                    }else if(dayOfMonth == mDay){  //设置日等于当前日，则向下开始判断时 
//                                        flag = 2; 
//                                        editText.setText(year + "年" +  
//                          (month+1) + "月" + dayOfMonth + "日"); 
//                                    }else{     //设置日大于当前日，提示重新设置 
//                                        flag = 3; 
//                                        Toast.makeText(FuwuxieyiActivity.this, "当前日不能大于今日,请重新设置", 2000).show(); 
//                                    } 
//                                }else{         //设置月大于当前月，提示重新设置 
//                                    flag = 3; 
//                                    Toast.makeText(FuwuxieyiActivity.this, "当前月不能大于今月，请重新设置", 2000).show(); 
//                                } 
//                            }else{             //设置年大于当前年，提示重新设置 
//                                flag = 3; 
//                                Toast.makeText(FuwuxieyiActivity.this, "当前年不能大于今年，请重新设置", 2000).show(); 
//                            } 
//                             
//                            if(flag == 3){ 
//                                datePicker.init(mYear, (mMonth-1), mDay, new DatePicker.OnDateChangedListener() { 
//                                        
//                                       @Override 
//                                       public void onDateChanged(DatePicker view, int year, int monthOfYear, 
//                                               int dayOfMonth) { 
//                                    	   EditText editText =  
//                                                   (EditText) findViewById(R.id.et_buytime);
//                                    	   //Calendar月份是从0开始,所以month要加1 
//                                           editText.setText(year + "年" +  
//                                                   (monthOfYear+1) + "月" + dayOfMonth + "日"); 
//                                       } 
//                                   }); 
//                            } 
//                           }  
//                       };
//                             
//                           
//                        
//                    dialog = new DatePickerDialog(this, dateListener, mYear, (mMonth-1), mDay);  
//                    break; 
//           
//            default: 
//                break; 
//        } 
//        return dialog; 
//    }
//	 private class BtnOnClickListener implements View.OnClickListener { 
//         
//	        private int dialogId = 0;   //默认为0则不显示对话框 
//	 
//	        public BtnOnClickListener(int dialogId) { 
//	            this.dialogId = dialogId; 
//	        } 
//	        @Override 
//	        public void onClick(View view) { 
//	            showDialog(dialogId); 
//	        } 
//	         
//	    } 
	
    
    //上传数据
	private void register(String oid, String platenum, String mileage) {
		try {
						
			RegisterTask registerTask = new RegisterTask(
					FuwuxieyiActivity.this, oid,  platenum,	mileage);
			registerTask.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class RegisterTask extends AsyncNetworkTask<String> {

		private String oid;
		private String platenum;
		private String mileage;

		public RegisterTask(Context context,String oid, String platenum, String mileage)
				throws UnsupportedEncodingException {
			super(context);
			
			this.oid = oid;
			this.platenum = platenum;
			this.mileage = mileage;

		}

		
		
		public String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.UpdateUserInfo(oid, platenum, mileage);
		}

		@Override
		public void handleResult(String result) {
			if (result != null && !result.equals("")) {
				Log.d("TAG上传车牌号和里程数",result);
				if (result.equals("0")) {
					Toast.makeText(FuwuxieyiActivity.this, "上传失败",
							Toast.LENGTH_SHORT).show();
				
				} 
				else {
					Toast.makeText(FuwuxieyiActivity.this, "信息确认后请签名",
							Toast.LENGTH_SHORT).show();
					
				}
				
			} else {
				Toast.makeText(FuwuxieyiActivity.this, "服务器异常",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	//	姓名  电话 地址等信息下载
	class getCardInfo extends AsyncNetworkTask<String> {
			
			private String oid;
	
			public 	getCardInfo(Context context, String oid) {
				super(context);
				this.oid = oid;
				
			}
			
			
			
			@Override
			protected String doNetworkTask() {
				WebService ws = WebServiceFactory.getWebService();
				return ws.getCardInfo(oid);
			}
	
			@Override
			protected void handleResult(String result) {
				if(!TextUtils.isEmpty(result)){
					Log.i("TAG", result);
					try {
						
						JSONArray jsonArray = new JSONArray(result);
						JSONObject jsonObject = jsonArray.getJSONObject(0);
						String username = jsonObject.getString("uName");
						String account = jsonObject.getString("uTel");
						String address = jsonObject.getString("uAddress");
						String buytime = jsonObject.getString("buyTime");
//						String platenum = jsonObject.getString("plateNum");
//						String mileage = jsonObject.getString("mileage");
						
						tv_username.setText(username);
						tv_account.setText(account);
						tv_address.setText(address);
//						tv_platenum.setText(platenum);
//						tv_mileage.setText(mileage);
						tv_buytime.setText(buytime);
						
						for(int i = 1; i < jsonArray.length(); i++) {
							
							jsonObject = jsonArray.getJSONObject(i);
							String taihao = jsonObject.getString("taihao");
							String chexing = jsonObject.getString("chexing");
							
							fuwuxieyitaihao.add(new Fuwuxieyitaihao(taihao, chexing));
						}
						
						

					}
					catch (Exception e) {
						e.printStackTrace();
						Log.d("TAG", "error:"+e.getMessage());
					}
					List<HashMap<String, Object>> ddd = new ArrayList<HashMap<String, Object>>();
					for (int i = 0; i < fuwuxieyitaihao.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("taihao", fuwuxieyitaihao.get(i).getTaihao());
						map.put("chexing", fuwuxieyitaihao.get(i).getChexing());
						ddd.add(map);
						Log.d(TAG, "获得的名字"+fuwuxieyitaihao.get(i).getTaihao());
				}
					// 对解析后的数据进行装载适配器
					SimpleAdapter adapter = new SimpleAdapter(
							FuwuxieyiActivity.this, ddd,
							R.layout.fuwuxieyi_tirecode_item, new String[] { "taihao","chexing"
									}, new int[] { R.id.fu_taihao ,R.id.fu_chexing});
					ls_taihaochexing.setAdapter(adapter);
					
					
				}else {
					Toast.makeText(FuwuxieyiActivity.this, "网络异常", Toast.LENGTH_LONG).show();
				}
				
			}
	
		}
	
//	上传图片
	private void uploadImg(String userId, String oid)
	{
		if (!TextUtils.isEmpty(signPath))
		{
						
			UploadImgTask task = new UploadImgTask(this, userId, oid, signPath);
			task.showProgressDialog("提示", "正在上传签名");
			Log.d("TAG", "正在上传图片");
			task.execute();
			
						
			
		} else
		{
			Toast.makeText(this, "请签名", Toast.LENGTH_LONG).show();
		}
	}

	class UploadImgTask extends AsyncNetworkTask<String>
	{
		String userId;
		String oid;
		String ImgUrl;

		public UploadImgTask(Context context, String userId, String oid, String imgUrl)
		{
			super(context);
			this.userId = userId;
			this.oid = oid;
			ImgUrl = imgUrl;
		}

		@Override
		protected String doNetworkTask()
		{
			WebService ws = WebServiceFactory.getWebService();
			return ws.uploadSignImg(userId,oid,ImgUrl);
		}

		@Override
		protected void handleResult(String result)
		{
			Log.d("TAG", "result"+result);
			if (!TextUtils.isEmpty(result))
			{
				Log.d("TAG", "result"+result);
				if (result.equals("1"))
				{
					Intent intent = new Intent();
					intent.setClass(FuwuxieyiActivity.this, Fuwuxieyi_listActivity.class);
					startActivity(intent);
					finish();
					Log.i("TAG", "测试使得否上传成功："+result);
					Toast.makeText(FuwuxieyiActivity.this, "图片上传成功", Toast.LENGTH_LONG).show();
					FuwuxieyiActivity.this.finish();

				} else
				{
					Toast.makeText(FuwuxieyiActivity.this, "图片上传失败", Toast.LENGTH_LONG).show();
				}

			} else
			{
				Toast.makeText(FuwuxieyiActivity.this, "网络异常，请检查网络连接后再试", Toast.LENGTH_LONG).show();
				Log.d("TAG", "网络异常");
			}

		}

	}

	private OnClickListener signListener = new View.OnClickListener()
	{
		//点击签字按钮，首先上传填写内容
		@Override
		public void onClick(View v)
		{
//			String address = tv_address.getText().toString();
//			String chexing = tv_chexing.getText().toString();
			String platenum = et_platenum.getText().toString();
			String mileage = et_mileage.getText().toString();
//			String buytime = tv_buytime.getText().toString();
//			String tirecode = tv_tirecode.getText().toString();
//			Log.i("TAG", address);
//			Log.i("TAG", chexing);
			Log.i("TAG", platenum);
			Log.i("TAG", mileage);
//			Log.i("TAG", buytime);
//			Log.i("TAG", tirecode);
			
			register(oid, platenum, mileage);
			
			
			
			WritePadDialog writeTabletDialog = new WritePadDialog(FuwuxieyiActivity.this, new DialogListener()
			{
				@Override
				public void refreshActivity(Object object)
				{

				    mSignBitmap = (Bitmap) object;
					signIV.setImageBitmap(mSignBitmap);
					signPath = createFile();
					Log.d("TAG", "图片路径"+signPath);
					/*
					 * BitmapFactory.Options options = new
					 * BitmapFactory.Options(); options.inSampleSize = 15;
					 * options.inTempStorage = new byte[5 * 1024]; Bitmap zoombm
					 * = BitmapFactory.decodeFile(signPath, options);
					 */

				}
			});
			writeTabletDialog.show();
		}
	};
	/**
     * 创建手写签名文件
     * 
     * @return
     */
    private String createFile() {
        ByteArrayOutputStream baos = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory()+File.separator;           
            _path = sign_dir + System.currentTimeMillis() + ".jpg";
            baos = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
            }
 
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _path;
    }


}
