/*
 * 文件名：ListHelper 
 * 作 者: Jim
 * 创建时间：2013-9-2 上午11:13:44 
 */
package com.epoint.epointuniversal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ListHelper {

	/*
	 * 距离本地时间的时间差（单位秒） param: time 时间 yyyy-MM-dd hh:mm:ss
	 */
	public static int timeLag(String time) {
		long playTime = -1;
		try {
			long timeLong = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(
					time).getTime();
			long currentLong = System.currentTimeMillis();
			playTime = timeLong - currentLong;

		} catch (ParseException e) {
			e.printStackTrace();
			return (int) playTime;
		}
		return (int) (playTime / 1000);
	}

	/*
	 * 距离现在的时间差 time：yyyy-MM-dd（单位天）
	 */
	public static int timeLag3(String time) {
		Calendar c = Calendar.getInstance();
		int now_year = c.get(Calendar.YEAR);
		int now_month = c.get(Calendar.MONTH) + 1;
		int now_day = c.get(Calendar.DAY_OF_MONTH);
		time = time.split(" ")[0];
		int year = Integer.parseInt(time.split("-")[0]);
		int month = Integer.parseInt(time.split("-")[1]);
		int day = Integer.parseInt(time.split("-")[2]);

		int yearLag = year - now_year;
		int monthLag = month - now_month;
		int dayLag = day - now_day;
		if (yearLag != 0) {
			return yearLag * 365;
		}
		if (monthLag != 0) {
			return monthLag * 30;
		}
		if (dayLag != 0) {
			return dayLag;
		}
		return 0;
	}

	/*
	 * 距离现在的时间差（现在的时间为服务器时间） time：（yyyy-MM-dd）hh:mm:ss（单位秒） 如果参数包括年月日，则忽略年月日
	 */
	public static int timeLag2(String time, int timeLag) {
		Calendar c = Calendar.getInstance();
		int now_hour = c.get(Calendar.HOUR_OF_DAY);
		int now_minute = c.get(Calendar.MINUTE);
		int now_second = c.get(Calendar.SECOND) + timeLag;

		int hour;
		if (time.split(" ").length < 2) {
			hour = Integer.parseInt(time.split(":")[0]);
		} else {
			hour = Integer.parseInt(time.split(" ")[1].split(":")[0]);
		}
		int minute = Integer.parseInt(time.split(":")[1]);
		int second = Integer.parseInt(time.split(":")[2]);

		int lag = hour * 60 * 60 + minute * 60 + second
				- (now_hour * 60 * 60 + now_minute * 60 + now_second);

		return lag;
	}

}
