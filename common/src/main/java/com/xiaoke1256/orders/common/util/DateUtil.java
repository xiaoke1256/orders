package com.xiaoke1256.orders.common.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class DateUtil {
	
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH)+1;
	}
	
	public static int getDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}
	
	public static int getHour(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getMinute(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}
	
	public static int getSecond(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.SECOND);
	}
	
	public static int getMillisecond(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MILLISECOND);
	}
	
	public static String format(Date date,String fmt) {
		String result = null;
		if (date == null) {
			return result;
		}
		if (StringUtils.isEmpty(fmt)) {
			fmt = "YYYY-MM-dd";
		}
		result = DateFormatUtils.format(date, fmt);
		return result;
	}
	
	public static Date addSeconds(Date date,int seconds) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.SECOND, seconds);
		return calender.getTime();
	}
	
}
