package com.ryf.weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.R.bool;
import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimeUtils {
	// 格式化时�?
//	@SuppressLint("SimpleDateFormat")
//	public static Calendar getDate(String time) {
//		Date date = null;
//		Calendar c = Calendar.getInstance();
//		if (time != null && !time.equals("")) {
//			SimpleDateFormat format = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss");
//			try {
//				date = format.parse(time);
//				c.setTime(date);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return c;
//	}
	public static Date getTimeDate(String strDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			return sdf.parse(strDate);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static String getTimeDiff(Date date) {
		Calendar cal = Calendar.getInstance();
		long diff = 0;
		Date dnow = cal.getTime();
		String str = "";
		diff = dnow.getTime() - date.getTime();

		if (diff > 24 * 60 * 60 * 1000) {
			long day = diff / (24 * 60 * 60 * 1000);
			if (day > 3) {
				str = getStringDate(date, "MM-dd HH:mm");
			} else {
				str = day + "天前";
			}
		} else if (diff > 60 * 60 * 1000) {
			str = diff / (60 * 60 * 1000) + "小时�?";
		} else if (diff > 60 * 1000) {
			str = diff / (60 * 1000) + "分钟�?";
		} else {
			str = "刚刚";
		}
		return str;
	}

	public static String getStringDate(Date date, String formatter) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.format(date);
	}

	@SuppressLint("SimpleDateFormat")
	public static String timeToDate(String time) {
		String res = "";
		if (time != null && !time.equals("")) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = format.parse(time);
				res = format1.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return res;
	}
	public static boolean isDateNowAfter(String end) {
		boolean isRight = false;
		SimpleDateFormat defaultDate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date nowDate = defaultDate.parse(defaultDate.format(new Date()));
			Date endDate = defaultDate.parse(end);
			if (nowDate.after(endDate)) {
				isRight = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return isRight;
	}

	public static boolean isTimeNowAfter(String end) {
		boolean isRight = false;
		SimpleDateFormat defaultDate = new SimpleDateFormat("HH:mm");
		try {
			Date nowDate = defaultDate.parse(defaultDate.format(new Date()));
			Date endDate = defaultDate.parse(end);
			if (nowDate.after(endDate)) {
				isRight = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return isRight;
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatDate(int year, int month, int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(year - 1900, month, day);
		return format.format(date);
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatTime(int hour, int minute) {
		SimpleDateFormat format = new SimpleDateFormat(" HH:mm:ss");
		Date date = new Date(0, 0, 1, hour, minute, 0);
		return format.format(date);
	}

	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String time = df.format(new Date());
		return time;
	}
}
