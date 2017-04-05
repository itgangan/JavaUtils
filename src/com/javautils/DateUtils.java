package com.javautils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ganxiangyong
 * @date 2015年2月9日 下午3:38:31
 */
public class DateUtils {

	private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将日期格式化为指定格式的字符串
	 * 
	 * @param date
	 *            待格式化的日期
	 * @param pattern
	 *            想要格式化为的模式（例如：yyyy-MM-dd），如果为null,则默认以yyyy-MM-dd HH:mm:ss格式化
	 * @return
	 */
	public static String formatDate(Date date, final String pattern) {
		SimpleDateFormat sdf = createSimpleDateFormat(pattern);

		return sdf.format(date);
	}

	/**
	 * 将pattern格式的时间字符串我转换为时间
	 * 
	 * @param text
	 *            时间字符串（例如：2015-01-11 22:10:22）
	 * @param pattern
	 *            时间模式（例如：yyyy-MM-dd），如果为null,则默认以yyyy-MM-dd HH:mm:ss格式解析
	 * @return 日期对象
	 */
	public static Date parseDate(String text, final String pattern) {
		SimpleDateFormat sdf = createSimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * 对时间进行运算
	 * 
	 * @param field
	 *            （例如：Calendar.MONTH,Calendar.DATE...）
	 * @param amount
	 *            +代表增加时间；-代表减少时间
	 * @param date
	 *            需要运算的时间，如果为空，则默认以当前时间进行运算
	 * @return 运算之后的时间
	 */
	public static Date addDate(int field, int amount, Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		c.add(field, amount);
		return c.getTime();
	}

	/**
	 * 判断两个日期是否同一周
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeek(Date date1, Date date2) {
		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		return c.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 判断日期是否为闰年
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isLeapYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, 2); // 3月
		c.set(Calendar.DATE, 1);// 1号
		c.add(Calendar.DATE, -1);// 3月1号减一天

		return c.get(Calendar.DATE) == 29; // 3月1号减1天是否是2月29号
	}

	/**
	 * 获取指定日期
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static Date getSpecifiedDate(int year, int month, int date) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, date);

		return c.getTime();
	}

	// 根据pattern创建一个SimpleDateFormat对象
	private static SimpleDateFormat createSimpleDateFormat(final String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		if (StringUtils.isBlank(pattern)) {
			sdf.applyPattern(PATTERN);
		} else {
			sdf.applyPattern(pattern);
		}

		return sdf;
	}
}
