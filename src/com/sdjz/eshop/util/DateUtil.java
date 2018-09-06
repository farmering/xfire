package com.sdjz.eshop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日期处理工具类
 * 
 * @author blues 2013-1-25 下午12:09:04
 */
public class DateUtil {
	
	public static void main(String[] args) {
		
		 List<String> li = DateUtil.process(DateUtil.addDate(DateUtil.toDateString(new Date()), -30), DateUtil.toDateString(new Date()));
		 
		 for(String s : li){
			 
			 System.out.println(s);
		 }
		 
	}

	private static final transient Log log = LogFactory.getLog(DateUtil.class);
	
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	 
    public static List<String> process(String date1, String date2){
   	 
   	 List<String> list = new ArrayList<String>();
   	 list.add(date1);
		if(date1.equals(date2)){
		    
		    return list;
		}
		
		String tmp;
		if(date1.compareTo(date2) > 0){  //确保 date1的日期不晚于date2
			tmp = date1; date1 = date2; date2 = tmp;
		}
		
		tmp = format.format(str2Date(date1).getTime() + 3600*24*1000);
		
        while(tmp.compareTo(date2) < 0){        	        
        	list.add(tmp);    
        	tmp = format.format(str2Date(tmp).getTime() + 3600*24*1000);
        }
        
       	 list.add(date2);
        
        return list;
	}
    
    public static  Date str2Date(String str) {
 		if (str == null) return null;
 		
 		try {
 			return format.parse(str);
 		} catch (ParseException e) {
 			e.printStackTrace();
 		}
 		return null;
 	}

	/***
	 * 实现日期加N年
	 * 
	 * @param str
	 * @param n
	 * @return
	 */
	public static String addYear(String str, int n) {

		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(dateStrToDate(str));
		gc.add(Calendar.YEAR, n);
		return toDateString(gc.getTime());

	}
	
	/***
	 * 比较两个日期的大小 第一个比第二个早就返回true 
	 * 
	 * @param str1 要比较的日期
	 * @param str2 当前月份的第一天
	 * @return
	 */
	public static boolean compare(String str1 , String str2 ){
		
		Date date1 = dateStrToDate(str1);
		
		Date date2 = dateStrToDate(str2);
		
		if(date1.getTime() <= date2.getTime()){
			
			return true;
			
		}else{
			
			return false;
			
		}
		
	}

	
	/***
	 * 计算两个日期之间的月份数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthSpace(String date1, String date2)
			 {

		int result = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		try {
			c1.setTime(sdf.parse(date1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			c2.setTime(sdf.parse(date2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		result = c2.get(Calendar.MONDAY) - c1.get(Calendar.MONTH) +1;

		return Math.abs(result);

	}
	
	public  static List<String[]> getDateInterval(Date begin, Date end) {
//		// 开始日期不能大于结束日期
//		if (!begin.before(end)) {
//			return null;
//		}
		
		List<String[]> list = new ArrayList<String[]>();

		Calendar cal_begin = Calendar.getInstance();
		cal_begin.setTime(begin);

		Calendar cal_end = Calendar.getInstance();
		cal_end.setTime(end);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	//	StringBuffer strbuf = new StringBuffer();

		while (true) {
			
			String[] ss = new String[2];
			
			if (cal_begin.get(Calendar.YEAR) == cal_end.get(Calendar.YEAR)
					&& cal_begin.get(Calendar.MONTH) == cal_end
							.get(Calendar.MONTH)) {
				ss[0] = sdf.format(cal_begin.getTime());
				ss[1] = sdf.format(cal_end.getTime());
				
				list.add(ss);
				break;
			}
			String str_begin = sdf.format(cal_begin.getTime());
			String str_end = getMonthEnd(cal_begin.getTime());
			ss[0] = str_begin;
			ss[1] = str_end;
			list.add(ss);
			cal_begin.add(Calendar.MONTH, 1);
			cal_begin.set(Calendar.DAY_OF_MONTH, 1);
			// String str_end =;
		}

		return list;

	}

	/**
	 * 取得指定月份的第一天
	 * 
	 * @param strdate
	 *            String
	 * @return String
	 */
	public static String getMonthBegin(Date date) {

		return formatDate(date, "yyyy-MM") + "-01";
	}

	/**
	 * 取得指定月份的最后一天
	 * 
	 * @param strdate
	 *            String
	 * @return String
	 */
	public static String getMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return formatDate(calendar.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		long msss = mss % (1000);

		if (days == 0) {

			if (hours == 0) {

				if (minutes == 0) {

					if (seconds == 0) {

						return msss + "毫秒";
					}

					return seconds + "秒" + msss + "毫秒";
				}

				return minutes + "分钟" + seconds + "秒" + msss + "毫秒";
			}

			return hours + "小时" + minutes + "分钟" + seconds + "秒" + msss + "毫秒";
		}

		return days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒"
				+ msss + "毫秒";
	}

	/****
	 * 日期格式返回日期加时间的String格式
	 * 
	 * @param date
	 * @return
	 */
	public static String toDateTimeString(Date date) {

		if (null == date) {
			return null;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);

	}

	/***
	 * 日期格式返回string“yyyy-MM-dd”格式
	 * 
	 * @param date
	 * @return
	 */
	public static String toDateString(Date date) {

		if (null == date) {
			return null;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}

	/**
	 * 缺省的日期格式
	 */
	private static final String DAFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 默认日期类型格试.
	 * 
	 * @see DAFAULT_DATE_FORMAT
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			DAFAULT_DATE_FORMAT);

	/**
	 * 缺省的日期时间格式
	 */
	private static final String DAFAULT_DATETIME_FORMAT = "yyyy-M-d HH:mm:ss";

	/**
	 * 时间格式
	 */
	private static String DATETIME_FORMAT = DAFAULT_DATETIME_FORMAT;

	private static SimpleDateFormat datetimeFormat = new SimpleDateFormat(
			DATETIME_FORMAT);

	/**
	 * 缺省的时间格式
	 */
	private static final String DAFAULT_TIME_FORMAT = "HH:mm:ss";

	/**
	 * 时间格式
	 */
	private static String TIME_FORMAT = DAFAULT_TIME_FORMAT;

	private static SimpleDateFormat timeFormat = new SimpleDateFormat(
			TIME_FORMAT);

	private DateUtil() {
		// 私用构造主法.因为此类是工具类.
	}

	/**
	 * 获取格式化实例.
	 * 
	 * @param pattern
	 *            如果为空使用DAFAULT_DATE_FORMAT
	 * @return
	 */
	public static SimpleDateFormat getFormatInstance(String pattern) {
		if (pattern == null || pattern.length() == 0) {
			pattern = DAFAULT_DATE_FORMAT;
		}
		return new SimpleDateFormat(pattern);
	}

	/**
	 * 格式化Calendar
	 * 
	 * @param calendar
	 * @return
	 */
	public static String formatCalendar(Calendar calendar) {
		if (calendar == null) {
			return "";
		}
		return dateFormat.format(calendar.getTime());
	}

	public static String formatDateTime(Date d) {
		if (d == null) {
			return "";
		}
		return datetimeFormat.format(d);
	}

	public static String formatDate(Date d) {
		if (d == null) {
			return "";
		}
		return dateFormat.format(d);
	}

	/**
	 * 格式化时间
	 * 
	 * @param calendar
	 * @return
	 */
	public static String formatTime(Date d) {
		if (d == null) {
			return "";
		}
		return timeFormat.format(d);
	}

	/**
	 * 格式化整数型日期
	 * 
	 * @param intDate
	 * @return
	 */
	public static String formatIntDate(Integer intDate) {
		if (intDate == null) {
			return "";
		}
		Calendar c = newCalendar(intDate);
		return formatCalendar(c);
	}

	/**
	 * 根据指定格式化来格式日期.
	 * 
	 * @param date
	 *            待格式化的日期.
	 * @param pattern
	 *            格式化样式或分格,如yyMMddHHmmss
	 * @return 字符串型日期.
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (StringUtils.isBlank(pattern)) {
			return formatDate(date);
		}
		SimpleDateFormat simpleDateFormat = null;
		try {
			simpleDateFormat = new SimpleDateFormat(pattern);
		} catch (Exception e) {
			e.printStackTrace();
			return formatDate(date);
		}
		return simpleDateFormat.format(date);
	}

	/****
	 * string“yyyy-MM-dd HH:mm:ss”格式返回日期格式
	 * 
	 * @param datetimeStr
	 * @return
	 */
	public static Date datetimeStrToDate(String datetimeStr) {

		if (null == datetimeStr) {
			return null;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		try {
			Date date = simpleDateFormat.parse(datetimeStr);
			return date;
		} catch (ParseException pe) {
			return null;
		}

	}
	
	/****
	 * string“yyyy-MM-dd HH:mm:ss”格式返回日期格式
	 * 
	 * @param datetimeStr
	 * @return
	 */
	public static Date datetimeStrToDate1(String datetimeStr) {

		if (null == datetimeStr) {
			return null;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");

		try {
			Date date = simpleDateFormat.parse(datetimeStr);
			return date;
		} catch (ParseException pe) {
			return null;
		}

	}

	/****
	 * Stirng“yyyy-MM-dd”返回日期格式
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date dateStrToDate(String dateStr) {

		if (null == dateStr) {
			return null;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Date date = simpleDateFormat.parse(dateStr);
			return date;
		} catch (ParseException pe) {
			return null;
		}

	}

	/**
	 * 取得Integer型的当前日期
	 * 
	 * @return
	 */
	public static Integer getIntNow() {
		return getIntDate(getNow());
	}

	/**
	 * 取得Integer型的当前日期
	 * 
	 * @return
	 */
	public static Integer getIntToday() {
		return getIntDate(getNow());
	}

	/**
	 * 取得Integer型的当前年份
	 * 
	 * @return
	 */
	public static Integer getIntYearNow() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 取得Integer型的当前月份
	 * 
	 * @return
	 */
	public static Integer getIntMonthNow() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;
		return month;
	}

	public static String getStringToday() {
		return getIntDate(getNow()) + "";
	}

	/**
	 * 根据年月日获取整型日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Integer getIntDate(int year, int month, int day) {
		return getIntDate(newCalendar(year, month, day));
	}

	/**
	 * 某年月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getFirstDayOfMonth(int year, int month) {
		return getIntDate(newCalendar(year, month, 1));
	}

	/**
	 * 某年月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getFirstDayOfThisMonth() {
		Integer year = DateUtil.getIntYearNow();
		Integer month = DateUtil.getIntMonthNow();
		return getIntDate(newCalendar(year, month, 1));
	}

	/**
	 * 某年月的第一天
	 * 
	 * @param date
	 * @return
	 * @time:2008-7-4 上午09:58:55
	 */
	public static Integer getFistDayOfMonth(Date date) {
		Integer intDate = getIntDate(date);
		int year = intDate / 10000;
		int month = intDate % 10000 / 100;
		return getIntDate(newCalendar(year, month, 1));
	}

	/**
	 * 某年月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getLastDayOfMonth(int year, int month) {
		return intDateSub(getIntDate(newCalendar(year, month + 1, 1)), 1);
	}

	/**
	 * 根据Calendar获取整型年份
	 * 
	 * @param c
	 * @return
	 */
	public static Integer getIntYear(Calendar c) {
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 根据Calendar获取整型日期
	 * 
	 * @param c
	 * @return
	 */
	public static Integer getIntDate(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + month * 100 + day;
	}

	/**
	 * 根据Date获取整型年份
	 * 
	 * @param d
	 * @return
	 */
	public static Integer getIntYear(Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return getIntYear(c);
	}

	/**
	 * 根据Date获取整型日期
	 * 
	 * @param d
	 * @return
	 */
	public static Integer getIntDate(Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return getIntDate(c);
	}

	/**
	 * 根据Integer获取Date日期
	 * 
	 * @param n
	 * @return
	 */
	public static Date getDate(Integer n) {
		if (n == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.set(n / 10000, n / 100 % 100 - 1, n % 100);
		return c.getTime();
	}

	public static Date getDate(String date) {
		if (date == null || date.length() == 0) {
			return null;
		}

		try {
			if (date.contains("/")) {
				date = date.replaceAll("/", "-");
			}
			return getFormatInstance(DATE_FORMAT).parse(date);
		} catch (ParseException e) {
			log.error("解析[" + date + "]错误！", e);
			return null;
		}
	}

	/**
	 * 根据年份Integer获取Date日期
	 * 
	 * @param year
	 * @return
	 */
	public static Date getFirstDayOfYear(Integer year) {
		if (year == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.set(year, 1, 1);
		return c.getTime();
	}

	/**
	 * 根据年月日生成Calendar
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Calendar newCalendar(int year, int month, int day) {
		Calendar ret = Calendar.getInstance();
		if (year < 100) {
			year = 2000 + year;
		}
		ret.set(year, month - 1, day);
		return ret;
	}

	/**
	 * 根据整型日期生成Calendar
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar newCalendar(int date) {
		int year = date / 10000;
		int month = (date % 10000) / 100;
		int day = date % 100;

		Calendar ret = Calendar.getInstance();
		ret.set(year, month - 1, day);
		return ret;
	}

	/**
	 * 取得Date型的当前日期
	 * 
	 * @return
	 */
	public static Date getNow() {
		return new Date();
	}

	/**
	 * 取得Date型的当前日期
	 * 
	 * @return
	 */
	public static Date getToday() {
		return DateUtil.getDate(DateUtil.getIntToday());
	}

	/**
	 * 整数型日期的加法
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Integer intDateAdd(int date, int days) {
		int year = date / 10000;
		int month = (date % 10000) / 100;
		int day = date % 100;

		day += days;

		return getIntDate(year, month, day);
	}

	/**
	 * 整数型日期的减法
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Integer intDateSub(int date, int days) {
		return intDateAdd(date, -days);
	}

	/**
	 * 计算两个整型日期之间的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer daysBetweenDate(Integer startDate, Integer endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		Calendar c1 = newCalendar(startDate);
		Calendar c2 = newCalendar(endDate);

		Long lg = (c2.getTimeInMillis() - c1.getTimeInMillis()) / 1000 / 60
				/ 60 / 24;
		return lg.intValue();
	}
	
	public static Integer daysBetweenDateHour(Integer startDate, Integer endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		Calendar c1 = newCalendar(startDate);
		Calendar c2 = newCalendar(endDate);

		Long lg = (c2.getTimeInMillis() - c1.getTimeInMillis()) / 1000 / 60
				/ 60 ;
		return lg.intValue();
	}

	/**
	 * 计算两个整型日期之间的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer daysBetweenDate(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		Long interval = endDate.getTime() - startDate.getTime();
		interval = interval / (24 * 60 * 60 * 1000);
		return interval.intValue();
	}
	
	/**
	 * 计算两个整型日期之间的小时数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer daysBetweenDateTime(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		Long interval = endDate.getTime() - startDate.getTime();
		interval = interval / ( 60 * 60 * 1000);
		return interval.intValue();
	}

	/**
	 * 计算两个日期之间的毫秒数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long milliSecondsBetweenDate(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0l;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(startDate);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(endDate);
		// System.out.println("["+c2.getTimeInMillis()+"]-["+c1.getTimeInMillis()+"]=["+(c2.getTimeInMillis()
		// - c1.getTimeInMillis())+"]");
		Long lg = (c2.getTimeInMillis() - c1.getTimeInMillis());
		return lg;
	}

	/**
	 * 取得当前日期.
	 * 
	 * @return 当前日期,字符串类型.
	 */
	public static String getStringDate() {
		return getStringDate(DateUtil.getNow());
	}

	/**
	 * 根据calendar产生字符串型日期
	 * 
	 * @param d
	 * @return eg:20080707
	 */
	public static String getStringDate(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(d);
	}

	public static String getFormatStringDate(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		return sdf.format(d);
	}

	/***
	 * 实现日期加N天算法
	 * 
	 * @param str
	 * @param n
	 * @return
	 */
	public static String addDate(String str, int n) {

		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(dateStrToDate(str));
		gc.add(Calendar.DATE, n);
		return toDateString(gc.getTime());

	}
	
	
}