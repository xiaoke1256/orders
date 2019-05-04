package com.xiaoke1256.orders.common.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

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
	
	public static Date parseDate(String str, String fmt) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		
		if(StringUtils.isEmpty(fmt)) {
			fmt = "YYYY-MM-dd";
		}
		try {
			return DateUtils.parseDate(str, new String[] {fmt});
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 得到当月的最后一天最后一毫秒。
	 * @param date
	 *            日期
	 * @return 当月最后一天最后一毫秒
	 */
	public static java.util.Date getLastTimeOfMonth(java.util.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	public static Date addSeconds(Date date,int seconds) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.SECOND, seconds);
		return calender.getTime();
	}
	
	public static Date addDays(Date date, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, num);
		return cal.getTime();
	}
	
}
