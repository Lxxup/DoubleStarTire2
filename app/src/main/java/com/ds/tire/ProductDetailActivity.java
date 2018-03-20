package com.ds.tire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ds.tire.bean.AsyncImageLoader;
import com.ds.tire.bean.AsyncImageLoader.ImageCallBack;
import com.ds.tire.bean.ProductDetail;
import com.ds.tire.bean.TagInfo;
import com.ds.tire.util.AsyncNetworkTask;
import com.ds.tire.util.Constant;
import com.ds.tire.util.SpUtils;
import com.ds.tire.util.WebService;
import com.ds.tire.util.WebServiceFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailActivity extends Activity {

	String tid=null;
	String did=null;
	String tname=null;
	String tintroduction=null;
	String tfeature=null;
	String timage1=null;
	String timage2=null;
	String timage3=null;
	private static final String TAG = "TAG";
	private ListView detaillist = null;
	private ProductDetailAdapter detailAdapter = new ProductDetailAdapter(this);
	LinearLayout back;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productdetail);
		back = (LinearLayout) findViewById(R.id.safe_back);
        back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                finish();
            }
        });
		 Bundle bundle = this.getIntent().getExtras();
	      did = bundle.getString("id");
		  detaillist = (ListView) findViewById(R.id.detaillist);
		
		  try {                                         
				SelectProductDetail selectProductdetail = new SelectProductDetail(ProductDetailActivity.this,SpUtils.getString(this, SpUtils.ACCOUNT, ""));
				Log.d("TAG", "���ԣ�");
				selectProductdetail.execute();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		  
	
			detaillist.setOnItemClickListener(new MyItemClickListener());
	}
	//listvie
		class MyItemClickListener implements AdapterView.OnItemClickListener {
			@Override
			public void onItemClick(AdapterView<?> parents, View view,
					int position, long id) {
				try {
					final ProductDetail detail = detailAdapter.getItem(position);
					Log.d(TAG, "�����б��");
					System.out.println(position);
					tid=detail.getId();
					tname=detail.getTname();
					 timage1=detail.getImage1();
					 timage2=detail.getImage2();
					 timage3=detail.getImage3();
					 tintroduction=detail.getIntroduction();					 
					 tfeature=detail.getFeature();
					StringBuffer sb1 = new StringBuffer();
					  sb1.append(Constant.TIRE_URL);
					  sb1.append(timage1);
					 String path1= sb1.toString();
					 Log.d("TAG","path:"+path1);
					 StringBuffer sb2 = new StringBuffer();
					  sb2.append(Constant.TIRE_URL);
					  sb2.append(timage2);
					 String path2= sb2.toString();
					 Log.d("TAG","path:"+path2);	
					StringBuffer sb3 = new StringBuffer();
					  sb3.append(Constant.TIRE_URL);
					  sb3.append(timage3);
					 String path3= sb3.toString();
					 Log.d("TAG","path:"+path3);
					Intent intent = new Intent(ProductDetailActivity.this,PDetailActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("id", tid);
					bundle.putString("name", tname);
					bundle.putString("feature", tfeature);
					bundle.putString("introduction", tintroduction);
					bundle.putString("path1", path1);
					bundle.putString("path2", path2);
					bundle.putString("path3", path3);
	                intent.putExtras(bundle);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	//����ӿڻ�ȡ���ݵ���	
	public class SelectProductDetail extends AsyncNetworkTask<String> {
		String id;
		public SelectProductDetail(Context context,String id) {
			super(context);
			this.id=did;
			
		}

		@Override
		protected String doNetworkTask() {
			WebService ws = WebServiceFactory.getWebService();
			return ws.selectProductDetail(id);
		}
		
		protected void handleResult(String result) {
			if(!TextUtils.isEmpty(result)){
				Log.i("TAG", "detailresult1111:"+result);		 
				    try {
				    	
				    	List<ProductDetail> list = new ArrayList<ProductDetail>();
				        
				        JSONArray jsonArray = new JSONArray(result);
				        for (int i = 0; i < jsonArray.length(); i++) {
				            JSONObject json = jsonArray.getJSONObject(i);
				            ProductDetail detail = new ProductDetail();
				            detail.setId(json.getString("tireid"));
				            Log.i("TAGId",json.getString("tireid"));				            
				            detail.setTname(json.getString("tirename"));
				            detail.setIntroduction(json.getString("introduction"));
				            list.add(detail);
				            detailAdapter.notifyDataSetChanged();
							detailAdapter.setList(list);
							detaillist.setAdapter(detailAdapter);
				            detail.setFeature(json.getString("feature"));
				            detail.setImage1(json.getString("picture"));
				            detail.setImage2(json.getString("pictureparameter"));
				            detail.setImage3(json.getString("picturenature"));
				            
				            
				      
				        }
				    } catch (JSONException e) {
				        e.printStackTrace();
				    }
				    
			}else {
				Toast.makeText(ProductDetailActivity.this, "网络异常", Toast.LENGTH_LONG).show();
			}
			
		}
 }
	//����ӿڻ�ȡͼƬ����	
//	public class SelectImage extends AsyncNetworkTask<Bitmap> {
//		private String url;
//		public SelectImage(Context context,String url) {
//			super(context);
//			this.url=path;
//			// TODO Auto-generated constructor stub
//		}
//		@Override
//		protected Bitmap doNetworkTask() {
//			WebService ws = WebServiceFactory.getWebService();
//			return ws.getBitmap(url);
//		}
//		
//		protected void handleResult(Bitmap result) {
//			image.setImageBitmap(result);
//			
//		}
 //}
 class ProductDetailAdapter extends BaseAdapter{

		private Context context = null;
		public static final String KEY_ID = "id";
		private String TAG = "TAG";
		private List<ProductDetail> detailList = null;
		HashMap<String, Drawable> imgCache; // 图片缓存
		HashMap<Integer, TagInfo> tag_map; // TagInfo缓存
		AsyncImageLoader loader; // 异步加载图片类

		public ProductDetailAdapter(Context context) {
			if (context == null) {
				throw new NullPointerException();
			}

			this.context = context;
			imgCache = new HashMap<String, Drawable>();
			loader = new AsyncImageLoader();
			tag_map = new HashMap<Integer, TagInfo>();

		}
		@Override
		public int getCount() {
			return detailList.size();
		}

		@Override
		public ProductDetail getItem(int position) {
			return detailList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setList(List<ProductDetail> list) {
			this.detailList = list;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ProductDetail detail = detailList.get(position);
		
			ViewHolder holder = null;

			if (convertView == null) {

				convertView = LayoutInflater.from(context).inflate(
						R.layout.productdetail_list, null);
				holder = new ViewHolder();
				holder.image= (ImageView) convertView.findViewById(R.id.image);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.introduction = (TextView) convertView.findViewById(R.id.introduction);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			
			//holder.center.getBackground().setAlpha(80);
			
			holder.name.setText(detail.getTname());
			holder.introduction.setText(detail.getIntroduction());
			String img= detailList.get(position).getImage1(); // 得到该项所代表的url地址
			StringBuffer sb = new StringBuffer();
			  sb.append(Constant.TIRE_URL);
			  sb.append(img);
			 String imgurl= sb.toString();
			 Log.d("TAG","path:"+imgurl);
			Drawable drawable = imgCache.get(imgurl); // 先去缓存中找
			
			TagInfo tag = new TagInfo();
			tag.setPosition(position); // 保存了当前在adapter中的位置
			tag.setUrl(imgurl); // 保存当前项所要加载的url
			
			holder.image.setTag(tag); // 为ImageView设置Tag，为以后再获取图片后好能找到它
			
			tag_map.put(position, tag); // 把该TagInfo对应position放入Tag缓存中
			
			if (null != drawable) { // 找到了直接设置为图像
				 
				holder.image.setImageDrawable(drawable);
			} else {
				drawable = loader.loadDrawableByTag(tag, new ImageCallBack() {
					
					@Override
					public void obtainImage(TagInfo ret_info) {
						
						imgCache.put(ret_info.getUrl(), ret_info.getDrawable()); // 首先把获取的图片放入到缓存中

						// 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
						ImageView tag_view = (ImageView)detaillist
								.findViewWithTag(tag_map.get(ret_info
										.getPosition()));
						Log.i("carter", "tag_view: " + tag_view + " position: "
								+ ret_info.getPosition());
						if (null != tag_view)

							tag_view.setImageDrawable(ret_info.getDrawable());
					}
				});

				if (null == drawable) { // 如果获取的图片为空，则默认显示一个图片
				// holder.img.setImageResource(R.drawable.ic_launcher);
				}
			}

			return convertView;

		}

		class ViewHolder {
			ImageView image;
			//ImageView img2;
			//ImageView st;
			//LinearLayout center;
			TextView name;
			TextView introduction;
			
		}

	}
}