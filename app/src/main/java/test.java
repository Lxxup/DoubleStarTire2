//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//
//import android.util.Log;
//
//import com.ds.tire.bluetooth.Constants;
//
//
//public class test {
//
//	private final static InputStream mmInStream=null;
//	private final OutputStream mmOutStream;
//	private static ArrayList<Integer> buffer = null;
//	
//	private static int mDateChange(int date) {
//		int finalDate;
//		if (date > 0x7fff) {
//			int fanma = date - 1;
//			int yuanma = fanma ^ 0xffff;
//			finalDate = 0-yuanma;
//		}else
//			finalDate= date;
//
//		return finalDate;
//	}
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		byte c = 0;
//		int num = 0;//右括号个数
//		int index = -1;//有括号的位置
//		while (true) {
//			try {
//				
//				buffer = new ArrayList<Integer>();
//				//完整数据格式3c3c3c    帧头 （4） ID号（4）胎压（2）胎温（2）加速度（2） 电压（2）        3e3e3e
//			
//				while (num != 3) {
//					c = (byte) mmInStream.read();
//					Log.i("TAG", "缓冲区c: "+c);
//					buffer.add((c+256)%256);
//					Log.d("数据:",(Integer.toHexString(c)).toString());					
//					//判断是否为>>>结尾，>对应的码值0x3e
//					if (Integer.toHexString(c).toString().equals("3e")) {														
//						num++;
//						if (num == 1) {
//							index = buffer.size();
//						} else {
//							if ((index + num - 1) != buffer.size()) {
//								index = -1;
//								num = 0;
//							}
//						}
//					}
//				}
//				Log.d("TAG", "缓冲区大小"+buffer.size());
//				if(buffer.size() == 13 ){
//				}else if(buffer.size() == 98){
//					
//					/*
//					//数据校验
//					if ((buffer.get(8) ^ buffer.get(9) ^ buffer.get(10) ^ buffer.get(11) ^ buffer.get(12) ^ buffer.get(13) ^ buffer
//							.get(14)) == buffer.get(15)) {
//						//左边的
//						if (buffer.get(8) <0xB0) {
//							int loc = ((buffer.get(8)&0x0f) - 1) * 2;
//							int yaqiang = buffer.get(9) * 256 + buffer.get(10);
//							mSendBroadcast(loc, yaqiang, buffer.get(11), buffer.get(12));
//						}
//						// 右边的
//						else{
//							int loc = (buffer.get(8) &0x0f)* 2 - 1;
//							int yaqiang = buffer.get(9) * 256 + buffer.get(10);
//							mSendBroadcast(loc, yaqiang, buffer.get(11), buffer.get(12));
//						}
//					}
//				*/
//					Constants.Tag=0;
//					String mID = Integer.toHexString(buffer.get(67))+Integer.toHexString(buffer.get(68))+Integer.toHexString(buffer.get(69))+Integer.toHexString(buffer.get(70));
//					float yaqiang = (float) (mDateChange(buffer.get(71) * 256 + buffer.get(72))*1.0/16.0);
//					float wendu = (float) (mDateChange(buffer.get(73) * 256 + buffer.get(74))*1.0/128.0);
//					float jiasudu = (float) (mDateChange(buffer.get(75) * 256 + buffer.get(76))*1.0/16.0);
//					//y=((yaqiang-b)/k);
//					//mID="000000000";
//					
//					
////					String mID = Integer.toHexString(buffer.get(11))+Integer.toHexString(buffer.get(12))+Integer.toHexString(buffer.get(13))+Integer.toHexString(buffer.get(14));
////					float yaqiang = (float) (mDateChange(buffer.get(15) * 256 + buffer.get(16))*1.0/16.0);
////					float wendu = (float) (mDateChange(buffer.get(17) * 256 + buffer.get(18))*1.0/128.0);
////					float jiasudu = (float) (mDateChange(buffer.get(19) * 256 + buffer.get(20))*1.0/16.0);
////					mSendBroadcast(mID, yaqiang, wendu, jiasudu);
//					Log.i("TAG", "设备号："+mID+"压强：："+yaqiang+"温度：："+wendu+"加速度：："+jiasudu);
//					}else if(buffer.size() == 14){
//					
//				}
//				num = 0;
//				
//			} catch (IOException e) {
//				break;
//			}
//		}
//		
//	}
//
//}
